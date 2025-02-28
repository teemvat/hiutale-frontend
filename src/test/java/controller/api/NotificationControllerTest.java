package controller.api;

import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.SessionManager;

import static org.junit.jupiter.api.Assertions.*;

class NotificationControllerTest {

    @BeforeAll
    static void setUp() {
        // Log in a user to get a valid token for authentication
        UserController.login("testuser@example.com", "password");
        User user = SessionManager.getInstance().getUser();
        assertNotNull(user);
    }

    @Test
    void getUserNotifications() {

    }

    @Test
    void markNotificationAsRead() {
    }

    @Test
    void createNotification() {
    }
}