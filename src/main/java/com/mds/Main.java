package com.mds;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Entry point for MDS (Medical Diagnosis System).
 * Launches the JavaFX application in a fixed mobile portrait form factor (390x844).
 */
public class Main extends Application {

    public static final double STAGE_WIDTH = 390.0;
    public static final double STAGE_HEIGHT = 844.0;
    public static final String APP_TITLE = "MDS — Medical Diagnosis System";

    @Override
    public void start(Stage primaryStage) {
        try {
            URL startFxml = getClass().getResource("/com/mds/view/StartPage.fxml");
            if (startFxml == null) {
                throw new IllegalStateException("Could not find start view: /com/mds/view/StartPage.fxml");
            }

            FXMLLoader loader = new FXMLLoader(startFxml);
            Parent root = loader.load();

            Scene scene = new Scene(root, STAGE_WIDTH, STAGE_HEIGHT);

            primaryStage.setTitle(APP_TITLE);
            primaryStage.setScene(scene);
            primaryStage.setWidth(STAGE_WIDTH);
            primaryStage.setHeight(STAGE_HEIGHT);
            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("[MDS Fatal Error] Failed to initialize application root window.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
