package com.mds;

import com.mds.model.Disease;
import com.mds.model.DiseaseMatch;
import com.mds.service.DiseaseMatchingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DiseaseMatchingService verifying:
 * - Hand-calculated numerical formula accuracy
 * - Single symptom matching
 * - Multiple symptom matching
 * - Jaccard intersection, union, and similarity percentage
 * - Ranking behavior
 * - Matched and unmatched symptom separation
 * - Zero denominator / edge case handling
 */
public class DiseaseMatchingServiceTest {

    private DiseaseMatchingService service;

    @BeforeEach
    void setUp() {
        service = new DiseaseMatchingService();
    }

    @Test
    @DisplayName("Verify exact worked numerical calculation matching the mathematical specification")
    void testWorkedNumericalCalculation() {
        // Build mock Common cold
        Disease cold = new Disease(1, "Common cold", "Viral upper respiratory infection",
                "Rhinoviruses", "Rest, hydration", "Mild", "CDC");
        cold.addSymptomWeight("Runny nose", 5);
        cold.addSymptomWeight("Stuffy nose", 5);
        cold.addSymptomWeight("Sneezing", 5);
        cold.addSymptomWeight("Sore throat", 4);
        cold.addSymptomWeight("Cough", 3);
        cold.addSymptomWeight("Fatigue", 2);
        cold.addSymptomWeight("Headache", 2);
        cold.addSymptomWeight("Watery eyes", 2);

        // Sum a(d,s) = 5 + 5 + 5 + 4 + 3 + 2 + 2 + 2 = 28.0
        Set<String> selected = new LinkedHashSet<>(List.of("Runny nose", "Stuffy nose", "Loss of smell"));

        DiseaseMatch match = service.calculateMatch(selected, cold, Collections.emptyMap());

        // Step A: I(d) = Σ [u(s) * a(d,s) / 5] = (5*5/5) + (5*5/5) = 10.0
        assertEquals(10.0, match.getIntersectionScore(), 0.0001, "Intersection score must equal 10.0");

        // Step B: U(d) = Σ u(s) + Σ a(d,s) - I(d) = 15.0 + 28.0 - 10.0 = 33.0
        assertEquals(33.0, match.getUnionScore(), 0.0001, "Union score must equal 33.0");

        // Step C: Match(d) = [10.0 / 33.0] * 100 = 30.30303%
        double expectedMatch = (10.0 / 33.0) * 100.0;
        assertEquals(expectedMatch, match.getSimilarityPercentage(), 0.0001);
        assertEquals(30, match.getFormattedPercentage());

        // Verify matched and unmatched symptoms
        assertEquals(List.of("Runny nose", "Stuffy nose"), match.getMatchedSymptoms());
        assertEquals(List.of("Loss of smell"), match.getUnmatchedSelectedSymptoms());
    }

    @Test
    @DisplayName("Verify single symptom match calculation")
    void testSingleSymptomMatch() {
        Disease migraine = new Disease(22, "Migraine", "Neurovascular headache disorder",
                "Trigeminovascular activation", "Dark quiet room", "Moderate to Severe", "IHS");
        migraine.addSymptomWeight("Headache", 5);
        migraine.addSymptomWeight("Sensitivity to light", 5);
        migraine.addSymptomWeight("Nausea", 4);
        migraine.addSymptomWeight("Vomiting", 3);
        migraine.addSymptomWeight("Dizziness", 3);
        migraine.addSymptomWeight("Blurred vision", 3);
        // Sum a(d,s) = 23.0

        Set<String> selected = Set.of("Headache");
        DiseaseMatch match = service.calculateMatch(selected, migraine, Collections.emptyMap());

        // u(s) = 5.0, a(d,s) = 5 -> I(d) = 5.0
        assertEquals(5.0, match.getIntersectionScore(), 0.0001);
        // U(d) = 5.0 + 23.0 - 5.0 = 23.0
        assertEquals(23.0, match.getUnionScore(), 0.0001);
        // Match = 5.0 / 23.0 * 100 = 21.739%
        assertEquals((5.0 / 23.0) * 100.0, match.getSimilarityPercentage(), 0.001);
        assertEquals(1, match.getMatchedSymptoms().size());
        assertTrue(match.getUnmatchedSelectedSymptoms().isEmpty());
    }

    @Test
    @DisplayName("Verify ranking orders diseases by similarity descending")
    void testRankingOrder() {
        Disease d1 = new Disease(1, "Condition A", "Desc", "Cause", "Prec", "Mild", "Src");
        d1.addSymptomWeight("Fever", 5);
        d1.addSymptomWeight("Cough", 5);

        Disease d2 = new Disease(2, "Condition B", "Desc", "Cause", "Prec", "Mild", "Src");
        d2.addSymptomWeight("Fever", 5);
        d2.addSymptomWeight("Cough", 5);
        d2.addSymptomWeight("Headache", 5);
        d2.addSymptomWeight("Fatigue", 5);
        d2.addSymptomWeight("Weakness", 5);

        Set<String> selected = Set.of("Fever", "Cough");
        DiseaseMatch m1 = service.calculateMatch(selected, d1, Collections.emptyMap());
        DiseaseMatch m2 = service.calculateMatch(selected, d2, Collections.emptyMap());

        List<DiseaseMatch> list = new ArrayList<>(List.of(m2, m1));
        Collections.sort(list);

        // Condition A has 100% match (2/2 symptoms), Condition B has 40% (2/5 symptoms)
        assertEquals("Condition A", list.get(0).getDiseaseName());
        assertEquals("Condition B", list.get(1).getDiseaseName());
        assertEquals(100, list.get(0).getFormattedPercentage());
        assertTrue(list.get(0).getSimilarityPercentage() > list.get(1).getSimilarityPercentage());
    }

    @Test
    @DisplayName("Verify edge case: zero shared symptoms produces 0% match")
    void testZeroMatch() {
        Disease asthma = new Disease(6, "Asthma", "Chronic airway inflammation",
                "Atopy", "Inhaler", "Severe", "GINA");
        asthma.addSymptomWeight("Wheezing", 5);
        asthma.addSymptomWeight("Shortness of breath", 5);

        Set<String> selected = Set.of("Skin rash", "Itching");
        DiseaseMatch match = service.calculateMatch(selected, asthma, Collections.emptyMap());

        assertEquals(0.0, match.getIntersectionScore(), 0.0001);
        assertEquals(0.0, match.getSimilarityPercentage(), 0.0001);
        assertTrue(match.getMatchedSymptoms().isEmpty());
        assertEquals(2, match.getUnmatchedSelectedSymptoms().size());
    }

    @Test
    @DisplayName("Verify empty symptom selection returns empty results")
    void testEmptySelection() throws Exception {
        List<DiseaseMatch> matches = service.matchSymptoms(Collections.emptyList());
        assertNotNull(matches);
        assertTrue(matches.isEmpty());

        List<DiseaseMatch> nullMatches = service.matchSymptoms(null);
        assertNotNull(nullMatches);
        assertTrue(nullMatches.isEmpty());
    }
}
