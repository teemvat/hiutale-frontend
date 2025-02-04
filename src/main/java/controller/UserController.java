package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.util.Scanner;

public class UserController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField emailField;

    public void edit() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();

        try {
            URL url = new URL("http://localhost:8080/api/users/edit"); // Placeholder backend URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String requestBody = "username=" + username + "&password=" + password + "&email=" + email;
            try (OutputStream os = conn.getOutputStream()) {
                os.write(requestBody.getBytes());
            }

            Scanner scanner = new Scanner(conn.getInputStream());
            String response = scanner.hasNext() ? scanner.next() : "";
            scanner.close();

            if (response.contains("success")) {
                showAlert("Success", "Edit successful!");
            } else {
                showAlert("Error", "Edit failed.");
            }
        } catch (Exception e) {
            showAlert("Error", "Cannot connect to server.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
