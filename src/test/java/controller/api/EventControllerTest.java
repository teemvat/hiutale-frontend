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
        UserController.login("testuser@example.com", "password");
        User user = SessionManager.getInstance().getUser();
        assertNotNull(user);
    }

    @Test
    @Order(1)
    void createEvent() {
        Event e = EventController.createEvent(
                "Testi",
                "Tämä on testi",
                "1",
                "200",
                "1",
                null,
                "2025-02-26",
                "2025-02-26",
                15.0
        );
        assertNotNull(e);
    }

    @Test
    @Order(2)
    void editEvent() {
        Event e = EventController.editEvent(
                "4",
                "Testi",
                "Tämä on muokattu testi",
                "1",
                "200",
                "1",
                LocalDate.now().toString(),
                "17:00",
                "23:59",
                25.0
        );
        assertNotNull(e);
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
        EventController.getAllEvents();
        List<Event> results = EventController.searchEvents(
                "Vappu",
                null,
                null,
                null,
                null,
                null,
                null
        );
        assertNotNull(results);
        assertFalse(results.isEmpty());

        results = EventController.searchEvents(
                null,
                null,
                null,
                "1",
                null,
                null,
                null
        );
        assertNotNull(results);
        assertFalse(results.isEmpty());

        results = EventController.searchEvents(
                null,
                "1",
                null,
                null,
                null,
                null,
                null
        );
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test
    @Order(5)
    void deleteEvent() {
        EventController.deleteEvent("Testi");
        List<Event> events = EventController.getAllEvents();
        assertNotNull(events);
        assertFalse(events.isEmpty());
    }

    @Test
    @Order(6)
    void getEvent() {
        Event e = EventController.getEvent("4");
        assertNotNull(e);
    }

    @Test
    @Order(7)
    void getEventsByOrganizer() {
        List<Event> events = EventController.getEventsByOrganizer("19");
        assertNotNull(events);
        assertFalse(events.isEmpty());
    }

    @AfterAll
    static void tearDown() {
        // Log out the user after tests
        UserController.logout();
        assertNull(SessionManager.getInstance().getUser());
    }
}