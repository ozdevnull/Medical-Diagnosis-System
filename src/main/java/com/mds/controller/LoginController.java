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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controller for the user sign-in screen (LoginPage.fxml).
 * Uses placeholder credentials for frontend demonstration.
 * <p>
 * TODO: Replace hardcoded credentials with a secure authentication service,
 * password hashing (e.g. Argon2/BCrypt), and enterprise identity provider.
 */
public class LoginController {

    // Frontend demonstration credentials (preserved for demo and quick-fill fallback)
    private static final String DEMO_USERNAME = "admin";
    private static final String DEMO_PASSWORD = "1234";

    private final com.mds.dao.UserDAO userDAO = new com.mds.dao.UserDAO();

    @FXML private Button btnBack;
    @FXML private VBox formCard;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnLogin;
    @FXML private Button btnQuickFill;
    @FXML private Label lblError;

    @FXML
    public void initialize() {
        // Screen entrance
        AnimationUtils.screenEntrance(formCard != null ? formCard : txtUsername);

        // Clear error whenever user modifies inputs
        txtUsername.textProperty().addListener((obs, oldVal, newVal) -> clearError());
        txtPassword.textProperty().addListener((obs, oldVal, newVal) -> clearError());

        // Focus grow animations on input fields
        txtUsername.focusedProperty().addListener((obs, o, focused) -> {
            if (focused) AnimationUtils.searchFocus(txtUsername);
            else         AnimationUtils.searchBlur(txtUsername);
        });
        txtPassword.focusedProperty().addListener((obs, o, focused) -> {
            if (focused) AnimationUtils.searchFocus(txtPassword);
            else         AnimationUtils.searchBlur(txtPassword);
        });
    }

    // ── Button press/release animations ───────────────────────────────────────
    @FXML private void onBackPressed(MouseEvent e)       { AnimationUtils.buttonPress(btnBack); }
    @FXML private void onBackReleased(MouseEvent e)      { AnimationUtils.buttonRelease(btnBack); }
    @FXML private void onLoginPressed(MouseEvent e)      { AnimationUtils.buttonPress(btnLogin); }
    @FXML private void onLoginReleased(MouseEvent e)     { AnimationUtils.buttonRelease(btnLogin); }
    @FXML private void onQuickFillPressed(MouseEvent e)  { AnimationUtils.buttonPress(btnQuickFill); }
    @FXML private void onQuickFillReleased(MouseEvent e) { AnimationUtils.buttonRelease(btnQuickFill); }

    // ── Action handlers ───────────────────────────────────────────────────────
    @FXML
    private void handleBack(ActionEvent event) {
        SceneNavigator.switchScene((Node) event.getSource(), "StartPage.fxml");
    }

    @FXML
    private void handleQuickFill(ActionEvent event) {
        txtUsername.setText(DEMO_USERNAME);
        txtPassword.setText(DEMO_PASSWORD);
        clearError();
        AnimationUtils.confirmPop(btnQuickFill);
        txtPassword.requestFocus();
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = txtUsername.getText() != null ? txtUsername.getText().trim() : "";
        String password = txtPassword.getText() != null ? txtPassword.getText() : "";

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password.");
            AnimationUtils.shake(username.isEmpty() ? txtUsername : txtPassword);
            return;
        }

        if (userDAO.authenticate(username, password)) {
            clearError();
            AppState.getInstance().setCurrentUsername(username);

            // Confirmation pop, then navigate
            AnimationUtils.confirmPop(btnLogin);
            PauseTransition delay = new PauseTransition(Duration.millis(180));
            delay.setOnFinished(e -> {
                Node sourceNode = btnLogin;
                SceneNavigator.switchScene(sourceNode, "DashboardPage.fxml");
            });
            delay.play();
        } else {
            txtUsername.clear();
            txtPassword.clear();
            showError("Invalid username or password.");
            AnimationUtils.shake(btnLogin);
            txtUsername.requestFocus();
        }
    }

    private void showError(String message) {
        lblError.setText(message);
        lblError.setVisible(true);
        lblError.setManaged(true);
        AnimationUtils.labelFadeIn(lblError);
    }

    private void clearError() {
        if (lblError.isVisible()) {
            lblError.setText("");
            lblError.setVisible(false);
            lblError.setManaged(false);
        }
    }
}
