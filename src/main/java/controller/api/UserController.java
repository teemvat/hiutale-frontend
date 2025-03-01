package controller.api;
import com.google.gson.Gson;
import model.User;
import utils.SessionManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
                String token = SessionManager.getInstance().getUser().getToken();
                conn.setRequestProperty("Authorization", "Bearer " + token);
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
        User user = gson.fromJson(response, User.class);
        if (user != null) {
            SessionManager.getInstance().login(user);
        }
        return user;
    }

    public static void logout() {
        SessionManager.getInstance().logout();
    }

    public static User register(String email, String password, String username) {
        String requestBody = '{' +
                "\"email\": \"" + email + "\"," +
                "\"password\": \"" + password + "\"," +
                "\"username\": \"" + username + "\"" +
                '}';
        String response = sendHttpRequest("POST", "/users/register", requestBody);
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
