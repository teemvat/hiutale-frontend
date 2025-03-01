package controller.ui;

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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/rsvp.fxml"));
            Parent rsvpRoot = fxmlLoader.load();
            RSVPController rsvpController = fxmlLoader.getController();
            rsvpController.setEvent(event);
            Stage rsvpStage = new Stage();
            rsvpStage.setTitle("RSVP for " + event.getTitle());
            rsvpStage.setScene(new Scene(rsvpRoot));
            rsvpStage.initModality(Modality.WINDOW_MODAL);
            rsvpStage.initOwner(eventTitle.getScene().getWindow());
            rsvpStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
