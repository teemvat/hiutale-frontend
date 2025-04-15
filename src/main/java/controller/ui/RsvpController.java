package controller.ui;

import controller.api.AttendanceController;
import java.util.logging.Logger;
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
  Logger logger = Logger.getLogger(getClass().getName());

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
      logger.info("RSVP submitted for event: " + this.event.getTitle());

      Stage stage = (Stage) rsvpButton.getScene().getWindow();
      stage.close();
    } else {
      logger.info("Event is not set.");
    }
  }
}
