package com.mds.controller;

import com.mds.AnimationUtils;
import com.mds.AppState;
import com.mds.SceneNavigator;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.List;
import java.util.Set;

/**
 * Controller for the diagnosis results view (ResultsPage.fxml).
 * Displays selected symptoms, placeholder condition matches with animated progress bars,
 * urgent symptom safety notices, and medical disclaimer.
 * <p>
 * NOTE: The match percentages and condition names are illustrative frontend
 * placeholders. They MUST be replaced with a validated medical knowledge base
 * or inference engine in future phases.
 */
public class ResultsController {

    @FXML private VBox mainContainer;
    @FXML private Button btnBackTop;
    @FXML private Button btnBackDashboard;
    @FXML private Button btnNewDiagnosis;

    @FXML private Label lblSelectedSymptomsSummary;
    @FXML private VBox urgentNoticeCard;
    @FXML private Label lblUrgentNoticeText;

    // Result cards (one VBox per rank)
    @FXML private VBox resultCard1;
    @FXML private VBox resultCard2;
    @FXML private VBox resultCard3;
    @FXML private VBox resultCard4;
    @FXML private VBox resultCard5;

    // Rank 1
    @FXML private Label lblRank1;
    @FXML private Label lblDisease1;
    @FXML private Label lblMatch1;
    @FXML private ProgressBar pbMatch1;
    @FXML private Label lblDescription1;

    // Rank 2
    @FXML private Label lblRank2;
    @FXML private Label lblDisease2;
    @FXML private Label lblMatch2;
    @FXML private ProgressBar pbMatch2;
    @FXML private Label lblDescription2;

    // Rank 3
    @FXML private Label lblRank3;
    @FXML private Label lblDisease3;
    @FXML private Label lblMatch3;
    @FXML private ProgressBar pbMatch3;
    @FXML private Label lblDescription3;

    // Rank 4
    @FXML private Label lblRank4;
    @FXML private Label lblDisease4;
    @FXML private Label lblMatch4;
    @FXML private ProgressBar pbMatch4;
    @FXML private Label lblDescription4;

    // Rank 5
    @FXML private Label lblRank5;
    @FXML private Label lblDisease5;
    @FXML private Label lblMatch5;
    @FXML private ProgressBar pbMatch5;
    @FXML private Label lblDescription5;

    // Potentially urgent symptoms requiring prominent medical safety advice
    private static final Set<String> URGENT_SYMPTOMS = Set.of(
            "Chest pain",
            "Shortness of breath",
            "Neck stiffness",
            "Palpitations",
            "Yellowing of skin/eyes"
    );

    private final com.mds.service.DiseaseMatchingService matchingService = new com.mds.service.DiseaseMatchingService();
    private VBox noResultsContainer;

    @FXML
    public void initialize() {
        // Screen entrance first
        AnimationUtils.screenEntrance(mainContainer != null ? mainContainer : btnBackTop);

        populateSelectedSymptoms();
        checkUrgentSymptoms();
        loadDiagnosisResults();
    }

    private void populateSelectedSymptoms() {
        List<String> symptoms = AppState.getInstance().getSelectedSymptoms();
        if (symptoms.isEmpty()) {
            lblSelectedSymptomsSummary.setText("No symptoms specified.");
        } else {
            lblSelectedSymptomsSummary.setText(String.join(", ", symptoms));
        }
    }

    private void checkUrgentSymptoms() {
        List<String> symptoms = AppState.getInstance().getSelectedSymptoms();
        boolean hasChestPain = symptoms.contains("Chest pain");
        boolean hasShortnessOfBreath = symptoms.contains("Shortness of breath");
        boolean hasFever = symptoms.contains("Fever");
        boolean hasNeckStiffness = symptoms.contains("Neck stiffness");
        boolean hasLightSensitivity = symptoms.contains("Sensitivity to light");
        boolean hasMeningealSign = hasFever && (hasNeckStiffness || hasLightSensitivity);
        boolean hasOtherUrgent = symptoms.stream().anyMatch(URGENT_SYMPTOMS::contains);

        boolean isUrgent = hasChestPain || hasShortnessOfBreath || hasMeningealSign || hasOtherUrgent;

        if (isUrgent) {
            urgentNoticeCard.setVisible(true);
            urgentNoticeCard.setManaged(true);
            AnimationUtils.labelFadeIn(urgentNoticeCard);

            if (hasChestPain || hasShortnessOfBreath) {
                lblUrgentNoticeText.setText(
                        "URGENT WARNING: Chest pain or shortness of breath may signify an acute cardiovascular " +
                        "or respiratory emergency. Please seek emergency medical care immediately (call emergency services).");
            } else if (hasMeningealSign) {
                lblUrgentNoticeText.setText(
                        "CRITICAL WARNING: Fever accompanied by neck stiffness or light sensitivity can indicate acute " +
                        "meningitis. Immediate emergency hospital evaluation is imperative.");
            } else {
                lblUrgentNoticeText.setText(
                        "Notice: One or more selected symptoms indicate potential clinical urgency. " +
                        "Please obtain an immediate professional medical evaluation.");
            }
        } else {
            urgentNoticeCard.setVisible(false);
            urgentNoticeCard.setManaged(false);
        }
    }

