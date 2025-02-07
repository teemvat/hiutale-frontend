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
    private Slider priceRangeSlider;

    @FXML
    private Button searchButton;

    @FXML
    private Button resetButton;

    @FXML
    private Button profileButton;

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
        priceRangeSlider.setValue(0);
        System.out.println("Reset button clicked");
    }

    @FXML
    private void handleProfileAction(ActionEvent event) {
        // Placeholder profile function
        System.out.println("Profile button clicked");
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
