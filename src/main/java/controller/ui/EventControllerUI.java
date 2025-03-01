package controller.ui;

import controller.api.LocationController;
import controller.api.UserController;
import javafx.event.ActionEvent;
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

import java.io.IOException;

public class EventControllerUI {

    @FXML private Label eventNameLabel, organizerLabel, dateLabel, startTimeLabel, endTimeLabel, locationLabel, priceLabel, descriptionLabel;
    @FXML private ImageView eventImage;
    @FXML private Button buyTicketButton;

    @FXML
    private void handleBuyTicketAction(ActionEvent event) {
        showModalWindow("/fxml/rsvp.fxml", "Buy ticket", buyTicketButton);
    }

    public void setEventDetails(Event event) {
        eventNameLabel.setText(event.getTitle());
        organizerLabel.setText(UserController.getUser(Integer.parseInt(event.getOrganizerId())).toString());
        dateLabel.setText(event.getDate());
        startTimeLabel.setText(event.getStart());
        endTimeLabel.setText(event.getEnd());
        locationLabel.setText(LocationController.getLocationById(event.getLocationId()).getName());
        priceLabel.setText(Double.toString(event.getPrice()));
        descriptionLabel.setText(event.getDescription());

//        if (event.getImageUrl() != null && !event.getImageUrl().isEmpty()) {
//            eventImage.setImage(new Image(event.getImageUrl())); // TODO
//        }
    }

    private void showModalWindow(String fxmlPath, String title, Button ownerButton) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(ownerButton.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("Error loading window: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
