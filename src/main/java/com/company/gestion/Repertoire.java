package com.company.gestion;

import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Repertoire {
	private Connection connection;

	// Constructeur vide sans initialisation de connexion
	public Repertoire() {}

	// Méthode pour définir la connexion
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	// Ajouter un contact
	public void ajouterContact(Contact contact) {
		if (connection == null) {
			System.out.println("Aucune connexion à la base de données.");
			return;
		}

		try {
			// Insertion selon le type de contact
			if (contact instanceof Etudiant) {
				String sqlEtudiant = "INSERT INTO etudiants (code, name, birthDate, address, email, telNumber, cycle, level) " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement pstmt = connection.prepareStatement(sqlEtudiant);

				Etudiant etudiant = (Etudiant) contact;

				pstmt.setString(1, etudiant.getCode());
				pstmt.setString(2, etudiant.getName());
				pstmt.setDate(3, Date.valueOf(etudiant.getBirthDate()));
				pstmt.setString(4, etudiant.getAddress());
				pstmt.setString(5, etudiant.getEmail());
				pstmt.setString(6, etudiant.getTelNumber());
				pstmt.setString(7, etudiant.getCycle().toString());
				pstmt.setString(8, etudiant.getLevel());

				pstmt.executeUpdate();

			} else if (contact instanceof Enseignant) {
				String sqlEnseignant = "INSERT INTO enseignants (code, name, birthDate, address, email, telNumber, statut) " +
						"VALUES (?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement pstmt = connection.prepareStatement(sqlEnseignant);

				Enseignant enseignant = (Enseignant) contact;

				pstmt.setString(1, enseignant.getCode());
				pstmt.setString(2, enseignant.getName());
				pstmt.setDate(3, Date.valueOf(enseignant.getBirthDate()));
				pstmt.setString(4, enseignant.getAddress());
				pstmt.setString(5, enseignant.getEmail());
				pstmt.setString(6, enseignant.getTelNumber());
				pstmt.setString(7, enseignant.getStatut().toString());

				pstmt.executeUpdate();
			} else if (contact instanceof Agent) {
				String sqlAgent = "INSERT INTO agents (code, name, birthDate, address, email, telNumber, salary, statut, category, salaryInd, occupation) " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement pstmt = connection.prepareStatement(sqlAgent);

				Agent agent = (Agent) contact;

				pstmt.setString(1, agent.getCode());
				pstmt.setString(2, agent.getName());
				pstmt.setDate(3, Date.valueOf(agent.getBirthDate()));
				pstmt.setString(4, agent.getAddress());
				pstmt.setString(5, agent.getEmail());
				pstmt.setString(6, agent.getTelNumber());
				pstmt.setDouble(7, agent.getSalary());
				pstmt.setString(8, agent.getStatut().toString());
				pstmt.setString(9, agent.getCategory().toString());
				pstmt.setInt(10, agent.getSalaryInd());
				pstmt.setString(11, agent.getOccupation());

				pstmt.executeUpdate();
			}

			System.out.println("Contact ajouté avec succès : " + contact.getName());
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Erreur lors de l'ajout du contact.");
		}
	}

	// Supprimer un contact par code
	public void supprimerContact(String code) {
		if (connection == null) {
			System.out.println("Aucune connexion à la base de données.");
			return;
		}

		try {
			// Essayer de supprimer de chaque table
			String[] tables = {"etudiants", "enseignants", "agents"};
			boolean contactSupprime = false;

			for (String table : tables) {
				String sql = "DELETE FROM " + table + " WHERE code = ?";
				PreparedStatement pstmt = connection.prepareStatement(sql);
				pstmt.setString(1, code);

				int rowsAffected = pstmt.executeUpdate();

				if (rowsAffected > 0) {
					contactSupprime = true;
					break;
				}
			}

			if (contactSupprime) {
				System.out.println("Contact supprimé avec succès.");
			} else {
				System.out.println("Aucun contact trouvé avec ce code.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Erreur lors de la suppression du contact.");
		}
	}

	// Modifier un contact
	public void modifierContact(Contact contactModifie) {
		if (connection == null) {
			System.out.println("Aucune connexion à la base de données.");
			return;
		}

		try {
			if (contactModifie instanceof Etudiant) {
				String sqlEtudiant = "UPDATE etudiants SET name=?, birthDate=?, address=?, email=?, telNumber=?, cycle=?, level=? WHERE code=?";
				PreparedStatement pstmt = connection.prepareStatement(sqlEtudiant);

				Etudiant etudiant = (Etudiant) contactModifie;

				pstmt.setString(1, etudiant.getName());
				pstmt.setDate(2, Date.valueOf(etudiant.getBirthDate()));
				pstmt.setString(3, etudiant.getAddress());
				pstmt.setString(4, etudiant.getEmail());
				pstmt.setString(5, etudiant.getTelNumber());
				pstmt.setString(6, etudiant.getCycle().toString());
				pstmt.setString(7, etudiant.getLevel());
				pstmt.setString(8, etudiant.getCode());

				pstmt.executeUpdate();
			} else if (contactModifie instanceof Enseignant) {
				String sqlEnseignant = "UPDATE enseignants SET name=?, date_naissance=?, address=?, email=?, telNumber=?, statut=? WHERE code=?";
				PreparedStatement pstmt = connection.prepareStatement(sqlEnseignant);

				Enseignant enseignant = (Enseignant) contactModifie;

				pstmt.setString(1, enseignant.getName());
				pstmt.setDate(2, Date.valueOf(enseignant.getBirthDate()));
				pstmt.setString(3, enseignant.getAddress());
				pstmt.setString(4, enseignant.getEmail());
				pstmt.setString(5, enseignant.getTelNumber());
				pstmt.setString(6, enseignant.getStatut().toString());
				pstmt.setString(7, enseignant.getCode());

				pstmt.executeUpdate();
			} else if (contactModifie instanceof Agent) {
				String sqlAgent = "UPDATE agents SET name=?, birthDate=?, address=?, email=?, telNumber=?, salary=?, statut=?, category=?, salaryInd=?, occupation=? WHERE code=?";
				PreparedStatement pstmt = connection.prepareStatement(sqlAgent);

				Agent agent = (Agent) contactModifie;

				pstmt.setString(1, agent.getName());
				pstmt.setDate(2, Date.valueOf(agent.getBirthDate()));
				pstmt.setString(3, agent.getAddress());
				pstmt.setString(4, agent.getEmail());
				pstmt.setString(5, agent.getTelNumber());
				pstmt.setDouble(6, agent.getSalary());
				pstmt.setString(7, agent.getStatut().toString());
				pstmt.setString(8, agent.getCategory().toString());
				pstmt.setInt(9, agent.getSalaryInd());
				pstmt.setString(10, agent.getOccupation());
				pstmt.setString(11, agent.getCode());

				pstmt.executeUpdate();
			}

			System.out.println("Contact modifié avec succès.");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Erreur lors de la modification du contact.");
		}
	}

	// Rechercher un contact par code
	public Contact rechercherContact(String code) {
		if (connection == null) {
			System.out.println("Aucune connexion à la base de données.");
			return null;
		}

		try {
			// Recherche dans les différentes tables
			String[] tables = {"etudiants", "enseignants", "agents"};

			for (String table : tables) {
				String sql = "SELECT * FROM " + table + " WHERE code = ?";
				PreparedStatement pstmt = connection.prepareStatement(sql);
				pstmt.setString(1, code);

				ResultSet rs = pstmt.executeQuery();

				if (rs.next()) {
					switch (table) {
						case "etudiants":
							Etudiant etudiant = new Etudiant();

							etudiant.setCode(rs.getString("code"));
							etudiant.setName(rs.getString("name"));
							etudiant.setBirthDate(rs.getDate("birthDate").toLocalDate());
							etudiant.setAddress(rs.getString("address"));
							etudiant.setEmail(rs.getString("email"));
							etudiant.setTelNumber(rs.getString("telNumber"));
							etudiant.setCycle(Etudiant.Cycle.valueOf(rs.getString("cycle")));
							etudiant.setLevel(rs.getString("level"));

							return etudiant;

						case "enseignants":
							Enseignant enseignant = new Enseignant();

							enseignant.setCode(rs.getString("code"));
							enseignant.setName(rs.getString("name"));
							enseignant.setBirthDate(rs.getDate("birthDate").toLocalDate());
							enseignant.setAddress(rs.getString("address"));
							enseignant.setEmail(rs.getString("email"));
							enseignant.setTelNumber(rs.getString("telNumber"));
							enseignant.setStatut(Enseignant.Statut.valueOf(rs.getString("statut")));

							return enseignant;

						case "agents":
							Agent agent = new Agent();

							agent.setCode(rs.getString("code"));
							agent.setName(rs.getString("name"));
							agent.setBirthDate(rs.getDate("birthDate").toLocalDate());
							agent.setAddress(rs.getString("address"));
							agent.setEmail(rs.getString("email"));
							agent.setTelNumber(rs.getString("telNumber"));
							agent.setSalary(rs.getDouble("salary"));
							agent.setStatut(Agent.Statut.valueOf(rs.getString("statut")));
							agent.setCategory(Agent.Categorie.valueOf(rs.getString("category")));
							agent.setSalaryInd(rs.getInt("salaryInd"));
							agent.setOccupation(rs.getString("occupation"));

							return agent;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Rechercher des contacts par nom
	public ObservableList<Contact> rechercherContactParNom(String nom) {
		if (connection == null) {
			System.out.println("Aucune connexion à la base de données.");
			return FXCollections.observableArrayList();
		}

		ObservableList<Contact> contactsTrouves = FXCollections.observableArrayList();
		String[] tables = {"etudiants", "enseignants", "agents"};

		try {
			for (String table : tables) {
				String sql = "SELECT * FROM " + table + " WHERE name LIKE ?";
				PreparedStatement pstmt = connection.prepareStatement(sql);
				pstmt.setString(1, "%" + nom + "%");

				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					Contact contact = null;

					switch (table) {
						case "etudiants":
							contact = new Etudiant();

							Etudiant etudiant = (Etudiant) contact;
							etudiant.setCode(rs.getString("code"));
							etudiant.setName(rs.getString("name"));
							etudiant.setBirthDate(rs.getDate("birthDate").toLocalDate());
							etudiant.setAddress(rs.getString("address"));
							etudiant.setEmail(rs.getString("email"));
							etudiant.setTelNumber(rs.getString("telNumber"));
							etudiant.setCycle(Etudiant.Cycle.valueOf(rs.getString("cycle")));
							etudiant.setLevel(rs.getString("level"));
							break;

						case "enseignants":
							contact = new Enseignant();

							Enseignant enseignant = (Enseignant) contact;
							enseignant.setCode(rs.getString("code"));
							enseignant.setName(rs.getString("name"));
							enseignant.setBirthDate(rs.getDate("birthDate").toLocalDate());
							enseignant.setAddress(rs.getString("address"));
							enseignant.setEmail(rs.getString("email"));
							enseignant.setTelNumber(rs.getString("telNumber"));
							enseignant.setStatut(Enseignant.Statut.valueOf(rs.getString("statut")));
							break;

						case "agents":
							contact = new Agent();

							Agent agent = (Agent) contact;
							agent.setCode(rs.getString("code"));
							agent.setName(rs.getString("name"));
							agent.setBirthDate(rs.getDate("birthDate").toLocalDate());
							agent.setAddress(rs.getString("address"));
							agent.setEmail(rs.getString("email"));
							agent.setTelNumber(rs.getString("telNumber"));
							agent.setSalary(rs.getDouble("salary"));
							agent.setStatut(Agent.Statut.valueOf(rs.getString("statut")));
							agent.setCategory(Agent.Categorie.valueOf(rs.getString("category")));
							agent.setSalaryInd(rs.getInt("salaryInd"));
							agent.setOccupation(rs.getString("occupation"));
							break;
					}
					contactsTrouves.add(contact);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return contactsTrouves;
	}

	// Lister tous les contacts
	public ObservableList<Contact> listerContacts() {
		if (connection == null) {
			System.out.println("Aucune connexion à la base de données.");
			return FXCollections.observableArrayList();
		}

		ObservableList<Contact> contacts = FXCollections.observableArrayList();
		String[] tables = {"etudiants", "enseignants", "agents"};

		try {
			for (String table : tables) {
				String sql = "SELECT * FROM " + table;
				Statement stmt = connection.createStatement();

				ResultSet rs = stmt.executeQuery(sql);

				while (rs.next()) {
					Contact contact = null;

					switch (table) {
						case "etudiants":
							contact = new Etudiant();

							Etudiant etudiant = (Etudiant) contact;
							etudiant.setCode(rs.getString("code"));
							etudiant.setName(rs.getString("name"));
							etudiant.setBirthDate(rs.getDate("birthDate").toLocalDate());
							etudiant.setAddress(rs.getString("address"));
							etudiant.setEmail(rs.getString("email"));
							etudiant.setTelNumber(rs.getString("telNumber"));
							etudiant.setCycle(Etudiant.Cycle.valueOf(rs.getString("cycle")));
							etudiant.setLevel(rs.getString("level"));
							break;

						case "enseignants":
							contact = new Enseignant();
							Enseignant enseignant = (Enseignant) contact;
							enseignant.setCode(rs.getString("code"));
							enseignant.setName(rs.getString("name"));
							enseignant.setBirthDate(rs.getDate("birthDate").toLocalDate());
							enseignant.setAddress(rs.getString("address"));
							enseignant.setEmail(rs.getString("email"));
							enseignant.setTelNumber(rs.getString("telNumber"));
							enseignant.setStatut(Enseignant.Statut.valueOf(rs.getString("statut")));
							break;

						case "agents":
							contact = new Agent();

							Agent agent = (Agent) contact;
							agent.setCode(rs.getString("code"));
							agent.setName(rs.getString("name"));
							agent.setBirthDate(rs.getDate("birthDate").toLocalDate());
							agent.setAddress(rs.getString("address"));
							agent.setEmail(rs.getString("email"));
							agent.setTelNumber(rs.getString("telNumber"));
							agent.setSalary(rs.getDouble("salary"));
							agent.setStatut(Agent.Statut.valueOf(rs.getString("statut")));
							agent.setCategory(Agent.Categorie.valueOf(rs.getString("category")));
							agent.setSalaryInd(rs.getInt("salaryInd"));
							agent.setOccupation(rs.getString("occupation"));
							break;
					}
					contacts.add(contact);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return contacts;
	}

	// Lister les étudiants
	public ObservableList<Etudiant> listerEtudiants() {
		if (connection == null) {
			System.out.println("Aucune connexion à la base de données.");
			return FXCollections.observableArrayList();
		}

		ObservableList<Etudiant> etudiants = FXCollections.observableArrayList();
		try {
			String sql = "SELECT * FROM etudiants";
			Statement stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				Etudiant etudiant = new Etudiant();
				etudiant.setCode(rs.getString("code"));
				etudiant.setName(rs.getString("name"));
				etudiant.setBirthDate(rs.getDate("birthDate").toLocalDate());
				etudiant.setAddress(rs.getString("address"));
				etudiant.setEmail(rs.getString("email"));
				etudiant.setTelNumber(rs.getString("telNumber"));
				etudiant.setCycle(Etudiant.Cycle.valueOf(rs.getString("cycle")));
				etudiant.setLevel(rs.getString("level"));
				etudiants.add(etudiant);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return etudiants;
	}

	// Lister les enseignants
	public ObservableList<Enseignant> listerEnseignants() {
		if (connection == null) {
			System.out.println("Aucune connexion à la base de données.");
			return FXCollections.observableArrayList();
		}

		ObservableList<Enseignant> enseignants = FXCollections.observableArrayList();
		try {
			String sql = "SELECT * FROM enseignants";
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				Enseignant enseignant = new Enseignant();
				enseignant.setCode(rs.getString("code"));
				enseignant.setName(rs.getString("name"));
				enseignant.setBirthDate(rs.getDate("birthDate").toLocalDate());
				enseignant.setAddress(rs.getString("address"));
				enseignant.setEmail(rs.getString("email"));
				enseignant.setTelNumber(rs.getString("telNumber"));
				enseignant.setStatut(Enseignant.Statut.valueOf(rs.getString("statut")));
				enseignants.add(enseignant);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return enseignants;
	}

	// Lister les agents
	public ObservableList<Agent> listerAgents() {
		if (connection == null) {
			System.out.println("Aucune connexion à la base de données.");
			return FXCollections.observableArrayList();
		}

		ObservableList<Agent> agents = FXCollections.observableArrayList();
		try {
			String sql = "SELECT * FROM agents";
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				Agent agent = new Agent();
				agent.setCode(rs.getString("code"));
				agent.setName(rs.getString("name"));
				agent.setBirthDate(rs.getDate("birthDate").toLocalDate());
				agent.setAddress(rs.getString("address"));
				agent.setEmail(rs.getString("email"));
				agent.setTelNumber(rs.getString("telNumber"));
				agent.setSalary(rs.getDouble("salary"));
				agent.setStatut(Agent.Statut.valueOf(rs.getString("statut")));
				agent.setCategory(Agent.Categorie.valueOf(rs.getString("category")));
				agent.setSalaryInd(rs.getInt("salaryInd"));
				agent.setOccupation(rs.getString("occupation"));
				agents.add(agent);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return agents;
	}
}