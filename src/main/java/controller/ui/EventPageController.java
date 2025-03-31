package controller.ui;

import app.Main;
import controller.api.ImageController;
import controller.api.LocationController;
import controller.api.UserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Event;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class EventPageController {

    @FXML private Label eventNameLabel, organizerLabel, dateLabel, startTimeLabel, endTimeLabel, locationLabel, priceLabel, descriptionLabel;
    @FXML private ImageView eventImage, eventImageView;
    @FXML private Button buyTicketButton;
    private Event event;

    private Image placeholderImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pictures/placeholder_event.jpg")));

    @FXML
    private void handleBuyTicketAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/rsvp.fxml"));
            Parent root = loader.load();
            RSVPController controller = loader.getController();
            controller.setEvent(this.event);
            Stage stage = new Stage();
            stage.setTitle(Main.bundle.getString("ticket.title"));
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(buyTicketButton.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("Error loading RSVP window: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setEventDetails(Event e) {
        this.event = e;
        eventNameLabel.setText(event.getTitle());
        organizerLabel.setText(UserController.getUser(Integer.parseInt(event.getOrganizerId())).getUsername());
        dateLabel.setText(event.getDate());
        startTimeLabel.setText(event.getStart());
        endTimeLabel.setText(event.getEnd());
        locationLabel.setText(LocationController.getLocationById(event.getLocationId()).getName());
        priceLabel.setText(Double.toString(event.getPrice()));
        descriptionLabel.setText(event.getDescription());

        if (event.getImage() != null) {
            try {
                File image = ImageController.getImage(event);
                eventImage.setImage(new Image(image.toURI().toString()));
                eventImageView.setImage(new Image(image.toURI().toString()));
            } catch (Exception ex) {
                System.err.println("Failed to load event image: " + ex.getMessage());
                eventImage.setImage(placeholderImage);
            }
        } else {
            eventImage.setImage(placeholderImage);
        }
    }

}
