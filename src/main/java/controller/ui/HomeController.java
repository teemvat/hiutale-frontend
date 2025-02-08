package controller.ui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import java.util.function.UnaryOperator;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
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
        sortChoiceBox.setItems(FXCollections.observableArrayList("Sort A-Z", "Sort Z-A", "Sort by Date"));
        calendarSortChoiceBox.setItems(FXCollections.observableArrayList("Sort A-Z", "Sort Z-A", "Sort by Date"));

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
        // Placeholder search function
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
        // Placeholder profile function
        System.out.println("Profile button clicked");
    }

    @FXML
    private void handleLogoutAction(ActionEvent event) {
        // Perform logout logic here
        System.out.println("Logout successful");
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
        // Placeholder sort function
        ChoiceBox<String> source = (ChoiceBox<String>) event.getSource();
        String selectedSortMethod = source.getValue();
        System.out.println("Sort method selected: " + selectedSortMethod);
    }
}
