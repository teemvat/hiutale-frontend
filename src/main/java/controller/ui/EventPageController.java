package controller.ui;

import app.Main;
import controller.api.LocationController;
import controller.api.UserController;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Event;
import utils.ImageUtil;

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
  //private Image placeholderImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pictures/placeholder_event.jpg")));

  /**
   * Handles the action of buying a ticket for the event.
   * Opens the RSVP window where the user can confirm their ticket purchase.
  */
  @FXML
  private void handleBuyTicketAction() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/rsvp.fxml"), Main.bundle);
      Parent root = loader.load();
      RsvpController controller = loader.getController();
      controller.setEvent(this.event);
      Stage stage = new Stage();
      stage.setTitle(Main.bundle.getString("rsvp.title"));
      stage.setScene(new Scene(root));
      stage.initModality(Modality.WINDOW_MODAL);
      stage.initOwner(buyTicketButton.getScene().getWindow());
      stage.showAndWait();
    } catch (IOException e) {
      System.err.println("Error loading RSVP window: " + e.getMessage());
      e.printStackTrace(System.err);
    }
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
