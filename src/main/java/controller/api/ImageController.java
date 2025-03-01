package controller.api;

import utils.SessionManager;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageController {
    private static final String BASE_URL = "http://37.27.9.255:8080/images";

    public static void uploadEventFile(long eventId, File file) {
        if (file == null) {
            return;
        }

        String boundary = "===" + System.currentTimeMillis() + "===";
        String LINE_FEED = "\r\n";
        HttpURLConnection conn = null;

        try {
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

            OutputStream outputStream = conn.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);

            // Add the eventId field
            writer.append("--" + boundary).append(LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"eventId\"").append(LINE_FEED);
            writer.append("Content-Type: text/plain; charset=UTF-8").append(LINE_FEED);
            writer.append(LINE_FEED).append(String.valueOf(eventId)).append(LINE_FEED);
            writer.flush();

            // Add the file part
            String fieldName = "file";
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

            writer.append(LINE_FEED).flush();
            writer.append("--" + boundary + "--").append(LINE_FEED);
            writer.close();

            // Read response
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
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static File getImage(String filename) {
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        File outputFile = null;
        try {
            // Set up the connection to the server
            URL url = new URL(BASE_URL + "/" + filename);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Add Authorization header if needed
            if (SessionManager.getInstance().isLoggedIn()) {
                String token = SessionManager.getInstance().getUser().getToken();
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            int status = conn.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                // Create a file to save the downloaded image
                String tempDir = System.getProperty("java.io.tmpdir");
                outputFile = new File(tempDir, filename);

                // Read the image data and save it to the file
                inputStream = conn.getInputStream();
                try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
                return outputFile; // Return the file if successful
            } else {
                System.out.println("Failed to retrieve image. HTTP Status: " + status);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void deleteImage(String filename) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(BASE_URL + "/" + filename);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            // Add Authorization header if needed
            if (SessionManager.getInstance().isLoggedIn()) {
                String token = SessionManager.getInstance().getUser().getToken();
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            int responseCode = conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
