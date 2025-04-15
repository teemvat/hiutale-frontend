package controller.ui;

import app.Main;
import controller.api.LocationController;
import controller.api.UserController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Event;
import utils.ImageUtil;
import utils.WindowUtil;

/**
 * Controller for managing the Event Page view.
 * This class handles the display of detailed event information and user interactions
 * such as buying tickets for the event.
 */
public class EventPageController {

  @FXML private Label eventNameLabel;
  @FXML private Label organizerLabel;
  @FXML private Label dateLabel;
  @FXML private Label startTimeLabel;
  @FXML private Label endTimeLabel;
  @FXML private Label locationLabel;
  @FXML private Label priceLabel;
  @FXML private Label descriptionLabel;
  @FXML private ImageView eventImage;
  @FXML private Button buyTicketButton;
  private Event event;

  /**
   * Handles the action of buying a ticket for the event.
   * Opens the RSVP window where the user can confirm their ticket purchase.
  */
  @FXML
  private void handleBuyTicketAction() {
    WindowUtil.openNewWindow(
            "/fxml/rsvp.fxml",
            Main.getBundle().getString("rsvp.title"),
            (Stage) buyTicketButton.getScene().getWindow(),
            Main.getBundle(),
            (RsvpController controller) -> controller.setEvent(event)
    );
  }

  /**
   * Sets the details of the event to be displayed on the Event Page.
   * Updates the labels and image with the event's information.
   *
   * @param event The event whose details are to be displayed.
  */
  public void setEventDetails(Event event) {
    this.event = event;
    eventNameLabel.setText(this.event.getTitle());
    organizerLabel.setText(UserController.getUser(Integer.parseInt(this.event.getOrganizerId())).getUsername());
    dateLabel.setText(this.event.getDate());
    startTimeLabel.setText(this.event.getStart());
    endTimeLabel.setText(this.event.getEnd());
    locationLabel.setText(LocationController.getLocationById(this.event.getLocationId()).getName());
    priceLabel.setText(Double.toString(this.event.getPrice()));
    descriptionLabel.setText(this.event.getDescription());
    ImageUtil.loadEventImage(this.event, eventImage);
  }
}
