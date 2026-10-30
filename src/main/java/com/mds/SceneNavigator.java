package com.mds;

import javafx.animation.FadeTransition;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

/**
 * Centralized scene navigation utility for MDS.
 * Handles transitions between screens while enforcing the mobile-proportioned Stage dimensions.
 */
public class SceneNavigator {

    public static final double APP_WIDTH = 390.0;
    public static final double APP_HEIGHT = 844.0;
    private static final String VIEW_PACKAGE_PATH = "/com/mds/view/";

    private SceneNavigator() {
        // Utility class
    }

    /**
     * Overload accepting an ActionEvent or generic Event.
     */
    public static void switchScene(Event event, String fxmlFileName) {
        if (event != null && event.getSource() instanceof Node node) {
            switchScene(node, fxmlFileName);
        } else {
            throw new IllegalArgumentException("Event must have a valid Node source.");
        }
    }

    /**
     * Switches the active scene or root to the requested FXML view.
     *
     * @param sourceNode   Any active node currently displayed on screen (used to find the Stage).
     * @param fxmlFileName The FXML filename (e.g. "LoginPage.fxml" or "view/LoginPage.fxml").
     */
    public static void switchScene(Node sourceNode, String fxmlFileName) {
        if (sourceNode == null) {
            throw new IllegalArgumentException("sourceNode cannot be null when navigating scenes.");
        }
        if (sourceNode.getScene() == null || sourceNode.getScene().getWindow() == null) {
            throw new IllegalStateException("sourceNode is not attached to a visible Scene or Window.");
        }

        Stage stage = (Stage) sourceNode.getScene().getWindow();
        String resolvedPath = resolveFxmlPath(fxmlFileName);

        URL fxmlUrl = SceneNavigator.class.getResource(resolvedPath);
        if (fxmlUrl == null) {
            String errorMsg = "FXML resource not found at classpath: " + resolvedPath;
            System.err.println("[MDS Navigation Error] " + errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        try {
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Scene scene = stage.getScene();
            if (scene == null) {
                scene = new Scene(root, APP_WIDTH, APP_HEIGHT);
                stage.setScene(scene);
                stage.sizeToScene();
            } else {
                scene.setRoot(root);
            }

            stage.setResizable(false);

            // Subtle micro-fade transition for smooth page switching
            FadeTransition fade = new FadeTransition(Duration.millis(120), root);
            fade.setFromValue(0.88);
            fade.setToValue(1.0);
            fade.play();

        } catch (IOException e) {
            System.err.println("[MDS Navigation Error] Failed to load FXML: " + resolvedPath);
            e.printStackTrace();
            throw new RuntimeException("Failed to navigate to: " + resolvedPath, e);
        }
    }

    /**
     * Resolves various FXML filename formats into a canonical classpath resource path.
     */
    private static String resolveFxmlPath(String fileName) {
        if (fileName.startsWith("/com/mds/view/")) {
            return fileName;
        }
        if (fileName.startsWith("/view/")) {
            return "/com/mds" + fileName;
        }
        if (fileName.startsWith("view/")) {
            return "/com/mds/" + fileName;
        }
        String cleanName = fileName.startsWith("/") ? fileName.substring(1) : fileName;
        return VIEW_PACKAGE_PATH + cleanName;
    }
}
