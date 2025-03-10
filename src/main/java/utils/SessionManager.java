package utils;

import model.User;

public class SessionManager {
    private static volatile SessionManager instance;
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
        System.out.println("SessionManager: User set to " + user.getUsername());
    }

    public void logout() {
        this.user = null;
        this.token = null;
    }

    public User getUser() {
        return user;
    }

    public boolean isLoggedIn() {
        boolean loggedIn =  user != null;
        //System.out.println("SessionManager: isLoggedIn = " + loggedIn);
        return loggedIn;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken(){
        return token;
    }

    public String getUserName(){
        return user != null ? user.getUsername() : "Guest";
    }
}
