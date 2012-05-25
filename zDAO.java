package com.jadex;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class zDAO {

	private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:8889/University?user=root&password=root"; //mamp
																								
	public static boolean ajouterApprenant(String[] t) {
		zDataSource d = new zDataSource(url, driver);
		
		Hashtable<String, String> h = getApprenant(t[1]);
		
		if(h == null)
		{
			d.Requete("insert into apprenants(CNE,nom,prenom,sexe,password,profil,niveau) " +
					"values('"+ t[1] + "','" + t[2] + "','" + t[3] + "','" + t[4] 
							+ "','" + t[5] + "','" + "intellectuel" + "','" + "L3" + "')");
			return true;
		}

		return false;
	}

	public static Hashtable<String, String> getApprenant(String CNE) {

		zDataSource d = new zDataSource(url, driver);
		Hashtable<String, String> h = new Hashtable<String, String>();
		ResultSet rs;

		rs = d.extraction("select * from apprenants where CNE='" + CNE + "'");

		try {
			if (!rs.next()) return null;
			h.put("nom", rs.getString("nom"));
			h.put("prenom", rs.getString("prenom"));
			h.put("sexe", rs.getString("sexe"));
			h.put("profil", rs.getString("profil"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return h;
	}
	
	public static ArrayList<String> listerApprenants()
	{
		zDataSource d = new zDataSource(url, driver);
		
		ResultSet rs; 
		
		ArrayList<String> a = new ArrayList<String>();
		
		rs = d.extraction("select CNE from apprenants");
		
		try {
			while(rs.next())
			{
				a.add(rs.getString("CNE"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return a;
	}

	public static Boolean verifierPassword(String CNE, String motdepasse) {

		zDataSource d = new zDataSource(url, driver);

		ResultSet rs;

		rs = d.extraction("select * from apprenants where CNE='" + CNE + "'");

		String password = "";
		try {
			if (!rs.next())
				return false;
			password = rs.getString("password");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (password.equals(motdepasse))
			return true;
		else
			return false;
	}
	
	public static String debut(String ref)
	{
		zDataSource d = new zDataSource(url,driver);
		ArrayList<String> a = new ArrayList<String>();
		ResultSet rs = null;
		Random r = new Random();
		
		
		rs = d.extraction("select message from expressions_debut where ressource='" + ref +"'");
		try {
			while(rs.next())
			{
				a.add(rs.getString(1));
			}
			int n = r.nextInt(a.size());
			System.out.println(a.get(n));
			return a.get(n);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return " ";
	}
	
	public static String fin(String profil,String type)
	{
		zDataSource d = new zDataSource(url, driver);
		
		ResultSet rs = d.extraction("select expression from expressions_fin e,exp_fin_profils p " +
				"where p.id_exp_fin=e.id and lower(p.profil)='"+ profil.toLowerCase() + "' and type='" + type + "'" );
		Random r = new Random();
		ArrayList<String> a = new ArrayList<String>();
		try {
			while(rs.next())
			{
				a.add(rs.getString("expression"));
			}
			int n = r.nextInt(a.size());
			return a.get(n);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "Erreur";
	}
	
	

}
