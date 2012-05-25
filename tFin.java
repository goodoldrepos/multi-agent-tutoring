package com.jadex;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Hashtable;

import jadex.adapter.fipa.AgentDescription;
import jadex.adapter.fipa.AgentIdentifier;
import jadex.adapter.fipa.ServiceDescription;
import jadex.runtime.GoalFailureException;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;

public class tFin extends Plan {

	@Override
	public void body() {
		
		System.out.println("[tFin]");

		String params = (String) getParameter("params").getValue();
		Socket client = (Socket) getParameter("client").getValue();

		String[] infos = params.split("/");
		String CNE = infos[1];
		
		System.out.println("CNE Apprenant is: " + CNE);

		// trouve l'agent
		IGoal df_search = createGoal("df_search");
		AgentDescription desc = new AgentDescription();
		desc.addService(new ServiceDescription(null, CNE, null));
		df_search.getParameter("description").setValue(desc);
		
		// lancer le subgoal et attendre
		dispatchSubgoalAndWait(df_search);
		AgentDescription[] result = (AgentDescription[]) df_search.getParameterSet("result").getValues();

		Hashtable<String, String> h = new Hashtable<String, String>();
		if (result.length != 0) {
			AgentIdentifier apprenant = result[0].getName();

			// etablir une connexion
			IGoal msg = createGoal("rp_initiate");
			msg.getParameter("action").setValue("cne/" + CNE);
			msg.getParameter("receiver").setValue(apprenant);
			try {
				dispatchSubgoalAndWait(msg);
				// recoit le message
				h =  (Hashtable<String, String>) msg.getParameter("result").getValue();
			} catch (GoalFailureException gfe) {
				System.out.println("[tFin] Erreur quand je demande de l'information a l'apprenant");
			}
			
			String profil = h.get("profil");
			String resultat = infos[2];
			System.out.println(zDAO.fin(profil, resultat));
			
			
			PrintStream out = null;
			try {
				out = new PrintStream(client.getOutputStream());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			//accede a la BD.
			out.println(h.get("prenom") + ", ");
			out.println(zDAO.fin(profil, resultat));
			out.flush();
			try {
				client.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}

		} else
			System.out.println("j'ai rien trouve");
		
		
	}
}
