package controller.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Event;
import java.util.ArrayList;
import java.util.List;

import static utils.ApiConnector.sendHttpRequest;

public class FavouriteController {
    private static final Gson gson = new Gson();

    public static boolean createFavourite(String eventId) {
        String requestBody = '{'
                + "\"id\": \""
                + eventId + "\""
                + '}';
        return sendHttpRequest("POST", "/favourites/create", requestBody).contains("success");
    }

    public static boolean deleteFavourite(String eventId) {
        return sendHttpRequest("DELETE", "/favourites/delete/" + eventId, "").contains("success");
    }

    public static List<Event> getUserFavourites() {
        String response = sendHttpRequest(
                "GET",
                "/favourites/me",
                ""
        );
        return gson.fromJson(response, new TypeToken<ArrayList<Event>>() {}.getType());
    }
}
