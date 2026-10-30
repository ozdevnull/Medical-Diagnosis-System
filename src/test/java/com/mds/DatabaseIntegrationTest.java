package com.mds;

import com.mds.dao.DiseaseDAO;
import com.mds.dao.MedicineDAO;
import com.mds.dao.SymptomDAO;
import com.mds.dao.UserDAO;
import com.mds.database.DatabaseConnection;
import com.mds.model.Disease;
import com.mds.model.DiseaseMatch;
import com.mds.model.Medicine;
import com.mds.model.Symptom;
import com.mds.service.DiseaseMatchingService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * End-to-end Integration Tests against the live MariaDB database (providencedb).
 */
public class DatabaseIntegrationTest {

    private static final List<String> EXPECTED_50_SYMPTOMS = List.of(
            "Fever", "Chills", "Headache", "Fatigue", "Weakness",
            "Dizziness", "Nausea", "Vomiting", "Loss of appetite", "Abdominal pain",
            "Diarrhea", "Constipation", "Cough", "Dry cough", "Phlegm",
            "Sore throat", "Runny nose", "Stuffy nose", "Sneezing", "Shortness of breath",
            "Chest pain", "Wheezing", "Muscle pain", "Joint pain", "Back pain",
            "Sweating", "Skin rash", "Itching", "Red eyes", "Watery eyes",
            "Loss of smell", "Loss of taste", "Ear pain", "Frequent urination", "Painful urination",
            "Increased thirst", "Swelling", "Palpitations", "Blurred vision", "Difficulty sleeping",
            "Anxiety/restlessness", "Yellowing of skin/eyes", "Dark urine", "Pale stools", "Neck stiffness",
            "Sensitivity to light", "Hair loss", "Sore muscles", "Heartburn", "Bloating"
    );

    @BeforeAll
    static void checkDatabaseReachable() {
        boolean available = DatabaseConnection.isDatabaseAvailable();
        assumeTrue(available, "MariaDB database is not reachable on localhost:3306; skipping integration tests");
    }

    @Test
    @DisplayName("Verify MariaDB JDBC connection establishment")
    void testDatabaseConnection() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            assertNotNull(conn);
            assertFalse(conn.isClosed());
            assertTrue(conn.isValid(2));
        }
    }

    @Test
    @DisplayName("Verify retrieval of all 50 symptoms with exact naming match")
    void testRetrieveAll50Symptoms() throws SQLException {
        SymptomDAO dao = new SymptomDAO();
        List<Symptom> symptoms = dao.getAllSymptoms();

        assertEquals(50, symptoms.size(), "Database must contain exactly 50 seeded symptoms");

        List<String> dbNames = symptoms.stream().map(Symptom::getName).toList();
        for (String expected : EXPECTED_50_SYMPTOMS) {
            assertTrue(dbNames.contains(expected), "Missing expected symptom in DB: " + expected);
        }
    }

    @Test
    @DisplayName("Verify disease record retrieval and clinical profile completeness")
    void testDiseaseRetrieval() throws SQLException {
        DiseaseDAO dao = new DiseaseDAO();
        List<Disease> diseases = dao.getAllDiseases();

        assertTrue(diseases.size() >= 40, "Database must contain all 40+ medically grounded conditions");

        Optional<Disease> coldOpt = diseases.stream()
                .filter(d -> "Common cold".equalsIgnoreCase(d.getName()))
                .findFirst();

        assertTrue(coldOpt.isPresent(), "Common cold must exist in diseases table");
        Disease cold = coldOpt.get();
        assertFalse(cold.getDescription().isBlank());
        assertFalse(cold.getCauses().isBlank());
        assertFalse(cold.getPrecautions().isBlank());
    }

    @Test
    @DisplayName("Verify disease search by partial query and symptom association loading")
    void testDiseaseSearch() throws SQLException {
        DiseaseDAO dao = new DiseaseDAO();

        List<Disease> results = dao.searchDiseases("bronchitis");
        assertFalse(results.isEmpty(), "Search for 'bronchitis' should return matching conditions");
        assertTrue(results.stream().anyMatch(d -> d.getName().toLowerCase().contains("bronchitis")));

        // Verify symptom association weights are loaded
        Disease bronchitis = results.get(0);
        assertFalse(bronchitis.getSymptomWeights().isEmpty(), "Associated symptoms should be loaded");
        assertTrue(bronchitis.getSymptomWeights().containsKey("Cough"));
    }

    @Test
    @DisplayName("Verify medicine search and disease-medicine relationship retrieval")
    void testMedicineSearch() throws SQLException {
        MedicineDAO medDao = new MedicineDAO();
        List<Medicine> meds = medDao.searchMedicines("paracetamol");

        assertFalse(meds.isEmpty(), "Search for 'paracetamol' must return records");
        Medicine paracetamol = meds.get(0);
        assertTrue(paracetamol.getName().toLowerCase().contains("paracetamol"));
        assertFalse(paracetamol.getUses().isBlank());
        assertFalse(paracetamol.getPrecautions().isBlank());

        // Verify related diseases for medicine
        List<Disease> diseases = medDao.getRelatedDiseasesForMedicine(paracetamol.getId());
        assertFalse(diseases.isEmpty(), "Paracetamol should be linked to related conditions");
    }

    @Test
    @DisplayName("Verify end-to-end disease matching with actual database records")
    void testDatabaseMatchingService() throws SQLException {
        DiseaseMatchingService service = new DiseaseMatchingService();

        // Test 1: Upper respiratory symptoms -> Common cold should rank high
        List<DiseaseMatch> coldMatches = service.matchSymptoms(List.of("Runny nose", "Stuffy nose", "Sneezing"));
        assertFalse(coldMatches.isEmpty(), "Should return matches for respiratory symptoms");

        DiseaseMatch topMatch = coldMatches.get(0);
        assertTrue(topMatch.getDiseaseName().equals("Common cold") || topMatch.getDiseaseName().equals("Allergic rhinitis"),
                "Top match should be Common cold or Allergic rhinitis, got: " + topMatch.getDiseaseName());
        assertTrue(topMatch.getSimilarityPercentage() > 0.0);

        // Test 2: Specific viral pattern -> COVID-19 top rank
        List<DiseaseMatch> covidMatches = service.matchSymptoms(List.of("Fever", "Dry cough", "Loss of smell", "Loss of taste"));
        assertFalse(covidMatches.isEmpty());
        assertEquals("COVID-19", covidMatches.get(0).getDiseaseName());
        assertTrue(covidMatches.get(0).getMatchedSymptoms().contains("Loss of smell"));
        assertTrue(covidMatches.get(0).getMatchedSymptoms().contains("Loss of taste"));
    }

    @Test
    @DisplayName("Verify user authentication with database and fallback credentials")
    void testUserAuthentication() {
        UserDAO dao = new UserDAO();

        // Valid demo credentials
        assertTrue(dao.authenticate("admin", "1234"), "Demo admin/1234 credentials must authenticate");

        // Invalid credentials
        assertFalse(dao.authenticate("admin", "wrong_password"), "Wrong password should fail");
        assertFalse(dao.authenticate("unknown_user", "1234"), "Non-existent user should fail");
        assertFalse(dao.authenticate(null, null), "Null credentials should fail");
    }
}
