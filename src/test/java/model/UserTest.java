package model;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserTest {
    static User user = new User(1, "username", "email", "token");

    @Order(2)
    @Test
    void getUsername() {
        assertEquals("username", user.getUsername());
    }

    @Order(3)
    @Test
    void setUsername() {
        user.setUsername("newUsername");
        assertEquals("newUsername", user.getUsername());
    }

    @Order(4)
    @Test
    void getEmail() {
        assertEquals("email", user.getEmail());
    }

    @Order(5)
    @Test
    void setEmail() {
        user.setEmail("newEmail");
        assertEquals("newEmail", user.getEmail());
    }

    @Order(6)
    @Test
    void getToken() {
        assertEquals("token", user.getToken());
    }

    @Order(7)
    @Test
    void setToken() {
        user.setToken("newToken");
        assertEquals("newToken", user.getToken());
    }

    @Order(8)
    @Test
    void getId() {
        assertEquals(1, user.getId());
    }

    @Order(9)
    @Test
    void getIdString() {
        assertEquals("1", user.getIdString());
    }

    @Order(10)
    @Test
    void setId() {
        user.setId(2);
        assertEquals(2, user.getId());
    }

    @Order(1)
    @Test
    void testToString() {
        assertEquals("username", user.toString());
    }
}