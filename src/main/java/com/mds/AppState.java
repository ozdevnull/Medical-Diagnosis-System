package com.mds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Shared application session state for MDS (Medical Diagnosis System).
 * Manages user session and selected symptoms across controllers.
 */
public class AppState {

    private static final AppState INSTANCE = new AppState();

    private final List<String> selectedSymptoms = new ArrayList<>();
    private final List<com.mds.model.DiseaseMatch> lastDiagnosisResults = new ArrayList<>();
    private String currentUsername = null;

    private AppState() {
        // Singleton pattern
    }

    public static AppState getInstance() {
        return INSTANCE;
    }

    /**
     * Returns an unmodifiable copy of the currently selected symptoms.
     */
    public synchronized List<String> getSelectedSymptoms() {
        return Collections.unmodifiableList(new ArrayList<>(selectedSymptoms));
    }

    /**
     * Updates the selected symptoms with defensive copying.
     *
     * @param symptoms List of chosen symptoms
     */
    public synchronized void setSelectedSymptoms(List<String> symptoms) {
        this.selectedSymptoms.clear();
        if (symptoms != null) {
            this.selectedSymptoms.addAll(symptoms);
        }
    }

    /**
     * Returns an unmodifiable copy of the most recent diagnosis results.
     */
    public synchronized List<com.mds.model.DiseaseMatch> getLastDiagnosisResults() {
        return Collections.unmodifiableList(new ArrayList<>(lastDiagnosisResults));
    }

    /**
     * Updates the recent diagnosis results with defensive copying.
     */
    public synchronized void setLastDiagnosisResults(List<com.mds.model.DiseaseMatch> results) {
        this.lastDiagnosisResults.clear();
        if (results != null) {
            this.lastDiagnosisResults.addAll(results);
        }
    }

    /**
     * Clears only the diagnosis/symptom state when starting a fresh diagnosis session.
     */
    public synchronized void clearDiagnosis() {
        this.selectedSymptoms.clear();
        this.lastDiagnosisResults.clear();
    }

    /**
     * Clears all session data (diagnosis symptoms and active user session) upon logout.
     */
    public synchronized void clearAll() {
        this.selectedSymptoms.clear();
        this.lastDiagnosisResults.clear();
        this.currentUsername = null;
    }

    public synchronized String getCurrentUsername() {
        return currentUsername;
    }

    public synchronized void setCurrentUsername(String username) {
        this.currentUsername = username;
    }
}
