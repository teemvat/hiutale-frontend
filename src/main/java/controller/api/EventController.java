package controller.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Event;
import utils.SessionManager;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.*;

public class EventController {
    private static final Gson gson = new Gson();
    private static final String BASE_URL = "http://localhost:8080/api/events"; // Backend URL

    private static String sendHttpRequest(String method, String endpoint, String requestBody) {
        try {
            URL url = new URL(BASE_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

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

    public static boolean createEvent(String eventTitle, String eventDescription, String eventLocationId, String eventCapacity, String eventCategories, LocalDate eventDate, String startTime, String endTime, double eventPrice) {
        Event event = new Event(null, eventTitle, eventDescription, eventLocationId, eventCapacity, SessionManager.getInstance().getUserName(), eventCategories, eventDate, startTime, endTime, eventPrice);
        String requestBody = gson.toJson(event);
        String response = sendHttpRequest("POST", "", requestBody);
        return response.contains("success");
    }

    public static boolean editEvent(String eventTitle, String eventDescription, String eventLocationId, String eventCapacity, String eventCategories, LocalDate eventDate, String startTime, String endTime, double eventPrice) {
        Event event = new Event(null, eventTitle, eventDescription, eventLocationId, eventCapacity, SessionManager.getInstance().getUserName(), eventCategories, eventDate, startTime, endTime, eventPrice);
        String requestBody = gson.toJson(event);
        String response = sendHttpRequest("PUT", "", requestBody);
        return response.contains("success");
    }

    public static List<Event> getAllEvents() {
        String response = sendHttpRequest("GET", "", "");
        return gson.fromJson(response, new TypeToken<List<Event>>(){}.getType());
    }

    public static List<Event> searchEvents(String keyword, String category, String date, String location, String startPrice, String endPrice, String organizer) {
        try {
            StringBuilder query = new StringBuilder("?");
            Map<String, String> params = new LinkedHashMap<>();

            if (keyword != null && !keyword.isEmpty()) params.put("keyword", keyword);
            if (category != null && !category.isEmpty()) params.put("category", category);
            if (date != null && !date.isEmpty()) params.put("date", date);
            if (location != null && !location.isEmpty()) params.put("location", location);
            if (startPrice != null && !startPrice.isEmpty()) params.put("startPrice", startPrice);
            if (endPrice != null && !endPrice.isEmpty()) params.put("endPrice", endPrice);
            if (organizer != null && !organizer.isEmpty()) params.put("organizer", organizer);

            for (Map.Entry<String, String> entry : params.entrySet()) {
                query.append(URLEncoder.encode(entry.getKey(), "UTF-8"))
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                        .append("&");
            }

            if (query.length() > 1) {
                query.setLength(query.length() - 1);
            }

            String response = sendHttpRequest("GET", query.toString(), "");

            return gson.fromJson(response, new TypeToken<List<Event>>(){}.getType());
        } catch (UnsupportedEncodingException e) {
            System.out.println("Encoding error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static boolean deleteEvent(String eventName) {
        String response = sendHttpRequest("DELETE", "/delete?eventName=" + eventName, "");
        return response.contains("success");
    }
}
