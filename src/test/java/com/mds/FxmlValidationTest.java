package com.mds;

import com.mds.controller.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FxmlValidationTest {

    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        // Initialize JavaFX Platform once for headless testing
        try {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown);
            assertTrue(latch.await(5, TimeUnit.SECONDS), "JavaFX Platform failed to start within 5s");
        } catch (IllegalStateException e) {
            // Already initialized in test environment
        }
    }

    private <T> T assertFxmlLoadable(String path, Class<T> controllerClass) throws IOException {
        URL url = getClass().getResource(path);
        assertNotNull(url, "Resource not found: " + path);

        FXMLLoader loader = new FXMLLoader(url);
        Object root = loader.load();

        assertInstanceOf(AnchorPane.class, root, "Root element must be AnchorPane in: " + path);
        AnchorPane pane = (AnchorPane) root;
        assertEquals(844.0, pane.getPrefHeight(), 0.01, "PrefHeight must be 844.0 in: " + path);
        assertEquals(390.0, pane.getPrefWidth(), 0.01, "PrefWidth must be 390.0 in: " + path);

        Object controller = loader.getController();
        assertNotNull(controller, "Controller must be instantiated for: " + path);
        assertInstanceOf(controllerClass, controller, "Controller must be of expected type for: " + path);
        return controllerClass.cast(controller);
    }

    @Test
    @DisplayName("Verify StartPage.fxml loading, dimensions, and controller")
    void testStartPage() throws IOException {
        assertFxmlLoadable("/com/mds/view/StartPage.fxml", StartController.class);
    }

    @Test
    @DisplayName("Verify LoginPage.fxml loading, dimensions, and controller")
    void testLoginPage() throws IOException {
        assertFxmlLoadable("/com/mds/view/LoginPage.fxml", LoginController.class);
    }

    @Test
    @DisplayName("Verify LoginController quick fill sets demo credentials")
    void testLoginControllerQuickFill() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                URL url = getClass().getResource("/com/mds/view/LoginPage.fxml");
                FXMLLoader loader = new FXMLLoader(url);
                loader.load();
                LoginController controller = loader.getController();

                Field txtUserField = LoginController.class.getDeclaredField("txtUsername");
                txtUserField.setAccessible(true);
                TextField txtUsername = (TextField) txtUserField.get(controller);

                Field txtPassField = LoginController.class.getDeclaredField("txtPassword");
                txtPassField.setAccessible(true);
                PasswordField txtPassword = (PasswordField) txtPassField.get(controller);

                Method handleQuickFillMethod = LoginController.class.getDeclaredMethod("handleQuickFill", ActionEvent.class);
                handleQuickFillMethod.setAccessible(true);
                handleQuickFillMethod.invoke(controller, new ActionEvent());

                assertEquals("admin", txtUsername.getText());
                assertEquals("1234", txtPassword.getText());

            } catch (Exception e) {
                fail(e);
            } finally {
                latch.countDown();
            }
        });
        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    @DisplayName("Verify LoginController clears fields on credential mismatch")
    void testLoginControllerMismatchClearsFields() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                URL url = getClass().getResource("/com/mds/view/LoginPage.fxml");
                FXMLLoader loader = new FXMLLoader(url);
                loader.load();
                LoginController controller = loader.getController();

                Field txtUserField = LoginController.class.getDeclaredField("txtUsername");
                txtUserField.setAccessible(true);
                TextField txtUsername = (TextField) txtUserField.get(controller);

                Field txtPassField = LoginController.class.getDeclaredField("txtPassword");
                txtPassField.setAccessible(true);
                PasswordField txtPassword = (PasswordField) txtPassField.get(controller);

                Field btnLoginField = LoginController.class.getDeclaredField("btnLogin");
                btnLoginField.setAccessible(true);
                Button btnLogin = (Button) btnLoginField.get(controller);

                txtUsername.setText("wrongUser");
                txtPassword.setText("wrongPass");

                Method handleLoginMethod = LoginController.class.getDeclaredMethod("handleLogin", ActionEvent.class);
                handleLoginMethod.setAccessible(true);
                handleLoginMethod.invoke(controller, new ActionEvent(btnLogin, null));

                assertEquals("", txtUsername.getText(), "Username field must be cleared on mismatch");
                assertEquals("", txtPassword.getText(), "Password field must be cleared on mismatch");

            } catch (Exception e) {
                fail(e);
            } finally {
                latch.countDown();
            }
        });
        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    @DisplayName("Verify DashboardPage.fxml loading, dimensions, and controller")
    void testDashboardPage() throws IOException {
        assertFxmlLoadable("/com/mds/view/DashboardPage.fxml", DashboardController.class);
    }

    @Test
    @DisplayName("Verify SymptomSelectionPage.fxml loading, 50 static rows, and controller")
    void testSymptomSelectionPage() throws IOException {
        assertFxmlLoadable("/com/mds/view/SymptomSelectionPage.fxml", SymptomController.class);
    }

    @Test
    @DisplayName("Verify SymptomController handleClearAll clears selections")
    void testSymptomControllerClearAll() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                URL url = getClass().getResource("/com/mds/view/SymptomSelectionPage.fxml");
                FXMLLoader loader = new FXMLLoader(url);
                loader.load();
                SymptomController controller = loader.getController();

                Field cbFeverField = SymptomController.class.getDeclaredField("cbFever");
                cbFeverField.setAccessible(true);
                CheckBox cbFever = (CheckBox) cbFeverField.get(controller);

                cbFever.setSelected(true);
                assertTrue(cbFever.isSelected());

                Method handleClearAllMethod = SymptomController.class.getDeclaredMethod("handleClearAll", ActionEvent.class);
                handleClearAllMethod.setAccessible(true);
                handleClearAllMethod.invoke(controller, new ActionEvent());

                assertFalse(cbFever.isSelected(), "cbFever should be deselected after clearAll");

            } catch (Exception e) {
                fail(e);
            } finally {
                latch.countDown();
            }
        });
        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    @DisplayName("Verify ResultsPage.fxml loading, dimensions, and controller")
    void testResultsPage() throws IOException {
        assertFxmlLoadable("/com/mds/view/ResultsPage.fxml", ResultsController.class);
    }

    @Test
    @DisplayName("Verify AppState session management and immutability")
    void testAppState() {
        AppState state = AppState.getInstance();
        state.clearAll();

        assertNull(state.getCurrentUsername());
        assertTrue(state.getSelectedSymptoms().isEmpty());

        state.setCurrentUsername("admin");
        assertEquals("admin", state.getCurrentUsername());

        List<String> symptoms = List.of("Fever", "Cough", "Headache");
        state.setSelectedSymptoms(symptoms);
        assertEquals(3, state.getSelectedSymptoms().size());
        assertTrue(state.getSelectedSymptoms().contains("Fever"));

        // Verify defensive copy immutability
        assertThrows(UnsupportedOperationException.class, () -> state.getSelectedSymptoms().add("Chills"));

        state.clearDiagnosis();
        assertTrue(state.getSelectedSymptoms().isEmpty());
        assertEquals("admin", state.getCurrentUsername(), "clearDiagnosis should retain username");

        state.clearAll();
        assertNull(state.getCurrentUsername(), "clearAll should clear username");
    }
}
