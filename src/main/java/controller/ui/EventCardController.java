package controller.ui;

import app.Main;
import controller.api.FavouriteController;
import controller.api.LocationController;
import java.io.InputStream;
import java.text.MessageFormat;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import model.Event;
import utils.ImageUtil;
import utils.WindowUtil;

/**
 * Controller for controlling the event cards.
 */
public class EventCardController {

  @FXML private ImageView eventImage;
  @FXML private ImageView ticketImage;
  @FXML private ImageView favoriteImage;
  @FXML private Label eventTitle;
  @FXML private Label eventDate;
  @FXML private Label eventLocation;
  @FXML private Button ticketButton;
  @FXML private Button shareButton;
  @FXML private Button favoriteButton;

  private Event event;

  /**
   * Method that updates the information on event card based on the event.
   *
   * @param event the event containing the information to be displayed.
   */
  public void setEventData(Event event) {
    this.event = event;
    ImageUtil.loadEventImage(event, eventImage);
    eventTitle.setText(event.getTitle());
    eventDate.setText(event.getDate());
    eventLocation.setText(LocationController.getLocationById(event.getLocationId()).getName());

    updateTicketIcon();
    updateFavouriteIcon();
  }

  private void updateTicketIcon() {
    int totalTickets = event.getCapacity();
    int ticketsLeft = totalTickets - event.getAttendanceCount();
    String greenTicket = "/pictures/icons/ticket_green.png";
    String yellowTicket = "/pictures/icons/ticket_yellow.png";
    String orangeTicket = "/pictures/icons/ticket_orange.png";
    String redTicket = "/pictures/icons/ticket_red.png";

    if (totalTickets > 0) {
      double percentageLeft = (double) ticketsLeft / totalTickets * 100;
      String iconPath;

      if (percentageLeft > 75) {
        iconPath = greenTicket;
      } else if (percentageLeft > 50) {
        iconPath = yellowTicket;
      } else if (percentageLeft > 25) {
        iconPath = orangeTicket;
      } else {
        iconPath = redTicket;
      }
      ticketImage.setImage(loadImage(iconPath));
    }
  }

  @FXML
  private void handleTicketAction() {
    WindowUtil.openNewWindow(
            "/fxml/rsvp.fxml",
            Main.getBundle().getString("ticket.title"),
            (Stage) ticketButton.getScene().getWindow(),
            Main.getBundle(),
            (RsvpController controller) -> controller.setEvent(event)
    );
  }

  private void updateFavouriteIcon() {
    String starFull = "/pictures/icons/star_filled.png";
    String startEmpty = "/pictures/icons/star.png";
    boolean isFavourite = FavouriteController.getUserFavourites().contains(this.event);
    favoriteImage.setImage(loadImage(isFavourite ? starFull : startEmpty));
  }

  @FXML
  private void handleFavoriteAction() {
    if (FavouriteController.getUserFavourites().contains(this.event)) {
      FavouriteController.deleteFavourite(this.event.getId());
    } else {
      FavouriteController.createFavourite(this.event.getId());
    }
    updateFavouriteIcon();
  }

  @FXML
  private void handleShareAction() {
    String eventInfo = MessageFormat.format(Main.getBundle().getString("card.info"),
            eventTitle.getText(),
            eventDate.getText(),
            eventLocation.getText(),
            event.getDescription());

    Clipboard clipboard = Clipboard.getSystemClipboard();
    ClipboardContent content = new ClipboardContent();
    content.putString(eventInfo);
    clipboard.setContent(content);
  }

  @FXML
  private void handleCardClick() {
    WindowUtil.openNewWindow(
            "/fxml/event_page.fxml",
            event.getTitle(),
            (Stage) eventTitle.getScene().getWindow(),
            Main.getBundle(),
            (EventPageController controller) -> controller.setEventDetails(event)
    );
  }

  private Image loadImage(String path) {
    InputStream stream = getClass().getResourceAsStream(path);
    if (stream != null) {
      return new Image(stream);
    }
    return null;
  }
}
