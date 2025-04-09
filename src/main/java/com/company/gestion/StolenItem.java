package com.company.gestion;

import java.util.List;
import java.util.ArrayList;
import javafx.beans.property.*;
import java.time.LocalDateTime;

public class StolenItem {
	private final IntegerProperty id;
	private final StringProperty type;
	private final StringProperty model;
	private final StringProperty serialNumber;
	private final StringProperty ownerName;
	private final StringProperty ownerEmail;
	private final StringProperty ownerPhone;
	private final ObjectProperty<LocalDateTime> reportDate;
	private final StringProperty policeReportNumber;
	private final StringProperty description;
	private final BooleanProperty recovered;
	private List<ItemImage> images;

	public StolenItem() {
		this.id = new SimpleIntegerProperty(0);
		this.type = new SimpleStringProperty("");
		this.model = new SimpleStringProperty("");
		this.serialNumber = new SimpleStringProperty("");
		this.ownerName = new SimpleStringProperty("");
		this.ownerEmail = new SimpleStringProperty("");
		this.ownerPhone = new SimpleStringProperty("");
		this.reportDate = new SimpleObjectProperty<>(LocalDateTime.now());
		this.policeReportNumber = new SimpleStringProperty("");
		this.description = new SimpleStringProperty("");
		this.recovered = new SimpleBooleanProperty(false);
		this.images = new ArrayList<>();
	}

	public StolenItem(String type, String model, String serialNumber, String ownerName,
					  String ownerEmail, String ownerPhone, LocalDateTime reportDate) {
		this();
		this.type.set(type);
		this.model.set(model);
		this.serialNumber.set(serialNumber);
		this.ownerName.set(ownerName);
		this.ownerEmail.set(ownerEmail);
		this.ownerPhone.set(ownerPhone);
		this.reportDate.set(reportDate);
	}

	// Getters et setters pour l'ID
	public int getId() { return id.get(); }
	public void setId(int id) { this.id.set(id); }
	public IntegerProperty idProperty() { return id; }

	// Getters et setters pour le type
	public String getType() { return type.get(); }
	public void setType(String type) { this.type.set(type); }
	public StringProperty typeProperty() { return type; }

	// Getters et setters pour le modèle
	public String getModel() { return model.get(); }
	public void setModel(String model) { this.model.set(model); }
	public StringProperty modelProperty() { return model; }

	// Getters et setters pour le numéro de série
	public String getSerialNumber() { return serialNumber.get(); }
	public void setSerialNumber(String serialNumber) { this.serialNumber.set(serialNumber); }
	public StringProperty serialNumberProperty() { return serialNumber; }

	// Getters et setters pour le nom du propriétaire
	public String getOwnerName() { return ownerName.get(); }
	public void setOwnerName(String ownerName) { this.ownerName.set(ownerName); }
	public StringProperty ownerNameProperty() { return ownerName; }

	// Getters et setters pour l'email du propriétaire
	public String getOwnerEmail() { return ownerEmail.get(); }
	public void setOwnerEmail(String ownerEmail) { this.ownerEmail.set(ownerEmail); }
	public StringProperty ownerEmailProperty() { return ownerEmail; }

	// Getters et setters pour le téléphone du propriétaire
	public String getOwnerPhone() { return ownerPhone.get(); }
	public void setOwnerPhone(String ownerPhone) { this.ownerPhone.set(ownerPhone); }
	public StringProperty ownerPhoneProperty() { return ownerPhone; }

	// Getters et setters pour la date de signalement
	public LocalDateTime getReportDate() { return reportDate.get(); }
	public void setReportDate(LocalDateTime reportDate) { this.reportDate.set(reportDate); }
	public ObjectProperty<LocalDateTime> reportDateProperty() { return reportDate; }

	// Getters et setters pour le numéro de rapport de police
	public String getPoliceReportNumber() { return policeReportNumber.get(); }
	public void setPoliceReportNumber(String policeReportNumber) { this.policeReportNumber.set(policeReportNumber); }
	public StringProperty policeReportNumberProperty() { return policeReportNumber; }

	// Getters et setters pour la description
	public String getDescription() { return description.get(); }
	public void setDescription(String description) { this.description.set(description); }
	public StringProperty descriptionProperty() { return description; }

	// Getters et setters pour l'état (récupéré ou non)
	public boolean isRecovered() { return recovered.get(); }
	public void setRecovered(boolean recovered) { this.recovered.set(recovered); }
	public BooleanProperty recoveredProperty() { return recovered; }

	// Getters et setters pour les images
	public List<ItemImage> getImages() { return images; }
	public void setImages(List<ItemImage> images) { this.images = images; }
	public void addImage(ItemImage image) { this.images.add(image); }
}
