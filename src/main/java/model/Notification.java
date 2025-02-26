package model;

public class Notification {
    private int id;
    private User user;
    private String message;
    private boolean isRead;

    public Notification(int id, User user, String message) {
        this.id = id;
        this.user = user;
        this.message = message;
        this.isRead = false;
    }

    public void toggleRead() {
        isRead = !isRead;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
