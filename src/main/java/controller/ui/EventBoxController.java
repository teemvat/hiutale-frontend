package controller.ui;

import app.Main;
import controller.api.LocationController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Event;
import utils.WindowUtil;

/**
 * Controller for controlling the EventBoxes inside the Home-views calendar.
 */
public class EventBoxController {

  @FXML private Label eventTitle;
  @FXML private Label eventTime;
  @FXML private Label eventLocation;

  private Event event;

  /**
  * Method that updates the labels in each EventBox with the correct event information.
  * The method is called from the HomeController that loads all the EventBoxes to the Calendar.

  * @param event the event containing the information to be displayed.
  */
  public void setEventData(Event event) {
    this.event = event;

    eventTitle.setText(event.getTitle());
    eventTime.setText(event.getStart());
    eventLocation.setText(LocationController.getLocationById(event.getLocationId()).getName());
  }

  @FXML
  private void handleBoxClick() {
    WindowUtil.openNewWindow(
            "/fxml/event_page.fxml",
            event.getTitle(),
            (Stage) eventTitle.getScene().getWindow(),
            Main.getBundle(),
            (EventPageController controller) -> controller.setEventDetails(event));
  }
}
