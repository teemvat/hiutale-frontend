package controller.ui;

import controller.api.EventController;
import controller.api.UserController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.function.UnaryOperator;

import java.io.IOException;

public class HomeController {

    @FXML
    private TextField searchField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> eventTypeComboBox;

    @FXML
    private ComboBox<String> locationComboBox;

    @FXML
    private ComboBox<String> organizerComboBox;

    @FXML
    private TextField minPriceField;

    @FXML
    private TextField maxPriceField;

    @FXML
    private Button searchButton;

    @FXML
    private Button resetButton;

    @FXML
    private Button profileButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button addEventButton;

    @FXML
    private ChoiceBox<String> sortChoiceBox;

    @FXML
    private ChoiceBox<String> calendarSortChoiceBox;

    @FXML
    private void initialize() {
        // Initialize ChoiceBox items
        sortChoiceBox.setItems(FXCollections.observableArrayList("Edullisin ensin", "Kallein ensin", "Päivämäärän mukaan", "Lajittele A-Ö, Lajittele Ö-A"));
        calendarSortChoiceBox.setItems(FXCollections.observableArrayList("Edullisin ensin", "Kallein ensin", "Päivämäärän mukaan", "Lajittele A-Ö, Lajittele Ö-A"));

        // Add a TextFormatter to ensure only numbers can be typed in the price fields
        configurePriceFields();
    }

    private void configurePriceFields() {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*(\\.\\d{0,2})?")) { // Vain numerot ja max 2 desimaalia
                return change;
            }
            return null;
        };

        TextFormatter<String> minFormatter = new TextFormatter<>(filter);
        TextFormatter<String> maxFormatter = new TextFormatter<>(filter);
        minPriceField.setTextFormatter(minFormatter);
        maxPriceField.setTextFormatter(maxFormatter);
    }

    @FXML
    private void handleSearchAction(ActionEvent event) {
        String searchQuery = searchField.getText();
        String date = datePicker.getValue() != null ? datePicker.getValue().toString() : "";
        String eventType = eventTypeComboBox.getValue();
        String location = locationComboBox.getValue();
        String organizer = organizerComboBox.getValue();
        String minPrice = minPriceField.getText();
        String maxPrice = maxPriceField.getText();

        EventController.searchEvents(searchQuery, eventType, date, location, minPrice, maxPrice, organizer);
        System.out.println("Search button clicked");
    }

    @FXML
    private void handleResetAction(ActionEvent event) {
        // Reset all fields
        searchField.clear();
        datePicker.setValue(null);
        eventTypeComboBox.getSelectionModel().clearSelection();
        locationComboBox.getSelectionModel().clearSelection();
        organizerComboBox.getSelectionModel().clearSelection();
        minPriceField.clear();
        maxPriceField.clear();
        System.out.println("Reset button clicked");
    }

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
    private void handleSortAction(ActionEvent event) {
        // TODO: sort function
        ChoiceBox<String> source = (ChoiceBox<String>) event.getSource();
        String selectedSortMethod = source.getValue();
        System.out.println("Sort method selected: " + selectedSortMethod);
    }
}
