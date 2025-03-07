package model;

public class User {
    private int id;
    private String username;
    private String email;
    private String token;

    public User(int id, String username, String email, String token) {
        this.id = id;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public String getIdString() {
        return id + "";
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return username;
    }
}
