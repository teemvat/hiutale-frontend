package controller.ui;

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

    @FXML
    private Label eventNameLabel;

    @FXML
    private Label organizerLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label startTimeLabel;

    @FXML
    private Label endTimeLabel;

    @FXML
    private Label locationLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private ImageView eventImage;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Button buyTicketButton;

    @FXML
    private void handleBuyTicketAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/rsvp.fxml"));
            Parent newEventRoot = fxmlLoader.load();
            Stage newEventStage = new Stage();
            newEventStage.setTitle("Buy ticket");
            newEventStage.setScene(new Scene(newEventRoot));
            newEventStage.initModality(Modality.WINDOW_MODAL);
            newEventStage.initOwner(buyTicketButton.getScene().getWindow());
            newEventStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setEventDetails(Event event) {
        eventNameLabel.setText(event.getEventTitle());
        organizerLabel.setText(UserController.getUser(event.getEventOrganizerId()).getUsername());
        dateLabel.setText(event.getEventDate().toString());
        startTimeLabel.setText(event.getStartTime());
        endTimeLabel.setText(event.getEndTime());
        //locationLabel.setText(event.getEventLocationId().getLocationName());    // TODO
        priceLabel.setText(Double.toString(event.getEventPrice()));
        descriptionLabel.setText(event.getEventDescription());
        //eventImage.setImage(new Image(imageUrl));                               // TODO
    }
}
