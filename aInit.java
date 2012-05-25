package com.jadex;

import jadex.adapter.fipa.AgentDescription;
import jadex.adapter.fipa.SFipa;
import jadex.adapter.fipa.ServiceDescription;
import jadex.runtime.IGoal;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

public class aInit extends Plan {

	@Override
	public void body() {
		System.out.println("[aInit]");
		
		
		IMessageEvent msg = (IMessageEvent)getInitialEvent();
		String contenu = (String) msg.getContent();
		String cne = contenu.split("/")[1];
		
		ServiceDescription service1 = new ServiceDescription(cne,cne, "PFE");
		AgentDescription desc = SFipa.createAgentDescription(null, service1);
		
		IGoal register = createGoal("df_register");
		register.getParameter("description").setValue(desc);
		dispatchSubgoalAndWait(register);	
		
		getBeliefbase().getBelief("CNE").setFact(cne);
	}

}
