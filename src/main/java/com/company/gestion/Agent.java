package com.company.gestion;

import java.time.LocalDate;

public class Agent extends Contact {
	public enum Statut {
		TEMPORAIRE,
		STAGIAIRE,
		PERMANENT
	}

	public enum Categorie {
		A,
		B,
		C
	}

	private double salary;
	private Statut statut;
	private Categorie category;
	private int salaryInd;
	private String occupation;

	// Constructeur par défaut
	public Agent() {
		super();
	}

	// Constructeur avec paramètres
	public Agent(String code, String nom, LocalDate birthDate,
				 String address, String email, String telNumber,
				 double salary, Statut statut, Categorie category,
				 int salaryInd, String occupation) {
		super(code, nom, birthDate, address, email, telNumber);
		this.salary = salary;
		this.statut = statut;
		this.category = category;
		this.salaryInd = salaryInd;
		this.occupation = occupation;
	}

	// Getters et Setters
	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public Statut getStatut() {
		return statut;
	}

	public void setStatut(Statut statut) {
		this.statut = statut;
	}

	public Categorie getCategory() {
		return category;
	}

	public void setCategory(Categorie category) {
		this.category = category;
	}

	public int getSalaryInd() {
		return salaryInd;
	}

	public void setSalaryInd(int salaryInd) {
		this.salaryInd = salaryInd;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	// Méthode toString surchargée
	@Override
	public String toString() {
		return String.format("Agent [ %s, salary=%s, statut=%s, category=%s, salaryInd=%s, occupation='%s']", super.toString(), salary, statut, category, salaryInd, occupation);
	}
}