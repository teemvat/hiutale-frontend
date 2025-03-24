package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    public static Locale currentLocale = Locale.getDefault();
    public static ResourceBundle bundle = ResourceBundle.getBundle("MessagesBundle", currentLocale);

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"), bundle);
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setTitle(bundle.getString("login.title"));
        stage.setScene(scene);
        stage.show();
    }

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle("MessagesBundle", currentLocale, new ResourceBundle.Control() {
            @Override
            public long getTimeToLive(String baseName, Locale locale) {
                return TTL_DONT_CACHE;
            }
        });
    }

    public static void main(String[] args) {
        // Force JavaFX to use software rendering
        System.setProperty("prism.order", "sw");

        launch(args);
    }
}
