package com.mds.dao;

import com.mds.database.DatabaseConnection;
import com.mds.model.Disease;
import com.mds.model.Medicine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for medicine entities and related condition linkages.
 */
public class MedicineDAO {

    public List<Medicine> getAllMedicines() throws SQLException {
        String sql = "SELECT medicine_id, medicine_name, description, uses, side_effects, " +
                "precautions, reference_source FROM medicines ORDER BY medicine_name ASC";
        List<Medicine> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToMedicine(rs));
            }
        }
        return list;
    }

    public Optional<Medicine> getMedicineById(int id) throws SQLException {
        String sql = "SELECT medicine_id, medicine_name, description, uses, side_effects, " +
                "precautions, reference_source FROM medicines WHERE medicine_id = ? LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToMedicine(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Medicine> searchMedicines(String query) throws SQLException {
        if (query == null || query.isBlank()) {
            return getAllMedicines();
        }

        String pattern = "%" + query.trim() + "%";
        String sql = "SELECT medicine_id, medicine_name, description, uses, side_effects, " +
                "       precautions, reference_source " +
                "FROM medicines " +
                "WHERE medicine_name LIKE ? OR description LIKE ? OR uses LIKE ? " +
                "ORDER BY medicine_name ASC";

        List<Medicine> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToMedicine(rs));
                }
            }
        }
        return list;
    }

    public List<Disease> getRelatedDiseasesForMedicine(int medicineId) throws SQLException {
        String sql = "SELECT d.disease_id, d.disease_name, d.description, d.causes, d.precautions, " +
                "       d.severity_information, d.reference_source " +
                "FROM diseases d " +
                "JOIN disease_medicines dm ON d.disease_id = dm.disease_id " +
                "WHERE dm.medicine_id = ? " +
                "ORDER BY d.disease_name ASC";

        List<Disease> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, medicineId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Disease(
                            rs.getInt("disease_id"),
                            rs.getString("disease_name"),
                            rs.getString("description"),
                            rs.getString("causes"),
                            rs.getString("precautions"),
                            rs.getString("severity_information"),
                            rs.getString("reference_source")
                    ));
                }
            }
        }
        return list;
    }

    private Medicine mapResultSetToMedicine(ResultSet rs) throws SQLException {
        return new Medicine(
                rs.getInt("medicine_id"),
                rs.getString("medicine_name"),
                rs.getString("description"),
                rs.getString("uses"),
                rs.getString("side_effects"),
                rs.getString("precautions"),
                rs.getString("reference_source")
        );
    }
}
