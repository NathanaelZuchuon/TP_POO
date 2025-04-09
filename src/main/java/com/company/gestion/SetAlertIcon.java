package com.company.gestion;

import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Objects;
import java.util.Optional;

public class SetAlertIcon {

	public static Optional<ButtonType> showInformationAlert(Alert.AlertType type, String title, String header, String message) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(message);

		// Récupérer la fenêtre (Stage) de l'alerte
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

		// Ajouter l'icône à la fenêtre
		try {
			Image icon = new Image(Objects.requireNonNull(SetAlertIcon.class.getResourceAsStream("boutique.jpg")));
			stage.getIcons().add(icon);

		} catch (Exception e) {
			System.err.println("Impossible de charger l'icône: " + e.getMessage());
		}

		return alert.showAndWait();
	}
}
