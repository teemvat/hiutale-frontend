package controller.ui;

import app.Main;
import controller.api.UserController;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import utils.FormValidator;
import utils.WindowUtil;

/**
 * Controller for managing the login page and its associated actions.
 *
 * <p>This class handles user interactions on the login page, including logging in,
 * signing up, and selecting a language. It also manages the UI components and
 * their behavior, such as validating input fields and switching scenes.</p>
 *
 * <p>The language selector allows users to choose their preferred language, and
 * the UI updates dynamically to reflect the selected language. The controller
 * also supports right-to-left (RTL) languages.</p>
 */
public class LoginController {

  @FXML private TextField emailField;
  @FXML private PasswordField passwordField;
  @FXML private Label emailError;
  @FXML private Label passwordError;
  @FXML private Button loginButton;
  @FXML private Button loginAsGuestButton;
  @FXML private Hyperlink signupLink;
  @FXML private ComboBox<String> languageSelector;

  private static final Map<String, Locale> LANGUAGE_MAP = new LinkedHashMap<>();

  static {
    LANGUAGE_MAP.put("English", Locale.of("en"));
    LANGUAGE_MAP.put("Suomi", Locale.of("fi", "FI"));
    LANGUAGE_MAP.put("日本語", Locale.of("ja", "JP")); // Japanese
    LANGUAGE_MAP.put("فارسی", Locale.of("fa", "IR")); // Farsi (Persian)
  }

  /**
   * Initializes the login controller by setting up the language selector.
   * Populates the language selector with available languages and sets the default
   * selection based on the system's locale. Also, attaches an event handler to
   * handle language switching when a new language is selected.
   */
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
      loader.setResources(Main.getBundle());
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
      stage.setTitle(Main.getBundle().getString("login.title"));

      LoginController controller = loader.getController();
      controller.setSelectedLanguage(selectedLanguage);
    } catch (IOException e) {
      System.err.println("Error reloading the scene: " + e.getMessage());
      e.printStackTrace(System.err);
    }
  }

  /**
   * Sets the selected language in the language selector ComboBox.
   * Updates the ComboBox value to the specified language and reattaches
   * the event handler for language switching.
   *
   * @param selectedLanguage the language to be set as selected in the ComboBox
   */
  public void setSelectedLanguage(String selectedLanguage) {
    languageSelector.setOnAction(null);
    languageSelector.setValue(selectedLanguage);
    languageSelector.setOnAction(event -> switchLanguage());
  }

  private boolean validateFields() {
    FormValidator validator = new FormValidator(Main.getBundle());
    boolean isValid = true;
    isValid &= validator.validateField(emailField, emailError, "empty.field");
    isValid &= validator.validateField(passwordField, passwordError, "empty.field");
    return isValid;
  }

  @FXML
  private void login() {
    if (!validateFields()) {
      return;
    }

    loginButton.setDisable(true);
    User user = UserController.login(emailField.getText(), passwordField.getText());

    if (user != null) {
      WindowUtil.changeScene("/fxml/home.fxml", "home.title", loginButton, Main.getBundle());
    } else {
      System.err.println("Error logging in");
      emailError.setText(Main.getBundle().getString("login.error"));
      passwordError.setText(Main.getBundle().getString("login.error"));
    }

    loginButton.setDisable(false);
  }

  @FXML
  private void loginAsGuest() {
    System.out.println("Login as guest");
    WindowUtil.changeScene("/fxml/home.fxml", "home.title", loginButton, Main.getBundle());
  }

  @FXML
  private void goToSignup() {
    WindowUtil.changeScene("/fxml/signup.fxml", "signup.title", signupLink, Main.getBundle());
  }
}
