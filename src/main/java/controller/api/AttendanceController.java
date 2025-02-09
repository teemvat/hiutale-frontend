package controller.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Event;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AttendanceController {
    private static final Gson gson = new Gson();

    public static boolean createAttendance(String eventID, String userID) {
        try {
            URL url = new URL("http://localhost:8080/api/attendance/create"); // Backend URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String requestBody = "eventID=" + URLEncoder.encode(eventID, "UTF-8") +
                    "&userID=" + URLEncoder.encode(userID, "UTF-8");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(requestBody.getBytes());
            }

            Scanner scanner = new Scanner(conn.getInputStream());
            String response = scanner.hasNext() ? scanner.next() : "";
            scanner.close();

            return response.contains("success");
        } catch (Exception e) {
            System.out.println("Cannot connect to server.");
            return false;
        }
    }

    public static boolean deleteAttendance(String eventID, String userID) {
        try {
            URL url = new URL("http://localhost:8080/api/attendance/delete"); // Backend URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String requestBody = "eventID=" + URLEncoder.encode(eventID, "UTF-8") +
                    "&userID=" + URLEncoder.encode(userID, "UTF-8");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(requestBody.getBytes());
            }

            Scanner scanner = new Scanner(conn.getInputStream());
            String response = scanner.hasNext() ? scanner.next() : "";
            scanner.close();

            return response.contains("success");
        } catch (Exception e) {
            System.out.println("Cannot connect to server.");
            return false;
        }
    }

    public static List<Event> getUserAttendances(String userID) {
        return fetchAttendanceData("userID", userID);
    }

    public static List<Event> getEventAttendances(String eventID) {
        return fetchAttendanceData("eventID", eventID);
    }

    private static List<Event> fetchAttendanceData(String key, String value) {
        List<Event> events = new ArrayList<>();

        try {
            URL url = new URL("http://localhost:8080/api/attendance/get?" + key + "=" + URLEncoder.encode(value, "UTF-8"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder jsonResponse = new StringBuilder();
            while (scanner.hasNext()) {
                jsonResponse.append(scanner.next());
            }
            scanner.close();

            events = gson.fromJson(jsonResponse.toString(), new TypeToken<List<Event>>(){}.getType());
        } catch (Exception e) {
            System.out.println("Cannot connect to server.");
        }

        return events;
    }
}
