package controller.api;

import model.Event;
import org.junit.jupiter.api.*;
import utils.SessionManager;
import model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EventControllerTest {

    @BeforeAll
    static void setUp() {
        // Log in a user to get a valid token for authentication
        User user = UserController.login("testuser@example.com", "password");
        assertNotNull(user);
        SessionManager.getInstance().login(user);
    }

    @Test
    @Order(1)
    void createEvent() {
        boolean success = EventController.createEvent(
                "Testi",
                "Tämä on testi",
                "1",
                "20",
                "Sitsit",
                LocalDate.now(),
                "18:00",
                "23:00",
                20.0
        );
        assertTrue(success);
    }

    @Test
    @Order(2)
    void editEvent() {
        boolean success = EventController.editEvent(
                "Testi",
                "Tämä on muokattu testi",
                "1",
                "200",
                "Bileet",
                LocalDate.now(),
                "17:00",
                "23:59",
                25.0
        );
        assertTrue(success);
    }

    @Test
    @Order(3)
    void getAllEvents() {
        List<Event> events = EventController.getAllEvents();
        assertNotNull(events);
        assertFalse(events.isEmpty());
    }

    @Test
    @Order(4)
    void searchEvents() {
        List<Event> events = EventController.searchEvents(
                "Testi",
                "Sitsit",
                LocalDate.now().toString(),
                "1",
                "15.0",
                "25.0",
                SessionManager.getInstance().getUserName()
        );
        assertNotNull(events);
        assertFalse(events.isEmpty());
    }

    @Test
    @Order(5)
    void deleteEvent() {
        boolean success = EventController.deleteEvent("Testi");
        assertTrue(success);
    }

    @AfterAll
    static void tearDown() {
        // Log out the user after tests
        UserController.logout();
        assertNull(SessionManager.getInstance().getUser());
    }
}