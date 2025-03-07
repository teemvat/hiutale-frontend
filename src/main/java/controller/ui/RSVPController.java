package controller.ui;

import controller.api.AttendanceController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.Event;

public class RSVPController {

    @FXML private Button rsvpButton;
    private Event event;

    public void setEvent(Event event) {
        this.event = event;
        System.out.println("Event set for RSVP: " + event.getTitle());
    }

    @FXML
    private void handleRSVPAction(ActionEvent event) {
        if (this.event != null) {
            AttendanceController.createAttendance(this.event.getId());
            System.out.println("RSVP submitted for event: " + this.event.getTitle());

            Stage stage = (Stage) rsvpButton.getScene().getWindow();
            stage.close();
        } else {
            System.out.println("Event is not set.");
        }
    }
}
