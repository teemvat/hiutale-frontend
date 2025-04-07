package controller.ui;

import app.Main;
import controller.api.FavouriteController;
import controller.api.ImageController;
import controller.api.LocationController;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Event;
import utils.ImageUtil;

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
  //private final Image placeholderImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pictures/placeholder_event.jpg")));

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

        if (totalTickets > 0) {
            double percentageLeft = (double) ticketsLeft / totalTickets * 100;
            String iconPath = percentageLeft > 75 ? "/pictures/icons/ticket_green.png" : percentageLeft > 50 ? "/pictures/icons/ticket_yellow.png" : percentageLeft > 25 ? "/pictures/icons/ticket_orange.png" : "/pictures/icons/ticket_red.png";
            ticketImage.setImage(loadImage(iconPath));
        }
    }

    @FXML
    private void handleTicketAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/rsvp.fxml"), Main.bundle);
            Parent root = loader.load();
            RSVPController controller = loader.getController();
            controller.setEvent(this.event);
            Stage stage = new Stage();
            stage.setTitle(Main.bundle.getString("ticket.title"));
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(ticketButton.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("Error loading RSVP window: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateFavouriteIcon() {
        boolean isFavourite = FavouriteController.getUserFavourites().contains(this.event);
        favoriteImage.setImage(loadImage(isFavourite ? "/pictures/icons/star_filled.png" : "/pictures/icons/star.png"));
    }

    @FXML
    private void handleShareAction() {
        String eventInfo = MessageFormat.format(Main.bundle.getString("card.info"),
                eventTitle.getText(), eventDate.getText(), eventLocation.getText(), event.getDescription());

        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(eventInfo);
        clipboard.setContent(content);
        System.out.println("Event information copied to clipboard");
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
    private void handleCardClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/event_page.fxml"), Main.bundle);
            Parent root = loader.load();
            EventPageController controller = loader.getController();
            controller.setEventDetails(event);
            Stage stage = new Stage();
            stage.setTitle(event.getTitle());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(eventTitle.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("Failed to open Event Page: " + e.getMessage());
        }
    }

    private Image loadImage(String path) {
        return loadImage(path, null);
    }

    private Image loadImage(String path, String fallbackpath) {
        try {
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        } catch (NullPointerException e) {
            return fallbackpath != null ? new Image(Objects.requireNonNull(getClass().getResourceAsStream(fallbackpath))) : null;
        }
    }
}
