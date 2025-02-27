package controller.ui;

import controller.api.AttendanceController;
import controller.api.EventController;
import controller.api.FavouriteController;
import controller.api.UserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Event;
import model.User;
import utils.SessionManager;

import java.io.IOException;
import java.util.List;

public class ProfileController {

    @FXML
    private Label username;

    @FXML
    private Label email;

    @FXML
    private Button logoutButton;

    @FXML
    private Button removeUserButton;

    @FXML
    private VBox favoriteVBox;

    @FXML
    private VBox upcomingVBox;

    @FXML
    private VBox myeventsVBox;

    @FXML
    private void initialize() {
        setUserInformation();
        loadFavoriteCards();
        loadUpcomingCards();
        loadOrganizerCards();
    }

    private void setUserInformation() {
        User user = SessionManager.getInstance().getUser();
        username.setText(user.getUsername());
        email.setText(user.getEmail());
    }

    private void loadFavoriteCards() {
        List<Event> favorites = FavouriteController.getUserFavourites();
        favoriteVBox.getChildren().clear();

        for (Event event : favorites) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/eventcard.fxml"));
                Parent eventCard = fxmlLoader.load();
                EventCardController controller = fxmlLoader.getController();
                controller.setEventData(event);
                favoriteVBox.getChildren().add(eventCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadUpcomingCards() {
        List<Event> upcoming = AttendanceController.getUserAttendances();
        upcomingVBox.getChildren().clear();

        for (Event event : upcoming) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/eventcard.fxml"));
                Parent eventCard = fxmlLoader.load();
                EventCardController controller = fxmlLoader.getController();
                controller.setEventData(event);
                upcomingVBox.getChildren().add(eventCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadOrganizerCards() {
        List<Event> myEvents = EventController.getEventsByOrganizer(Integer.toString(SessionManager.getInstance().getUser().getId()));
        myeventsVBox.getChildren().clear();

        for (Event event : myEvents) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/eventcard.fxml"));
                Parent eventCard = fxmlLoader.load();
                EventCardController controller = fxmlLoader.getController();
                controller.setEventData(event);
                myeventsVBox.getChildren().add(eventCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleLogoutAction() {
        UserController.logout();

        // Close all open windows
        for (Window window : Stage.getWindows()) {
            if (window instanceof Stage) {
                ((Stage) window).close();
            }
        }

        // Open the login page
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Scene loginScene = new Scene(loginPage);
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRemoveUserAction() {
        // Delete the user
        UserController.deleteUser(SessionManager.getInstance().getUser().getId());

        // Logout (to clean SessionManager) and load the login page
        handleLogoutAction();
        System.out.println("User deletion failed.");
    }
}
