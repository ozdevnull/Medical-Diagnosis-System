package com.mds.dao;

import com.mds.database.DatabaseConnection;
import com.mds.model.Disease;
import com.mds.model.Medicine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Data Access Object for disease profiles, symptoms association, and related medications.
 */
public class DiseaseDAO {

    /**
     * Retrieves all candidate diseases that share at least one symptom with the provided symptom names.
     * Each returned Disease is populated with its complete symptom-weight profile.
     *
     * @param selectedSymptomNames collection of user-selected symptoms
     * @return list of candidate diseases with full symptom associations
     * @throws SQLException on database error
     */
    public List<Disease> getCandidateDiseasesForSymptoms(Collection<String> selectedSymptomNames) throws SQLException {
        if (selectedSymptomNames == null || selectedSymptomNames.isEmpty()) {
            return Collections.emptyList();
        }

        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < selectedSymptomNames.size(); i++) {
            if (i > 0) placeholders.append(",");
            placeholders.append("?");
        }

        // 1. Identify distinct disease IDs matching at least one symptom
        String candidateSql = "SELECT DISTINCT ds.disease_id " +
                "FROM disease_symptoms ds " +
                "JOIN symptoms s ON ds.symptom_id = s.symptom_id " +
                "WHERE s.symptom_name IN (" + placeholders + ")";

        List<Integer> candidateIds = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(candidateSql)) {

            int idx = 1;
            for (String symptom : selectedSymptomNames) {
                stmt.setString(idx++, symptom.trim());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    candidateIds.add(rs.getInt("disease_id"));
                }
            }
        }

        if (candidateIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. Load complete disease data along with all associated symptoms
        StringBuilder idPlaceholders = new StringBuilder();
        for (int i = 0; i < candidateIds.size(); i++) {
            if (i > 0) idPlaceholders.append(",");
            idPlaceholders.append("?");
        }

        String fullSql = "SELECT d.disease_id, d.disease_name, d.description, d.causes, d.precautions, " +
                "       d.severity_information, d.reference_source, " +
                "       s.symptom_name, ds.association_weight " +
                "FROM diseases d " +
                "JOIN disease_symptoms ds ON d.disease_id = ds.disease_id " +
                "JOIN symptoms s ON ds.symptom_id = s.symptom_id " +
                "WHERE d.disease_id IN (" + idPlaceholders + ") " +
                "ORDER BY d.disease_name ASC, ds.association_weight DESC";

        Map<Integer, Disease> diseaseMap = new LinkedHashMap<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(fullSql)) {

            int idx = 1;
            for (Integer id : candidateIds) {
                stmt.setInt(idx++, id);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int diseaseId = rs.getInt("disease_id");
                    Disease disease = diseaseMap.computeIfAbsent(diseaseId, k -> {
                        try {
                            return new Disease(
                                    diseaseId,
                                    rs.getString("disease_name"),
                                    rs.getString("description"),
                                    rs.getString("causes"),
                                    rs.getString("precautions"),
                                    rs.getString("severity_information"),
                                    rs.getString("reference_source")
                            );
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    String symptomName = rs.getString("symptom_name");
                    int weight = rs.getInt("association_weight");
                    disease.addSymptomWeight(symptomName, weight);
                }
            }
        }

        return new ArrayList<>(diseaseMap.values());
    }

    /**
     * Searches diseases by name or description using SQL LIKE.
     *
     * @param query search query
     * @return list of matching diseases
     * @throws SQLException on database error
     */
    public List<Disease> searchDiseases(String query) throws SQLException {
        if (query == null || query.isBlank()) {
            return getAllDiseases();
        }

        String pattern = "%" + query.trim() + "%";
        String sql = "SELECT d.disease_id, d.disease_name, d.description, d.causes, d.precautions, " +
                "       d.severity_information, d.reference_source, " +
                "       s.symptom_name, ds.association_weight " +
                "FROM diseases d " +
                "LEFT JOIN disease_symptoms ds ON d.disease_id = ds.disease_id " +
                "LEFT JOIN symptoms s ON ds.symptom_id = s.symptom_id " +
                "WHERE d.disease_name LIKE ? OR d.description LIKE ? OR d.causes LIKE ? " +
                "ORDER BY d.disease_name ASC, ds.association_weight DESC";

        Map<Integer, Disease> map = new LinkedHashMap<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("disease_id");
                    Disease disease = map.computeIfAbsent(id, k -> {
                        try {
                            return new Disease(
                                    id,
                                    rs.getString("disease_name"),
                                    rs.getString("description"),
                                    rs.getString("causes"),
                                    rs.getString("precautions"),
                                    rs.getString("severity_information"),
                                    rs.getString("reference_source")
                            );
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    String symptomName = rs.getString("symptom_name");
                    if (symptomName != null) {
                        disease.addSymptomWeight(symptomName, rs.getInt("association_weight"));
                    }
                }
            }
        }

        return new ArrayList<>(map.values());
    }

    /**
     * Retrieves all diseases ordered alphabetically.
     */
    public List<Disease> getAllDiseases() throws SQLException {
        String sql = "SELECT disease_id, disease_name, description, causes, precautions, " +
                "severity_information, reference_source FROM diseases ORDER BY disease_name ASC";
        List<Disease> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

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
        return list;
    }

    /**
     * Retrieves educational medicines linked to a specific disease.
     */
    public List<Medicine> getRelatedMedicinesForDisease(int diseaseId) throws SQLException {
        String sql = "SELECT m.medicine_id, m.medicine_name, m.description, m.uses, m.side_effects, " +
                "       m.precautions, m.reference_source " +
                "FROM medicines m " +
                "JOIN disease_medicines dm ON m.medicine_id = dm.medicine_id " +
                "WHERE dm.disease_id = ? " +
                "ORDER BY m.medicine_name ASC";

        List<Medicine> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, diseaseId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Medicine(
                            rs.getInt("medicine_id"),
                            rs.getString("medicine_name"),
                            rs.getString("description"),
                            rs.getString("uses"),
                            rs.getString("side_effects"),
                            rs.getString("precautions"),
                            rs.getString("reference_source")
                    ));
                }
            }
        }
        return list;
    }
}
