package controller.ui;

import app.Main;
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
        loadEventCards(favoriteEventsVBox, FavouriteController.getUserFavourites(), Main.bundle.getString("no.favourite.events"));
        loadEventCards(upcomingEventsVBox, AttendanceController.getUserAttendances(), Main.bundle.getString("no.upcoming.events"));
        loadEventCards(organizedEventsVBox, EventController.getEventsByOrganizer(String.valueOf(SessionManager.getInstance().getUser().getId())), Main.bundle.getString("no.organized.events"));
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/eventcard.fxml"), Main.bundle);
                Parent root = loader.load();
                EventCardController controller = loader.getController();
                controller.setEventData(event);
                container.getChildren().add(root);
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
