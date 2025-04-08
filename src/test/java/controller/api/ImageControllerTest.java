package controller.api;

import model.Event;
import model.User;
import org.junit.jupiter.api.*;
import utils.SessionManager;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ImageControllerTest {
    static String id;
    static List<Event> events;

    @BeforeAll
    static void setUp() {
        // Log in a user to get a valid token for authentication
        UserController.login("testuser@example.com", "password");
        User user = SessionManager.getInstance().getUser();
        assertNotNull(user);

        events = EventController.getAllEvents();
    }

    @Test
    @Order(2)
    void getImageUrl() {
        try {
            // Download an image
            String imageFile = ImageController.getImageUrl(events.get(0).getId());

            // Check that the file is not null
            assertNotNull(imageFile);

        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    void getImage(){
        try {
            // Download an image
            File image = ImageController.getImage(events.get(0));

            // Check that the file is not null
            assertNotNull(image);
            assertTrue(image.exists());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    void deleteImage() {
        String response = ImageController.deleteImage(id);
        assertNotNull(response);
        assertFalse(response.isEmpty());
    }
}
