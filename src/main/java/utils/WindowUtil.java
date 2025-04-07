package utils;

import java.io.IOException;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Utility class that opens new windows.
 */
public class WindowUtil {

  /**
  * Method that opens a nw window.

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
      System.err.println("Failed to open Event Page: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
