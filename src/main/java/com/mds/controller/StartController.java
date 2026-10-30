package com.mds.controller;

import com.mds.AnimationUtils;
import com.mds.SceneNavigator;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controller for the welcome/landing screen (StartPage.fxml).
 */
public class StartController {

    @FXML
    private VBox mainContainer;

    @FXML
    private Button btnLogin;

    @FXML
    public void initialize() {
        AnimationUtils.screenEntrance(mainContainer != null ? mainContainer : btnLogin);
    }

    // ── Button press/release animations ───────────────────────────────────────
    @FXML
    private void onLoginPressed(MouseEvent e) {
        AnimationUtils.buttonPress(btnLogin);
    }

    @FXML
    private void onLoginReleased(MouseEvent e) {
        AnimationUtils.buttonRelease(btnLogin);
    }

    // ── Action handler ────────────────────────────────────────────────────────
    @FXML
    private void handleLogin(ActionEvent event) {
        AnimationUtils.confirmPop(btnLogin);
        PauseTransition delay = new PauseTransition(Duration.millis(160));
        delay.setOnFinished(e -> SceneNavigator.switchScene(btnLogin, "LoginPage.fxml"));
        delay.play();
    }
}
