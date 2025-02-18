package controller.api;

import model.User;
import org.junit.jupiter.api.*;
import utils.SessionManager;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {
    private static final Random r = new Random();
    private static final int rnum = r.nextInt(10000);

    @Test
    @Order(1)
    void login() {
        User user = UserController.login("pertti@email.com", "asd123");
        assertNotNull(user);
        assertEquals("pertti@email.com", user.getEmail());
    }

    @Test
    @Order(2)
    void logout() {
        UserController.logout();
        assertNull(SessionManager.getInstance().getUser());
    }

    @Test
    @Order(3)
    void register() {
        User user = UserController.register("testuser" + rnum + "@example.com", "password", "testuser" + rnum);
        assertNotNull(user);
        assertEquals("testuser" + rnum + "@example.com", user.getEmail());
    }

    @Test
    @Order(4)
    void edit() {
        User user = UserController.edit("testuser" + rnum + "@example.com", "password", "newUsername" + rnum);
        assertNotNull(user);
        assertEquals("newUsername", user.getUsername());
    }

    @Test
    @Order(5)
    void getUser() {
        User user = UserController.getUser("pertti");
        assertNotNull(user);
        assertEquals("pertti", user.getUsername());
    }

    @Test
    @Order(6)
    void deleteUser() {
        boolean success = UserController.deleteUser("testuser" + rnum);
        assertTrue(success);
    }
}