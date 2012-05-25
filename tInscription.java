package com.jadex;

import java.io.IOException;
import java.io.PrintStream;
import java.net.*;

import jadex.adapter.fipa.AgentIdentifier;
import jadex.runtime.GoalFailureException;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;

public class tInscription extends Plan {

	@Override
	public void body() {
		// TODO Auto-generated method stub
		System.out.println("[tInscription]");

		String params = (String)getParameter("params").getValue();
		Socket client = (Socket)getParameter("client").getValue();
		
		String[] informations = params.split("/");
		boolean reussit = zDAO.ajouterApprenant(informations);
		
		PrintStream out=null;
		try {
			out = new PrintStream(client.getOutputStream());
		} catch (IOException e) {
 			System.out.println(e.getLocalizedMessage());
		}

		
		if(reussit)
		{
			System.out.println("ajout nouveau apprenant");
			
			IGoal ca = createGoal("ams_create_agent");
			ca.getParameter("type").setValue("com.jadex.Apprenant");
			dispatchSubgoalAndWait(ca);
			
			AgentIdentifier createdagent = (AgentIdentifier) ca.getParameter("agentidentifier").getValue();

			IGoal tw = createGoal("rp_initiate");
			tw.getParameter("action").setValue("init/" + informations[1]);
			tw.getParameter("receiver").setValue(createdagent);

			try {
				dispatchTopLevelGoal(tw);
			} catch (GoalFailureException gfe) {
				System.out.println("Erreur lorsque j'essaye de communiquer avec le nouveau Apprenant");
			}
			out.println("Good");
		}
		else 
		{
			System.out.println("CNE existe deja");
			out.println("Bad");
		}
		out.println("cc");
		out.flush();
		try {
			client.close();
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}
		 
		
	}

}
