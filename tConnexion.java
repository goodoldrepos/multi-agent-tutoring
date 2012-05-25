package com.jadex;

import java.io.IOException;
import java.io.PrintStream;
import java.net.*;

import jadex.runtime.Plan;

public class tConnexion extends Plan {

	@Override
	public void body() {
		
		System.out.println("tConnexion UP!");
		
		String params = (String)getParameter("params").getValue();
		Socket client = (Socket)getParameter("client").getValue();
		
		String[] infos = params.split("/");
		
		boolean b = zDAO.verifierPassword(infos[1], infos[2]);	
		
		
		
		try {
			PrintStream out = new PrintStream(client.getOutputStream());
			
			if(b) out.println("good");
			else out.println("bad");
			
			out.println("ccc");
			out.flush();
			client.close();
		
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		
		
	}
	
	

}
