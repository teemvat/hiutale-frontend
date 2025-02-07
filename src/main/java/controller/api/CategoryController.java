package controller.api;

import model.Category;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class CategoryController {

    public static ArrayList<Category> getAllCategories() {
        ArrayList<Category> categories = new ArrayList<>();

        try {
            URL url = new URL("http://localhost:8080/categories"); // Placeholder backend URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNext()) {
                String[] categoryData = scanner.next().split(",");
                categories.add(new Category(Integer.parseInt(categoryData[0]), categoryData[1], categoryData[2]));
            }

        } catch (Exception e) {
            System.out.println("Cannot connect to server.");
        }

        return categories;
    }
}
