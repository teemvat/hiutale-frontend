package utils;

import static utils.RtlLanguageUtil.isRtl;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
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

  /**
  * Opens a new modal window.

  * @param resource the resource file
  * @param title the title for the new window
  * @param controller the controller class for the new window
  * @param ownerStage the owner stage
  * @param bundle the resource bundle for localization
  */
  public static void openNewWindow(String resource, String title, Object controller, Stage ownerStage, ResourceBundle bundle) {
    try {
      FXMLLoader loader = new FXMLLoader(controller.getClass().getResource(resource), bundle);
      Parent root = loader.load();
      loader.setController(controller);

      Stage stage = new Stage();
      stage.setTitle(title);
      stage.setScene(new Scene(root));
      stage.initModality(Modality.WINDOW_MODAL);
      stage.initOwner(ownerStage);
      stage.showAndWait();
    } catch (IOException e) {
      System.err.println("Failed to open new window: " + e.getMessage());
      e.printStackTrace(System.err);
    }
  }

//  /**
//   * Switches the current stage scene.
//   *
//   * @param resource the FXML resource file
//   * @param title    the title for the new scene
//   * @param bundle   the resource bundle for localization
//   * @param currentStage the current stage
//   */
//  public static void switchScene(String resource, String title, ResourceBundle bundle, Stage currentStage) {
//    try {
//      FXMLLoader loader = new FXMLLoader(WindowUtil.class.getResource(resource), bundle);
//      Parent root = loader.load();
//      Scene scene = new Scene(root);
//
//      currentStage.setTitle(title);
//      currentStage.setScene(scene);
//    } catch (IOException e) {
//      System.err.println("Error switching scene: " + e.getMessage());
//      e.printStackTrace(System.err);
//    }
//  }

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
      System.err.println("Error changing the scene: " + e.getMessage());
      e.printStackTrace(System.err);
    }
  }
}
