package controller.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Event;
import utils.SessionManager;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class EventController {
    private static final Gson gson = new Gson();
    private static final String BASE_URL = "http://37.27.9.255:8080"; // Backend URL
    private static final List<Event> events = new ArrayList<>();

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

    public static Event createEvent(String title,
                                      String description,
                                      String locationId,
                                      String capacity,
                                      String categories,
                                      String date,
                                      String start,
                                      String end,
                                      double price) {
        Event event = new Event(null, title, description, locationId, capacity, null, categories, date, start, end, price, 0, 0);
        String requestBody = gson.toJson(event);
        String response = sendHttpRequest("POST", "/event/create", requestBody);
        Event newEvent = gson.fromJson(response, Event.class);
        events.add(newEvent);
        return newEvent;
    }

    public static Event editEvent(String eventId, String eventTitle, String eventDescription, String eventLocationId, String eventCapacity, String eventCategories, String eventDate, String startTime, String endTime, double eventPrice) {
        Event event = new Event(eventId, eventTitle, eventDescription, eventLocationId, eventCapacity, eventCategories, eventDate, startTime, endTime, eventPrice);
        String requestBody = gson.toJson(event);
        String response = sendHttpRequest("PUT", "/events/update/" + eventId, requestBody);
        return gson.fromJson(response, Event.class);
    }

    public static List<Event> getAllEvents() {
        String response = sendHttpRequest("GET", "/events/all", "");
        List<Event> allEvents = gson.fromJson(response, new TypeToken<List<Event>>() {
        }.getType());
        events.addAll(allEvents);
        return allEvents;
    }

    public static List<Event> searchEvents(String query, String category, String date, String location, String minPrice, String maxPrice, String organizerId) {
        List<Event> searchResults = new ArrayList<>();

        for (Event event : events) {
            if (query != null && !event.getTitle().toLowerCase().contains(query.toLowerCase())) {
                continue;
            }
            if (category != null && event.getCategories() != null && !Arrays.asList(event.getCategories()).contains(category.toLowerCase())) {
                continue;
            }
            if (date != null && !event.getDate().equals(date)) {
                continue;
            }
            if (location != null && !event.getLocationId().toLowerCase().contains(location.toLowerCase())) {
                continue;
            }
//            if (minPrice != null && event.getPrice() < Double.parseDouble(minPrice)) {
//                continue;
//            }
//            if (maxPrice != null && event.getPrice() > Double.parseDouble(maxPrice)) {
//                continue;
//            }
            if (organizerId != null && !event.getOrganizerId().toLowerCase().contains(organizerId.toLowerCase())) {
                continue;
            }
            searchResults.add(event);
        }
        return searchResults;
    }

    public static void deleteEvent(String eventId) {
        sendHttpRequest("DELETE", "/events/delete/" + eventId, "");
    }

    public static Event getEvent(String eventId) {
        for (Event event : events) {
            if (event.getId().equals(eventId)) {
                return event;
            }
        }
        return null;
    }

    public static List<Event> getEventsByOrganizer(String organizerId) {
        List<Event> organizerEvents = new ArrayList<>();
        for (Event event : events) {
            if (event.getOrganizerId().equals(organizerId)) {
                organizerEvents.add(event);
            }
        }
        return organizerEvents;
    }
}
