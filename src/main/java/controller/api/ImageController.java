package controller.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import model.Event;
import utils.SessionManager;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static utils.ApiConnector.sendHttpRequest;

/**
 * The ImageController class provides methods to handle image-related operations,
 * such as uploading, downloading, retrieving, and deleting images associated with events.
 */
public class ImageController {
    // Base URL for image-related API endpoints
    private static final String BASE_URL = "http://37.27.9.255:8080/files";

    /**
     * Uploads an image file associated with a specific event.
     *
     * @param eventId The ID of the event the image is associated with.
     * @param file    The image file to upload.
     */
    public static void uploadImage(long eventId, File file) {
        if (file == null) {
            return;
        }

        String boundary = "===" + System.currentTimeMillis() + "===";
        String lineFeed = "\r\n";
        HttpURLConnection conn = null;

        try {
            // Make a connection to the upload endpoint
            URL url = URI.create(BASE_URL + "/upload").toURL();
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
            PrintWriter writer = new PrintWriter(
                    new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true);

            // Add the eventId field
            writer.append("--").append(boundary).append(lineFeed);
            writer.append("Content-Disposition: form-data; name=\"eventId\"").append(lineFeed);
            writer.append("Content-Type: text/plain; charset=UTF-8").append(lineFeed);
            writer.append(lineFeed).append(String.valueOf(eventId)).append(lineFeed);
            writer.flush();

            // Add the file field
            String fieldName = "file";  // The key expected by the backend
            String fileName = file.getName();
            writer.append("--").append(boundary).append(lineFeed);
            writer.append("Content-Disposition: form-data; name=\"")
                    .append(fieldName)
                    .append("\"; filename=\"")
                    .append(fileName)
                    .append("\"")
                    .append(lineFeed);
            writer.append("Content-Type: ")
                    .append(HttpURLConnection.guessContentTypeFromName(fileName))
                    .append(lineFeed);
            writer.append(lineFeed);
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
            writer.append(lineFeed).flush();
            writer.append("--").append(boundary).append("--").append(lineFeed);
            writer.close();

            // Read the response from the server
            int status = conn.getResponseCode();
            InputStream is = (status < HttpURLConnection.HTTP_BAD_REQUEST)
                    ? conn.getInputStream()
                    : conn.getErrorStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            conn.disconnect();

            System.out.println("Response: " + response);

        } catch (Exception e) {
            System.err.println("Error during image upload: " + e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * Retrieves the URL of an image associated with a specific event.
     *
     * @param eventId The ID of the event whose image URL is to be retrieved.
     * @return The URL of the image as a String, or an empty string if no image is found.
     */
    public static String getImageUrl(String eventId) {      // Tässä vikaa
        String response = sendHttpRequest("GET", "/files/event/" + eventId, "");
        if (response.isEmpty()) {
            System.out.println("Error: Empty response for eventId " + eventId);
            return "";
        }

        try {
            JsonArray jsonArray = JsonParser.parseString(response).getAsJsonArray();
            if (jsonArray.isEmpty()) {
                System.out.println("No image found for eventId " + eventId);
                return "";
            }
            return jsonArray.get(0).getAsJsonObject().get("filename").getAsString();
        } catch (Exception e) {
            System.out.println("Error parsing JSON for eventId " + eventId + ": " + e.getMessage());
            return "";
        }
    }

    /**
     * Downloads an image associated with a specific event and saves it to a temporary file.
     *
     * @param event The Event object containing the image filename to download.
     * @return The downloaded image file, or null if the download fails.
     */
    public static File getImage(Event event) {
        if (event == null || event.getImage() == null || event.getImage().isEmpty()) {
            System.out.println("Invalid event or image filename");
            return null;
        }

        String fileUrl = "http://37.27.9.255:8080/resources/" + event.getImage();
        String saveAs = System.getProperty("java.io.tmpdir")
                + File.separator
                + "downloaded_"
                + event.getImage();

        try (InputStream in = URI.create(fileUrl).toURL().openStream()) {
            Path destination = Paths.get(saveAs);
            Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File downloaded successfully: " + destination.toAbsolutePath());
            return destination.toFile();
        } catch (IOException e) {
            System.err.println("Error downloading file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Deletes an image by its ID from the server.
     *
     * @param imageId The ID of the image to delete.
     * @return The server's response message indicating the result of the deletion.
     */
    public static String deleteImage(String imageId) {
        HttpURLConnection conn = null;
        String output = null;
        try {
            URL url = URI.create(BASE_URL + "/files//delete/" + imageId).toURL();
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
            System.out.println("Error deleting image: " + e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return output;
    }
}