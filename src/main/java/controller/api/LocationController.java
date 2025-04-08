package controller.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Location;
import java.util.ArrayList;
import java.util.List;

import static utils.ApiConnector.sendHttpRequest;

public class LocationController {
    private static final Gson gson = new Gson();

    public static List<Location> getAllLocations() {
        String result = sendHttpRequest("GET", "/locations/all", "");
        return gson.fromJson(result, new TypeToken<ArrayList<Location>>(){}.getType());
    }

    public static Location getLocationById(String locationId) {
        String result = sendHttpRequest("GET", "/locations/one/" + locationId, "");
        return gson.fromJson(result, Location.class);
    }

}