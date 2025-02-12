package controller.ui;

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

import java.io.IOException;

public class EventController {

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
    private Button profileButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button addEventButton;

    @FXML
    private Button buyTicketButton;

    @FXML
    private void handleProfileAction(ActionEvent event) {
        // TODO: Add functionality
        System.out.println("Profile button clicked");
    }

    @FXML
    private void handleLogoutAction(ActionEvent event) {
        UserController.logout();
        System.out.println("Logout button clicked");
        try {
            Parent homePage = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Scene homeScene = new Scene(homePage);
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(homeScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddEventAction(ActionEvent event) {
        System.out.println("Add new event button clicked");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/newevent.fxml"));
            Parent newEventRoot = fxmlLoader.load();
            Stage newEventStage = new Stage();
            newEventStage.setTitle("Add New Event");
            newEventStage.setScene(new Scene(newEventRoot));
            newEventStage.initModality(Modality.WINDOW_MODAL);
            newEventStage.initOwner(addEventButton.getScene().getWindow());
            newEventStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBuyTicketAction(ActionEvent event) {
        System.out.println("Buy ticket button clicked");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/rsvp.fxml"));
            Parent newEventRoot = fxmlLoader.load();
            Stage newEventStage = new Stage();
            newEventStage.setTitle("Buy ticket");
            newEventStage.setScene(new Scene(newEventRoot));
            newEventStage.initModality(Modality.WINDOW_MODAL);
            newEventStage.initOwner(addEventButton.getScene().getWindow());
            newEventStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setEventDetails(String organizer, String date, String startTime, String endTime, String location, String price, String description, String imageUrl) {
        organizerLabel.setText(organizer);
        dateLabel.setText(date);
        startTimeLabel.setText(startTime);
        endTimeLabel.setText(endTime);
        locationLabel.setText(location);
        priceLabel.setText(price);
        descriptionLabel.setText(description);
        eventImage.setImage(new Image(imageUrl));
    }
}
