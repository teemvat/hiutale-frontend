package model;

public class User {
    private String username;
    private String email;
    private String token;

    public User(String username, String email, String token) {
        this.username = username;
        this.email = email;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return token;
    }

    public void setPassword(String password) {
        this.token = token;
    }
}
