package controller.ui;

import app.Main;
import controller.api.LocationController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Event;

import java.io.IOException;

public class EventBoxController {

    @FXML private Label eventTitle;
    @FXML private Label eventTime;
    @FXML private Label eventLocation;

    private Event event;

    public void setEventData(Event event) {
        this.event = event;

        eventTitle.setText(event.getTitle());
        eventTime.setText(event.getStart());
        eventLocation.setText(LocationController.getLocationById(event.getLocationId()).getName());
    }

    @FXML
    private void handleBoxClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/eventpage.fxml"), Main.bundle);
            Parent root = loader.load();
            EventPageController controller = loader.getController();
            controller.setEventDetails(event);
            Stage stage = new Stage();
            stage.setTitle(event.getTitle());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(eventTitle.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("Failed to open Event Page: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
