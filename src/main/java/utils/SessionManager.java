package utils;

import model.User;

public class SessionManager {
    private static SessionManager instance;
    private User user;
    private String token;

    private SessionManager() { }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(User user) {
        this.user = user;
    }

    public void logout() {
        this.user = null;
    }

    public User getUser() {
        return user;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public String getToken(){
        return token;
    }

    public String getUserName(){
        return user.getUsername();
    }
}
