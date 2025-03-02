package controller.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import model.Event;
import utils.SessionManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AttendanceController {
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

    public static void createAttendance(String eventId) {
        String requestBody = '{' +
                "\"id\": \"" + eventId + "\"," +
                '}';
        sendHttpRequest("POST", "/attendances/create", requestBody);
    }

    public static void deleteAttendance(String eventId) {
        sendHttpRequest("DELETE", "/attendances/delete/" + eventId, "");
    }

    // todo: palauttaa väärän id:n, vaatii muokkausta
    public static List<Event> getUserAttendances() {
        List<Event> events = new ArrayList<>();
        String result = sendHttpRequest("GET", "/attendances/me", "");
        JsonArray jsonArray = JsonParser.parseString(result).getAsJsonArray();
        for (JsonElement element : jsonArray) {
            String id = element.getAsJsonObject().get("eventId").getAsString();
            events.add(EventController.getEvent(id));
        }
        return events;
    }
}
