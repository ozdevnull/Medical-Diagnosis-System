package com.mds.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Model representing a medically grounded disease or clinical condition.
 */
public class Disease {

    private final int id;
    private final String name;
    private final String description;
    private final String causes;
    private final String precautions;
    private final String severityInformation;
    private final String referenceSource;
    private final Map<String, Integer> symptomWeights = new LinkedHashMap<>();

    public Disease(int id, String name, String description, String causes,
                   String precautions, String severityInformation, String referenceSource) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "Disease name cannot be null");
        this.description = description != null ? description : "";
        this.causes = causes != null ? causes : "";
        this.precautions = precautions != null ? precautions : "";
        this.severityInformation = severityInformation != null ? severityInformation : "Moderate";
        this.referenceSource = referenceSource != null ? referenceSource : "WHO / CDC / NHS";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCauses() {
        return causes;
    }

    public String getPrecautions() {
        return precautions;
    }

    public String getSeverityInformation() {
        return severityInformation;
    }

    public String getReferenceSource() {
        return referenceSource;
    }

    public Map<String, Integer> getSymptomWeights() {
        return Collections.unmodifiableMap(symptomWeights);
    }

    public void addSymptomWeight(String symptomName, int weight) {
        if (symptomName != null && !symptomName.isBlank()) {
            symptomWeights.put(symptomName, weight);
        }
    }

    public void setSymptomWeights(Map<String, Integer> map) {
        symptomWeights.clear();
        if (map != null) {
            symptomWeights.putAll(map);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disease disease = (Disease) o;
        return id == disease.id || name.equalsIgnoreCase(disease.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }

    @Override
    public String toString() {
        return name;
    }
}
