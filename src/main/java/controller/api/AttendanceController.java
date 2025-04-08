package controller.api;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import model.Event;
import java.util.ArrayList;
import java.util.List;

import static utils.ApiConnector.sendHttpRequest;

public class AttendanceController {
    private static final Gson gson = new Gson();

    public static void createAttendance(String eventId) {
        String requestBody = '{'
                + "\"id\": \""
                + eventId + "\""
                + '}';
        sendHttpRequest("POST", "/attendances/create", requestBody);
    }

    public static void deleteAttendance(String eventId) {
        sendHttpRequest("DELETE", "/attendances/delete/" + eventId, "");
    }

    public static List<Event> getUserAttendances() {
        String response = sendHttpRequest(
                "GET",
                "/attendances/me",
                "");
        return gson.fromJson(response, new TypeToken<ArrayList<Event>>() {}.getType());
    }
}
