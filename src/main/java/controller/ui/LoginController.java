package controller.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label emailError;

    @FXML
    private Label passwordError;

    @FXML
    private Button loginButton;

    @FXML
    private Button loginAsGuestButton;

    @FXML
    private Hyperlink signupLink;

    @FXML
    private void handleLoginAction(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

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

        if (!email.isEmpty() && !password.isEmpty()) {
            // Perform login logic here
            System.out.println("Login successful");
            try {
                Parent homePage = FXMLLoader.load(getClass().getResource("/fxml/home.fxml"));
                Scene homeScene = new Scene(homePage);
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(homeScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleLoginAsGuestAction(ActionEvent event) {
        // Perform login logic here
        System.out.println("Login as guest successful");
        try {
            Parent homePage = FXMLLoader.load(getClass().getResource("/fxml/home.fxml"));
            Scene homeScene = new Scene(homePage);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(homeScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSignupAction(ActionEvent event) {
        try {
            Parent signupPage = FXMLLoader.load(getClass().getResource("/fxml/signup.fxml"));
            Scene signupScene = new Scene(signupPage);
            Stage stage = (Stage) signupLink.getScene().getWindow();
            stage.setScene(signupScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        // TODO
    }
}