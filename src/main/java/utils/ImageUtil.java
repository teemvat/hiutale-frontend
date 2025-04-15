package utils;

import controller.api.ImageController;
import java.io.File;
import java.util.Objects;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Event;

/**
 * Utility class for handling image-related operations in the application.
 */
public class ImageUtil {
  private static final Image placeholderImage = new Image(Objects.requireNonNull(ImageUtil.class.getResourceAsStream("/pictures/placeholder_event.jpg")));

  /**
   * Loads the image associated with the given event into the specified ImageView.
   * If the event has no image or the image fails to load, a placeholder image is used.
   *
   * @param event the event whose image is to be loaded
   * @param imageView the ImageView where the image will be displayed
   */
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
