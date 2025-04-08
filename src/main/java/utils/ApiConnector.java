package utils;

import java.io.*; // Checkstyle doesn't like this, but IDE wants this
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

/**
 * The ApiConnector class provides utility methods to send HTTP requests to a backend server.
 * It supports sending requests with various HTTP methods, endpoints, and request bodies.
 */
public class ApiConnector {
    // The base URL of the backend server
    private static final String BASE_URL = "http://37.27.9.255:8080"; // Backend URL

    /**
     * Sends an HTTP request to the specified endpoint with the given method and request body.
     *
     * @param method      The HTTP method to use (e.g., "GET", "POST").
     * @param endpoint    The endpoint to send the request to (relative to the base URL).
     * @param requestBody The request body to include in the request (if applicable).
     * @return The response from the server as a string.
     */
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

    /**
     * Creates and configures an HttpURLConnection object for the specified method and endpoint.
     *
     * @param method   The HTTP method to use (e.g., "GET", "POST").
     * @param endpoint The endpoint to connect to (relative to the base URL).
     * @return A configured HttpURLConnection object.
     * @throws IOException If an I/O error occurs while creating the connection.
     */
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