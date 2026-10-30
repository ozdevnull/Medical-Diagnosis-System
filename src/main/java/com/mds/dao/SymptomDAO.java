package com.mds.dao;

import com.mds.database.DatabaseConnection;
import com.mds.model.Symptom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Data Access Object for symptom entities.
 */
public class SymptomDAO {

    public List<Symptom> getAllSymptoms() throws SQLException {
        String sql = "SELECT symptom_id, symptom_name, description FROM symptoms ORDER BY symptom_name ASC";
        List<Symptom> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToSymptom(rs));
            }
        }
        return list;
    }

    public Optional<Symptom> getSymptomByName(String name) throws SQLException {
        if (name == null || name.isBlank()) return Optional.empty();

        String sql = "SELECT symptom_id, symptom_name, description FROM symptoms WHERE LOWER(symptom_name) = LOWER(?) LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToSymptom(rs));
                }
            }
        }
        return Optional.empty();
    }

    public Map<String, Symptom> getSymptomsByNames(Collection<String> names) throws SQLException {
        Map<String, Symptom> map = new HashMap<>();
        if (names == null || names.isEmpty()) return map;

        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < names.size(); i++) {
            if (i > 0) placeholders.append(",");
            placeholders.append("?");
        }

        String sql = "SELECT symptom_id, symptom_name, description FROM symptoms WHERE symptom_name IN (" + placeholders + ")";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int index = 1;
            for (String name : names) {
                stmt.setString(index++, name);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Symptom s = mapResultSetToSymptom(rs);
                    map.put(s.getName(), s);
                }
            }
        }
        return map;
    }

    private Symptom mapResultSetToSymptom(ResultSet rs) throws SQLException {
        return new Symptom(
                rs.getInt("symptom_id"),
                rs.getString("symptom_name"),
                rs.getString("description")
        );
    }
}
