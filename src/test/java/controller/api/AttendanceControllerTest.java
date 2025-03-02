package controller.api;

import model.Event;
import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import utils.SessionManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
class AttendanceControllerTest {

    @BeforeAll
    static void setUp() {
        // Log in a user to get a valid token for authentication
        UserController.login("testuser@example.com", "password");
        User user = SessionManager.getInstance().getUser();
        assertNotNull(user);
    }

    @Test
    @Order(1)
    void createAttendance() {
        assertDoesNotThrow(() -> AttendanceController.createAttendance("1"));

    }

    @Test
    @Order(2)
    void getUserAttendances() {
        List<Event> attendances = AttendanceController.getUserAttendances();
        assertNotNull(attendances);
        assertFalse(attendances.isEmpty());
    }

    @Test
    @Order(3)
    void deleteAttendance() {
        assertDoesNotThrow(() -> AttendanceController.deleteAttendance("1"));
    }

}