package controller.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Category;
import utils.SessionManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

// pitäis olla ok kaikki
public class CategoryController {
    private static final Gson gson = new Gson();
    private static final String BASE_URL = "http://37.27.9.255:8080"; // Backend URL

    // pitäis olla ok kaikki
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

    public static List<Category> getAllCategories() {
        String response = sendHttpRequest("GET", "/categories/all", "");
        return gson.fromJson(response, new TypeToken<ArrayList<Category>>() {}.getType());
    }

    public static Category getCategoryById(String categoryId) {
        String response = sendHttpRequest("GET", "/categories/one/" + categoryId, "");
        return gson.fromJson(response, Category.class);
    }

    public static Category createCategory(String categoryName, String categoryDescription) {
        String requestBody = '{' +
                "\"categoryName\": \"" + categoryName + "\"," +
                "\"categoryDescription\": \"" + categoryDescription + "\"" +
                '}';
        String response = sendHttpRequest("POST", "/categories/create", requestBody);
        return gson.fromJson(response, Category.class);
    }

    public static void editCategory(String categoryId, String categoryName, String categoryDescription) {
        String requestBody = '{' +
                "\"categoryId\": \"" + categoryId + "\"," +
                "\"categoryName\": \"" + categoryName + "\"," +
                "\"categoryDescription\": \"" + categoryDescription + "\"" +
                '}';
        sendHttpRequest("PUT", "/categories/update/" + categoryId, requestBody);
    }

}
