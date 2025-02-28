package controller.api;

import model.Event;
import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.SessionManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AttendanceControllerTest {

    @BeforeAll
    static void setUp() {
        // Log in a user to get a valid token for authentication
        UserController.login("testuser@example.com", "password");
        User user = SessionManager.getInstance().getUser();
        assertNotNull(user);
    }

    @Test
    void createAttendance() {
        assertDoesNotThrow(() -> AttendanceController.createAttendance("1"));

    }

    @Test
    void deleteAttendance() {
        assertDoesNotThrow(() -> AttendanceController.deleteAttendance("1"));
    }

    @Test
    void getUserAttendances() {
        List<Event> attendances = AttendanceController.getUserAttendances();
        assertNotNull(attendances);
        assertFalse(attendances.isEmpty());
    }
}