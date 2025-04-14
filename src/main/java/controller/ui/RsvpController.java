package controller.ui;

import controller.api.AttendanceController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.Event;

/**
 * Controller class for handling RSVP actions for events.
 * Manages the RSVP process by interacting with the AttendanceController
 * and closing the RSVP window upon successful submission.
 */
public class RsvpController {

  @FXML private Button rsvpButton;
  private Event event;

  /**
   * Sets the event for which the RSVP is being made.
   *
   * @param event The event to set.
   */
  public void setEvent(Event event) {
    this.event = event;
  }

  /**
   * Handles the RSVP action when the RSVP button is clicked.
   * Submits the RSVP for the specified event and closes the RSVP window.
   * If no event is set, logs a message indicating the event is not set.
   */
  @FXML
  private void handleRsvpAction() {
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
