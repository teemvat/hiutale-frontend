package model;

/**
 * The Notification class represents a notification sent to a user.
 * It includes attributes such as the notification ID, the associated user,
 * the message content, and the read status. It also provides methods to
 * manipulate and retrieve these attributes.
 */
public class Notification {
    // The unique identifier for the notification
    private int id;
    // The user associated with the notification
    private final User user;
    // The message content of the notification
    private String message;
    // The read status of the notification
    private boolean isRead;

    /**
     * Constructs a new Notification object with the specified ID, user, and message.
     * The notification is marked as unread by default.
     *
     * @param id      The unique identifier for the notification.
     * @param user    The user associated with the notification.
     * @param message The message content of the notification.
     */
    public Notification(int id, User user, String message) {
        this.id = id;
        this.user = user;
        this.message = message;
        this.isRead = false;
    }

    /**
     * Toggles the read status of the notification.
     * If the notification is unread, it will be marked as read, and vice versa.
     */
    public void toggleRead() {
        isRead = !isRead;
    }

    /**
     * Retrieves the user associated with the notification.
     *
     * @return The user associated with the notification.
     */
    public User getUser() {
        return user;
    }

    /**
     * Retrieves the message content of the notification.
     *
     * @return The message content of the notification.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Updates the message content of the notification.
     *
     * @param message The new message content to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Checks if the notification has been read.
     *
     * @return True if the notification is read, false otherwise.
     */
    public boolean isRead() {
        return isRead;
    }

    /**
     * Sets the read status of the notification.
     *
     * @param read The new read status to set.
     */
    public void setRead(boolean read) {
        isRead = read;
    }

    /**
     * Retrieves the unique identifier of the notification.
     *
     * @return The unique identifier of the notification.
     */
    public int getId() {
        return id;
    }

    /**
     * Updates the unique identifier of the notification.
     *
     * @param id The new unique identifier to set.
     */
    public void setId(int id) {
        this.id = id;
    }
}