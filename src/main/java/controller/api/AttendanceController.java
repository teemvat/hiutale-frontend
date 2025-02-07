package controller.api;

import model.Event;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class AttendanceController {

    public static boolean createAttendance(String eventID, String userID) {
        try {
            URL url = new URL("http://localhost:8080/api/attendance/create"); // Placeholder backend URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String requestBody = "eventID=" + eventID + "&userID=" + userID;
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

    public static boolean deleteAttendance(String eventID, String userID) {
        try {
            URL url = new URL("http://localhost:8080/api/attendance/delete"); // Placeholder backend URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String requestBody = "eventID=" + eventID + "&userID=" + userID;
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

    public static ArrayList<Event> getUserAttendances(String userID) {
        ArrayList<Event> events = new ArrayList<>();

        try {
            URL url = new URL("http://localhost:8080/api/attendance/get?userID=" + userID); // Placeholder backend URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNext()) {
                String[] eventDetails = scanner.next().split(",");
                events.add(new Event(eventDetails[0], eventDetails[1], eventDetails[2], eventDetails[3], eventDetails[4], eventDetails[5], eventDetails[6], eventDetails[7], eventDetails[8], Double.parseDouble(eventDetails[9])));
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Cannot connect to server.");
        }

        return events;
    }

    public static ArrayList<Event> getEventAttendances(String eventID) {
        ArrayList<Event> events = new ArrayList<>();

        try {
            URL url = new URL("http://localhost:8080/api/attendance/get?eventID=" + eventID); // Placeholder backend URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNext()) {
                String[] eventDetails = scanner.next().split(",");
                events.add(new Event(eventDetails[0], eventDetails[1], eventDetails[2], eventDetails[3], eventDetails[4], eventDetails[5], eventDetails[6], eventDetails[7], eventDetails[8], Double.parseDouble(eventDetails[9])));
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Cannot connect to server.");
        }

        return events;
    }

    // vois olla viel joku edit attendance jos haluaa vaihtaa useria lipulla
}
