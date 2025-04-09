package com.company.gestion;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.scene.image.ImageView;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;

import java.io.File;
import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.io.ByteArrayInputStream;
import java.time.format.DateTimeFormatter;

public class StolenItemChecker extends Application {

	private final StolenItemDAO stolenItemDAO = new StolenItemDAO();
	private final ObservableList<StolenItem> stolenItemsList = FXCollections.observableArrayList();
	private final ObservableList<ItemImage> imagesList = FXCollections.observableArrayList();

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("KEN'S Brocante !");

		// Création des onglets
		TabPane tabPane = new TabPane();
		Tab searchTab = new Tab("Recherche d'Objet");
		Tab reportTab = new Tab("Signaler un Vol");
		Tab adminTab = new Tab("Administration");

		searchTab.setContent(createSearchTab());
		reportTab.setContent(createReportTab());
		adminTab.setContent(createAdminTab());

		tabPane.getTabs().addAll(searchTab, reportTab, adminTab);

		Scene scene = new Scene(tabPane, 900, 1000);

		Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("boutique.jpg")));
		primaryStage.getIcons().add(icon); primaryStage.setResizable(false);

		primaryStage.setScene(scene);
		primaryStage.show();

		// Charger les données initiales
		refreshItemsList();
	}

	private VBox createSearchTab() {
		VBox vbox = new VBox(10);
		vbox.setPadding(new Insets(20));
		vbox.setAlignment(Pos.TOP_CENTER);

		Label titleLabel = new Label("Vérifiez si un objet est signalé comme volé");
		titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

		ComboBox<String> typeComboBox = new ComboBox<>();
		typeComboBox.setPromptText("Type d'objet");
		typeComboBox.getItems().addAll("Téléphone", "Ordinateur portable", "Tablette", "Appareil photo", "Console de jeux", "Autre");
		typeComboBox.setPrefWidth(300);

		TextField modelField = new TextField();
		modelField.setPromptText("Modèle (ex: iPhone 13, MacBook Pro, etc.)");
		modelField.setPrefWidth(300);

		TextField serialField = new TextField();
		serialField.setPromptText("Numéro de série");
		serialField.setPrefWidth(300);

		Button searchButton = new Button("Rechercher");
		searchButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

		// Zone de résultat avec une partie texte et images
		VBox resultBox = new VBox(10);
		TextArea resultArea = new TextArea();
		resultArea.setEditable(false);
		resultArea.setPrefHeight(150);
		resultArea.setWrapText(true);

		// Zone pour afficher les images
		FlowPane imagesPane = new FlowPane(10, 10);
		imagesPane.setPrefHeight(200);
		imagesPane.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10;");

		Label imagesLabel = new Label("Photos de l'objet:");
		ScrollPane scrollPane = new ScrollPane(imagesPane);
		scrollPane.setFitToWidth(true);
		scrollPane.setPrefHeight(220);

		searchButton.setOnAction(e -> {
			String serial = serialField.getText();

			if (serial.isEmpty()) {
				resultArea.setText("Veuillez entrer un numéro de série pour effectuer la recherche.");
				imagesPane.getChildren().clear();
				return;
			}

			Optional<StolenItem> foundItemOpt = stolenItemDAO.findBySerialNumber(serial);

			if (foundItemOpt.isPresent()) {
				StolenItem item = foundItemOpt.get();
				resultArea.setText("ALERTE: Cet objet a été signalé comme volé !\n\n" +
						"Type: " + item.getType() + "\n" +
						"Modèle: " + item.getModel() + "\n" +
						"Description: " + item.getDescription() + "\n" +
						"Date de signalement: " + item.getReportDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n\n" +
						"Le propriétaire a été alerté automatiquement.");

				// Afficher les images
				imagesPane.getChildren().clear();
				if (item.getImages() != null && !item.getImages().isEmpty()) {
					for (ItemImage img : item.getImages()) {
						try {
							Image image = new Image(new ByteArrayInputStream(img.getImageData()));
							ImageView imageView = new ImageView(image);
							imageView.setFitHeight(150);
							imageView.setFitWidth(150);
							imageView.setPreserveRatio(true);
							imagesPane.getChildren().add(imageView);

						} catch (Exception ex) {
							System.err.println("Erreur lors du chargement d'une image: " + ex.getMessage());
						}
					}
				} else {
					imagesPane.getChildren().add(new Label("Aucune image disponible pour cet objet."));
				}

				// Simuler l'alerte au propriétaire
				showAlert("Alerte envoyée", "Le propriétaire de l'objet a été alerté par email et SMS.");

			} else {
				resultArea.setText("Aucun signalement trouvé pour cet objet. Il n'apparaît pas dans notre base de données d'objets volés.");
				imagesPane.getChildren().clear();
				imagesPane.getChildren().add(new Label("Aucun objet trouvé."));
			}
		});

		resultBox.getChildren().addAll(resultArea, imagesLabel, scrollPane);

		vbox.getChildren().addAll(titleLabel, new Separator(),
				new Label("Type d'objet:"), typeComboBox,
				new Label("Modèle:"), modelField,
				new Label("Numéro de série:"), serialField,
				searchButton,
				new Label("Résultat:"), resultBox);
		return vbox;
	}

	private VBox createReportTab() {
		VBox vbox = new VBox(10);
		vbox.setPadding(new Insets(20));
		vbox.setAlignment(Pos.TOP_CENTER);

		Label titleLabel = new Label("Signaler un objet volé");
		titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

		ComboBox<String> typeComboBox = new ComboBox<>();
		typeComboBox.setPromptText("Type d'objet");
		typeComboBox.getItems().addAll("Téléphone", "Ordinateur portable", "Tablette", "Appareil photo", "Console de jeux", "Autre");
		typeComboBox.setPrefWidth(300);

		TextField modelField = new TextField();
		modelField.setPromptText("Modèle (ex: iPhone 13, MacBook Pro, etc.)");
		modelField.setPrefWidth(300);

		TextField serialField = new TextField();
		serialField.setPromptText("Numéro de série");
		serialField.setPrefWidth(300);

		TextArea descriptionField = new TextArea();
		descriptionField.setPromptText("Description détaillée de l'objet (couleur, signes particuliers, etc.)");
		descriptionField.setPrefWidth(300);
		descriptionField.setPrefHeight(100);

		TextField nameField = new TextField();
		nameField.setPromptText("Votre nom");
		nameField.setPrefWidth(300);

		TextField emailField = new TextField();
		emailField.setPromptText("Votre email");
		emailField.setPrefWidth(300);

		TextField phoneField = new TextField();
		phoneField.setPromptText("Votre numéro de téléphone");
		phoneField.setPrefWidth(300);

		TextField policeReportField = new TextField();
		policeReportField.setPromptText("Numéro de procès-verbal de police");
		policeReportField.setPrefWidth(300);

		CheckBox policeReportCheckBox = new CheckBox("J'ai déposé plainte auprès de la police");

		// Section pour ajouter des images
		Label imagesLabel = new Label("Ajouter des photos de l'objet:");
		Button addImageButton = new Button("Ajouter une photo");

		// Affichage des images ajoutées
		FlowPane imagesPane = new FlowPane(10, 10);
		imagesPane.setPrefHeight(150);
		imagesPane.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10;");
		ScrollPane imagesScrollPane = new ScrollPane(imagesPane);
		imagesScrollPane.setFitToWidth(true);
		imagesScrollPane.setPrefHeight(170);

		addImageButton.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Sélectionner une image");
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
			);
			File selectedFile = fileChooser.showOpenDialog(null);

			if (selectedFile != null) {
				try {
					byte[] imageData = Files.readAllBytes(selectedFile.toPath());
					ItemImage newImage = new ItemImage();
					newImage.setImageData(imageData);

					newImage.setImageName("i_n");
					newImage.setContentType("c_t");

					imagesList.add(newImage);

					// Afficher la miniature
					Image image = new Image(new ByteArrayInputStream(imageData));
					ImageView imageView = new ImageView(image);
					imageView.setFitHeight(120);
					imageView.setFitWidth(120);
					imageView.setPreserveRatio(true);

					// Ajouter bouton de suppression
					Button removeButton = new Button("X");
					removeButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
					removeButton.setOnAction(event -> {
						imagesList.remove(newImage);
						imagesPane.getChildren().remove(imageView.getParent());
					});

					VBox imageBox = new VBox(5);
					imageBox.setAlignment(Pos.CENTER);
					imageBox.getChildren().addAll(imageView, removeButton);
					imagesPane.getChildren().add(imageBox);

				} catch (IOException ex) {
					showAlert("Erreur", "Impossible de charger l'image: " + ex.getMessage());
				}
			}
		});

		Button submitButton = new Button("Signaler le vol");
		submitButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

		submitButton.setOnAction(e -> {
			// Validation des champs
			if (typeComboBox.getValue() == null || modelField.getText().isEmpty() || serialField.getText().isEmpty() ||
					nameField.getText().isEmpty() || emailField.getText().isEmpty() || phoneField.getText().isEmpty()) {
				showAlert("Erreur", "Veuillez remplir tous les champs obligatoires.");
				return;
			}

			// Créer un nouvel objet volé
			StolenItem newItem = new StolenItem();
			newItem.setType(typeComboBox.getValue());
			newItem.setModel(modelField.getText());
			newItem.setSerialNumber(serialField.getText());
			newItem.setDescription(descriptionField.getText());
			newItem.setOwnerName(nameField.getText());
			newItem.setOwnerEmail(emailField.getText());
			newItem.setOwnerPhone(phoneField.getText());
			newItem.setPoliceReportNumber(policeReportField.getText());
			newItem.setReportDate(LocalDateTime.now());
			newItem.setImages(imagesList);

			// Enregistrer dans la base de données
			stolenItemDAO.addStolenItem(newItem);

			showAlert("Signalement enregistré", "Votre signalement a été enregistré avec succès dans notre base de données.");

			// Réinitialiser le formulaire
			typeComboBox.setValue(null);
			modelField.clear();
			serialField.clear();
			descriptionField.clear();
			nameField.clear();
			emailField.clear();
			phoneField.clear();
			policeReportField.clear();
			policeReportCheckBox.setSelected(false);
			imagesList.clear();
			imagesPane.getChildren().clear();

			// Rafraîchir la liste des objets dans l'onglet Admin
			refreshItemsList();
		});

		vbox.getChildren().addAll(titleLabel, new Separator(),
				new Label("Type d'objet:"), typeComboBox,
				new Label("Modèle:"), modelField,
				new Label("Numéro de série:"), serialField,
				new Label("Description:"), descriptionField,
				new Label("Vos coordonnées:"),
				nameField, emailField, phoneField,
				new Label("Informations sur la plainte:"),
				policeReportCheckBox, policeReportField,
				imagesLabel, addImageButton, imagesScrollPane,
				submitButton);
		return vbox;
	}

	private VBox createAdminTab() {
		VBox vbox = new VBox(10);
		vbox.setPadding(new Insets(20));
		vbox.setAlignment(Pos.TOP_CENTER);

		Label titleLabel = new Label("Administration des objets volés");
		titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

		// Tableau des objets volés
		TableView<StolenItem> itemsTable = new TableView<>();
		itemsTable.setItems(stolenItemsList);

		TableColumn<StolenItem, String> typeCol = new TableColumn<>("Type");
		typeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));

		TableColumn<StolenItem, String> modelCol = new TableColumn<>("Modèle");
		modelCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getModel()));

		TableColumn<StolenItem, String> serialCol = new TableColumn<>("Numéro de série");
		serialCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSerialNumber()));

		TableColumn<StolenItem, String> ownerCol = new TableColumn<>("Propriétaire");
		ownerCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOwnerName()));

		TableColumn<StolenItem, String> dateCol = new TableColumn<>("Date de signalement");
		dateCol.setCellValueFactory(cellData ->
				new SimpleStringProperty(cellData.getValue().getReportDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
		);

		itemsTable.getColumns().addAll(typeCol, modelCol, serialCol, ownerCol, dateCol);
		itemsTable.setPrefHeight(400);

		// Zone de détails pour l'élément sélectionné
		VBox detailsBox = new VBox(10);
		detailsBox.setPadding(new Insets(10));
		detailsBox.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1px; -fx-border-radius: 5px;");

		TextArea detailsArea = new TextArea();
		detailsArea.setEditable(false);
		detailsArea.setPrefHeight(100);
		detailsArea.setWrapText(true);

		FlowPane imageDetails = new FlowPane(10, 10);
		ScrollPane imageScroll = new ScrollPane(imageDetails);
		imageScroll.setFitToWidth(true);
		imageScroll.setPrefHeight(150);

		Button deleteButton = new Button("Supprimer le signalement");
		deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
		deleteButton.setDisable(true);

		detailsBox.getChildren().addAll(new Label("Détails:"), detailsArea,
				new Label("Images:"), imageScroll, deleteButton);

		// Gérer la sélection dans le tableau
		itemsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				detailsArea.setText(
						"Type: " + newSelection.getType() + "\n" +
								"Modèle: " + newSelection.getModel() + "\n" +
								"Numéro de série: " + newSelection.getSerialNumber() + "\n" +
								"Description: " + newSelection.getDescription() + "\n" +
								"Propriétaire: " + newSelection.getOwnerName() + "\n" +
								"Contact: " + newSelection.getOwnerEmail() + " / " + newSelection.getOwnerPhone() + "\n" +
								"PV Police: " + newSelection.getPoliceReportNumber() + "\n" +
								"Date: " + newSelection.getReportDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
				);

				// Afficher les images
				imageDetails.getChildren().clear();

				Optional<StolenItem> foundItemOpt = stolenItemDAO.findBySerialNumber(newSelection.getSerialNumber());
				StolenItem item = foundItemOpt.get();

				if (item.getImages() != null && !item.getImages().isEmpty()) {
					for (ItemImage img : item.getImages()) {
						try {
							Image image = new Image(new ByteArrayInputStream(img.getImageData()));
							ImageView imageView = new ImageView(image);
							imageView.setFitHeight(150);
							imageView.setFitWidth(150);
							imageView.setPreserveRatio(true);
							imageDetails.getChildren().add(imageView);

						} catch (Exception ex) {
							System.err.println("Erreur lors du chargement d'une image: " + ex.getMessage());
						}
					}

				} else {
					imageDetails.getChildren().add(new Label("Aucune image disponible"));
				}

				deleteButton.setDisable(false);

			} else {
				detailsArea.clear();
				imageDetails.getChildren().clear();
				deleteButton.setDisable(true);
			}
		});

		// Action du bouton supprimer
		deleteButton.setOnAction(e -> {
			StolenItem selectedItem = itemsTable.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				Optional<ButtonType> result = SetAlertIcon.showInformationAlert(Alert.AlertType.CONFIRMATION, "Confirmation",
						"Supprimer le signalement", "Êtes-vous sûr de vouloir supprimer ce signalement ?");;

				if (result.isPresent() && result.get() == ButtonType.OK) {
					stolenItemDAO.deleteStolenItem(selectedItem.getId());
					refreshItemsList();
					detailsArea.clear();
					imageDetails.getChildren().clear();
					deleteButton.setDisable(true);
				}
			}
		});

		Button refreshButton = new Button("Rafraîchir la liste");
		refreshButton.setOnAction(e -> refreshItemsList());

		vbox.getChildren().addAll(titleLabel, new Separator(), refreshButton, itemsTable, detailsBox);
		return vbox;
	}

	private void refreshItemsList() {
		stolenItemsList.clear();
		List<StolenItem> items = stolenItemDAO.getAllStolenItems();
		stolenItemsList.addAll(items);
	}

	private void showAlert(String title, String message) {
		SetAlertIcon.showInformationAlert(Alert.AlertType.INFORMATION, title, null, message);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
