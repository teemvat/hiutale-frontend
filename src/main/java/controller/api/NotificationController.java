package controller.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Notification;
import java.util.ArrayList;
import java.util.List;

import static utils.ApiConnector.sendHttpRequest;

public class NotificationController {
    private static final Gson gson = new Gson();

    public static List<Notification> getUserNotifications() {
        String result = sendHttpRequest("GET", "/notifications/me", "");
        return gson.fromJson(result, new TypeToken<ArrayList<Notification>>() {}.getType());
    }

    public static void markNotificationAsRead(String notificationId) {
        sendHttpRequest("PUT", "/notifications/" + notificationId + "/read", "");
    }
}
