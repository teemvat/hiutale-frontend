package controller.api;

import com.google.gson.Gson;
import model.Location;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class LocationController {
    private static final Gson gson = new Gson();

    public static ArrayList<Location> getAllLocations() {
        ArrayList<Location> locations = new ArrayList<>();

        try {
            URL url = new URL("http://localhost:8080/locations"); // Placeholder backend URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNext()) {
                locations.add(gson.fromJson(scanner.next(), Location.class));
            }

        } catch (Exception e) {
            System.out.println("Cannot connect to server.");
        }

        return locations;
    }

    public static Location getLocationById(String locationId) {
        try {
            URL url = new URL("http://localhost:8080/locations/" + locationId); // Placeholder backend URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner scanner = new Scanner(conn.getInputStream());
            return gson.fromJson(scanner.next(), Location.class);

        } catch (Exception e) {
            System.out.println("Cannot connect to server.");
            return null;
        }
    }

    public static boolean createLocation(String name, String address, String city, String postalCode) {
        try {
            URL url = new URL("http://localhost:8080/locations/create"); // Placeholder backend URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String requestBody = "name=" + name +
                    "&address=" + address +
                    "&city=" + city +
                    "&postalCode=" + postalCode;

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            return conn.getResponseCode() == 200;

        } catch (Exception e) {
            System.out.println("Cannot connect to server.");
            return false;
        }
    }

    public static boolean editLocation(String locationId, String name, String address, String city, String postalCode) {
        try {
            URL url = new URL("http://localhost:8080/locations/" + locationId); // Placeholder backend URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PATCH");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String requestBody = "name=" + name +
                    "&address=" + address +
                    "&city=" + city +
                    "&postalCode=" + postalCode;

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            return conn.getResponseCode() == 200;

        } catch (Exception e) {
            System.out.println("Cannot connect to server.");
            return false;
        }
    }

    public static boolean deleteLocation(String locationId) {
        try {
            URL url = new URL("http://localhost:8080/locations/" + locationId); // Placeholder backend URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            return conn.getResponseCode() == 200;

        } catch (Exception e) {
            System.out.println("Cannot connect to server.");
            return false;
        }
    }
}
