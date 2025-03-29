package controller.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import javafx.scene.image.Image;
import javafx.util.Pair;
import model.Event;
import utils.SessionManager;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;


public class ImageController {
    private static final String BASE_URL = "http://37.27.9.255:8080/files";
    private static List<Pair<String, File>> imageCache = new ArrayList<>();

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
                //System.out.println("User is logged in, token set");
            } else {
                System.out.println("User is not logged in");
            }

            if (!requestBody.isEmpty()) {
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(requestBody.getBytes());
                    System.out.println("Request body is not empty");
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

    public static String uploadImage(long eventId, File file) {
        if (file == null) {
            return "File is null";
        }

        String boundary = "===" + System.currentTimeMillis() + "===";
        String LINE_FEED = "\r\n";
        HttpURLConnection conn = null;

        try {
            // Make a connection to the upload endpoint
            URL url = new URL(BASE_URL + "/upload");
            conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            // Add Authorization header if user is logged in
            if (SessionManager.getInstance().isLoggedIn()) {
                String token = SessionManager.getInstance().getUser().getToken();
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            // Get the output stream to write the data to the server
            OutputStream outputStream = conn.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);

            // Add the eventId field
            writer.append("--" + boundary).append(LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"eventId\"").append(LINE_FEED);
            writer.append("Content-Type: text/plain; charset=UTF-8").append(LINE_FEED);
            writer.append(LINE_FEED).append(String.valueOf(eventId)).append(LINE_FEED);
            writer.flush();

            // Add the file field
            String fieldName = "file";  // The key expected by the backend
            String fileName = file.getName();
            writer.append("--" + boundary).append(LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"")
                    .append(LINE_FEED);
            writer.append("Content-Type: " + HttpURLConnection.guessContentTypeFromName(fileName))
                    .append(LINE_FEED);
            writer.append(LINE_FEED);
            writer.flush();

            // Write the file data
            FileInputStream inputStream = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();

            // Final boundary
            writer.append(LINE_FEED).flush();
            writer.append("--" + boundary + "--").append(LINE_FEED);
            writer.close();

            // Read the response from the server
            int status = conn.getResponseCode();
            InputStream is = (status < HttpURLConnection.HTTP_BAD_REQUEST) ? conn.getInputStream() : conn.getErrorStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            conn.disconnect();

            System.out.println("Response: " + response.toString());

            // Return the response
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                conn.disconnect();
            }
            return "Error: " + e.getMessage();
        }
    }

    public static String getImageURL(String eventId) {
        String response = sendHttpRequest("GET", "/event/" + eventId, "");
        if (response == null || response.isEmpty()) {
            System.out.println("Error: Empty response for eventId " + eventId);
            return "";
        }

        try {
            JsonArray jsonArray = JsonParser.parseString(response).getAsJsonArray();
            if (jsonArray.size() == 0) {
                System.out.println("No image found for eventId " + eventId);
                return "";
            }
            return jsonArray.get(0).getAsJsonObject().get("filename").getAsString();
        } catch (Exception e) {
            System.out.println("Error parsing JSON for eventId " + eventId + ": " + e.getMessage());
            return "";
        }
    }


    public static File getImage(Event event) {
        if (event == null || event.getImage() == null || event.getImage().isEmpty()) {
            System.out.println("Invalid event or image filename");
            return null;
        }

        String fileUrl = "http://37.27.9.255:8080/resources/" + event.getImage();
        String saveAs = "downloaded_" + event.getImage();

        for (Pair<String, File> pair : imageCache) {
            if (pair.getKey().equals(event.getImage())) {
                System.out.println("Image already downloaded: " + pair.getValue().getAbsolutePath());
                return pair.getValue();
            }
        }

        try (InputStream in = new URL(fileUrl).openStream()) {
            Path destination = Paths.get(saveAs);
            Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File downloaded successfully: " + destination.toAbsolutePath());
            File file = destination.toFile();
            imageCache.add(new Pair<>(event.getImage(), file));
            return file;
        } catch (IOException e) {
            System.err.println("Error downloading file: " + e.getMessage());
            return null;
        }
    }


    public static String deleteImage(String imageId) {
        HttpURLConnection conn = null;
        String output = null;
        try {
            URL url = new URL(BASE_URL + "/delete/" + imageId);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            // Add Authorization header if needed
            if (SessionManager.getInstance().isLoggedIn()) {
                String token = SessionManager.getInstance().getUser().getToken();
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            int responseCode = conn.getResponseCode();
            output = conn.getResponseMessage() + " (" + responseCode + ")";
            System.out.println("Response: " + output);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return output;
    }
}
