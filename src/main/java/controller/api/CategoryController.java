package controller.api;

import model.Category;

import java.io.OutputStream;
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

    public static Category getCategoryById(int categoryId) {
        try {
            URL url = new URL("http://localhost:8080/categories/" + categoryId); // Placeholder backend URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner scanner = new Scanner(conn.getInputStream());
            String[] categoryData = scanner.next().split(",");
            return new Category(Integer.parseInt(categoryData[0]), categoryData[1], categoryData[2]);

        } catch (Exception e) {
            System.out.println("Cannot connect to server.");
            return null;
        }
    }

    public static boolean createCategory(String categoryName, String categoryDescription) {
        try {
            URL url = new URL("http://localhost:8080/categories/create"); // Placeholder backend URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String requestBody = "categoryName=" + categoryName +
                    "&categoryDescription=" + categoryDescription;

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

    public static boolean editCategory(int categoryId, String categoryName, String categoryDescription) {
        try {
            URL url = new URL("http://localhost:8080/categories/edit"); // Placeholder backend URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PATCH");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String requestBody = "categoryId=" + categoryId +
                    "&categoryName=" + categoryName +
                    "&categoryDescription=" + categoryDescription;

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

    public static boolean deleteCategory(int categoryId) {
        try {
            URL url = new URL("http://localhost:8080/categories/delete"); // Placeholder backend URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String requestBody = "categoryId=" + categoryId;

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

    public static ArrayList<Category> getPlaceholderCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "Sitsit", "Istutaan ja ryypätään"));
        categories.add(new Category(2, "Bileet", "Ryypätään baarissa"));
        categories.add(new Category(3, "Rastikierros", "Kävellään ja ryypätään"));
        return categories;
    }
}
