package controller.api;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import model.Event;
import java.util.ArrayList;
import java.util.List;

import static utils.ApiConnector.sendHttpRequest;

/**
 * The AttendanceController class provides methods to manage user attendance for events.
 * It includes functionality to create, delete, and retrieve attendance records.
 */
public class AttendanceController {
    // Gson instance for JSON serialization and deserialization
    private static final Gson gson = new Gson();

    /**
     * Private constructor to prevent instantiation of the AttendanceController class.
     * This class is designed to be used statically.
     */
    private AttendanceController() {
        // Private constructor to prevent instantiation
    }

    /**
     * Creates an attendance record for a specific event.
     *
     * @param eventId The ID of the event for which attendance is being created.
     */
    public static void createAttendance(String eventId) {
        // Construct the request body as a JSON string
        String requestBody = '{'
                + "\"id\": \""
                + eventId + "\""
                + '}';
        // Send a POST request to create the attendance
        sendHttpRequest("POST", "/attendances/create", requestBody);
    }

    /**
     * Deletes an attendance record for a specific event.
     *
     * @param eventId The ID of the event for which attendance is being deleted.
     */
    public static void deleteAttendance(String eventId) {
        // Send a DELETE request to remove the attendance
        sendHttpRequest("DELETE", "/attendances/delete/" + eventId, "");
    }

    /**
     * Retrieves the list of events the user is attending.
     *
     * @return A list of Event objects representing the user's attendances.
     */
    public static List<Event> getUserAttendances() {
        // Send a GET request to retrieve the user's attendances
        String response = sendHttpRequest(
                "GET",
                "/attendances/me",
                "");
        // Deserialize the JSON response into a list of Event objects
        return gson.fromJson(response, new TypeToken<ArrayList<Event>>() {}.getType());
    }
}