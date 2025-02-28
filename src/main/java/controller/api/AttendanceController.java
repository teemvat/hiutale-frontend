package controller.api;

import com.google.gson.Gson;
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

    // pitäis olla ok kaikki paits getUserAttendances
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

    public static void createAttendance(String eventId) {
        String requestBody = '{' +
                "\"id\": \"" + eventId + "\"," +
                '}';
        sendHttpRequest("POST", "/attendance/create", requestBody);
    }

    public static void deleteAttendance(String eventId) {
        sendHttpRequest("DELETE", "/attendance/delete/" + eventId, "");
    }

    // todo: varmista toimivuus, endpoint puuttuu!
    public static List<Event> getUserAttendances() {
        String result = sendHttpRequest("GET", "/attendance/user", "");
        return gson.fromJson(result, new TypeToken<ArrayList<Event>>(){}.getType());
    }
}
