package model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NotificationTest {
    static Notification notification;

    @BeforeAll
    static void setUp() {
        User user = new User(1, "username", "email", "token");
        notification = new Notification(1, user, "message");
    }

    @Order(1)
    @Test
    void toggleRead() {
        assertFalse(notification.isRead());
        notification.toggleRead();
        assertTrue(notification.isRead());
        notification.toggleRead();
        assertFalse(notification.isRead());
    }

    @Order(2)
    @Test
    void getUser() {
        assertEquals("username", notification.getUser().getUsername());
    }

    @Order(3)
    @Test
    void getMessage() {
        assertEquals("message", notification.getMessage());
    }

    @Order(4)
    @Test
    void setMessage() {
        notification.setMessage("new message");
        assertEquals("new message", notification.getMessage());
    }

    @Order(5)
    @Test
    void isRead() {
        assertFalse(notification.isRead());
    }

    @Order(6)
    @Test
    void setRead() {
        notification.setRead(true);
        assertTrue(notification.isRead());
    }

    @Order(7)
    @Test
    void getId() {
        assertEquals(1, notification.getId());
    }

    @Order(8)
    @Test
    void setId() {
        notification.setId(2);
        assertEquals(2, notification.getId());
    }
}