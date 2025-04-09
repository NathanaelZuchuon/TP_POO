package com.company.gestion;

import java.time.LocalDateTime;

public class ItemImage {
	private int id;
	private int itemId;
	private byte[] imageData;
	private String imageName;
	private String contentType;
	private LocalDateTime uploadDate;

	public ItemImage() {
		this.uploadDate = LocalDateTime.now();
	}

	public ItemImage(int itemId, byte[] imageData, String imageName, String contentType) {
		this();
		this.itemId = itemId;
		this.imageData = imageData;
		this.imageName = imageName;
		this.contentType = contentType;
	}

	// Getters et setters
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public int getItemId() { return itemId; }
	public void setItemId(int itemId) { this.itemId = itemId; }

	public byte[] getImageData() { return imageData; }
	public void setImageData(byte[] imageData) { this.imageData = imageData; }

	public String getImageName() { return imageName; }
	public void setImageName(String imageName) { this.imageName = imageName; }

	public String getContentType() { return contentType; }
	public void setContentType(String contentType) { this.contentType = contentType; }

	public LocalDateTime getUploadDate() { return uploadDate; }
	public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }
}
