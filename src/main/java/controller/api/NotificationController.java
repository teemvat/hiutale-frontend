package controller.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Event;
import model.Notification;
import utils.SessionManager;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NotificationController {
    private static Gson gson = new Gson();

    public static ArrayList<Notification> getUserNotifications() {
        ArrayList<Notification> notifications = new ArrayList<>();

        try {
            // todo: URL kuntoon
            URL url = new URL("http://localhost:8080/notifications/user/" + SessionManager.getInstance().getUserName());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder jsonResponse = new StringBuilder();
            while (scanner.hasNext()) {
                jsonResponse.append(scanner.next());
            }
            scanner.close();

            notifications = gson.fromJson(jsonResponse.toString(), new TypeToken<List<Event>>(){}.getType());
        } catch (Exception e) {
            System.out.println("Cannot connect to server.");
        }

        return notifications;
    }

    public static void markNotificationAsRead(Notification notification) {
        try {
            URL url = new URL("http://localhost:8080/notifications/update/" + notification.getId());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            notification.setRead(true);
            String jsonInputString = gson.toJson(notification);
            conn.getOutputStream().write(jsonInputString.getBytes());
            conn.getResponseCode();
        } catch (Exception e) {
            System.out.println("Cannot connect to server.");
        }
    }

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
