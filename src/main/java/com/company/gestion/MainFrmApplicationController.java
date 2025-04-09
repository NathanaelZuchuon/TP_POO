package com.company.gestion;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.Objects;

public class MainFrmApplicationController {
	@FXML
	private TableView<Contact> contactsTableView;

	@FXML
	private TableColumn<Contact, String> codeColumn;

	@FXML
	private TableColumn<Contact, String> nameColumn;

	@FXML
	private TableColumn<Contact, String> birthDate;

	@FXML
	private TableColumn<Contact, String> address;

	@FXML
	private TableColumn<Contact, String> email;

	@FXML
	private TableColumn<Contact, String> telNumber;

	private Connection connection;
	private Repertoire repertoire;
	private Stage AjouterContactStage = null;

	// Méthode pour définir la connexion
	public void setConnection(Connection connection, Repertoire rip) {
		this.connection = connection;
		this.repertoire = rip;
	}

	// N'oubliez pas de fermer la connexion quand l'application se termine
	public void closeConnection() {
		MainFrmApplication.closeConnection(this.connection);
	}
	// ---

	private void initializeTableView() {
		// Configuration des colonnes
		codeColumn.setCellValueFactory(cellData -> cellData.getValue().codeProperty());
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		birthDate.setCellValueFactory(cellData -> cellData.getValue().birthDateProperty());
		address.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
		email.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
		telNumber.setCellValueFactory(cellData -> cellData.getValue().telNumberProperty());

		// Charger les contacts dans la TableView
		contactsTableView.setItems(repertoire.listerContacts());
	}

	@FXML
	protected void onEnregistrerAction() {
		// Méthode pour enregistrer tous les contacts dans la base de données
	}

	@FXML
	protected void onQuitterAction() {
		this.closeConnection();
		Platform.exit();
	}

	@FXML
	protected void onListeContactsAction() {
		// Actualiser ou recharger la liste des contacts
		initializeTableView();
	}

	@FXML
	protected void onRechercherAction() {
		// Implémenter la recherche de contacts
	}

	@FXML
	protected void onAjouterContactAction() throws IOException {
		// Ouvrir une fenêtre pour ajouter un nouveau contact
		if ( this.AjouterContactStage == null ) {
			this.AjouterContactStage = new Stage();

			FXMLLoader fxmlLoader = new FXMLLoader(MainFrmApplicationController.class.getResource("ajouter-contact-view.fxml"));
			Scene scene = new Scene(fxmlLoader.load(), 1200, 600);
			this.AjouterContactStage.setTitle("Gestionnaire.");
			this.AjouterContactStage.setScene(scene);

			Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.jpg")));
			this.AjouterContactStage.getIcons().add(icon);

			// Récupérer le contrôleur pour potentiellement passer la connexion
			AjouterContactController controller = fxmlLoader.getController();
			if (controller != null) {
				controller.setConnection(this.connection, this.repertoire);
			}
		}
		this.AjouterContactStage.setX(100);
		this.AjouterContactStage.setY(100);

		this.AjouterContactStage.show();
	}

	@FXML
	protected void onModifierContactAction() {
		// Ouvrir une fenêtre pour modifier le contact sélectionné
		Contact contactSelectionne = contactsTableView.getSelectionModel().getSelectedItem();
		if (contactSelectionne != null) {
			System.out.println("Modif...");
			// repertoire.modifierContact(contactModifie);
		}
	}

	@FXML
	protected void onSupprimerContactAction() {
		// Supprimer le contact sélectionné
		Contact contactSelectionne = contactsTableView.getSelectionModel().getSelectedItem();
		if (contactSelectionne != null) {
			repertoire.supprimerContact(contactSelectionne.getCode());
		}
	}
}