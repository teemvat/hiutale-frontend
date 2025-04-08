package model;

/**
 * The User class represents a user with attributes such as ID, username, email, and token.
 * It provides methods to retrieve and modify attributes.
 */
public class User {
    // The unique identifier for the user
    private int id;
    // The username of the user
    private String username;
    // The email address of the user
    private String email;
    // The authentication token of the user
    private String token;

    /**
     * Constructs a new User object with the specified attributes.
     *
     * @param id       The unique identifier for the user.
     * @param username The username of the user.
     * @param email    The email address of the user.
     * @param token    The authentication token of the user.
     */
    public User(int id, String username, String email, String token) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.token = token;
    }

    /**
     * Retrieves the username of the user.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username The username to set for the user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retrieves the email address of the user.
     *
     * @return The email address of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email The email address to set for the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retrieves the authentication token of the user.
     *
     * @return The authentication token of the user.
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the authentication token of the user.
     *
     * @param token The authentication token to set for the user.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Retrieves the unique identifier of the user.
     *
     * @return The unique identifier of the user.
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves the unique identifier of the user as a string.
     *
     * @return The unique identifier of the user as a string.
     */
    public String getIdString() {
        return id + "";
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param id The unique identifier to set for the user.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns a string representation of the user, which is their username.
     *
     * @return The username of the user.
     */
    @Override
    public String toString() {
        return username;
    }
}