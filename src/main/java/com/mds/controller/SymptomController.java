package com.mds.controller;

import com.mds.AnimationUtils;
import com.mds.AppState;
import com.mds.SceneNavigator;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the symptom selection screen (SymptomSelectionPage.fxml).
 * Manages 50 statically declared symptom rows in FXML, live search filtering,
 * selection count tracking, and persistent diagnosis state.
 */
public class SymptomController {

    @FXML private VBox mainContainer;
    @FXML private Button btnBackToDashboard;
    @FXML private TextField txtSearch;
    @FXML private Label lblSelectedCount;
    @FXML private Button btnClearAll;
    @FXML private Label lblValidation;
    @FXML private Button btnStartAnalysis;

    // 50 Symptom row containers
    @FXML private HBox rowFever;
    @FXML private HBox rowChills;
    @FXML private HBox rowHeadache;
    @FXML private HBox rowFatigue;
    @FXML private HBox rowWeakness;
    @FXML private HBox rowDizziness;
    @FXML private HBox rowNausea;
    @FXML private HBox rowVomiting;
    @FXML private HBox rowLossOfAppetite;
    @FXML private HBox rowAbdominalPain;
    @FXML private HBox rowDiarrhea;
    @FXML private HBox rowConstipation;
    @FXML private HBox rowCough;
    @FXML private HBox rowDryCough;
    @FXML private HBox rowPhlegm;
    @FXML private HBox rowSoreThroat;
    @FXML private HBox rowRunnyNose;
    @FXML private HBox rowStuffyNose;
    @FXML private HBox rowSneezing;
    @FXML private HBox rowShortnessOfBreath;
    @FXML private HBox rowChestPain;
    @FXML private HBox rowWheezing;
    @FXML private HBox rowMusclePain;
    @FXML private HBox rowJointPain;
    @FXML private HBox rowBackPain;
    @FXML private HBox rowSweating;
    @FXML private HBox rowSkinRash;
    @FXML private HBox rowItching;
    @FXML private HBox rowRedEyes;
    @FXML private HBox rowWateryEyes;
    @FXML private HBox rowLossOfSmell;
    @FXML private HBox rowLossOfTaste;
    @FXML private HBox rowEarPain;
    @FXML private HBox rowFrequentUrination;
    @FXML private HBox rowPainfulUrination;
    @FXML private HBox rowIncreasedThirst;
    @FXML private HBox rowSwelling;
    @FXML private HBox rowPalpitations;
    @FXML private HBox rowBlurredVision;
    @FXML private HBox rowDifficultySleeping;
    @FXML private HBox rowAnxietyRestlessness;
    @FXML private HBox rowYellowingOfSkinEyes;
    @FXML private HBox rowDarkUrine;
    @FXML private HBox rowPaleStools;
    @FXML private HBox rowNeckStiffness;
    @FXML private HBox rowSensitivityToLight;
    @FXML private HBox rowHairLoss;
    @FXML private HBox rowSoreMuscles;
    @FXML private HBox rowHeartburn;
    @FXML private HBox rowBloating;

    // 50 Symptom checkboxes
    @FXML private CheckBox cbFever;
    @FXML private CheckBox cbChills;
    @FXML private CheckBox cbHeadache;
    @FXML private CheckBox cbFatigue;
    @FXML private CheckBox cbWeakness;
    @FXML private CheckBox cbDizziness;
    @FXML private CheckBox cbNausea;
    @FXML private CheckBox cbVomiting;
    @FXML private CheckBox cbLossOfAppetite;
    @FXML private CheckBox cbAbdominalPain;
    @FXML private CheckBox cbDiarrhea;
    @FXML private CheckBox cbConstipation;
    @FXML private CheckBox cbCough;
    @FXML private CheckBox cbDryCough;
    @FXML private CheckBox cbPhlegm;
    @FXML private CheckBox cbSoreThroat;
    @FXML private CheckBox cbRunnyNose;
    @FXML private CheckBox cbStuffyNose;
    @FXML private CheckBox cbSneezing;
    @FXML private CheckBox cbShortnessOfBreath;
    @FXML private CheckBox cbChestPain;
    @FXML private CheckBox cbWheezing;
    @FXML private CheckBox cbMusclePain;
    @FXML private CheckBox cbJointPain;
    @FXML private CheckBox cbBackPain;
    @FXML private CheckBox cbSweating;
    @FXML private CheckBox cbSkinRash;
    @FXML private CheckBox cbItching;
    @FXML private CheckBox cbRedEyes;
    @FXML private CheckBox cbWateryEyes;
    @FXML private CheckBox cbLossOfSmell;
    @FXML private CheckBox cbLossOfTaste;
    @FXML private CheckBox cbEarPain;
    @FXML private CheckBox cbFrequentUrination;
    @FXML private CheckBox cbPainfulUrination;
    @FXML private CheckBox cbIncreasedThirst;
    @FXML private CheckBox cbSwelling;
    @FXML private CheckBox cbPalpitations;
    @FXML private CheckBox cbBlurredVision;
    @FXML private CheckBox cbDifficultySleeping;
    @FXML private CheckBox cbAnxietyRestlessness;
    @FXML private CheckBox cbYellowingOfSkinEyes;
    @FXML private CheckBox cbDarkUrine;
    @FXML private CheckBox cbPaleStools;
    @FXML private CheckBox cbNeckStiffness;
    @FXML private CheckBox cbSensitivityToLight;
    @FXML private CheckBox cbHairLoss;
    @FXML private CheckBox cbSoreMuscles;
    @FXML private CheckBox cbHeartburn;
    @FXML private CheckBox cbBloating;

