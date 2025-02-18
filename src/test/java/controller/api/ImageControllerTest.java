package controller.api;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;



// todo: testiclassi ei toimi, vaihda samanlaiseks ku muihin
class ImageControllerTest {

    // WireMock server running on port 8080 (matches BASE_URL in ImageController)
    private static WireMockServer wireMockServer;

    @BeforeAll
    static void setupServer() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8081));
        wireMockServer.start();
    }

    @AfterAll
    static void stopServer() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @BeforeEach
    void resetStubs() {
        wireMockServer.resetAll();
    }

    @Test
    void testUploadImage() throws Exception {
        // Arrange: Stub the POST endpoint to simulate a successful upload.
        stubFor(post(urlEqualTo("/api/images/upload"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/plain")
                        .withBody("uploaded_image_url")));

        // Create a temporary file to simulate an image.
        File tempFile = File.createTempFile("test-image", ".jpg");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write("dummy image content".getBytes());
        }

        // Act: Call the method under test.
        String response = ImageController.uploadImage(tempFile);

        // Assert: Validate the response.
        assertEquals("uploaded_image_url", response);

        // Clean up the temporary file.
        tempFile.delete();
    }

    @Test
    void testGetImage() throws Exception {
        // Arrange: Create dummy binary content.
        byte[] imageBytes = "dummy image content".getBytes();
        stubFor(get(urlEqualTo("/api/images/test-image.jpg"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "image/jpeg")
                        .withBody(imageBytes)));

        // Act: Call the getImage method.
        InputStream is = ImageController.getImage("test-image.jpg");

        // Assert: Read and verify the image data.
        assertNotNull(is, "Expected non-null InputStream");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read;
        while ((read = is.read(buffer)) != -1) {
            baos.write(buffer, 0, read);
        }
        byte[] result = baos.toByteArray();
        assertArrayEquals(imageBytes, result);
    }

    @Test
    void testDeleteImage() {
        // Arrange: Stub the DELETE endpoint to simulate successful deletion.
        stubFor(delete(urlEqualTo("/api/images/test-image.jpg"))
                .willReturn(aResponse()
                        .withStatus(200)));

        // Act: Call the deleteImage method.
        boolean success = ImageController.deleteImage("test-image.jpg");

        // Assert: Verify that deletion was reported as successful.
        assertTrue(success);
    }
}
