package controller.ui;

import app.Main;
import controller.api.AttendanceController;
import controller.api.EventController;
import controller.api.FavouriteController;
import controller.api.UserController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

/**
 * Controller class for managing the user profile view.
 * Handles displaying user information, favorite events, upcoming events, and organized events.
 * Provides functionality for logging out and removing the user account.
*/
public class ProfileController {

  @FXML private Label usernameLabel;
  @FXML private Label emailLabel;
  @FXML private Button logoutButton;
  @FXML private Button removeUserButton;
  @FXML private VBox favoriteEventsBox;
  @FXML private VBox upcomingEventsBox;
  @FXML private VBox organizedEventsBox;

  /**
   * Initializes the profile view by loading event cards and setting user information.
  */
  @FXML
  private void initialize() {
    loadEventCards(favoriteEventsBox, FavouriteController.getUserFavourites(), Main.getBundle().getString("no.favourite.events"));
    loadEventCards(upcomingEventsBox, AttendanceController.getUserAttendances(), Main.getBundle().getString("no.upcoming.events"));
    loadEventCards(organizedEventsBox, EventController.getEventsByOrganizer(String.valueOf(SessionManager.getInstance().getUser().getId())), Main.getBundle().getString("no.organized.events"));
    setUserInformation();
  }

  /**
   * Sets the user information (username and email) in the profile view.
  */
  private void setUserInformation() {
    User user = SessionManager.getInstance().getUser();
    usernameLabel.setText(user.getUsername());
    emailLabel.setText(user.getEmail());
  }

  /**
   * Loads event cards into the specified container.
   * If no events are available, displays a message indicating the absence of events.
   *
   * @param container The VBox container to load the event cards into.
   * @param events The list of events to display.
   * @param emptyMessage The message to display if the event list is empty.
   */
  private void loadEventCards(VBox container, List<Event> events, String emptyMessage) {
    container.getChildren().clear();
    if (events == null || events.isEmpty()) {
      container.getChildren().add(new Label(emptyMessage));
      return;
    }

    for (Event event : events) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/event_card.fxml"), Main.getBundle());
        Parent root = loader.load();
        EventCardController controller = loader.getController();
        controller.setEventData(event);
        container.getChildren().add(root);
      } catch (IOException e) {
        System.err.println("Failed to load event card for: " + event.getTitle() + " " + e.getMessage());
        e.printStackTrace(System.err);
      }
    }
  }

  /**
   * Handles the logout action by logging out the user, closing all windows, and opening the login page.
   */
  @FXML
  private void handleLogoutAction() {
    UserController.logout();
    closeAllWindows();
    openLoginPage();
  }

  /**
   * Closes all open windows in the application.
   */
  private void closeAllWindows() {
    List<Window> windows = new ArrayList<>(Stage.getWindows());
    for (Window window : windows) {
      if (window instanceof Stage) {
        ((Stage) window).close();
      }
    }
  }

  /**
   * Opens the login page after the user logs out.
   */
  private void openLoginPage() {
    try {
      Parent loginPage = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
      Stage stage = (Stage) logoutButton.getScene().getWindow();
      stage.setScene(new Scene(loginPage));
      stage.show();
    } catch (IOException e) {
      System.err.println("Failed to open login page: " + e.getMessage());
      e.printStackTrace(System.err);
    }
  }

  /**
   * Handles the action to remove the user account.
   * Deletes the user account and logs out the user.
   */
  @FXML
  private void handleRemoveUserAction() {
    UserController.deleteUser(SessionManager.getInstance().getUser().getId());
    handleLogoutAction();
  }
}
