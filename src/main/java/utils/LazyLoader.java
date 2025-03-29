package utils;

import controller.api.ImageController;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Event;

import java.io.File;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class LazyLoader {

    private static final Map<String, Image> imageCache = new ConcurrentHashMap<>();
    private static final Image placeholderImage = new Image(Objects.requireNonNull(LazyLoader.class.getResourceAsStream("/pictures/placeholder_event.jpg")));

    public static void getImage(ImageView view, Event event) {
        view.setImage(placeholderImage);
        String url = event.getImage();

        if (imageCache.containsKey(url)) {
            view.setImage(imageCache.get(url));
            return;
        }

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

        loadImageTask.setOnSucceeded(e -> {
            view.setImage(loadImageTask.getValue());
            imageCache.put(url, loadImageTask.getValue());
        });

    }
}
