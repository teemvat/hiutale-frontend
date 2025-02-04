package utils;

public class SessionManager {
    private static SessionManager instance;
    private String username;

    private SessionManager() { }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(String username) {
        this.username = username;
    }

    public void logout() {
        this.username = null;
    }

    public String getUsername() {
        return username;
    }

    public boolean isLoggedIn() {
        return username != null;
    }
}
