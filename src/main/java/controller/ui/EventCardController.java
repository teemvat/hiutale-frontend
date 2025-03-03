package controller.ui;

import controller.api.FavouriteController;
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

import java.io.IOException;
import java.util.Objects;

public class EventCardController {

    @FXML private ImageView eventImage, ticketImage, favoriteImage;
    @FXML private Label eventTitle, eventDate, eventLocation;
    @FXML private Button ticketButton, shareButton, favoriteButton;

    private Event event;

    public void setEventData(Event event) {
        this.event = event;

        //eventImage.setImage(loadImage(event.getImagePath(), "../pictures/placeholder_event_picture.png"));   // TODO image
        eventTitle.setText(event.getTitle());
        eventDate.setText(event.getDate());
        eventLocation.setText(event.getLocationId());

        //updateTicketIcon();
        //updateFavouriteIcon();
    }

//    private void updateTicketIcon() {
//        int totalTickets = event.getCapacity();
//        int ticketsLeft = totalTickets - event.getAttendanceCount();
//
//        if (totalTickets > 0) {
//            double percentageLeft = (double) ticketsLeft / totalTickets * 100;
//            String iconPath = percentageLeft > 75 ? "../pictures/icons/ticket_green.png" : percentageLeft > 50 ? "../pictures/icons/ticket_yellow.png" : percentageLeft > 25 ? "../pictures/icons/ticket_orange.png" : "../pictures/icons/ticket_red.png";
//            ticketImage.setImage(loadImage(iconPath));
//        }
//    }

    private void updateFavouriteIcon() {
        // TODO lisää nämä takaisin sitten kun getUserFavourites() on saatu kuntoon
        //boolean isFavourite = FavouriteController.getUserFavourites().contains(this.event);
        //favoriteImage.setImage(loadImage(isFavourite ? "../pictures/icons/star_filled.png" : "../pictures/icons/star.png"));
    }

    @FXML
    private void handleShareAction() {
        String eventInfo = String.format("Event Title: %s\nDate: %s\nLocation: %s\nDescription: %s",
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
        //updateFavouriteIcon();
    }

    @FXML
    private void handleCardClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/eventpage.fxml"));
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

//    private Image loadImage(String path) {
//        return loadImage(path, null);
//    }
//
//    private Image loadImage(String path, String fallbackpath) {
//        try {
//            return new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
//        } catch (NullPointerException e) {
//            return fallbackpath != null ? new Image(Objects.requireNonNull(getClass().getResourceAsStream(fallbackpath))) : null;
//        }
//    }
}
