package controller.api;

import app.Main;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Category;
import java.util.ArrayList;
import java.util.List;

import static utils.ApiConnector.sendHttpRequest;

/**
 * The CategoryController class provides methods to interact with category-related API endpoints.
 * It includes functionality to retrieve all categories and fetch a specific category by its ID.
 */
public class CategoryController {
    // Gson instance for JSON serialization and deserialization
    private static final Gson gson = new Gson();

    /**
     * Retrieves all categories available in the system for the current locale.
     *
     * @return A list of Category objects representing all categories.
     */
    public static List<Category> getAllCategories() {
        // Send a GET request to fetch all categories for the current locale
        String response = sendHttpRequest(
                "GET",
                "/categories/"
                        + Main.currentLocale.getLanguage()
                        + "/all",
                ""
        );
        // Deserialize the JSON response into a list of Category objects
        return gson.fromJson(response, new TypeToken<ArrayList<Category>>() {}.getType());
    }

    /**
     * Retrieves a specific category by its unique ID.
     *
     * @param categoryId The unique identifier of the category to retrieve.
     * @return A Category object representing the requested category.
     */
    public static Category getCategoryById(String categoryId) {
        // Send a GET request to fetch the category by its ID
        String response = sendHttpRequest(
                "GET",
                "/categories/one/" + categoryId,
                ""
        );
        // Deserialize the JSON response into a Category object
        return gson.fromJson(response, Category.class);
    }
}