package controller.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.User;
import utils.SessionManager;

import static utils.ApiConnector.sendHttpRequest;

/**
 * The UserController class provides methods to manage user-related operations,
 * such as login, logout, registration, retrieval, and deletion of user accounts.
 */
public class UserController {
    // Gson instance for JSON serialization and deserialization
    private static final Gson gson = new Gson();

    /**
     * Logs in a user with the provided email and password.
     *
     * @param email    The email address of the user.
     * @param password The password of the user.
     * @return The User object if login is successful, or null if authentication fails.
     */
    public static User login(String email, String password) {
        String requestBody = '{'
                + "\"email\": \""
                + email
                + "\","
                + "\"password\": \""
                + password
                + "\""
                + '}';
        String response = sendHttpRequest("POST", "/users/login", requestBody);
        System.out.println("Login response: " + response);

        if (response.contains("Could not authenticate user")) {
            System.out.println("Login failed: " + response);
            SessionManager.getInstance().logout();
            return null;
        }

        try {
            JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
            if (jsonResponse.has("token") && jsonResponse.has("user")) {
                String token = jsonResponse.get("token").getAsString();
                User user = gson.fromJson(jsonResponse.get("user"), User.class);
                user.setToken(token);

                if (!SessionManager.getInstance().isLoggedIn()) {
                    SessionManager.getInstance().login(user);
                    SessionManager.getInstance().setToken(token);
                }
                System.out.println("User logged in: " + user.getUsername());
                return user;
            } else {
                System.out.println("Login failed: " + response);
                SessionManager.getInstance().logout();
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error parsing login response: " + e.getMessage());
            return null;
        }
    }

    /**
     * Logs out the currently logged-in user.
     */
    public static void logout() {
        SessionManager.getInstance().logout();
    }

    /**
     * Registers a new user with the provided username, password, and email.
     *
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @param email    The email address of the new user.
     * @return The newly registered User object.
     */
    public static User register(String username, String password, String email) {
        String requestBody = '{'
                + "\"username\": \""
                + username
                + "\","
                + "\"password\": \""
                + password
                + "\","
                + "\"email\": \""
                + email
                + "\","
                + "\"role\": \"USER\""
                + '}';
        String response = sendHttpRequest("POST", "/users/register", requestBody);
        System.out.println("Registration response: " + response);
        User user = gson.fromJson(response, User.class);
        if (user != null) {
            SessionManager.getInstance().login(user);
        }
        return user;
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id The unique identifier of the user to retrieve.
     * @return The User object representing the requested user.
     */
    public static User getUser(int id) {
        String response = sendHttpRequest("GET", "/users/one/" + id, "");
        return gson.fromJson(response, User.class);
    }

    /**
     * Deletes a user by their unique ID and logs out the current session.
     *
     * @param id The unique identifier of the user to delete.
     */
    public static void deleteUser(int id) {
        sendHttpRequest("DELETE", "/users/" + id, "");
        SessionManager.getInstance().logout();
    }
}