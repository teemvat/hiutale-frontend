package controller.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Location;
import java.util.ArrayList;
import java.util.List;

import static utils.ApiConnector.sendHttpRequest;

/**
 * The LocationController class provides methods to interact with location-related API endpoints.
 * It includes functionality to retrieve all locations and fetch a specific location by its ID.
 */
public class LocationController {
    // Gson instance for JSON serialization and deserialization
    private static final Gson gson = new Gson();

    /**
     * Retrieves all locations available in the system.
     *
     * @return A list of Location objects representing all locations.
     */
    public static List<Location> getAllLocations() {
        // Send a GET request to fetch all locations
        String result = sendHttpRequest("GET", "/locations/all", "");
        // Deserialize the JSON response into a list of Location objects
        return gson.fromJson(result, new TypeToken<ArrayList<Location>>(){}.getType());
    }

    /**
     * Retrieves a specific location by its unique ID.
     *
     * @param locationId The unique identifier of the location to retrieve.
     * @return A Location object representing the requested location.
     */
    public static Location getLocationById(String locationId) {
        // Send a GET request to fetch the location by its ID
        String result = sendHttpRequest("GET", "/locations/one/" + locationId, "");
        // Deserialize the JSON response into a Location object
        return gson.fromJson(result, Location.class);
    }
}