package com.mds.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Model representing a weighted disease match outcome based on selected symptoms.
 */
public class DiseaseMatch implements Comparable<DiseaseMatch> {

    private final Disease disease;
    private final double similarityPercentage;
    private final List<String> matchedSymptoms;
    private final List<String> unmatchedSelectedSymptoms;
    private final Map<String, Integer> matchedSymptomWeights;
    private final double intersectionScore;
    private final double unionScore;

    public DiseaseMatch(Disease disease,
                        double similarityPercentage,
                        List<String> matchedSymptoms,
                        List<String> unmatchedSelectedSymptoms,
                        Map<String, Integer> matchedSymptomWeights,
                        double intersectionScore,
                        double unionScore) {
        this.disease = Objects.requireNonNull(disease, "Disease cannot be null");
        this.similarityPercentage = similarityPercentage;
        this.matchedSymptoms = matchedSymptoms != null ? List.copyOf(matchedSymptoms) : List.of();
        this.unmatchedSelectedSymptoms = unmatchedSelectedSymptoms != null ? List.copyOf(unmatchedSelectedSymptoms) : List.of();
        this.matchedSymptomWeights = matchedSymptomWeights != null ? Map.copyOf(matchedSymptomWeights) : Map.of();
        this.intersectionScore = intersectionScore;
        this.unionScore = unionScore;
    }

    public Disease getDisease() {
        return disease;
    }

    public String getDiseaseName() {
        return disease.getName();
    }

    public double getSimilarityPercentage() {
        return similarityPercentage;
    }

    public int getFormattedPercentage() {
        return (int) Math.round(similarityPercentage);
    }

    public List<String> getMatchedSymptoms() {
        return matchedSymptoms;
    }

    public List<String> getUnmatchedSelectedSymptoms() {
        return unmatchedSelectedSymptoms;
    }

    public Map<String, Integer> getMatchedSymptomWeights() {
        return matchedSymptomWeights;
    }

    public double getIntersectionScore() {
        return intersectionScore;
    }

    public double getUnionScore() {
        return unionScore;
    }

    @Override
    public int compareTo(DiseaseMatch o) {
        // Descending order by similarity percentage
        int cmp = Double.compare(o.similarityPercentage, this.similarityPercentage);
        if (cmp != 0) return cmp;
        // Secondary descending order by intersection score
        cmp = Double.compare(o.intersectionScore, this.intersectionScore);
        if (cmp != 0) return cmp;
        // Tertiary tie-breaker by disease name
        return this.disease.getName().compareToIgnoreCase(o.disease.getName());
    }

    @Override
    public String toString() {
        return String.format("%s: %.1f%% (Matched: %d, Union: %.2f)",
                disease.getName(), similarityPercentage, matchedSymptoms.size(), unionScore);
    }
}
