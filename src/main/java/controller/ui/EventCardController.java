package controller.ui;

import javafx.event.ActionEvent;
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

public class EventCardController {

    @FXML
    private ImageView eventImage;

    @FXML
    private Label eventTitle;

    @FXML
    private Label eventDate;

    @FXML
    private Label eventLocation;

    @FXML
    private Button ticketButton;

    @FXML
    private ImageView ticketImage;

    @FXML
    private Button shareButton;

    @FXML
    private Button favoriteButton;

    @FXML
    private ImageView favoriteImage;

    private Event event;
    private Image image;
    private String title;
    private String date;
    private String location;
    private int totalTickets;
    private int ticketsLeft;

    private void initialize() {
        updateTicketIcon();
        updateEventInformation();
    }

    public void setEventData(Event event) {
        this.event = event;
        // TODO image
        eventTitle.setText(event.getEventTitle());
        eventDate.setText(event.getEventDate().toString());
        eventLocation.setText(event.getEventLocationId());
    }

    private void updateEventInformation() {
        eventImage.setImage(image);
        eventTitle.setText(title);
        eventDate.setText(date);
        eventLocation.setText(location);
    }

    private void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public void setTicketsLeft(int ticketsLeft) {
        this.ticketsLeft = ticketsLeft;
        updateTicketIcon();
    }

    private void updateTicketIcon() {
        if (totalTickets == 0) {
            return;
        }

        double percentageLeft = (double) ticketsLeft / totalTickets * 100;
        String iconPath;
        if (percentageLeft > 75) {
            iconPath = "../pictures/icons/ticket_green.png";
        } else if (percentageLeft > 50) {
            iconPath = "../pictures/icons/ticket_yellow.png";
        } else if (percentageLeft > 25) {
            iconPath = "../pictures/icons/ticket_orange.png";
        } else {
            iconPath = "../pictures/icons/ticket_red.png";
        }
        ticketImage.setImage(new Image(getClass().getResourceAsStream(iconPath)));
    }

    @FXML
    private void handleShareAction() {
        String eventInfo = String.format("Event Title: %s\nDate: %s\nLocation: %s\nDescription: %s",
                eventTitle.getText(), eventDate.getText(), eventLocation.getText(), "Event description here");

        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(eventInfo);
        clipboard.setContent(content);

        System.out.println("Event information copied to clipboard");
    }

    @FXML
    private void handleFavoriteAction() {
        // TODO: Add event to favorites
        System.out.println("Favorite button clicked");
    }

    @FXML
    private void handleCardClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/rsvp.fxml"));
            Parent rsvpRoot = fxmlLoader.load();
            RSVPController rsvpController = fxmlLoader.getController();
            rsvpController.setEvent(event);
            Stage rsvpStage = new Stage();
            rsvpStage.setTitle("RSVP for " + event.getEventTitle());
            rsvpStage.setScene(new Scene(rsvpRoot));
            rsvpStage.initModality(Modality.WINDOW_MODAL);
            rsvpStage.initOwner(eventTitle.getScene().getWindow());
            rsvpStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
