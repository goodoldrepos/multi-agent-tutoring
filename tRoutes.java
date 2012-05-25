package com.jadex;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import jadex.adapter.fipa.AMSAgentDescription;
import jadex.adapter.fipa.AgentDescription;
import jadex.adapter.fipa.AgentIdentifier;
import jadex.adapter.fipa.SFipa;
import jadex.adapter.fipa.ServiceDescription;
import jadex.runtime.GoalFailureException;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;

public class tRoutes extends Plan {

	private AgentIdentifier ta;

	

	@Override
	public void body() {
		// TODO Auto-generated method stub

		Socket client = (Socket) getParameter("client").getValue();
		
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			String	request	= in.readLine();
			System.out.println("[request] " + request);
			if(request==null) return;
			int	slash	= request.indexOf("/");
			int	space	= request.indexOf(" ", slash);
			String msg	= request.substring(slash+1, space);
			
			if(msg.startsWith("inscription"))
			{
				IGoal inscription = createGoal("inscription"); 
				inscription.getParameter("params").setValue(msg);
				inscription.getParameter("client").setValue(client); 
				dispatchSubgoalAndWait(inscription);
			}
			else if(msg.startsWith("connexion"))
			{
				IGoal connexion = createGoal("connexion"); 
				connexion.getParameter("params").setValue(msg);
				connexion.getParameter("client").setValue(client); 
				dispatchSubgoalAndWait(connexion);
			}
			else if(msg.startsWith("debut"))
			{
				IGoal debut = createGoal("debut"); 
				debut.getParameter("params").setValue(msg);
				debut.getParameter("client").setValue(client); 
				dispatchSubgoalAndWait(debut);
			}
			else if(msg.startsWith("inactive"))
			{
				IGoal inactive = createGoal("inactive"); 
				inactive.getParameter("params").setValue(msg);
				inactive.getParameter("client").setValue(client); 
				dispatchSubgoalAndWait(inactive);			
			}
			else if(msg.startsWith("fin"))
			{
				IGoal debut = createGoal("fin"); 
				debut.getParameter("params").setValue(msg);
				debut.getParameter("client").setValue(client); 
				dispatchSubgoalAndWait(debut);
			}
			
			
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		
		
		
		
		
		  
		 

	}

}
