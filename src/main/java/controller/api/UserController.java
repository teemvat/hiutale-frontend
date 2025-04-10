package controller.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.User;
import utils.SessionManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserController {
    private static final Gson gson = new Gson();
    private static final String BASE_URL = "http://37.27.9.255:8080"; // Backend URL

    private static String sendHttpRequest(String method, String endpoint, String requestBody) {
        try {
            URL url = new URL(BASE_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            if (SessionManager.getInstance().isLoggedIn()) {
                String token = SessionManager.getInstance().getToken();
                if (token != null) {
                    conn.setRequestProperty("Authorization", "Bearer " + token);
                }
            }

            if (!requestBody.isEmpty()) {
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(requestBody.getBytes());
                }
            }

            int responseCode = conn.getResponseCode();
            InputStream is = (responseCode < 400) ? conn.getInputStream() : conn.getErrorStream();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return "";
        }
    }

    public static User login(String email, String password) {
        String requestBody = '{' +
                "\"email\": \"" + email + "\"," +
                "\"password\": \"" + password + "\"" +
                '}';
        String response = sendHttpRequest("POST", "/users/login", requestBody);
        System.out.println("Login response: " + response);

        if (response.contains("Could not authenticate user")) {
            System.out.println("Login failed: " + response);
            SessionManager.getInstance().logout();
            return null;
        }

        try {
            JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
            if (jsonResponse.has("token") && jsonResponse.has("user")) {
                String token = jsonResponse.get("token").getAsString();
                User user = gson.fromJson(jsonResponse.get("user"), User.class);
                user.setToken(token);

                if (!SessionManager.getInstance().isLoggedIn()) {
                    SessionManager.getInstance().login(user);
                    SessionManager.getInstance().setToken(token);
                }
                System.out.println("User logged in: " + user.getUsername());
                return user;
            } else {
                System.out.println("Login failed: " + response);
                SessionManager.getInstance().logout();
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error parsing login response: " + e.getMessage());
            return null;
        }
    }

    public static void logout() {
        SessionManager.getInstance().logout();
    }

    public static User register(String username, String password, String email) {
        String requestBody = '{' +
                "\"username\": \"" + username + "\"," +
                "\"password\": \"" + password + "\"," +
                "\"email\": \"" + email + "\"," +
                "\"role\": \"USER\"" +
                '}';
        String response = sendHttpRequest("POST", "/users/register", requestBody);
        System.out.println("Registration response: " + response);
        User user = gson.fromJson(response, User.class);
        if (user != null) {
            SessionManager.getInstance().login(user);
        }
        return user;
    }

    public static User getUser(int id) {
        String response = sendHttpRequest("GET", "/users/one/" + id, "");
        return gson.fromJson(response, User.class);
    }

    public static void deleteUser(int id) {
        sendHttpRequest("DELETE", "/users/" + id, "");
        SessionManager.getInstance().logout();
    }
}