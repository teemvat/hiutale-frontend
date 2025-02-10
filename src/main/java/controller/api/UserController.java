package controller.api;
import com.google.gson.Gson;
import model.User;
import utils.SessionManager;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserController {
    private static final Gson gson = new Gson();

    public static User login(String email, String password) {

        try {
            // Connect to the backend REST API
            URL url = new URL("http://37.27.9.255:8080/users/authenticate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // make json
            Map<String, String> json = new HashMap<>();
            json.put("email", email);
            json.put("password", password);
            String jsonInputString = gson.toJson(json);

            // Send request parameters
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInputString.getBytes());
                os.flush();
            }

            // Read response
            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder responseBuilder = new StringBuilder();
            while (scanner.hasNext()) {
                responseBuilder.append(scanner.next());
            }
            scanner.close();
            String response = responseBuilder.toString();

            // Check the response & store session
            // Check for expected data
            User user = gson.fromJson(response, User.class); // Convert JSON to User object
            SessionManager.getInstance().login(user);
            return user;

        } catch (Exception e) {
            System.out.println("Cannot connect to server.");
            return null;
        }
    }

    public static void logout() {
        SessionManager.getInstance().logout();
    }

    public static User register(String email, String password, String username) {

        try {
            URL url = new URL("http://37.27.9.255:8080/users/register");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // make json
            Map<String, String> json = new HashMap<>();
            json.put("email", email);
            json.put("password", password);
            json.put("username", username);
            json.put("role", "user");
            String jsonInputString = gson.toJson(json);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInputString.getBytes());
                os.flush();
            }

            // Read response
            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder responseBuilder = new StringBuilder();
            while (scanner.hasNext()) {
                responseBuilder.append(scanner.next());
            }
            scanner.close();
            String response = responseBuilder.toString();

            // Check the response & store session
            // Check for expected data
            User user = gson.fromJson(response, User.class); // Convert JSON to User object
            SessionManager.getInstance().login(user);
            return user;

        } catch (Exception e) {
            System.out.println("Cannot connect to server.");
            return null;
        }
    }

    public static User edit(String email, String password, String username) {

        try {
            URL url = new URL("http://37.27.9.255:8080/users/edit"); // en tiiä onko olemassa
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String token = SessionManager.getInstance().getUser().getToken();
            conn.setRequestProperty("Authorization", "Bearer " + token);

            // make json
            Map<String, String> json = new HashMap<>();
            json.put("email", email);
            json.put("password", password);
            json.put("username", username);
            String jsonInputString = gson.toJson(json);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInputString.getBytes());
                os.flush();
            }

            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder responseBuilder = new StringBuilder();
            while (scanner.hasNext()) {
                responseBuilder.append(scanner.next());
            }
            scanner.close();
            String response = responseBuilder.toString();

            User user = gson.fromJson(response, User.class);
            SessionManager.getInstance().login(user); // ei varsinaisesti login mut päivittää tiedot tonne

            return user;
        } catch (Exception e) {
            System.out.println("Cannot connect to server.");
            return null;
        }
    }

    public static User getUser(String userId) {
        try {
            URL url = new URL("http://37.27.9.255:8080/users/" + userId); // Hakee userId:llä
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            String token = SessionManager.getInstance().getUser().getToken();
            conn.setRequestProperty("Authorization", "Bearer " + token);

            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder responseBuilder = new StringBuilder();
            while (scanner.hasNext()) {
                responseBuilder.append(scanner.next());
            }
            scanner.close();
            String response = responseBuilder.toString();

            return gson.fromJson(response, User.class);

        } catch (Exception e) {
            System.out.println("Cannot connect to server.");
            return null;
        }
    }
}
