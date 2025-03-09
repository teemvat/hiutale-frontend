package controller.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Notification;
import model.User;
import org.junit.jupiter.api.*;
import utils.SessionManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
class NotificationControllerTest {
    static int notificationId;
    static Notification notification;

    @BeforeAll
    static void setUp() {
        // Log in a user to get a valid token for authentication
        UserController.login("testuser@example.com", "password");
        User user = SessionManager.getInstance().getUser();
        assertNotNull(user);
        // Construct JSON using Gson
        JsonObject userObject = new JsonObject();
        userObject.addProperty("userId", user.getId());

        JsonObject requestBodyJson = new JsonObject();
        requestBodyJson.add("user", userObject);
        requestBodyJson.addProperty("message", "Test");
        requestBodyJson.addProperty("readStatus", false);
        requestBodyJson.addProperty("displayAfter", "2025-03-02T10:00:00Z");

        // Convert to String
        String requestBody = new Gson().toJson(requestBodyJson);

        // create a notification
        try {
            URL url = new URL("http://37.27.9.255:8080/notifications/create");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            if (SessionManager.getInstance().isLoggedIn()) {
                String token = SessionManager.getInstance().getUser().getToken();
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            try (OutputStream os = conn.getOutputStream()) {
                os.write(requestBody.getBytes());
            }

            int responseCode = conn.getResponseCode();
            InputStream is = (responseCode < 400) ? conn.getInputStream() : conn.getErrorStream();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                System.out.println("Create: " + response);
                JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
                notificationId = jsonObject.get("notificationId").getAsInt();
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Test
    @Order(1)
    void getUserNotifications() {
        List<Notification> notifications = NotificationController.getUserNotifications();
        notification = notifications.getFirst();
        assertNotNull(notifications);

    }

    @Test
    @Order(2)
    void markNotificationAsRead() {
        NotificationController.markNotificationAsRead(notification.getId() + "");
        List<Notification> notifications = NotificationController.getUserNotifications();
        notification = notifications.getFirst();
        assertTrue(notification.isRead());
    }

    @AfterAll
    static void tearDown() {

        try {
            URL url = new URL("http://37.27.9.255:8080/notifications/delete/" + notificationId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            if (SessionManager.getInstance().isLoggedIn()) {
                String token = SessionManager.getInstance().getUser().getToken();
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            int responseCode = conn.getResponseCode();
            InputStream is = (responseCode < 400) ? conn.getInputStream() : conn.getErrorStream();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                System.out.println("Delete: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}