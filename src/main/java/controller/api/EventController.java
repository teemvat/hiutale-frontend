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

    public static boolean createEvent(String eventTitle,
                                      String eventDescription,
                                      String eventLocationId,
                                      String eventCapacity,
                                      String eventCategories,
                                      LocalDate eventDate,
                                      String startTime,
                                      String endTime,
                                      double eventPrice) {
        Event event = new Event(null, eventTitle, eventDescription, eventLocationId, eventCapacity, SessionManager.getInstance().getUserName(), eventCategories, eventDate, startTime, endTime, eventPrice, 0, 0);
        String requestBody = gson.toJson(event);
        String response = sendHttpRequest("POST", "/event/create", requestBody);
        return response.contains("success");
    }

    public static boolean editEvent(String eventId, String eventTitle, String eventDescription, String eventLocationId, String eventCapacity, String eventCategories, LocalDate eventDate, String startTime, String endTime, double eventPrice) {
        Event event = new Event(eventId, eventTitle, eventDescription, eventLocationId, eventCapacity, eventCategories, eventDate, startTime, endTime, eventPrice);
        String requestBody = gson.toJson(event);
        String response = sendHttpRequest("PUT", "/events/update/" + eventId, requestBody);
        return response.contains("success");
    }

    public static List<Event> getAllEvents() {
        String response = sendHttpRequest("GET", "/events/all", "");
        return gson.fromJson(response, new TypeToken<List<Event>>(){}.getType());
    }

    public static List<Event> searchEvents(String query, String category, String date, String location, String minPrice, String maxPrice, String organizerId) {
        List<Event> allEvents = getAllEvents();
        List<Event> searchResults = new ArrayList<>();

        for (Event event : allEvents) {
            if (query != null && !event.getEventTitle().toLowerCase().contains(query.toLowerCase()) || !event.getEventDescription().toLowerCase().contains(query.toLowerCase())) {
                continue;
            }
            if (category != null && !event.getEventCategories().toLowerCase().contains(category.toLowerCase())) {
                continue;
            }
            if (date != null && !event.getEventDate().toString().equals(date)) {
                continue;
            }
            if (location != null && !event.getEventLocationId().toLowerCase().contains(location.toLowerCase())) {
                continue;
            }
            if (minPrice != null && event.getEventPrice() < Double.parseDouble(minPrice)) {
                continue;
            }
            if (maxPrice != null && event.getEventPrice() > Double.parseDouble(maxPrice)) {
                continue;
            }
            if (organizerId != null && !event.getEventOrganizerId().toLowerCase().contains(organizerId.toLowerCase())) {
                continue;
            }
            searchResults.add(event);
        }
        return searchResults;
    }

    public static boolean deleteEvent(String eventId) {
        String response = sendHttpRequest("DELETE", "/events/delete/" + eventId, "");
        return response.contains("success");
    }
}
