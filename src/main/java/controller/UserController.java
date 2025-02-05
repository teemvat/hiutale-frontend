package controller;
import model.User;
import utils.SessionManager;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


public class UserController {

    public static User login(String username, String password) {

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
                User user = new User(username, password);
                SessionManager.getInstance().login(user);

                return user;
            } else {
                return null;
            }

        } catch (Exception e) {
            System.out.println("Cannot connect to server.");
            return null;
        }
    }

    private static void logout() {
        SessionManager.getInstance().logout();
    }

    public static User register(String username, String password, String email) {

        try {
            URL url = new URL("http://localhost:8080/api/users/register"); // Placeholder backend URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String requestBody = "username=" + username + "&password=" + password + "&email=" + email + "&role=user";
            try (OutputStream os = conn.getOutputStream()) {
                os.write(requestBody.getBytes());
            }

            Scanner scanner = new Scanner(conn.getInputStream());
            String response = scanner.hasNext() ? scanner.next() : "";
            scanner.close();

            if (response.contains("success")) {
                User user = new User(username, password);
                SessionManager.getInstance().login(user);

                return user;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("Cannot connect to server.");
            return null;
        }
    } // tää ehkä jo toimis backissa

    public static User edit(String username, String password, String email) {

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
                User user = new User(username, password);
                SessionManager.getInstance().login(user);

                return user;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("Cannot connect to server.");
            return null;
        }
    }
}
