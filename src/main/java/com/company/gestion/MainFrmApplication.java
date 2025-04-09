package com.company.gestion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class MainFrmApplication extends Application {
	// Paramètres de connexion
	private static final String URL = "jdbc:mysql://localhost:3306/test";
	private static final String USER = "root";
	private static final String PASSWORD = "";

	// Méthode de connexion à la base de données
	public static Connection getConnection() {
		Connection connection = null;
		try {
			// Charger le driver JDBC MySQL
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Établir la connexion
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("✅ Connexion à la base de données réussie !");
		} catch (ClassNotFoundException e) {
			System.out.println("Erreur : Driver MySQL non trouvé");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Erreur de connexion à la base de données");
			e.printStackTrace();
		}
		return connection;
	}

	// Méthode pour fermer la connexion
	public static void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
				System.out.println("Connexion fermée avec succès ✅");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void start(Stage stage) throws IOException {
		// Tenter une connexion au démarrage de l'application
		Connection connection = getConnection();

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 1200, 600);
		stage.setTitle("Gestionnaire.");
		stage.setScene(scene);

		// Ajouter l'icône de la fenêtre
		Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.jpg")));
		stage.getIcons().add(icon);

		// Affichage
		stage.show();

		// Crée un nouveau répertoire
		Repertoire rip = new Repertoire();
		rip.setConnection(connection);

		// Récupérer le contrôleur pour potentiellement passer la connexion
		MainFrmApplicationController controller = fxmlLoader.getController();
		if (controller != null) {
			controller.setConnection(connection, rip);
		}
	}

	public static void main(String[] args) {
		launch();
	}
}