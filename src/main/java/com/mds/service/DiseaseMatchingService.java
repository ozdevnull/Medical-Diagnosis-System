package com.mds.service;

import com.mds.dao.DiseaseDAO;
import com.mds.model.Disease;
import com.mds.model.DiseaseMatch;

import java.sql.SQLException;
import java.util.*;

/**
 * Service implementing the weighted disease matching algorithm for MDS.
 * <p>
 * Mathematical Specification:
 * <ul>
 *   <li>S = symptoms selected by the user.</li>
 *   <li>D = symptoms associated with a candidate disease.</li>
 *   <li>u(s) = user-side importance weight of symptom s (default = 5.0 on a 1–5 scale).</li>
 *   <li>a(d,s) = disease association weight, between 1 and 5.</li>
 *   <li>Step A (Weighted intersection): I(d) = Σ [u(s) × a(d,s) / 5]</li>
 *   <li>Step B (Weighted union): U(d) = Σ u(s) + Σ a(d,s) - I(d)</li>
 *   <li>Step C (Similarity percentage): Match(d) = [I(d) / U(d)] × 100</li>
 * </ul>
 */
public class DiseaseMatchingService {

    public static final double DEFAULT_USER_WEIGHT = 5.0; // Standard 1–5 clinical scale
    private final DiseaseDAO diseaseDAO;

    public DiseaseMatchingService() {
        this(new DiseaseDAO());
    }

    public DiseaseMatchingService(DiseaseDAO diseaseDAO) {
        this.diseaseDAO = Objects.requireNonNull(diseaseDAO, "DiseaseDAO cannot be null");
    }

    /**
     * Executes the complete matching workflow against database records.
     *
     * @param selectedSymptoms list of symptom display names chosen by user
     * @return sorted list of DiseaseMatch objects in descending similarity rank
     * @throws SQLException on database error
     */
    public List<DiseaseMatch> matchSymptoms(List<String> selectedSymptoms) throws SQLException {
        if (selectedSymptoms == null || selectedSymptoms.isEmpty()) {
            return Collections.emptyList();
        }

        // Clean and deduplicate selected symptoms
        Set<String> cleanSelected = new LinkedHashSet<>();
        for (String s : selectedSymptoms) {
            if (s != null && !s.isBlank()) {
                cleanSelected.add(s.trim());
            }
        }

        if (cleanSelected.isEmpty()) {
            return Collections.emptyList();
        }

        List<Disease> candidates = diseaseDAO.getCandidateDiseasesForSymptoms(cleanSelected);
        List<DiseaseMatch> matches = new ArrayList<>();

        for (Disease disease : candidates) {
            DiseaseMatch match = calculateMatch(cleanSelected, disease, Collections.emptyMap());
            if (match.getSimilarityPercentage() > 0.0) {
                matches.add(match);
            }
        }

        Collections.sort(matches);
        return matches;
    }

    /**
     * Calculates the weighted similarity match between user symptoms and a candidate disease.
     * Pure calculation logic with zero UI dependency — fully unit-testable.
     *
     * @param selectedSymptoms Set of user selected symptom names
     * @param disease          Candidate disease with complete symptom profile
     * @param customUserWeights Optional custom user weights u(s); falls back to DEFAULT_USER_WEIGHT (5.0)
     * @return DiseaseMatch record
     */
    public DiseaseMatch calculateMatch(Set<String> selectedSymptoms,
                                       Disease disease,
                                       Map<String, Double> customUserWeights) {
        Objects.requireNonNull(selectedSymptoms, "selectedSymptoms cannot be null");
        Objects.requireNonNull(disease, "disease cannot be null");

        Map<String, Integer> diseaseSymptoms = disease.getSymptomWeights();

        List<String> matchedSymptoms = new ArrayList<>();
        List<String> unmatchedSelectedSymptoms = new ArrayList<>();
        Map<String, Integer> matchedSymptomWeights = new LinkedHashMap<>();

        double sumUserWeights = 0.0;
        double intersectionScore = 0.0;

        for (String s : selectedSymptoms) {
            double u = customUserWeights != null && customUserWeights.containsKey(s)
                    ? customUserWeights.get(s)
                    : DEFAULT_USER_WEIGHT;
            sumUserWeights += u;

            // Case-insensitive lookup in disease symptoms
            Integer weight = findCaseInsensitiveWeight(diseaseSymptoms, s);
            if (weight != null) {
                matchedSymptoms.add(s);
                matchedSymptomWeights.put(s, weight);
                // Step A: I(d) = Σ [u(s) × a(d,s) / 5]
                intersectionScore += (u * weight) / 5.0;
            } else {
                unmatchedSelectedSymptoms.add(s);
            }
        }

        // Order matched symptoms by disease association weight descending
        matchedSymptoms.sort((a, b) -> Integer.compare(
                matchedSymptomWeights.getOrDefault(b, 0),
                matchedSymptomWeights.getOrDefault(a, 0)
        ));

        // Sum of all association weights for the candidate disease: Σ a(d,s)
        double sumDiseaseWeights = 0.0;
        for (int w : diseaseSymptoms.values()) {
            sumDiseaseWeights += w;
        }

        // Step B: U(d) = Σ u(s) + Σ a(d,s) - I(d)
        double unionScore = sumUserWeights + sumDiseaseWeights - intersectionScore;

        // Step C: Match(d) = [I(d) / U(d)] × 100
        double similarityPercentage = 0.0;
        if (unionScore > 0.0 && intersectionScore > 0.0) {
            similarityPercentage = (intersectionScore / unionScore) * 100.0;
            similarityPercentage = Math.min(100.0, Math.max(0.0, similarityPercentage));
        }

        return new DiseaseMatch(
                disease,
                similarityPercentage,
                matchedSymptoms,
                unmatchedSelectedSymptoms,
                matchedSymptomWeights,
                intersectionScore,
                unionScore
        );
    }

    private Integer findCaseInsensitiveWeight(Map<String, Integer> map, String key) {
        if (map.containsKey(key)) return map.get(key);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(key)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
