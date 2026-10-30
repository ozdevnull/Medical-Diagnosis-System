package com.mds.controller;

import com.mds.AnimationUtils;
import com.mds.AppState;
import com.mds.SceneNavigator;
import com.mds.dao.DiseaseDAO;
import com.mds.dao.MedicineDAO;
import com.mds.model.Disease;
import com.mds.model.Medicine;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the user dashboard (DashboardPage.fxml).
 * Provides bottom navigation between Diagnosis, Disease Search, and Medicine Search panels.
 */
public class DashboardController {

    private final DiseaseDAO diseaseDAO = new DiseaseDAO();
    private final MedicineDAO medicineDAO = new MedicineDAO();

    // ── Top Bar ──────────────────────────────────────────────────────────────
    @FXML private Label lblWelcome;
    @FXML private Label lblAvatarInitial;
    @FXML private Button btnLogout;

    // ── Diagnosis panel ──────────────────────────────────────────────────────
    @FXML private ScrollPane paneDiagnosis;
    @FXML private Button btnStartDiagnosis;
    @FXML private Label lblInfoMessage;

    // ── Disease Search panel ─────────────────────────────────────────────────
    @FXML private ScrollPane paneSearchDiseases;
    @FXML private TextField tfDiseaseSearch;
    @FXML private Button btnDiseaseSearch;
    @FXML private VBox diseaseResultsBox;

    // ── Medicine Search panel ─────────────────────────────────────────────────
    @FXML private ScrollPane paneSearchMedicine;
    @FXML private TextField tfMedicineSearch;
    @FXML private Button btnMedicineSearch;
    @FXML private VBox medicineResultsBox;

    // ── Bottom Nav ────────────────────────────────────────────────────────────
    @FXML private HBox bottomNavBar;
    @FXML private VBox navDiagnosis;
    @FXML private VBox navDiseases;
    @FXML private VBox navMedicine;

    @FXML private StackPane navDiagnosisIconWrap;
    @FXML private StackPane navDiseasesIconWrap;
    @FXML private StackPane navMedicineIconWrap;

    @FXML private Label navDiagnosisLabel;
    @FXML private Label navDiseasesLabel;
    @FXML private Label navMedicineLabel;

    @FXML private Label navDiagnosisCaption;
    @FXML private Label navDiseasesCaption;
    @FXML private Label navMedicineCaption;

    /** Currently active tab (0 = Diagnosis, 1 = Diseases, 2 = Medicine) */
    private int activeTab = 0;
    private int lastTab   = -1;

