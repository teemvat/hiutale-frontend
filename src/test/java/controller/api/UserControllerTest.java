package controller.api;

import model.User;
import org.junit.jupiter.api.*;
import utils.SessionManager;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {
    private static final Random r = new Random();
    private static final int rnum = r.nextInt(100000);

    @Test
    @Order(1)
    void register() {
        UserController.register("testuser" + rnum + "@example.com", "password", "testuser" + rnum);
        User user = SessionManager.getInstance().getUser();
        assertNotNull(user);
    }

    @Test
    @Order(2)
    void logout() {
        UserController.logout();
        assertNull(SessionManager.getInstance().getUser());
    }

    @Test
    @Order(3)
    void login() {
        UserController.login("testuser" + rnum + "@example.com", "password");
        User user = SessionManager.getInstance().getUser();
        assertNotNull(user);
    }

    @Test
    @Order(4)
    void getUser() {
        User user = UserController.getUser(1);
        assertNotNull(user);
        assertEquals("Testing", user.getUsername());
    }

    @Test
    @Order(5)
    void deleteUser() {
        UserController.deleteUser(SessionManager.getInstance().getUser().getId());
        assertNull(SessionManager.getInstance().getUser());
    }

    @Test
    @Order(6)
    void loginFail() {
        UserController.login("testuser" + rnum + "@example.com", "wrongpassword");
        assertNull(SessionManager.getInstance().getUser());
    }

}