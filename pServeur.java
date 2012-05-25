package com.jadex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;

import jadex.adapter.fipa.AgentIdentifier;
import jadex.runtime.*;

public class pServeur extends Plan implements Runnable
{
 
	protected ServerSocket	server;
	 
 
	public pServeur(int port) 
	{
		System.out.println("[pServeur]");
		try
		{
			this.server	= new ServerSocket(port);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e.getMessage());
		}
 	}

	public void close()
	{
		try
		{
			server.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public void body()
	{
		new Thread(this).start();

		getScope().addAgentListener(new IAgentListener()
		{
			public void agentTerminating(AgentEvent ae)
			{
				close();
			}
		}
		, false);

	}

	public void	run()
	{
		try
		{
			while(true)
			{
				Socket	client	= server.accept();

				IGoal goal = getExternalAccess().getGoalbase().createGoal("routes");
				goal.getParameter("client").setValue(client);
				getExternalAccess().getGoalbase().dispatchTopLevelGoal(goal);
				
			}
		}
		catch(IOException e)
		{
 		}
		catch(AgentDeathException e)
		{
			close();
		}
	}
}
