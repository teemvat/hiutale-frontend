package controller.ui;

import controller.api.AttendanceController;
import controller.api.EventController;
import controller.api.FavouriteController;
import controller.api.UserController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Event;
import model.User;
import utils.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfileController {

    @FXML private Label usernameLabel, emailLabel;
    @FXML private Button logoutButton, removeUserButton;
    @FXML private VBox favoriteEventsVBox, upcomingEventsVBox, organizedEventsVBox;

    @FXML
    private void initialize() {
        loadEventCards(favoriteEventsVBox, FavouriteController.getUserFavourites(), "No favourite events found");
        loadEventCards(upcomingEventsVBox, AttendanceController.getUserAttendances(), "No upcoming events found");
        loadEventCards(organizedEventsVBox, EventController.getEventsByOrganizer(String.valueOf(SessionManager.getInstance().getUser().getId())), "No organized events found");
        setUserInformation();
    }

    private void setUserInformation() {
        User user = SessionManager.getInstance().getUser();
        usernameLabel.setText(user.getUsername());
        emailLabel.setText(user.getEmail());
    }

    private void loadEventCards(VBox container, List<Event> events, String emptyMessage) {
        container.getChildren().clear();
        if (events == null || events.isEmpty()) {
            container.getChildren().add(new Label(emptyMessage));
            return;
        }

        for (Event event : events) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/eventcard.fxml"));
                Parent eventCard = fxmlLoader.load();
                EventCardController controller = fxmlLoader.getController();
                controller.setEventData(event);
                container.getChildren().add(eventCard);
            } catch (IOException e) {
                System.err.println("Failed to load event card for: " + event.getTitle());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleLogoutAction() {
        UserController.logout();
        closeAllWindows();
        openLoginPage();
    }

    private void closeAllWindows() {
        List<Window> windows = new ArrayList<>(Stage.getWindows());
        for (Window window : windows) {
            if (window instanceof Stage) {
                ((Stage) window).close();
            }
        }
    }

    private void openLoginPage() {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(loginPage));
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to open login page.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRemoveUserAction() {
        UserController.deleteUser(SessionManager.getInstance().getUser().getId());
        handleLogoutAction();
        System.out.println("User deletion failed.");
    }
}
