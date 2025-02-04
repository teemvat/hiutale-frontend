package controller;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import utils.SessionManager;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    public void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            // Connect to the backend REST API
            URL url = new URL("http://localhost:8080/api/users/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            // Send request parameters
            String requestBody = "username=" + username + "&password=" + password;
            try (OutputStream os = conn.getOutputStream()) {
                os.write(requestBody.getBytes());
            }

            // Read response
            Scanner scanner = new Scanner(conn.getInputStream());
            String response = scanner.hasNext() ? scanner.next() : "";
            scanner.close();

            // Check the response & store session
            if (response.contains("success")) {
                showAlert("Success", "Login successful!");
                SessionManager.getInstance().login(username);
            } else {
                showAlert("Error", "Invalid credentials.");
            }

        } catch (Exception e) {
            showAlert("Error", "Cannot connect to server.");
        }
    }

    private void logout() {
        SessionManager.getInstance().logout();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
