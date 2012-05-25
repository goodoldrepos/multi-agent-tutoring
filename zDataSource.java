package com.jadex;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class zDataSource {

	private String driver, url;
	private ResultSet rs;
	private Connection connexion;
	private Statement s;

	public zDataSource(String url, String driver) {

		this.url = url;
		this.driver = driver;
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("Error Class.forName(driver) ");
		}

		try {

			connexion = DriverManager.getConnection(url);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error DriverManager.getConnection(url)");

		}

	}

	public void Requete(String req) {
		try {
			s = connexion.createStatement();
			s.execute(req);
		} catch (SQLException e) {
			System.out.println(e.getMessage());

		}
	}

	public ResultSet extraction(String req) {

		try {
			s = connexion.createStatement();
		} catch (SQLException e) {
			System.out.println("Error Extraction.createStatement");

		}
		try {
			rs = s.executeQuery(req);
		} catch (SQLException e) {
			System.out.println("Error executeQuery");

		}

		return rs;
	}

}