    private void loadDiagnosisResults() {
        List<String> symptoms = AppState.getInstance().getSelectedSymptoms();
        if (symptoms.isEmpty()) {
            hideAllCards();
            showNoResultsMessage("No Symptoms Selected", "Please return to the clinical triage screen and select your symptoms to receive an assessment.");
            return;
        }

        // Run database matching asynchronously to never block JavaFX Application Thread
        javafx.concurrent.Task<List<com.mds.model.DiseaseMatch>> task = new javafx.concurrent.Task<>() {
            @Override
            protected List<com.mds.model.DiseaseMatch> call() throws Exception {
                return matchingService.matchSymptoms(symptoms);
            }
        };

        task.setOnSucceeded(e -> {
            List<com.mds.model.DiseaseMatch> matches = task.getValue();
            AppState.getInstance().setLastDiagnosisResults(matches);
            displayMatches(matches);
        });

        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            System.err.println("[MDS Results Error] Disease matching query failed: " + ex.getMessage());
            hideAllCards();
            showNoResultsMessage("Database Connection Notice",
                    "Unable to query local MariaDB database. Ensure XAMPP MariaDB is running on port 3306.\nError: " + ex.getMessage());
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void displayMatches(List<com.mds.model.DiseaseMatch> matches) {
        removeNoResultsMessage();

        if (matches == null || matches.isEmpty()) {
            hideAllCards();
            showNoResultsMessage("No Direct Condition Matches",
                    "No matching conditions met the diagnostic threshold for the selected symptom combination. Please consult a qualified physician for clinical evaluation.");
            return;
        }

        List<VBox> cardContainers = List.of(resultCard1, resultCard2, resultCard3, resultCard4, resultCard5);
        List<Label> rankLabels = List.of(lblRank1, lblRank2, lblRank3, lblRank4, lblRank5);
        List<Label> diseaseLabels = List.of(lblDisease1, lblDisease2, lblDisease3, lblDisease4, lblDisease5);
        List<Label> matchLabels = List.of(lblMatch1, lblMatch2, lblMatch3, lblMatch4, lblMatch5);
        List<ProgressBar> progressBars = List.of(pbMatch1, pbMatch2, pbMatch3, pbMatch4, pbMatch5);
        List<Label> descLabels = List.of(lblDescription1, lblDescription2, lblDescription3, lblDescription4, lblDescription5);

        List<VBox> visibleCards = new java.util.ArrayList<>();
        List<Runnable> barAnimators = new java.util.ArrayList<>();

        int count = Math.min(5, matches.size());
        for (int i = 0; i < 5; i++) {
            VBox card = cardContainers.get(i);
            if (card == null) continue;

            if (i < count) {
                com.mds.model.DiseaseMatch match = matches.get(i);
                com.mds.model.Disease disease = match.getDisease();

                rankLabels.get(i).setText(String.valueOf(i + 1));
                diseaseLabels.get(i).setText(disease.getName());
                matchLabels.get(i).setText(match.getFormattedPercentage() + "% match");
                descLabels.get(i).setText(formatCardDescription(match));

                card.setVisible(true);
                card.setManaged(true);
                visibleCards.add(card);

                ProgressBar pb = progressBars.get(i);
                pb.setProgress(0.0);
                double targetProgress = Math.min(1.0, Math.max(0.0, match.getSimilarityPercentage() / 100.0));
                barAnimators.add(() -> AnimationUtils.animateProgressBar(pb, targetProgress));
            } else {
                card.setVisible(false);
                card.setManaged(false);
            }
        }

        // Staggered reveal for visible cards
        AnimationUtils.staggerReveal(visibleCards, 80);

        // Animate progress bars after card appearance
        PauseTransition barDelay = new PauseTransition(Duration.millis(200));
        barDelay.setOnFinished(e -> {
            for (Runnable r : barAnimators) {
                r.run();
            }
        });
        barDelay.play();
    }

    private String formatCardDescription(com.mds.model.DiseaseMatch match) {
        StringBuilder sb = new StringBuilder();
        sb.append(match.getDisease().getDescription());

        if (!match.getMatchedSymptoms().isEmpty()) {
            sb.append("\n\n• Matched symptoms: ").append(String.join(", ", match.getMatchedSymptoms()));
        }
        if (!match.getUnmatchedSelectedSymptoms().isEmpty()) {
            sb.append("\n• Unmatched selected: ").append(String.join(", ", match.getUnmatchedSelectedSymptoms()));
        }
        if (match.getDisease().getPrecautions() != null && !match.getDisease().getPrecautions().isBlank()) {
            sb.append("\n• Precautions: ").append(match.getDisease().getPrecautions());
        }
        if (match.getDisease().getSeverityInformation() != null && !match.getDisease().getSeverityInformation().isBlank()) {
            sb.append(" [Severity: ").append(match.getDisease().getSeverityInformation()).append("]");
        }
        return sb.toString();
    }

    private void hideAllCards() {
        List<VBox> cards = List.of(resultCard1, resultCard2, resultCard3, resultCard4, resultCard5);
        for (VBox card : cards) {
            if (card != null) {
                card.setVisible(false);
                card.setManaged(false);
            }
        }
    }

    private void showNoResultsMessage(String title, String message) {
        removeNoResultsMessage();
        if (resultCard1 != null && resultCard1.getParent() instanceof javafx.scene.layout.Pane parent) {
            noResultsContainer = new VBox(10);
            noResultsContainer.getStyleClass().add("empty-state-card");
            noResultsContainer.setAlignment(javafx.geometry.Pos.CENTER);
            noResultsContainer.setPadding(new javafx.geometry.Insets(24));

            Label iconLabel = new Label("📋");
            iconLabel.getStyleClass().add("empty-state-icon");

            Label titleLabel = new Label(title);
            titleLabel.getStyleClass().add("section-title");

            Label descLabel = new Label(message);
            descLabel.getStyleClass().add("helper-text");
            descLabel.setWrapText(true);
            descLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

            noResultsContainer.getChildren().addAll(iconLabel, titleLabel, descLabel);
            int insertIndex = parent.getChildren().indexOf(resultCard1);
            if (insertIndex >= 0) {
                parent.getChildren().add(insertIndex, noResultsContainer);
            } else {
                parent.getChildren().add(noResultsContainer);
            }
            AnimationUtils.panelReveal(noResultsContainer);
        }
    }

    private void removeNoResultsMessage() {
        if (noResultsContainer != null) {
            if (noResultsContainer.getParent() instanceof javafx.scene.layout.Pane parent) {
                parent.getChildren().remove(noResultsContainer);
            }
            noResultsContainer = null;
        }
    }

    // ── Button press/release animations ───────────────────────────────────────
    @FXML private void onBackTopPressed(MouseEvent e)         { AnimationUtils.buttonPress(btnBackTop); }
    @FXML private void onBackTopReleased(MouseEvent e)        { AnimationUtils.buttonRelease(btnBackTop); }
    @FXML private void onBackDashboardPressed(MouseEvent e)   { AnimationUtils.buttonPress(btnBackDashboard); }
    @FXML private void onBackDashboardReleased(MouseEvent e)  { AnimationUtils.buttonRelease(btnBackDashboard); }
    @FXML private void onNewDiagnosisPressed(MouseEvent e)    { AnimationUtils.buttonPress(btnNewDiagnosis); }
    @FXML private void onNewDiagnosisReleased(MouseEvent e)   { AnimationUtils.buttonRelease(btnNewDiagnosis); }

    // ── Action handlers ───────────────────────────────────────────────────────
    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        SceneNavigator.switchScene((Node) event.getSource(), "DashboardPage.fxml");
    }

    @FXML
    private void handleNewDiagnosis(ActionEvent event) {
        AnimationUtils.confirmPop(btnNewDiagnosis);
        AppState.getInstance().clearDiagnosis();
        PauseTransition delay = new PauseTransition(Duration.millis(160));
        delay.setOnFinished(e -> SceneNavigator.switchScene(btnNewDiagnosis, "SymptomSelectionPage.fxml"));
        delay.play();
    }
}
