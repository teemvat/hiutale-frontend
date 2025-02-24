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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FavouriteController {
    private static final Gson gson = new Gson();
    private static final String BASE_URL = "37.27.9.255:8080"; // Backend URL

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

    public static boolean createFavourite(Event event) {
        String requestBody = '{' +
                "\"Id\": \"" + event.getEventId() + "\"," +
                '}';
        return sendHttpRequest("POST", "/favourite/delete", requestBody).contains("success");
    }

    public static boolean deleteFavourite(Event event) {
        String requestBody = '{' +
                "\"Id\": \"" + event.getEventId() + "\"," +
                '}';
        return sendHttpRequest("DELETE", "/favourite/delete", requestBody).contains("success");
    }

    public static List<Event> getUserFavourites() {
        String result = sendHttpRequest("GET", "/favourite/all", "");
        return gson.fromJson(result, new TypeToken<ArrayList<Event>>(){}.getType());
    }
}
