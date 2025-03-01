package controller.ui;

import controller.api.UserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label emailError, passwordError;
    @FXML private Button loginButton, loginAsGuestButton;
    @FXML private Hyperlink signupLink;

    @FXML
    private void handleLoginAction(ActionEvent event) {
        if (validateLogin()) {
            performLogin();
        }
    }

    private boolean validateLogin() {
        boolean isValid = true;
        isValid &= validateField(emailField, emailError, "Email is required");
        isValid &= validateField(passwordField, passwordError, "Password is required");
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
        User user = UserController.login(emailField.getText(), passwordField.getText());
        if (user != null) {
            System.out.println("Login successful");
            switchScene("/fxml/home.fxml", loginButton);
        } else {
            System.out.println("Login failed");
            emailError.setText("Invalid email or password");
            passwordError.setText("Invalid email or password");
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

    private void switchScene(String fxmlPath, Control control) {
        try {
            Parent page = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = new Scene(page);
            Stage stage = (Stage) control.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}