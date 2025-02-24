package controller.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Event;
import model.Notification;
import utils.SessionManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NotificationController {
    private static Gson gson = new Gson();
    private static final String BASE_URL = "37.27.9.255:8080"; // Backend URL

    // tää pitää tutkia kuntoon!
    // todo: testaa postmanilla
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

    // varmista et hakee vain yhden käyttäjän, pitäis saada useri tokenista
    public static List<Notification> getUserNotifications() {
        String result = sendHttpRequest("GET", "/notifications/all", "");
        return gson.fromJson(result, new TypeToken<ArrayList<Notification>>() {
        }.getType());
    }

    public static boolean markNotificationAsRead(Notification notification) {
        String requestBody = "{\"id\":" + notification.getId() + "," + "\"read\":true}";
        return sendHttpRequest("PUT", "/notifications/update", requestBody).isEmpty();
    }

    // todo: tää pitäis varmaan toteuttaa backendissä?
    public static Notification createNotification(int userId, String message) {
        try {
            URL url = new URL("http://localhost:8080/notifications/create");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"user\":" + userId + ",\"message\":\"" + message + "\"}";
            conn.getOutputStream().write(jsonInputString.getBytes());
            conn.getResponseCode();

            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder jsonResponse = new StringBuilder();
            while (scanner.hasNext()) {
                jsonResponse.append(scanner.next());
            }
            scanner.close();

            return gson.fromJson(jsonResponse.toString(), Notification.class);
        } catch (Exception e) {
            System.out.println("Cannot connect to server.");
        }

        return null;
    }
}
