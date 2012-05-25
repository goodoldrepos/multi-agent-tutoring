package com.jadex;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Hashtable;

import jadex.adapter.fipa.AgentDescription;
import jadex.adapter.fipa.AgentIdentifier;
import jadex.adapter.fipa.ServiceDescription;
import jadex.runtime.GoalFailureException;
import jadex.runtime.IGoal;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;

public class tDebut extends Plan {

	@Override
	public void body() {

		System.out.println("[Debut d'activite]");

		String params = (String) getParameter("params").getValue();
		Socket client = (Socket) getParameter("client").getValue();

		String[] infos = params.split("/");
		String CNE = infos[2];

		// trouve l'agent
		IGoal df_search = createGoal("df_search");
		AgentDescription desc = new AgentDescription();
		desc.addService(new ServiceDescription(null, CNE, null));
		df_search.getParameter("description").setValue(desc);
		// lancer le subgoal et attendre
		dispatchSubgoalAndWait(df_search);
		
		AgentDescription[] result = (AgentDescription[]) df_search.getParameterSet("result").getValues();

		Hashtable<String, String> h = new Hashtable<String, String>();
		String res="";
		
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
				res = h.get("nom") + " " + h.get("prenom");
				
			} catch (GoalFailureException gfe) {
				System.out.println("[tDebut] Erreur quand je demande de l'information a l'apprenant");
			}

			System.out.println(res);
			
			PrintStream out = null;
			try {
				out = new PrintStream(client.getOutputStream());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			out.println("Bonjour ");
			out.println(res + " ");
			// analyse la reponse et accede a la BD
			out.println(zDAO.debut(infos[1]));
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
