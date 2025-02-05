package controller;

import model.Event;
import model.User;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class EventController {

    public static boolean createEvent(String eventName, String eventDate, String eventLocation, String eventDescription, String eventCapacity, String eventPrice, String eventCategory) {

        try {
            URL url = new URL("http://localhost:8080/api/events/create"); // Placeholder backend URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String requestBody = "eventName=" + eventName + "&eventDate=" + eventDate + "&eventLocation=" + eventLocation + "&eventDescription=" + eventDescription + "&eventCapacity=" + eventCapacity + "&eventPrice=" + eventPrice + "&eventCategory=" + eventCategory;
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

    public static boolean editEvent(String eventName, String eventDate, String eventLocation, String eventDescription, String eventCapacity, String eventPrice, String eventCategory) {

        try {
            URL url = new URL("http://localhost:8080/api/events/edit"); // Placeholder backend URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String requestBody = "eventName=" + eventName + "&eventDate=" + eventDate + "&eventLocation=" + eventLocation + "&eventDescription=" + eventDescription + "&eventCapacity=" + eventCapacity + "&eventPrice=" + eventPrice + "&eventCategory=" + eventCategory;
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

    public static ArrayList<Event> getAllEvents() {
        ArrayList<Event> events = new ArrayList<>();

        try {
            URL url = new URL("http://localhost:8080/api/events"); // Placeholder backend URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNext()) {
                String[] eventDetails = scanner.next().split(",");
                Event event = new Event(eventDetails[0], eventDetails[1], eventDetails[2], eventDetails[3], eventDetails[4], eventDetails[5], eventDetails[6], eventDetails[7], eventDetails[8], Double.parseDouble(eventDetails[9]));
                events.add(event);
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Cannot connect to server.");
        }
        return events;
    }

    public static ArrayList<Event> searchEvents(String keyword,
                                                String category,
                                                String date,
                                                String location,
                                                String startPrice,
                                                String endPrice,
                                                String organizer) {
        ArrayList<Event> events = new ArrayList<>();

        Map<String, String> params = new LinkedHashMap<>();

        if (keyword != null && !keyword.isEmpty()) params.put("keyword", keyword);
        if (category != null && !category.isEmpty()) params.put("category", category);
        if (date != null && !date.isEmpty()) params.put("date", date);
        if (location != null && !location.isEmpty()) params.put("location", location);
        if (startPrice != null && !startPrice.isEmpty()) params.put("startPrice", startPrice);
        if (endPrice != null && !endPrice.isEmpty()) params.put("endPrice", endPrice);
        if (organizer != null && !organizer.isEmpty()) params.put("organizer", organizer);

        String requestBody = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        try {
            URL url = new URL("http://localhost:8080/api/events/search"); // Placeholder backend URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(requestBody.getBytes());
            }

            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNext()) {
                String[] eventDetails = scanner.next().split(",");
                Event event = new Event(eventDetails[0], eventDetails[1], eventDetails[2], eventDetails[3], eventDetails[4], eventDetails[5], eventDetails[6], eventDetails[7], eventDetails[8], Double.parseDouble(eventDetails[9]));
                events.add(event);
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Cannot connect to server.");
        }

        return events;
    }

    public static boolean deleteEvent(String eventName) {
        try {
            URL url = new URL("http://localhost:8080/api/events/delete"); // Placeholder backend URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String requestBody = "eventName=" + eventName;
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


}