    // ── Lifecycle ─────────────────────────────────────────────────────────────
    @FXML
    public void initialize() {
        String username = AppState.getInstance().getCurrentUsername();
        if (username != null && !username.isBlank()) {
            lblWelcome.setText("Hello, " + username);
            if (lblAvatarInitial != null)
                lblAvatarInitial.setText(username.substring(0, 1).toUpperCase());
        } else {
            lblWelcome.setText("Health Dashboard");
            if (lblAvatarInitial != null) lblAvatarInitial.setText("U");
        }

        // Wire Enter key on search fields
        tfDiseaseSearch.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) handleDiseaseSearch(null);
        });
        tfMedicineSearch.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) handleMedicineSearch(null);
        });

        // Search field focus glow animations
        tfDiseaseSearch.focusedProperty().addListener((obs, o, focused) -> {
            if (focused) AnimationUtils.searchFocus(tfDiseaseSearch);
            else         AnimationUtils.searchBlur(tfDiseaseSearch);
        });
        tfMedicineSearch.focusedProperty().addListener((obs, o, focused) -> {
            if (focused) AnimationUtils.searchFocus(tfMedicineSearch);
            else         AnimationUtils.searchBlur(tfMedicineSearch);
        });

        // Screen entrance
        AnimationUtils.screenEntrance(paneDiagnosis);
        AnimationUtils.screenEntrance(bottomNavBar);

        // Default panel
        showPanel(0);
    }

    // ── Bottom Nav Handlers ───────────────────────────────────────────────────
    @FXML
    private void handleNavDiagnosis(MouseEvent event) {
        if (activeTab != 0) AnimationUtils.navTap(navDiagnosis);
        showPanel(0);
    }

    @FXML
    private void handleNavDiseases(MouseEvent event) {
        if (activeTab != 1) AnimationUtils.navTap(navDiseases);
        showPanel(1);
    }

    @FXML
    private void handleNavMedicine(MouseEvent event) {
        if (activeTab != 2) AnimationUtils.navTap(navMedicine);
        showPanel(2);
    }

    private void showPanel(int tab) {
        activeTab = tab;

        setPanel(paneDiagnosis,      tab == 0);
        setPanel(paneSearchDiseases, tab == 1);
        setPanel(paneSearchMedicine, tab == 2);

        updateNavActive(navDiagnosisCaption, navDiagnosisIconWrap, tab == 0);
        updateNavActive(navDiseasesCaption,  navDiseasesIconWrap,  tab == 1);
        updateNavActive(navMedicineCaption,  navMedicineIconWrap,  tab == 2);

        lastTab = tab;
    }

    private void setPanel(ScrollPane pane, boolean visible) {
        pane.setVisible(visible);
        pane.setManaged(visible);
        if (visible) {
            AnimationUtils.panelReveal(pane);
        }
    }

    private void updateNavActive(Label caption, StackPane iconWrap, boolean active) {
        if (active) {
            caption.getStyleClass().removeAll("nav-caption");
            if (!caption.getStyleClass().contains("nav-caption-active"))
                caption.getStyleClass().add("nav-caption-active");
            if (!iconWrap.getStyleClass().contains("nav-icon-wrap-active"))
                iconWrap.getStyleClass().add("nav-icon-wrap-active");
        } else {
            caption.getStyleClass().removeAll("nav-caption-active");
            if (!caption.getStyleClass().contains("nav-caption"))
                caption.getStyleClass().add("nav-caption");
            iconWrap.getStyleClass().removeAll("nav-icon-wrap-active");
        }
    }

    // ── Button press/release micro-animations ─────────────────────────────────
    @FXML private void onStartDiagnosisPressed(MouseEvent e)  { AnimationUtils.buttonPress(btnStartDiagnosis); }
    @FXML private void onStartDiagnosisReleased(MouseEvent e) { AnimationUtils.buttonRelease(btnStartDiagnosis); }
    @FXML private void onDiseaseSearchPressed(MouseEvent e)   { AnimationUtils.buttonPress(btnDiseaseSearch); }
    @FXML private void onDiseaseSearchReleased(MouseEvent e)  { AnimationUtils.buttonRelease(btnDiseaseSearch); }
    @FXML private void onMedicineSearchPressed(MouseEvent e)  { AnimationUtils.buttonPress(btnMedicineSearch); }
    @FXML private void onMedicineSearchReleased(MouseEvent e) { AnimationUtils.buttonRelease(btnMedicineSearch); }
    @FXML private void onLogoutPressed(MouseEvent e)          { AnimationUtils.buttonPress(btnLogout); }
    @FXML private void onLogoutReleased(MouseEvent e)         { AnimationUtils.buttonRelease(btnLogout); }

    // ── Diagnosis ─────────────────────────────────────────────────────────────
    @FXML
    private void handleStartDiagnosis(ActionEvent event) {
        AnimationUtils.confirmPop(btnStartDiagnosis);
        // Brief delay so the pop animation plays before navigating
        javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(Duration.millis(150));
        delay.setOnFinished(e -> SceneNavigator.switchScene(btnStartDiagnosis, "SymptomSelectionPage.fxml"));
        delay.play();
    }

    // ── Disease Search ────────────────────────────────────────────────────────
    @FXML
    private void handleDiseaseSearchTyped(KeyEvent event) {
        // Reserved for future autocomplete integration
    }

    @FXML
    private void handleDiseaseSearch(ActionEvent event) {
        String query = tfDiseaseSearch.getText() != null ? tfDiseaseSearch.getText().trim() : "";
        if (query.isEmpty()) {
            AnimationUtils.shake(tfDiseaseSearch);
            return;
        }
        AnimationUtils.confirmPop(btnDiseaseSearch);

        // Run database query on background thread
        Task<List<DiseaseSearchResult>> task = new Task<>() {
            @Override
            protected List<DiseaseSearchResult> call() throws Exception {
                List<Disease> diseases = diseaseDAO.searchDiseases(query);
                List<DiseaseSearchResult> results = new ArrayList<>();
                for (Disease d : diseases) {
                    List<Medicine> relatedMeds = diseaseDAO.getRelatedMedicinesForDisease(d.getId());
                    results.add(new DiseaseSearchResult(d, relatedMeds));
                }
                return results;
            }
        };

        task.setOnSucceeded(e -> {
            List<DiseaseSearchResult> results = task.getValue();
            populateDiseaseResults(query, results);
        });

        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            System.err.println("[MDS Disease Search Error] " + ex.getMessage());
            showSearchError(diseaseResultsBox, "Database Error",
                    "Unable to connect to MariaDB: " + ex.getMessage());
        });

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    private record DiseaseSearchResult(Disease disease, List<Medicine> relatedMedicines) {}

    private void populateDiseaseResults(String query, List<DiseaseSearchResult> results) {
        diseaseResultsBox.getChildren().clear();

        if (results == null || results.isEmpty()) {
            VBox emptyCard = createEmptyStateCard("🔬", "No Conditions Found",
                    "No diseases matching \"" + query + "\" were found in the database. Try searching for symptoms or common names.");
            diseaseResultsBox.getChildren().add(emptyCard);
            AnimationUtils.panelReveal(emptyCard);
            return;
        }

        List<VBox> cards = new ArrayList<>();
        for (DiseaseSearchResult res : results) {
            cards.add(createDiseaseCard(res.disease(), res.relatedMedicines()));
        }

        diseaseResultsBox.getChildren().addAll(cards);
        AnimationUtils.staggerReveal(cards, 60);
    }

    private VBox createDiseaseCard(Disease disease, List<Medicine> medicines) {
        VBox card = new VBox(8);
        card.getStyleClass().add("result-card");
        card.setPadding(new Insets(14));

        // Header: Disease Name + Severity Badge
        HBox header = new HBox(8);
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label(disease.getName());
        title.getStyleClass().add("condition-title");
        title.setWrapText(true);
        HBox.setHgrow(title, Priority.ALWAYS);

        Label severity = new Label(disease.getSeverityInformation());
        severity.getStyleClass().add("feature-chip");

        header.getChildren().addAll(title, severity);
        card.getChildren().add(header);

        // Description
        Label desc = new Label(disease.getDescription());
        desc.getStyleClass().add("helper-text");
        desc.setWrapText(true);
        card.getChildren().add(desc);

        // Causes
        if (disease.getCauses() != null && !disease.getCauses().isBlank()) {
            Label causes = new Label("Etiology / Causes: " + disease.getCauses());
            causes.getStyleClass().add("helper-text");
            causes.setWrapText(true);
            card.getChildren().add(causes);
        }

        // Associated Symptoms
        if (!disease.getSymptomWeights().isEmpty()) {
            List<String> symptomItems = new ArrayList<>();
            disease.getSymptomWeights().forEach((s, w) -> symptomItems.add(s + " (" + w + "/5)"));
            Label symptoms = new Label("Associated Symptoms: " + String.join(", ", symptomItems));
            symptoms.getStyleClass().add("helper-text");
            symptoms.setStyle("-fx-font-weight: 600; -fx-text-fill: #3F5A40;");
            symptoms.setWrapText(true);
            card.getChildren().add(symptoms);
        }

        // Precautions
        if (disease.getPrecautions() != null && !disease.getPrecautions().isBlank()) {
            Label precautions = new Label("Clinical Precautions: " + disease.getPrecautions());
            precautions.getStyleClass().add("helper-text");
            precautions.setWrapText(true);
            card.getChildren().add(precautions);
        }

        // Related educational medicines
        if (medicines != null && !medicines.isEmpty()) {
            List<String> medNames = medicines.stream().map(Medicine::getName).toList();
            Label meds = new Label("Related Educational Medicines: " + String.join(", ", medNames));
            meds.getStyleClass().add("helper-text");
            meds.setStyle("-fx-text-fill: #6B8E6B; -fx-font-weight: 600;");
            meds.setWrapText(true);
            card.getChildren().add(meds);
        }

        // Reference citation
        Label source = new Label("Reference: " + disease.getReferenceSource());
        source.setStyle("-fx-font-size: 10px; -fx-text-fill: #9CAF88; -fx-font-style: italic;");
        card.getChildren().add(source);

        return card;
    }

    // ── Medicine Search ───────────────────────────────────────────────────────
    @FXML
    private void handleMedicineSearchTyped(KeyEvent event) {
        // Reserved for future autocomplete integration
    }

    @FXML
    private void handleMedicineSearch(ActionEvent event) {
        String query = tfMedicineSearch.getText() != null ? tfMedicineSearch.getText().trim() : "";
        if (query.isEmpty()) {
            AnimationUtils.shake(tfMedicineSearch);
            return;
        }
        AnimationUtils.confirmPop(btnMedicineSearch);

        // Run database query on background thread
        Task<List<MedicineSearchResult>> task = new Task<>() {
            @Override
            protected List<MedicineSearchResult> call() throws Exception {
                List<Medicine> medicines = medicineDAO.searchMedicines(query);
                List<MedicineSearchResult> results = new ArrayList<>();
                for (Medicine m : medicines) {
                    List<Disease> relatedDiseases = medicineDAO.getRelatedDiseasesForMedicine(m.getId());
                    results.add(new MedicineSearchResult(m, relatedDiseases));
                }
                return results;
            }
        };

        task.setOnSucceeded(e -> {
            List<MedicineSearchResult> results = task.getValue();
            populateMedicineResults(query, results);
        });

        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            System.err.println("[MDS Medicine Search Error] " + ex.getMessage());
            showSearchError(medicineResultsBox, "Database Error",
                    "Unable to connect to MariaDB: " + ex.getMessage());
        });

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    private record MedicineSearchResult(Medicine medicine, List<Disease> relatedDiseases) {}

    private void populateMedicineResults(String query, List<MedicineSearchResult> results) {
        medicineResultsBox.getChildren().clear();

        if (results == null || results.isEmpty()) {
            VBox emptyCard = createEmptyStateCard("💊", "No Medicines Found",
                    "No medications matching \"" + query + "\" were found in the database. Try searching for generic names.");
            medicineResultsBox.getChildren().add(emptyCard);
            AnimationUtils.panelReveal(emptyCard);
            return;
        }

        List<VBox> cards = new ArrayList<>();
        for (MedicineSearchResult res : results) {
            cards.add(createMedicineCard(res.medicine(), res.relatedDiseases()));
        }

        medicineResultsBox.getChildren().addAll(cards);
        AnimationUtils.staggerReveal(cards, 60);
    }

    private VBox createMedicineCard(Medicine medicine, List<Disease> diseases) {
        VBox card = new VBox(8);
        card.getStyleClass().add("result-card");
        card.setPadding(new Insets(14));

        Label title = new Label(medicine.getName());
        title.getStyleClass().add("condition-title");
        title.setWrapText(true);

        Label desc = new Label(medicine.getDescription());
        desc.getStyleClass().add("helper-text");
        desc.setWrapText(true);

        card.getChildren().addAll(title, desc);

        if (medicine.getUses() != null && !medicine.getUses().isBlank()) {
            Label uses = new Label("Approved Clinical Indications: " + medicine.getUses());
            uses.getStyleClass().add("helper-text");
            uses.setWrapText(true);
            card.getChildren().add(uses);
        }

        if (medicine.getSideEffects() != null && !medicine.getSideEffects().isBlank()) {
            Label sideEffects = new Label("Documented Adverse Effects: " + medicine.getSideEffects());
            sideEffects.getStyleClass().add("helper-text");
            sideEffects.setWrapText(true);
            card.getChildren().add(sideEffects);
        }

        if (medicine.getPrecautions() != null && !medicine.getPrecautions().isBlank()) {
            Label precautions = new Label("Precautions & Warnings: " + medicine.getPrecautions());
            precautions.getStyleClass().add("helper-text");
            precautions.setWrapText(true);
            card.getChildren().add(precautions);
        }

        if (diseases != null && !diseases.isEmpty()) {
            List<String> dNames = diseases.stream().map(Disease::getName).toList();
            Label relDiseases = new Label("Associated Conditions: " + String.join(", ", dNames));
            relDiseases.getStyleClass().add("helper-text");
            relDiseases.setStyle("-fx-text-fill: #3F5A40; -fx-font-weight: 600;");
            relDiseases.setWrapText(true);
            card.getChildren().add(relDiseases);
        }

        Label disclaimer = new Label("⚠ Educational reference only. Do not self-prescribe or alter dosage without a doctor.");
        disclaimer.setStyle("-fx-font-size: 10px; -fx-text-fill: #9C3D32; -fx-font-weight: 600;");
        disclaimer.setWrapText(true);
        card.getChildren().add(disclaimer);

        return card;
    }

    private VBox createEmptyStateCard(String icon, String title, String description) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setMinHeight(200.0);
        card.getStyleClass().add("empty-state-card");
        card.setPadding(new Insets(20));

        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("empty-state-icon");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("section-title");

        Label descLabel = new Label(description);
        descLabel.getStyleClass().add("helper-text");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(260.0);
        descLabel.setTextAlignment(TextAlignment.CENTER);

        card.getChildren().addAll(iconLabel, titleLabel, descLabel);
        return card;
    }

    private void showSearchError(VBox targetBox, String title, String error) {
        targetBox.getChildren().clear();
        VBox errorCard = createEmptyStateCard("⚠️", title, error);
        targetBox.getChildren().add(errorCard);
        AnimationUtils.panelReveal(errorCard);
    }

    // ── Logout ────────────────────────────────────────────────────────────────
    @FXML
    private void handleLogout(ActionEvent event) {
        AppState.getInstance().clearAll();
        SceneNavigator.switchScene((Node) event.getSource(), "StartPage.fxml");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private void showInfo(String message) {
        lblInfoMessage.setText(message);
        lblInfoMessage.setVisible(true);
        lblInfoMessage.setManaged(true);
        AnimationUtils.labelFadeIn(lblInfoMessage);
    }
}
