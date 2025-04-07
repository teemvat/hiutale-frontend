package utils;

import controller.api.ImageController;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import model.Event;

import java.io.File;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class LazyImageView extends StackPane {
    static final String PLACEHOLDER_URL = "/pictures/placeholder_event.jpg"; // Placeholder image
    private static final Map<String, Image> imageCache = new ConcurrentHashMap<>(); // Cache for loaded images
    private final ImageView imageView;
    final ProgressIndicator progressIndicator = new ProgressIndicator();

    public LazyImageView(Event event) {
        // Load placeholder image first
        var placeholderStream = getClass().getResourceAsStream(PLACEHOLDER_URL);
        if (placeholderStream == null) {
            throw new IllegalArgumentException("Placeholder image not found: " + PLACEHOLDER_URL);
        }
        imageView = new ImageView(new Image(placeholderStream));

        getChildren().addAll(imageView, progressIndicator); // Show placeholder & loading spinner

        loadEventImage(event);
    }

    private void loadEventImage(Event event) {
        if (event == null) return; // No valid event

        String imageUrl = ImageController.getImageURL(String.valueOf(event.getId()));

        if (imageUrl == null || imageUrl.isEmpty()) {
            System.out.println("No image found for event " + event.getId());
            getChildren().remove(progressIndicator); // Stop loading spinner
            return;
        }

        if (imageCache.containsKey(imageUrl)) {
            imageView.setImage(imageCache.get(imageUrl));
            getChildren().remove(progressIndicator);
            return;
        }

        // Load image asynchronously
        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() {
                File downloadedFile = ImageController.getImage(event);
                if (downloadedFile != null) {
                    return new Image(downloadedFile.toURI().toString());
                }
                return null;
            }
        };

        loadImageTask.setOnSucceeded(event1 -> {
            Image loadedImage = loadImageTask.getValue();
            if (loadedImage != null) {
                imageCache.put(imageUrl, loadedImage); // Cache it
                imageView.setImage(loadedImage);
            }
            getChildren().remove(progressIndicator); // Remove spinner
        });

        new Thread(loadImageTask).start(); // Run task in separate thread
    }

    public ImageView getImageView() {
        return imageView;
    }
}
