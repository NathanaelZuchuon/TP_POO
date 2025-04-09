package com.company.gestion;

import java.time.LocalDate;

public class Etudiant extends Contact {
	public enum Cycle {
		LICENCE,
		INGENIEUR
	}

	private Cycle cycle;
	private String level;

	// Constructeur par défaut
	public Etudiant() {
		super();
	}

	// Constructeur avec paramètres
	public Etudiant(String code, String name, LocalDate birthDate,
					String address, String email, String telNumber,
					Cycle cycle, String level) {
		super(code, name, birthDate, address, email, telNumber);
		this.cycle = cycle;
		this.level = level;
	}

	// Getters et Setters
	public Cycle getCycle() {
		return cycle;
	}

	public void setCycle(Cycle cycle) {
		this.cycle = cycle;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	// Méthode toString surchargée
	@Override
	public String toString() {
		return String.format("Etudiant [ %s, cycle=%s, level='%s']", super.toString(), cycle, level);
	}
}