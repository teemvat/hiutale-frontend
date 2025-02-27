package controller.ui;

import controller.api.AttendanceController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import model.Event;

public class RSVPController {

    @FXML private Button rsvpButton;

    private Event event;

    public void setEvent(Event event) {
        this.event = event;
    }

    @FXML
    private void handleRSVPAction(ActionEvent event) {
        if (this.event != null) {
            AttendanceController.createAttendance(this.event);
        } else {
            System.out.println("Event is not set.");
        }
    }
}
