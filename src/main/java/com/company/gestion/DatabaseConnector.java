package com.company.gestion;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DatabaseConnector {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/stolen_items_db";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "";

	private static DatabaseConnector instance;

	private DatabaseConnector() {
		// Chargement du driver MySQL
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Erreur de chargement du driver MySQL: " + e.getMessage());
		}
	}

	public static synchronized DatabaseConnector getInstance() {
		if (instance == null) {
			instance = new DatabaseConnector();
		}
		return instance;
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	}

	// Méthode utilitaire pour fermer proprement les ressources
	public static void closeResources(AutoCloseable... resources) {
		for (AutoCloseable resource : resources) {
			if (resource != null) {
				try {
					resource.close();

				} catch (Exception e) {
					System.err.println("Erreur lors de la fermeture d'une ressource: " + e.getMessage());
				}
			}
		}
	}
}
