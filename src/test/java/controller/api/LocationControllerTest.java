package controller.api;

import model.Location;
import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.SessionManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocationControllerTest {
    static List<Location> locations;

    @BeforeAll
    static void setUp() {
        // Log in a user to get a valid token for authentication
        UserController.login("testuser@example.com", "password");
        User user = SessionManager.getInstance().getUser();
        assertNotNull(user);
    }

    @Test
    void getAllLocations() {
        locations = LocationController.getAllLocations();
        assertNotNull(locations);
    }

    @Test
    void getLocationById() {
        Location location = LocationController.getLocationById(locations.get(0).getId());
        assertNotNull(location);
    }

}