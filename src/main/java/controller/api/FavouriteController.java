package controller.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Event;
import utils.SessionManager;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FavouriteController {
    private static final Gson gson = new Gson();

    // voi tarvita jotain muuta sisään
    public static boolean createFavourite(String eventID, String userID) {
        try {
            URL url = new URL("http://localhost:8080/api/favourite/create"); // Backend URL
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

    // tähän oikeat tiedot sisään toiminnalissuuden toteuttamiseksi
    public static boolean deleteFavourite(String eventID, String userID) {
        try {
            URL url = new URL("http://localhost:8080/api/favourite/delete"); // Backend URL
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

    public static List<Event> getUserFavourites() {
        String username = SessionManager.getInstance().getUserName();
        return fetchFavouriteData("username", username);
    }

    public static List<Event> getEventAttendances(Event event) {

        return fetchFavouriteData("eventID", event.getEventId());
    }

    // utility funktio
    private static List<Event> fetchFavouriteData(String key, String value) {
        List<Event> events = new ArrayList<>();

        try {
            URL url = new URL("http://localhost:8080/api/favourite/get?" + key + "=" + URLEncoder.encode(value, "UTF-8"));
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
