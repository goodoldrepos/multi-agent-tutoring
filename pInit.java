package com.jadex;

import java.util.ArrayList;

import jadex.adapter.fipa.AgentIdentifier;
import jadex.runtime.GoalFailureException;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;

public class pInit extends Plan {

	public pInit() {
		System.out.println("[pInit]");
	}

	@Override
	public void body() {
		
		ArrayList<String> a = zDAO.listerApprenants();
	
		for (int i = 0; i <a.size(); i++) 
		{
			IGoal ca = createGoal("ams_create_agent");
			ca.getParameter("type").setValue("com.jadex.Apprenant");
			dispatchSubgoalAndWait(ca);
			
			AgentIdentifier createdagent = (AgentIdentifier) ca.getParameter("agentidentifier").getValue();

			IGoal tw = createGoal("rp_initiate");
			tw.getParameter("action").setValue("init/" + a.get(i));
			tw.getParameter("receiver").setValue(createdagent);

			try {
				dispatchTopLevelGoal(tw);
			} catch (GoalFailureException gfe) {
				System.out.println("Erreur lorsque j'essaye de communiquer avec le nouveau Apprenant");
			}
		}

	}

}
