package controller.ui;

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

    @FXML
    private Label eventTitle;

    @FXML
    private Label eventTime;

    @FXML
    private Label eventLocation;

    private Event event;
    private String title;
    private String time;
    private String location;

    @FXML
    private void initialize() {
        updateEventInformation();
    }

    public void setEventData(Event event) {
        this.event = event;
        this.title = event.getTitle();
        this.time = event.getStartTime();
        this.location = event.getLocationId();
        updateEventInformation();
    }

    private void updateEventInformation() {
        eventTitle.setText(title);
        eventTime.setText(time);
        eventLocation.setText(location);
    }

    @FXML
    private void handleBoxClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/rsvp.fxml"));
            Parent rsvpRoot = fxmlLoader.load();
            RSVPController rsvpController = fxmlLoader.getController();
            rsvpController.setEvent(event);
            Stage rsvpStage = new Stage();
            rsvpStage.setTitle("RSVP for " + title);
            rsvpStage.setScene(new Scene(rsvpRoot));
            rsvpStage.initModality(Modality.WINDOW_MODAL);
            rsvpStage.initOwner(eventTitle.getScene().getWindow());
            rsvpStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
