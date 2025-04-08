package utils;

import java.io.*; // Checkstyle doesn't like this, but IDE wants this
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ApiConnector {
    private static final String BASE_URL = "http://37.27.9.255:8080"; // Backend URL

    public static String sendHttpRequest(String method, String endpoint, String requestBody) {
        try {
            HttpURLConnection conn = getHttpUrlConnection(method, endpoint);

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

    private static HttpURLConnection getHttpUrlConnection(
            String method,
            String endpoint
    ) throws IOException {
        URL url = URI.create(BASE_URL + endpoint).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        if (SessionManager.getInstance().isLoggedIn()) {
            String token = SessionManager.getInstance().getUser().getToken();
            conn.setRequestProperty("Authorization", "Bearer " + token);
        }
        return conn;
    }
}
