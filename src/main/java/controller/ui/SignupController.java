package controller.ui;

import app.Main;
import controller.api.UserController;
import java.io.IOException;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller class for managing the signup view.
 * Handles user registration and navigation to the login page.
 */
public class SignupController {

  @FXML private TextField usernameField;
  @FXML private TextField emailField;
  @FXML private PasswordField passwordField;
  @FXML private PasswordField confirmPasswordField;
  @FXML private Label usernameError;
  @FXML private Label emailError;
  @FXML private Label passwordError;
  @FXML private Label confirmPasswordError;
  @FXML private Label signupError;
  @FXML private Button signupButton;
  @FXML private Hyperlink loginLink;

  Logger logger = Logger.getLogger(getClass().getName());

  /**
   * Handles the signup action when the signup button is clicked.
   * Validates user input, registers the user, and navigates to the home page if successful.
   */
  @FXML
  private void handleSignupAction() {
    String username = usernameField.getText();
    String email = emailField.getText();
    String password = passwordField.getText();
    String confirmPassword = confirmPasswordField.getText();
    String empty = "empty.field";

    if (username.isEmpty()) {
      usernameError.setText(Main.getBundle().getString(empty));
    } else {
      usernameError.setText("");
    }

    if (email.isEmpty()) {
      emailError.setText(Main.getBundle().getString(empty));
    } else {
      emailError.setText("");
    }

    if (password.isEmpty()) {
      passwordError.setText(Main.getBundle().getString(empty));
    } else {
      passwordError.setText("");
    }

    if (confirmPassword.isEmpty()) {
      confirmPasswordError.setText(Main.getBundle().getString(empty));
    } else if (!password.equals(confirmPassword)) {
      confirmPasswordError.setText(Main.getBundle().getString("password.match.error"));
    } else {
      confirmPasswordError.setText("");
    }

    if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && password.equals(confirmPassword)) {
      if (UserController.register(username, password, email) != null) {
        try {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"), Main.getBundle());
          Parent root = loader.load();
          Scene scene = new Scene(root);
          Stage stage = (Stage) signupButton.getScene().getWindow();
          stage.setTitle(Main.getBundle().getString("home.title"));
          stage.setScene(scene);
        } catch (IOException e) {
          logger.info("Error opening homepage: " + e.getMessage());
        }
      } else {
        signupError.setText(Main.getBundle().getString("signup.error"));
      }
    }
  }

  /**
   * Handles the action to navigate to the login page when the login link is clicked.
   */
  @FXML
  private void handleLoginAction() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"), Main.getBundle());
      Parent root = loader.load();
      Scene scene = new Scene(root);
      Stage stage = (Stage) loginLink.getScene().getWindow();
      stage.setTitle(Main.getBundle().getString("login.title"));
      stage.setScene(scene);
    } catch (IOException e) {
      logger.info("Error with login: " + e.getMessage());
    }
  }
}
