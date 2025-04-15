package utils;

import static utils.RtlLanguageUtil.isRtl;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Utility class that opens new windows and switches scenes in current window.
 */
public class WindowUtil {

  private static final Logger logger = Logger.getLogger(WindowUtil.class.getName());

  private WindowUtil() {}

  public static <T> void openNewWindow(String resource, String title, Stage ownerStage, ResourceBundle bundle, Consumer<T> controllerInitializer) {
    try {
      FXMLLoader loader = new FXMLLoader(WindowUtil.class.getResource(resource), bundle);
      Parent root = loader.load();

      T controller = loader.getController();
      controllerInitializer.accept(controller);

      Stage stage = new Stage();
      stage.setTitle(title);
      stage.setScene(new Scene(root));
      stage.initModality(Modality.WINDOW_MODAL);
      stage.initOwner(ownerStage);
      stage.showAndWait();
    } catch (IOException e) {
      logger.info("Failed to open new window: " + e.getMessage());
    }
  }

  public static void changeScene(String resource, String titleKey, Control control, ResourceBundle bundle) {
    try {
      FXMLLoader loader = new FXMLLoader(WindowUtil.class.getResource(resource), bundle);
      Parent root = loader.load();
      Scene scene = new Scene(root);

      // Check text directionality
      Locale locale = bundle.getLocale();
      if (isRtl(locale)) {
        scene.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
      } else {
        scene.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
      }

      Stage stage = (Stage) control.getScene().getWindow();
      stage.setTitle(bundle.getString(titleKey));
      stage.setScene(scene);
    } catch (IOException e) {
      logger.info("Error changing the scene: " + e.getMessage());
    }
  }


}
