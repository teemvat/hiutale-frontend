package utils;

import controller.api.ImageController;
import java.io.File;
import java.util.Objects;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Event;

/**
 * JavaDoc.
 */
public class ImageUtil {
  private static final Image placeholderImage = new Image(Objects.requireNonNull(ImageUtil.class.getResourceAsStream("/pictures/placeholder_event.jpg")));

  public static void loadEventImage(Event event, ImageView imageView) {
    if (event.getImage() != null) {
      try {
        File image = ImageController.getImage(event);
        imageView.setImage(new Image(image.toURI().toString()));
        } catch (Exception e) {
                System.err.println("Failed to load event image: " + e.getMessage());
                imageView.setImage(placeholderImage);
            }
  } else {
            imageView.setImage(placeholderImage);
    }
  }
}
