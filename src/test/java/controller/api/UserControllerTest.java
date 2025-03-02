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
    void login() {
        UserController.login("testuser@example.com", "password");
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
    void register() {
        UserController.register("testuser" + rnum, "password", "testuser" + rnum + "@example.com");
        User user = SessionManager.getInstance().getUser();
        assertNotNull(user);
    }

    // todo: fix this test
    // endpoint ei oo auki muille ku adminille
    @Test
    @Order(5)
    void getUser() {
        User user = UserController.getUser(1);
        assertNotNull(user);
        assertEquals("Testing", user.getUsername());
    }

    @Test
    @Order(6)
    void deleteUser() {
        UserController.deleteUser(SessionManager.getInstance().getUser().getId());
        assertNull(SessionManager.getInstance().getUser());
    }
}