package controller.api;

import app.Main;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Category;
import java.util.ArrayList;
import java.util.List;

import static utils.ApiConnector.sendHttpRequest;

public class CategoryController {
    private static final Gson gson = new Gson();

    public static List<Category> getAllCategories() {
        String response = sendHttpRequest(
                "GET",
                "/categories/"
                + Main.currentLocale.getLanguage()
                + "/all",
                ""
        );
        return gson.fromJson(response, new TypeToken<ArrayList<Category>>() {}.getType());
    }

    public static Category getCategoryById(String categoryId) {
        String response = sendHttpRequest(
                "GET",
                "/categories/one/" + categoryId,
                "");
        return gson.fromJson(response, Category.class);
    }

}
