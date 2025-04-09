package com.company.gestion;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import java.time.LocalDate;

public abstract class Contact {
	protected String code;
	protected String name;
	protected LocalDate birthDate;
	protected String address;
	protected String email;
	protected String telNumber;

	// Constructeur par défaut
	public Contact() {}

	// Constructeur avec paramètres
	public Contact(String code, String name, LocalDate birthDate,
				   String address, String email, String telNumber) {
		this.code = code;
		this.name = name;
		this.birthDate = birthDate;
		this.address = address;
		this.email = email;
		this.telNumber = telNumber;
	}

	// Getters et Setters
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelNumber() {
		return telNumber;
	}

	public void setTelNumber(String telNumber) {
		this.telNumber = telNumber;
	}

	// Méthode toString pour affichage
	@Override
	public String toString() {
		return String.format("Contact [code='%s', name='%s', birthDate=%s, address='%s', email='%s', telNumber='%s']",
				code, name, birthDate, address, email, telNumber);
	}

	public ObservableValue<String> codeProperty() {
		return new SimpleStringProperty(this.code);
	}

	public ObservableValue<String> nameProperty() {
		return new SimpleStringProperty(this.name);
	}

	public ObservableValue<String> birthDateProperty() {
		return new SimpleStringProperty(this.birthDate.toString());
	}

	public ObservableValue<String> addressProperty() {
		return new SimpleStringProperty(this.address);
	}

	public ObservableValue<String> emailProperty() {
		return new SimpleStringProperty(this.email);
	}

	public ObservableValue<String> telNumberProperty() {
		return new SimpleStringProperty(this.telNumber);
	}
}