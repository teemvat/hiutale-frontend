package controller.ui;

import controller.api.UserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class SignupController {

    @FXML private TextField usernameField, emailField;
    @FXML private PasswordField passwordField, confirmPasswordField;
    @FXML private Label usernameError, emailError, passwordError, confirmPasswordError, signupError;
    @FXML private Button signupButton;
    @FXML private Hyperlink loginLink;

    @FXML
    private void handleSignupAction(ActionEvent event) {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty()) {
            usernameError.setText("Username is required");
        } else {
            usernameError.setText("");
        }

        if (email.isEmpty()) {
            emailError.setText("Email is required");
        } else {
            emailError.setText("");
        }

        if (password.isEmpty()) {
            passwordError.setText("Password is required");
        } else {
            passwordError.setText("");
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordError.setText("Passwords do not match");
        } else {
            confirmPasswordError.setText("");
        }

        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && password.equals(confirmPassword)) {
            if (UserController.register(username, password, email) != null) {
                try {
                    Parent homePage = FXMLLoader.load(getClass().getResource("/fxml/home.fxml"));
                    Scene homeScene = new Scene(homePage);
                    Stage stage = (Stage) signupButton.getScene().getWindow();
                    stage.setScene(homeScene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                signupError.setText("Signup failed");
            }
        }
    }

    @FXML
    private void handleLoginAction(ActionEvent event) {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Scene loginScene = new Scene(loginPage);
            Stage stage = (Stage) loginLink.getScene().getWindow();
            stage.setScene(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
