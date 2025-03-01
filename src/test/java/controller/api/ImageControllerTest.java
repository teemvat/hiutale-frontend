package controller.api;

import model.User;
import org.junit.jupiter.api.*;
import utils.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ImageControllerTest {
    static String id;

    @BeforeAll
    static void setUp() {
        // Log in a user to get a valid token for authentication
        UserController.login("testuser@example.com", "password");
        User user = SessionManager.getInstance().getUser();
        assertNotNull(user);
    }

    @Test
    @Order(1)
    void uploadImage() {
        try {
            // Create a temporary file
            File tempFile = File.createTempFile("test", ".jpg");
            tempFile.deleteOnExit();

            // Write some data to the file
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write("Hello, world!".getBytes());
            }

            // Upload the file
            String response = ImageController.uploadImage(1, tempFile);

            JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
            id = jsonResponse.get("id").getAsString();

            // Check that the response is not empty
            assertNotNull(response);
            assertFalse(response.isEmpty());

            assertTrue(response.contains("createdAt"));
            System.out.println("ID: " + id);
            // Check that the response contains the event ID
            assertTrue(response.contains("1"));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    void getImage() {
        try {
            // Download an image
            File imageFile = ImageController.getImage("1");

            // Check that the file is not null
            assertNotNull(imageFile);

            // Check that the file exists
            assertTrue(imageFile.exists());

            // Check that the file is not empty
            assertTrue(imageFile.length() > 0);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    void deleteImage() {
        String response = ImageController.deleteImage(id);
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertTrue(response.contains("200"));
    }
}