    /**
     * Internal representation binding the FXML row container, checkbox, and display name.
     */
    private record SymptomBinding(HBox row, CheckBox checkBox, String displayName) {}

    private final List<SymptomBinding> symptomBindings = new ArrayList<>();

    @FXML
    public void initialize() {
        registerSymptomBindings();

        // Register selection listeners — update count + animate card on each tick
        for (SymptomBinding binding : symptomBindings) {
            binding.checkBox().selectedProperty().addListener((obs, oldVal, newVal) -> {
                updateSelectionState();
                // Animate the card row when selected
                if (Boolean.TRUE.equals(newVal)) {
                    AnimationUtils.checkboxSelect(binding.row());
                }
            });
        }

        // Live case-insensitive search filtering
        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> filterSymptoms(newVal));

        // Search field focus expand
        txtSearch.focusedProperty().addListener((obs, o, focused) -> {
            if (focused) AnimationUtils.searchFocus(txtSearch);
            else         AnimationUtils.searchBlur(txtSearch);
        });

        updateSelectionState();

        // Screen entrance animation
        AnimationUtils.screenEntrance(mainContainer != null ? mainContainer : btnBackToDashboard);
    }

    private void registerSymptomBindings() {
        symptomBindings.clear();
        addBinding(rowFever, cbFever, "Fever");
        addBinding(rowChills, cbChills, "Chills");
        addBinding(rowHeadache, cbHeadache, "Headache");
        addBinding(rowFatigue, cbFatigue, "Fatigue");
        addBinding(rowWeakness, cbWeakness, "Weakness");
        addBinding(rowDizziness, cbDizziness, "Dizziness");
        addBinding(rowNausea, cbNausea, "Nausea");
        addBinding(rowVomiting, cbVomiting, "Vomiting");
        addBinding(rowLossOfAppetite, cbLossOfAppetite, "Loss of appetite");
        addBinding(rowAbdominalPain, cbAbdominalPain, "Abdominal pain");
        addBinding(rowDiarrhea, cbDiarrhea, "Diarrhea");
        addBinding(rowConstipation, cbConstipation, "Constipation");
        addBinding(rowCough, cbCough, "Cough");
        addBinding(rowDryCough, cbDryCough, "Dry cough");
        addBinding(rowPhlegm, cbPhlegm, "Phlegm");
        addBinding(rowSoreThroat, cbSoreThroat, "Sore throat");
        addBinding(rowRunnyNose, cbRunnyNose, "Runny nose");
        addBinding(rowStuffyNose, cbStuffyNose, "Stuffy nose");
        addBinding(rowSneezing, cbSneezing, "Sneezing");
        addBinding(rowShortnessOfBreath, cbShortnessOfBreath, "Shortness of breath");
        addBinding(rowChestPain, cbChestPain, "Chest pain");
        addBinding(rowWheezing, cbWheezing, "Wheezing");
        addBinding(rowMusclePain, cbMusclePain, "Muscle pain");
        addBinding(rowJointPain, cbJointPain, "Joint pain");
        addBinding(rowBackPain, cbBackPain, "Back pain");
        addBinding(rowSweating, cbSweating, "Sweating");
        addBinding(rowSkinRash, cbSkinRash, "Skin rash");
        addBinding(rowItching, cbItching, "Itching");
        addBinding(rowRedEyes, cbRedEyes, "Red eyes");
        addBinding(rowWateryEyes, cbWateryEyes, "Watery eyes");
        addBinding(rowLossOfSmell, cbLossOfSmell, "Loss of smell");
        addBinding(rowLossOfTaste, cbLossOfTaste, "Loss of taste");
        addBinding(rowEarPain, cbEarPain, "Ear pain");
        addBinding(rowFrequentUrination, cbFrequentUrination, "Frequent urination");
        addBinding(rowPainfulUrination, cbPainfulUrination, "Painful urination");
        addBinding(rowIncreasedThirst, cbIncreasedThirst, "Increased thirst");
        addBinding(rowSwelling, cbSwelling, "Swelling");
        addBinding(rowPalpitations, cbPalpitations, "Palpitations");
        addBinding(rowBlurredVision, cbBlurredVision, "Blurred vision");
        addBinding(rowDifficultySleeping, cbDifficultySleeping, "Difficulty sleeping");
        addBinding(rowAnxietyRestlessness, cbAnxietyRestlessness, "Anxiety/restlessness");
        addBinding(rowYellowingOfSkinEyes, cbYellowingOfSkinEyes, "Yellowing of skin/eyes");
        addBinding(rowDarkUrine, cbDarkUrine, "Dark urine");
        addBinding(rowPaleStools, cbPaleStools, "Pale stools");
        addBinding(rowNeckStiffness, cbNeckStiffness, "Neck stiffness");
        addBinding(rowSensitivityToLight, cbSensitivityToLight, "Sensitivity to light");
        addBinding(rowHairLoss, cbHairLoss, "Hair loss");
        addBinding(rowSoreMuscles, cbSoreMuscles, "Sore muscles");
        addBinding(rowHeartburn, cbHeartburn, "Heartburn");
        addBinding(rowBloating, cbBloating, "Bloating");
    }

    private void addBinding(HBox row, CheckBox cb, String name) {
        if (row != null && cb != null) {
            symptomBindings.add(new SymptomBinding(row, cb, name));
        }
    }

    private void filterSymptoms(String query) {
        String filter = query == null ? "" : query.trim().toLowerCase();
        for (SymptomBinding binding : symptomBindings) {
            boolean matches = filter.isEmpty() || binding.displayName().toLowerCase().contains(filter);
            binding.row().setVisible(matches);
            binding.row().setManaged(matches);
        }
    }

    private void updateSelectionState() {
        int count = 0;
        for (SymptomBinding binding : symptomBindings) {
            if (binding.checkBox().isSelected()) count++;
        }

        String text = count == 1 ? "1 symptom selected" : count + " symptoms selected";
        lblSelectedCount.setText(text);

        // Pulse the counter pill on every change
        AnimationUtils.countPulse(lblSelectedCount);

        if (count > 0 && lblValidation.isVisible()) {
            lblValidation.setVisible(false);
            lblValidation.setManaged(false);
        }
    }

    // ── Button press/release animations ───────────────────────────────────────
    @FXML private void onBackPressed(MouseEvent e)         { AnimationUtils.buttonPress(btnBackToDashboard); }
    @FXML private void onBackReleased(MouseEvent e)        { AnimationUtils.buttonRelease(btnBackToDashboard); }
    @FXML private void onClearAllPressed(MouseEvent e)     { AnimationUtils.buttonPress(btnClearAll); }
    @FXML private void onClearAllReleased(MouseEvent e)    { AnimationUtils.buttonRelease(btnClearAll); }
    @FXML private void onAnalysisPressed(MouseEvent e)     { AnimationUtils.buttonPress(btnStartAnalysis); }
    @FXML private void onAnalysisReleased(MouseEvent e)    { AnimationUtils.buttonRelease(btnStartAnalysis); }

    // ── Action handlers ───────────────────────────────────────────────────────
    @FXML
    private void handleClearAll(ActionEvent event) {
        AnimationUtils.confirmPop(btnClearAll);
        clearAllSelections();
    }

    private void clearAllSelections() {
        for (SymptomBinding binding : symptomBindings) {
            binding.checkBox().setSelected(false);
        }
        updateSelectionState();
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        SceneNavigator.switchScene((Node) event.getSource(), "DashboardPage.fxml");
    }

    @FXML
    private void handleStartAnalysis(ActionEvent event) {
        List<String> selected = getSelectedSymptomNames();
        if (selected.isEmpty()) {
            // Shake both the validation label and the button to signal error
            lblValidation.setText("Please select at least one symptom.");
            lblValidation.setVisible(true);
            lblValidation.setManaged(true);
            AnimationUtils.labelFadeIn(lblValidation);
            AnimationUtils.shake(btnStartAnalysis);
            return;
        }

        lblValidation.setVisible(false);
        lblValidation.setManaged(false);

        // Confirmation pop before navigating
        AnimationUtils.confirmPop(btnStartAnalysis);
        AppState.getInstance().setSelectedSymptoms(selected);
        clearAllSelections();

        PauseTransition delay = new PauseTransition(Duration.millis(160));
        delay.setOnFinished(e -> SceneNavigator.switchScene(btnStartAnalysis, "ResultsPage.fxml"));
        delay.play();
    }

    private List<String> getSelectedSymptomNames() {
        List<String> list = new ArrayList<>();
        for (SymptomBinding binding : symptomBindings) {
            if (binding.checkBox().isSelected()) list.add(binding.displayName());
        }
        return list;
    }
}
