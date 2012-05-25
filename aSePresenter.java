package com.jadex;

 import java.util.Hashtable;

import jadex.runtime.*;
 

public class aSePresenter extends Plan {

	@Override
	public void body() {
		
		IMessageEvent msg = (IMessageEvent)getInitialEvent();
		//recuperer contenu du message
		String contenu = (String) msg.getContent();
		
		String cne = contenu.split("/")[1];
		 		
		System.out.println("[aSePresenter]");
		
		Hashtable<String,String> h = zDAO.getApprenant(cne);
		
		
 	    IMessageEvent reply = msg.createReply("inform",h);
	    sendMessage(reply); 	
		
		
	}
	
	

}
