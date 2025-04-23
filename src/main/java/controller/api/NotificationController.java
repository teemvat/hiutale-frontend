package controller.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Notification;
import java.util.ArrayList;
import java.util.List;

import static utils.ApiConnector.sendHttpRequest;

/**
 * The NotificationController class provides methods to manage user notifications.
 * It includes functionality to retrieve notifications and mark them as read.
 */
public class NotificationController {
    // Gson instance for JSON serialization and deserialization
    private static final Gson gson = new Gson();

    /**
     * Retrieves the list of notifications for the currently logged-in user.
     *
     * @return A list of Notification objects representing the user's notifications.
     */
    public static List<Notification> getUserNotifications() {
        // Send a GET request to fetch the user's notifications
        String result = sendHttpRequest("GET", "/notifications/me", "");
        // Deserialize the JSON response into a list of Notification objects
        return gson.fromJson(result, new TypeToken<ArrayList<Notification>>() {}.getType());
    }

    /**
     * Marks a specific notification as read.
     *
     * @param notificationId The unique identifier of the notification to mark as read.
     */
    public static void markNotificationAsRead(String notificationId) {
        // Send a PUT request to mark the notification as read
        sendHttpRequest("PUT", "/notifications/" + notificationId + "/read", "");
    }
}