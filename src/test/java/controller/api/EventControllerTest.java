package controller.api;

import model.Category;
import model.Event;
import model.Location;
import org.junit.jupiter.api.*;
import utils.SessionManager;
import model.User;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EventControllerTest {
    static List<Location> locations;
    static List<Category> categories;

    @BeforeAll
    static void setUp() {
        // Log in a user to get a valid token for authentication
        UserController.login("testuser@example.com", "password");
        User user = SessionManager.getInstance().getUser();
        assertNotNull(user);

        locations = LocationController.getAllLocations();
        categories = CategoryController.getAllCategories();
    }

    @Test
    @Order(1)
    void createEvent() {
        //String[] eventCategories = new String[Integer.parseInt(categories.get(0).getId())];
        File file = new File("src/test/resources/test.png");
        Event e = EventController.createEvent(
                "Testi",
                "Tämä on testi",
                locations.get(0).getId(),
                "200",
                new String[]{categories.get(0).getId()},
                "2025-02-26",
                "2025-02-26",
                "17:00",
                "19:00",
                15.0,
                file
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
                "Testi",
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
                locations.get(0).getId(),
                null,
                null,
                null
        );
        assertNotNull(results);
        assertFalse(results.isEmpty());

//        results = EventController.searchEvents(
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                "1"
//        );
//        assertNotNull(results);
//        assertFalse(results.isEmpty());

        results = EventController.searchEvents(
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        assertNotNull(results);
        assertFalse(results.isEmpty());

//        results = EventController.searchEvents(
//                null,
//                null,
//                null,
//                null,
//                null,
//                "10",
//                null
//        );
//        assertNotNull(results);
//        assertFalse(results.isEmpty());
    }

    @Test
    @Order(5)
    void deleteEvent() {
        List<Event> events = EventController.getAllEvents();
        int lastEventIndex = events.size() - 1;
        EventController.deleteEvent(String.valueOf(lastEventIndex));
        assertNotNull(events);
    }

    @Test
    @Order(6)
    void getEvent() {
        List<Event> events = EventController.getAllEvents();
        Event e = EventController.getEvent(events.get(0).getId());
        assertNotNull(e);
    }

    @Test
    @Order(7)
    void getEventsByOrganizer() {
        EventController.getAllEvents();
        List<Event> events = EventController.getEventsByOrganizer(SessionManager.getInstance().getUser().getId() + "");
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