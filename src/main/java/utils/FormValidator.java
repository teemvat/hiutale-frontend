package utils;

import java.util.ResourceBundle;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Utility class for validating form fields in a JavaFX application.
 * Provides methods to validate text fields and ensure password fields match.
 */
public class FormValidator {
  private final ResourceBundle bundle;

  /**
   * Constructs a FormValidator with the specified resource bundle.
   *
   * @param bundle the resource bundle used for retrieving error messages
   */
  public FormValidator(ResourceBundle bundle) {
    this.bundle = bundle;
  }

  /**
   * Validates a text field to ensure it is not empty.
   *
   * @param field the text field to validate
   * @param errorLabel the label to display the error message
   * @param errorKey the key for the error message in the resource bundle
   * @return true if the field is not empty, false otherwise
   */
  public boolean validateField(TextField field, Label errorLabel, String errorKey) {
    if (field.getText().isEmpty()) {
      errorLabel.setText(bundle.getString(errorKey));
      return false;
    } else {
      errorLabel.setText("");
      return true;
    }
  }

  /**
   * Validates that two password fields have matching values.
   *
   * @param passwordField1 the first password field
   * @param passwordField2 the second password field
   * @param errorLabel the label to display the error message
   * @param errorKey the key for the error message in the resource bundle
   * @return true if the passwords match, false otherwise
   */
  public boolean validatePasswordMatch(TextField passwordField1, TextField passwordField2, Label errorLabel, String errorKey) {
    if (!passwordField1.getText().equals(passwordField2.getText())) {
      errorLabel.setText(bundle.getString(errorKey));
      return false;
    } else {
      errorLabel.setText("");
      return true;
    }
  }
}
