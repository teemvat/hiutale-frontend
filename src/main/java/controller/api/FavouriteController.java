package controller.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Event;
import java.util.ArrayList;
import java.util.List;

import static utils.ApiConnector.sendHttpRequest;

/**
 * The FavouriteController class provides methods to manage user favourites for events.
 * It includes functionality to create, delete, and retrieve favourite events.
 */
public class FavouriteController {
    // Gson instance for JSON serialization and deserialization
    private static final Gson gson = new Gson();

    /**
     * Creates a favourite record for a specific event.
     *
     * @param eventId The ID of the event to mark as favourite.
     * @return True if the favourite was successfully created, false otherwise.
     */
    public static boolean createFavourite(String eventId) {
        // Construct the request body as a JSON string
        String requestBody = '{'
                + "\"id\": \""
                + eventId + "\""
                + '}';
        // Send a POST request to create the favourite and check for success in the response
        return sendHttpRequest("POST", "/favourites/create", requestBody).contains("success");
    }

    /**
     * Deletes a favourite record for a specific event.
     *
     * @param eventId The ID of the event to remove from favourites.
     * @return True if the favourite was successfully deleted, false otherwise.
     */
    public static boolean deleteFavourite(String eventId) {
        // Send a DELETE request to remove the favourite and check for success in the response
        return sendHttpRequest("DELETE", "/favourites/delete/" + eventId, "").contains("success");
    }

    /**
     * Retrieves the list of events marked as favourites by the user.
     *
     * @return A list of Event objects representing the user's favourite events.
     */
    public static List<Event> getUserFavourites() {
        // Send a GET request to retrieve the user's favourite events
        String response = sendHttpRequest(
                "GET",
                "/favourites/me",
                ""
        );
        // Deserialize the JSON response into a list of Event objects
        return gson.fromJson(response, new TypeToken<ArrayList<Event>>() {}.getType());
    }
}