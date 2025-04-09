package com.company.gestion;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.io.ByteArrayInputStream;

public class StolenItemDAO {

	// Rechercher un objet par numéro de série
	public Optional<StolenItem> findBySerialNumber(String serialNumber) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = DatabaseConnector.getInstance().getConnection();
			stmt = conn.prepareStatement(
					"SELECT * FROM stolen_items WHERE serial_number = ? AND is_recovered = FALSE"
			);
			stmt.setString(1, serialNumber);
			rs = stmt.executeQuery();

			if (rs.next()) {
				StolenItem item = extractStolenItemFromResultSet(rs);
				// Récupérer les images associées
				item.setImages(findImagesByItemId(item.getId()));
				return Optional.of(item);
			}

		} catch (SQLException e) {
			System.err.println("Erreur lors de la recherche d'un objet: " + e.getMessage());

		} finally {
			DatabaseConnector.closeResources(rs, stmt, conn);
		}

		return Optional.empty();
	}

	// Ajouter un nouvel objet volé
	public void addStolenItem(StolenItem item) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet generatedKeys = null;

		try {
			conn = DatabaseConnector.getInstance().getConnection();
			stmt = conn.prepareStatement(
					"INSERT INTO stolen_items (type, model, serial_number, owner_name, owner_email, " +
							"owner_phone, report_date, police_report_number, description) " +
							"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS
			);

			stmt.setString(1, item.getType());
			stmt.setString(2, item.getModel());
			stmt.setString(3, item.getSerialNumber());
			stmt.setString(4, item.getOwnerName());
			stmt.setString(5, item.getOwnerEmail());
			stmt.setString(6, item.getOwnerPhone());
			stmt.setTimestamp(7, Timestamp.valueOf(item.getReportDate()));
			stmt.setString(8, item.getPoliceReportNumber());
			stmt.setString(9, item.getDescription());

			int affectedRows = stmt.executeUpdate();

			if (affectedRows == 0) {
				return;
			}

			// Récupérer l'ID généré
			generatedKeys = stmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				int itemId = generatedKeys.getInt(1);
				item.setId(itemId);

				// Ajouter les images si présentes
				if (item.getImages() != null && !item.getImages().isEmpty()) {
					for (ItemImage image : item.getImages()) {
						image.setItemId(itemId);
						addItemImage(image);
					}
				}

			}

		} catch (SQLException e) {
			System.err.println("Erreur lors de l'ajout d'un objet volé: " + e.getMessage());

		} finally {
			DatabaseConnector.closeResources(generatedKeys, stmt, conn);
		}
	}

	// Ajouter une image pour un objet
	public void addItemImage(ItemImage image) {
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DatabaseConnector.getInstance().getConnection();
			stmt = conn.prepareStatement(
					"INSERT INTO item_images (item_id, image_data, image_name, content_type, upload_date) " +
							"VALUES (?, ?, ?, ?, ?)"
			);

			stmt.setInt(1, image.getItemId());
			stmt.setBlob(2, new ByteArrayInputStream(image.getImageData()));
			stmt.setString(3, image.getImageName());
			stmt.setString(4, image.getContentType());
			stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));

			stmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println("Erreur lors de l'ajout d'une image: " + e.getMessage());

		} finally {
			DatabaseConnector.closeResources(stmt, conn);
		}
	}

	// Récupérer toutes les images pour un objet
	public List<ItemImage> findImagesByItemId(int itemId) {
		List<ItemImage> images = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = DatabaseConnector.getInstance().getConnection();
			stmt = conn.prepareStatement("SELECT * FROM item_images WHERE item_id = ?");
			stmt.setInt(1, itemId);
			rs = stmt.executeQuery();

			while (rs.next()) {
				ItemImage image = new ItemImage();
				image.setId(rs.getInt("id"));
				image.setItemId(rs.getInt("item_id"));

				// Récupérer les données binaires de l'image
				Blob blob = rs.getBlob("image_data");
				if (blob != null) {
					byte[] imageData = blob.getBytes(1, (int) blob.length());
					image.setImageData(imageData);
				}

				image.setImageName(rs.getString("image_name"));
				image.setContentType(rs.getString("content_type"));
				image.setUploadDate(rs.getTimestamp("upload_date").toLocalDateTime());

				images.add(image);
			}

		} catch (SQLException e) {
			System.err.println("Erreur lors de la récupération des images: " + e.getMessage());

		} finally {
			DatabaseConnector.closeResources(rs, stmt, conn);
		}

		return images;
	}

	// Récupérer tous les objets volés (pour l'administration)
	public List<StolenItem> getAllStolenItems() {
		List<StolenItem> items = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = DatabaseConnector.getInstance().getConnection();
			stmt = conn.prepareStatement("SELECT * FROM stolen_items ORDER BY report_date DESC");
			rs = stmt.executeQuery();

			while (rs.next()) {
				StolenItem item = extractStolenItemFromResultSet(rs);
				// Ne pas charger les images ici pour éviter de trop charger la mémoire
				items.add(item);
			}

		} catch (SQLException e) {
			System.err.println("Erreur lors de la récupération des objets volés: " + e.getMessage());

		} finally {
			DatabaseConnector.closeResources(rs, stmt, conn);
		}

		return items;
	}

	// Marquer un objet comme récupéré
	public boolean markItemAsRecovered(int itemId) {
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DatabaseConnector.getInstance().getConnection();
			stmt = conn.prepareStatement("UPDATE stolen_items SET is_recovered = TRUE WHERE id = ?");
			stmt.setInt(1, itemId);

			return stmt.executeUpdate() > 0;

		} catch (SQLException e) {
			System.err.println("Erreur lors du marquage d'un objet comme récupéré: " + e.getMessage());

		} finally {
			DatabaseConnector.closeResources(stmt, conn);
		}

		return false;
	}

	// Supprimer un objet et ses images
	public void deleteStolenItem(int itemId) {
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DatabaseConnector.getInstance().getConnection();
			// Les images seront supprimées automatiquement grâce à ON DELETE CASCADE
			stmt = conn.prepareStatement("DELETE FROM stolen_items WHERE id = ?");
			stmt.setInt(1, itemId);

			stmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println("Erreur lors de la suppression d'un objet: " + e.getMessage());

		} finally {
			DatabaseConnector.closeResources(stmt, conn);
		}
	}

	// Extraire un objet volé du ResultSet
	private StolenItem extractStolenItemFromResultSet(ResultSet rs) throws SQLException {
		StolenItem item = new StolenItem();

		item.setId(rs.getInt("id"));
		item.setType(rs.getString("type"));
		item.setModel(rs.getString("model"));
		item.setSerialNumber(rs.getString("serial_number"));
		item.setOwnerName(rs.getString("owner_name"));
		item.setOwnerEmail(rs.getString("owner_email"));
		item.setOwnerPhone(rs.getString("owner_phone"));
		item.setReportDate(rs.getTimestamp("report_date").toLocalDateTime());
		item.setPoliceReportNumber(rs.getString("police_report_number"));
		item.setDescription(rs.getString("description"));
		item.setRecovered(rs.getBoolean("is_recovered"));

		return item;
	}
}
