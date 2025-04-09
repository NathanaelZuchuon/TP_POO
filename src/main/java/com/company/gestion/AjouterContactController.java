package com.company.gestion;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.Connection;

public class AjouterContactController {

	@FXML private GridPane gridPane;

	@FXML private TextField codeTextField;
	@FXML private TextField nomTextField;
	@FXML private DatePicker dateNaissanceDatePicker;
	@FXML private TextField addressTextField;
	@FXML private TextField emailTextField;
	@FXML private TextField telNumberTextField;

	@FXML private ComboBox<String> typeComboBox;

	// --- Enseignant
	@FXML private Label statutLabel;
	@FXML private ComboBox<String> statutComboBox;
	// ---

	// --- Etudiant
	@FXML private Label cycleLabel;
	@FXML private ComboBox<String> cycleComboBox;

	@FXML private Label levelLabel;
	@FXML private TextField levelTextField;
	// ---

	// --- Agent
	@FXML private Label salaryLabel;
	@FXML private TextField salaryTextField;

	@FXML private Label statutAgentLabel;
	@FXML private ComboBox<String> statutAgentComboBox;

	@FXML private Label categoryLabel;
	@FXML private ComboBox<String> categoryComboBox;

	@FXML private Label salaryIndLabel;
	@FXML private TextField salaryIndTextField;

	@FXML private Label occupationLabel;
	@FXML private TextField occupationTextField;
	// ---

	private Repertoire repertoire;

