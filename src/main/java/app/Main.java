package app;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The Main class serves as the entry point for the JavaFX application.
 * It initializes the application, sets up the primary stage, and manages localization.
 */
public class Main extends Application {

  private static Locale currentLocale = Locale.getDefault();
  private static ResourceBundle bundle = ResourceBundle.getBundle("MessagesBundle", currentLocale);

  /**
   * The main entry point for the JavaFX application.
   * This method is called after the application is launched.
   *
   * @param stage The primary stage for this application.
   * @throws Exception If an error occurs during loading the FXML or resources.
  */
  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"), bundle);
    Parent root = loader.load();

    Scene scene = new Scene(root);

    scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());

    stage.setTitle(bundle.getString("login.title"));
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Sets the locale for the application and updates the resource bundle.
   * This method allows dynamic changes to the application's language.
   *
   * @param locale The new locale to set.
  */
  public static void setLocale(Locale locale) {
    currentLocale = locale;
    bundle = ResourceBundle.getBundle("MessagesBundle", currentLocale, new ResourceBundle.Control() {
      @Override
      public long getTimeToLive(String baseName, Locale locale) {
        return TTL_DONT_CACHE;
      }
    });
  }

  public static Locale getCurrentLocale() {
    return currentLocale;
  }

  public static ResourceBundle getBundle() {
    return bundle;
  }

  /**
   * The main method, which serves as the entry point for the application.
   * It forces JavaFX to use software rendering and launches the application.
   *
   * @param args Command-line arguments passed to the application.
  */
  public static void main(String[] args) {
    // Force JavaFX to use software rendering
    System.setProperty("prism.order", "sw");

    launch(args);
  }
}
