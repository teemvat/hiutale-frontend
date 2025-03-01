package controller.api;

import model.Location;
import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.SessionManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocationControllerTest {

    @BeforeAll
    static void setUp() {
        // Log in a user to get a valid token for authentication
        UserController.login("testuser@example.com", "password");
        User user = SessionManager.getInstance().getUser();
        assertNotNull(user);
    }

    @Test
    void getAllLocations() {
        List<Location> locations = LocationController.getAllLocations();
        assertNotNull(locations);
    }

    @Test
    void getLocationById() {
        Location location = LocationController.getLocationById("1");
        assertNotNull(location);
    }

    @Test
    void createLocation() {
        assertDoesNotThrow(() -> LocationController.createLocation("Test Location", "Test Description",  "Test City", "Test Postal Code"));
    }

    @Test
    void deleteLocation() {
        List<Location> locations = LocationController.getAllLocations();
        assertNotNull(locations);
        int lastLocationIndex = locations.size() - 1;
        assertDoesNotThrow(() -> LocationController.deleteLocation(String.valueOf(lastLocationIndex)));
    }
}