package controller.ui;

import app.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;
import controller.api.UserController;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label emailError, passwordError;
    @FXML private Button loginButton, loginAsGuestButton;
    @FXML private Hyperlink signupLink;
    @FXML private ComboBox<String> languageSelector;

    private static final Map<String, Locale> LANGUAGE_MAP = new LinkedHashMap<>();

    static {
        LANGUAGE_MAP.put("English", new Locale("en"));
        LANGUAGE_MAP.put("Suomi", new Locale("fi", "FI"));
        LANGUAGE_MAP.put("日本語", new Locale("ja", "JP")); // Japanese
        LANGUAGE_MAP.put("فارسی", new Locale("fa", "IR")); // Farsi (Persian)
    }

    @FXML
    public void initialize() {
        // Populate the ComboBox with language names
        languageSelector.getItems().addAll(LANGUAGE_MAP.keySet());

        // Set the default selection based on system locale
        Locale defaultLocale = Locale.getDefault();
        languageSelector.setValue(LANGUAGE_MAP.entrySet().stream()
                .filter(entry -> entry.getValue().getLanguage().equals(defaultLocale.getLanguage()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("English"));

        languageSelector.setOnAction(event -> switchLanguage());
    }

    private void switchLanguage() {
        languageSelector.setOnAction(null);

        String selectedLanguage = languageSelector.getValue();
        Locale newLocale = LANGUAGE_MAP.get(selectedLanguage);

        if (newLocale != null) {
            Main.setLocale(newLocale);
            reloadScene(selectedLanguage);
        }
    }

    private void reloadScene(String selectedLanguage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            loader.setResources(Main.bundle);
            Parent root = loader.load();

            // Detect RTL language
            if (selectedLanguage.equals("فارسی")) {
                root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            } else {
                root.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            }

            Scene scene = new Scene(root);
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(Main.bundle.getString("login.title"));

            LoginController controller = loader.getController();
            controller.setSelectedLanguage(selectedLanguage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSelectedLanguage(String selectedLanguage) {
        languageSelector.setOnAction(null);
        languageSelector.setValue(selectedLanguage);
        languageSelector.setOnAction(event -> switchLanguage());
    }

    @FXML
    private void handleLoginAction(ActionEvent event) {
        if (validateLogin()) {
            performLogin();
        }
    }

    @FXML
    private void handleLoginAsGuestAction(ActionEvent event) {
        System.out.println("Login as guest");
        switchScene("/fxml/home.fxml", loginButton);
    }

    @FXML
    private void handleSignupAction(ActionEvent event) {
        switchScene("/fxml/signup.fxml", signupLink);
    }

    private boolean validateLogin() {
        boolean isValid = true;
        isValid &= validateField(emailField, emailError, Main.bundle.getString("login.email.error"));
        isValid &= validateField(passwordField, passwordError, Main.bundle.getString("login.password.error"));
        return isValid;
    }

    private boolean validateField(TextField field, Label errorLabel, String errorMessage) {
        if (field.getText().isEmpty()) {
            errorLabel.setText(errorMessage);
            return false;
        }
        errorLabel.setText("");
        return true;
    }

    private void performLogin() {
        loginButton.setDisable(true);

        User user = UserController.login(emailField.getText(), passwordField.getText());

        if (user != null) {
            System.out.println("Login successful");
            switchScene("/fxml/home.fxml", loginButton);
        } else {
            System.out.println("Login failed");
            emailError.setText(Main.bundle.getString("login.invalid"));
            passwordError.setText(Main.bundle.getString("login.invalid"));
        }
        loginButton.setDisable(false);
    }

    private void switchScene(String fxmlPath, Control control) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath), Main.bundle);
            Parent page = loader.load();
            Scene scene = new Scene(page);
            Stage stage = (Stage) control.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}