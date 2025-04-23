package utils;

import model.User;

/**
 * The SessionManager class is a singleton that manages the session state of the application.
 * It handles user login, logout, and provides access to the current user and their token.
 */
public class SessionManager {
    // The single instance of the SessionManager
    private static volatile SessionManager instance;
    // The currently logged-in user
    private User user;
    // The authentication token for the current session
    private String token;

    /**
     * Private constructor to prevent instantiation from outside the class.
     */
    private SessionManager() {}

    /**
     * Retrieves the single instance of the SessionManager.
     * If the instance does not exist, it is created.
     *
     * @return The single instance of the SessionManager.
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Logs in a user by setting the current user and printing a message.
     *
     * @param user The user to log in.
     */
    public void login(User user) {
        this.user = user;
        System.out.println("SessionManager: User set to " + user.getUsername());
    }

    /**
     * Logs out the current user by clearing the user and token.
     */
    public void logout() {
        this.user = null;
        this.token = null;
    }

    /**
     * Retrieves the currently logged-in user.
     *
     * @return The currently logged-in user, or null if no user is logged in.
     */
    public User getUser() {
        return user;
    }

    /**
     * Checks if a user is currently logged in.
     *
     * @return True if a user is logged in, false otherwise.
     */
    public boolean isLoggedIn() {
        return user != null;
    }

    /**
     * Sets the authentication token for the current session.
     *
     * @param token The authentication token to set.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Retrieves the authentication token for the current session.
     *
     * @return The authentication token, or null if no token is set.
     */
    public String getToken() {
        return token;
    }

    /**
     * Retrieves the username of the currently logged-in user.
     * If no user is logged in, returns "Guest".
     *
     * @return The username of the logged-in user, or "Guest" if no user is logged in.
     */
    public String getUserName() {
        return user != null ? user.getUsername() : "Guest";
    }
}