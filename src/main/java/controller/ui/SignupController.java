package controller.ui;

import app.Main;
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
            usernameError.setText(Main.bundle.getString("empty.field"));
        } else {
            usernameError.setText("");
        }

        if (email.isEmpty()) {
            emailError.setText(Main.bundle.getString("empty.field"));
        } else {
            emailError.setText("");
        }

        if (password.isEmpty()) {
            passwordError.setText(Main.bundle.getString("empty.field"));
        } else {
            passwordError.setText("");
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordError.setText(Main.bundle.getString("empty.field"));
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordError.setText(Main.bundle.getString("password.match.error"));
        } else {
            confirmPasswordError.setText("");
        }

        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && password.equals(confirmPassword)) {
            if (UserController.register(username, password, email) != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"), Main.bundle);
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) signupButton.getScene().getWindow();
                    stage.setTitle(Main.bundle.getString("home.title"));
                    stage.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                signupError.setText(Main.bundle.getString("signup.error"));
            }
        }
    }

    @FXML
    private void handleLoginAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"), Main.bundle);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) loginLink.getScene().getWindow();
            stage.setTitle(Main.bundle.getString("login.title"));
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
