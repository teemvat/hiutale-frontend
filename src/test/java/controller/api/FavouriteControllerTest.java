package controller.api;

import model.Event;
import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.SessionManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FavouriteControllerTest {

    @BeforeAll
    static void setUp() {
        // Log in a user to get a valid token for authentication
        UserController.login("testuser@example.com", "password");
        User user = SessionManager.getInstance().getUser();
        assertNotNull(user);
    }

    @Test
    void createFavourite() {
        assertDoesNotThrow(() -> FavouriteController.createFavourite("1"));
    }

    @Test
    void deleteFavourite() {
        assertDoesNotThrow(() -> FavouriteController.deleteFavourite("1"));
    }

    @Test
    void getUserFavourites() {
        List<Event> favourites = FavouriteController.getUserFavourites();
        assertNotNull(favourites);
        assertFalse(favourites.isEmpty());
    }
}