	@FXML
	public void initialize() {
		// Initialiser les types de contacts
		typeComboBox.getItems().addAll("Etudiant", "Enseignant", "Agent");

		// Ajout d'un écouteur permanent sur la valeur du ComboBox
		typeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			// Méthode appelée à chaque changement de valeur
			if (newValue != null) {
				onTypeContactChanged(newValue);
			}
		});

		telNumberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				telNumberTextField.setText(newValue.replaceAll("\\D", ""));
			}
		});
	}

	private void onTypeContactChanged(String nouveauType) {
		switch (nouveauType) {
			case "Etudiant":
				// Supprimer le champ statut s'il existe déjà
				supprimerChampsStatut();

				// Supprimer les champs d'Agent s'ils existent déjà
				supprimerChampsSalary();
				supprimerChampsStatutAgent();
				supprimerChampsCategory();
				supprimerChampsSalaryInd();
				supprimerChampsOccupation();

				// Actions spécifiques pour un étudiant
				if (cycleLabel == null && cycleComboBox == null && levelLabel == null && levelTextField == null) {
					configurerChampsEtudiant();
				}
				break;

			case "Enseignant":
				// Supprimer le champ cycle s'il existe déjà
				supprimerChampsCycle();

				// Supprimer le champ level s'il existe déjà
				supprimerChampsLevel();

				// Supprimer les champs d'Agent s'ils existent déjà
				supprimerChampsSalary();
				supprimerChampsStatutAgent();
				supprimerChampsCategory();
				supprimerChampsSalaryInd();
				supprimerChampsOccupation();

				// Actions spécifiques pour un enseignant
				if (statutLabel == null && statutComboBox == null) {
					configurerChampsEnseignant();
				}
				break;

			case "Agent":
				// Supprimer le champ statut s'il existe déjà
				supprimerChampsStatut();

				// Supprimer le champ cycle s'il existe déjà
				supprimerChampsCycle();

				// Supprimer le champ level s'il existe déjà
				supprimerChampsLevel();

				// Actions spécifiques pour un agent
				if (salaryLabel == null && salaryTextField == null &&
						statutAgentLabel == null && statutAgentComboBox == null &&
						categoryLabel == null && categoryComboBox == null &&
						salaryIndLabel == null && salaryIndTextField == null &&
						occupationLabel == null && occupationTextField == null) {
					configurerChampsAgent();
				}
				break;
		}
	}

	private void supprimerChampsSalary() {
		// Supprimer les champs salary s'ils existent
		if (salaryLabel != null && salaryTextField != null) {
			gridPane.getChildren().removeAll(salaryLabel, salaryTextField);
			salaryLabel = null;
			salaryTextField = null;
		}
	}

	private void supprimerChampsStatutAgent() {
		// Supprimer les champs statutAgent s'ils existent
		if (statutAgentLabel != null && statutAgentComboBox != null) {
			gridPane.getChildren().removeAll(statutAgentLabel, statutAgentComboBox);
			statutAgentLabel = null;
			statutAgentComboBox = null;
		}
	}

	private void supprimerChampsCategory() {
		// Supprimer les champs cycle s'ils existent
		if (categoryLabel != null && categoryComboBox != null) {
			gridPane.getChildren().removeAll(categoryLabel, categoryComboBox);
			categoryLabel = null;
			categoryComboBox = null;
		}
	}

	private void supprimerChampsSalaryInd() {
		// Supprimer les champs cycle s'ils existent
		if (salaryIndLabel != null && salaryIndTextField != null) {
			gridPane.getChildren().removeAll(salaryIndLabel, salaryIndTextField);
			salaryIndLabel = null;
			salaryIndTextField = null;
		}
	}

	private void supprimerChampsOccupation() {
		// Supprimer les champs cycle s'ils existent
		if (occupationLabel != null && occupationTextField != null) {
			gridPane.getChildren().removeAll(occupationLabel, occupationTextField);
			occupationLabel = null;
			occupationTextField = null;
		}
	}

	private void supprimerChampsCycle() {
		// Supprimer les champs cycle s'ils existent
		if (cycleLabel != null && cycleComboBox != null) {
			gridPane.getChildren().removeAll(cycleLabel, cycleComboBox);
			cycleLabel = null;
			cycleComboBox = null;
		}
	}

	private void supprimerChampsLevel() {
		// Supprimer les champs cycle s'ils existent
		if (levelLabel != null && levelTextField != null) {
			gridPane.getChildren().removeAll(levelLabel, levelTextField);
			levelLabel = null;
			levelTextField = null;
		}
	}

	private void supprimerChampsStatut() {
		// Supprimer les champs statut s'ils existent
		if (statutLabel != null && statutComboBox != null) {
			gridPane.getChildren().removeAll(statutLabel, statutComboBox);
			statutLabel = null;
			statutComboBox = null;
		}
	}

	private void configurerChampsAgent() {
		// Créer les labels
		salaryLabel = new Label("Salaire :");
		salaryLabel.setAlignment(Pos.CENTER_RIGHT);

		salaryTextField = new TextField();
		salaryTextField.setPromptText("Entrez le salaire");

		statutAgentLabel = new Label("Statut :");
		statutAgentComboBox = new ComboBox<>();
		statutAgentComboBox.getItems().addAll("TEMPORAIRE", "STAGIAIRE","PERMANENT");
		statutAgentComboBox.setPromptText("Sélectionnez le statut");

		categoryLabel = new Label("Catégorie :");
		categoryComboBox = new ComboBox<>();
		categoryComboBox.getItems().addAll("A", "B", "C");
		categoryComboBox.setPromptText("Sélectionnez la catégorie");

		salaryIndLabel = new Label("Indice de salaire :");
		salaryIndLabel.setAlignment(Pos.CENTER_RIGHT);

		salaryIndTextField = new TextField();
		salaryIndTextField.setPromptText("Entrez l'indice de salaire");

		occupationLabel = new Label("Occupation :");
		occupationLabel.setAlignment(Pos.CENTER_RIGHT);

		occupationTextField = new TextField();
		occupationTextField.setPromptText("Entrez l'occupation");

		salaryTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				salaryTextField.setText(newValue.replaceAll("\\D", ""));
			}
		});
		salaryIndTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				salaryIndTextField.setText(newValue.replaceAll("\\D", ""));
			}
		});

		gridPane.add(salaryLabel, 0, 7);
		gridPane.add(salaryTextField, 1, 7);

		gridPane.add(statutAgentLabel, 0, 8);
		gridPane.add(statutAgentComboBox, 1, 8);

		gridPane.add(categoryLabel, 0, 9);
		gridPane.add(categoryComboBox, 1, 9);

		gridPane.add(salaryIndLabel, 0, 10);
		gridPane.add(salaryIndTextField, 1, 10);

		gridPane.add(occupationLabel, 0, 11);
		gridPane.add(occupationTextField, 1, 11);
	}

	private void configurerChampsEnseignant() {
		// Créer le label
		statutLabel = new Label("Statut :");
		statutLabel.setAlignment(Pos.CENTER_RIGHT);

		// Créer le ComboBox pour le statut
		statutComboBox = new ComboBox<>();
		statutComboBox.getItems().addAll("VACATAIRE", "PERMANENT");
		statutComboBox.setPromptText("Sélectionnez le statut");

		// Ajouter au GridPane
		gridPane.add(statutLabel, 0, 7);
		gridPane.add(statutComboBox, 1, 7);
	}

	private void configurerChampsEtudiant() {
		// Créer les labels
		cycleLabel = new Label("Cycle :");
		cycleLabel.setAlignment(Pos.CENTER_RIGHT);

		levelLabel = new Label("Niveau :");
		levelLabel.setAlignment(Pos.CENTER_RIGHT);

		// Créer le ComboBox pour le statut
		cycleComboBox = new ComboBox<>();
		cycleComboBox.getItems().addAll("LICENCE", "INGENIEUR");
		cycleComboBox.setPromptText("Sélectionnez le cycle");

		// Créer le TextField pour le statut
		levelTextField = new TextField();
		levelTextField.setPromptText("Sélectionnez le niveau");

		// Ajouter au GridPane
		gridPane.add(cycleLabel, 0, 7);
		gridPane.add(cycleComboBox, 1, 7);

		gridPane.add(levelLabel, 0, 8);
		gridPane.add(levelTextField, 1, 8);
	}

	// Méthode pour définir la connexion
	public void setConnection(Connection connection, Repertoire repertoire) {
		this.repertoire = repertoire;
	}

	@FXML
	protected void onAjouterContactAction() {
		// Validation de base
		if (validerChamps()) {
			// Créer le contact en fonction du type sélectionné
			Contact nouveauContact = creerContact();

			if (nouveauContact != null) {
				// Ajouter au répertoire
				repertoire.ajouterContact(nouveauContact);

			} else {
				Alert alert = new Alert(Alert.AlertType.WARNING, "Remplissez TOUS les champs !");
				alert.show();
			}
		}
	}

	private boolean validerChamps() {
		// Validation basique des champs
		if (codeTextField.getText().isEmpty() ||
				nomTextField.getText().isEmpty() ||
				typeComboBox.getValue() == null ||
				dateNaissanceDatePicker.getValue() == null ||
				addressTextField.getText().isEmpty() ||
				emailTextField.getText().isEmpty() ||
				telNumberTextField.getText().isEmpty()) {

			// Afficher une alerte d'erreur.
			Alert alert = new Alert(Alert.AlertType.WARNING, "Remplissez TOUS les champs !");
			alert.show();

			if (typeComboBox.getValue() == null) return false;

			switch (typeComboBox.getValue()) {
				case "Agent":
					if (salaryTextField.getText().isEmpty() ||
							statutAgentComboBox.getValue() == null ||
							categoryComboBox.getValue() == null ||
							salaryIndTextField.getText().isEmpty() ||
							occupationTextField.getText().isEmpty()) {
						return false;
					}
					break;

				case "Enseignant":
					if (statutComboBox.getValue() == null) {
						return false;
					}
					break;

				case "Etudiant":
					if (cycleComboBox.getValue() == null ||
							levelTextField.getText().isEmpty()) {
						return false;
					}
					break;

				default:
					return false;
			}
		}
		return true;
	}

	private Contact creerContact() {
		// Créer le bon type de contact selon la sélection
		switch (typeComboBox.getValue()) {

			case "Etudiant":
				if (cycleComboBox.getValue() == null) {
					return null;

				} else {
					Etudiant.Cycle c = switch (cycleComboBox.getValue()) {
						case "LICENCE" -> Etudiant.Cycle.LICENCE;
						case "INGENIEUR" -> Etudiant.Cycle.INGENIEUR;
						default -> null;
					};
					return new Etudiant(
							codeTextField.getText(),
							nomTextField.getText(),
							dateNaissanceDatePicker.getValue(),
							addressTextField.getText(),
							emailTextField.getText(),
							telNumberTextField.getText(),
							c,
							levelTextField.getText()
					);
				}

			case "Enseignant":
				if (statutComboBox.getValue() == null) {
					return null;

				} else {
					Enseignant.Statut s = switch (statutComboBox.getValue()) {
						case "VACATAIRE" -> Enseignant.Statut.VACATAIRE;
						case "PERMANENT" -> Enseignant.Statut.PERMANENT;
						default -> null;
					};

					return new Enseignant(
							codeTextField.getText(),
							nomTextField.getText(),
							dateNaissanceDatePicker.getValue(),
							addressTextField.getText(),
							emailTextField.getText(),
							telNumberTextField.getText(),
							s
					);
				}

			case "Agent":
				if (statutAgentComboBox.getValue() == null || categoryComboBox.getValue() == null) {
					return null;

				} else {
					Agent.Statut sa = switch (statutAgentComboBox.getValue()) {
						case "TEMPORAIRE" -> Agent.Statut.TEMPORAIRE;
						case "STAGIAIRE" -> Agent.Statut.STAGIAIRE;
						case "PERMANENT" -> Agent.Statut.PERMANENT;
						default -> null;
					};

					Agent.Categorie ca = switch (categoryComboBox.getValue()) {
						case "A" -> Agent.Categorie.A;
						case "B" -> Agent.Categorie.B;
						case "C" -> Agent.Categorie.C;
						default -> null;
					};

					return new Agent(
							codeTextField.getText(),
							nomTextField.getText(),
							dateNaissanceDatePicker.getValue(),
							addressTextField.getText(),
							emailTextField.getText(),
							telNumberTextField.getText(),
							Double.parseDouble(salaryTextField.getText()),
							sa,
							ca,
							Integer.parseInt(salaryIndTextField.getText()),
							occupationTextField.getText()
					);
				}

			default:
				throw new IllegalArgumentException("Type de contact non reconnu.");
		}
	}

	@FXML
	protected void onAnnulerAction() {
		// Fermer la fenêtre sans ajouter de contact
		fermerFenetre();
	}

	private void fermerFenetre() {
		// Récupérer la fenêtre actuelle et la fermer
		Stage stage = (Stage) codeTextField.getScene().getWindow();
		stage.close();
	}
}