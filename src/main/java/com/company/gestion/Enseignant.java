package com.company.gestion;

import java.time.LocalDate;

public class Enseignant extends Contact {
	public enum Statut {
		VACATAIRE,
		PERMANENT
	}

	private Statut statut;

	// Constructeur par défaut
	public Enseignant() {
		super();
	}

	// Constructeur avec paramètres
	public Enseignant(String code, String name, LocalDate birthDate,
					  String address, String email, String telNumber,
					  Statut statut) {
		super(code, name, birthDate, address, email, telNumber);
		this.statut = statut;
	}

	// Getters et Setters
	public Statut getStatut() {
		return statut;
	}

	public void setStatut(Statut statut) {
		this.statut = statut;
	}

	// Méthode toString surchargée
	@Override
	public String toString() {
		return String.format("Enseignant [ %s, statut=%s]", super.toString(), statut);
	}
}