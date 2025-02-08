package controller.ui;

import controller.api.EventController;
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

public class NewEventController {

    @FXML
    private ImageView eventImageView;

    @FXML
    private TextField eventNameField;

    @FXML
    private TextField eventDescriptionField;

    @FXML
    private ComboBox<String> eventTypeComboBox;

    @FXML
    private DatePicker eventDatePicker;

    @FXML
    private ComboBox<String> eventLocationComboBox;

    @FXML
    private TextField eventCapacityField;

    @FXML
    private TextField eventPriceField;

    @FXML
    private Label eventImageError;

    @FXML
    private Label eventNameError;

    @FXML
    private Label eventDescriptionError;

    @FXML
    private Label eventTypeError;

    @FXML
    private Label eventDateError;

    @FXML
    private Label eventLocationError;

    @FXML
    private Label eventCapacityError;

    @FXML
    private Label eventPriceError;

    @FXML
    private Label addEventError;

    @FXML
    private Button selectImageButton;

    @FXML
    private Button addEventButton;

    @FXML
    private void initialize() {
        // Add a TextFormatter to ensure only numbers can be typed in the price field
        configurePriceField();
    }

    private void configurePriceField() {
        // UnaryOperator to filter the input
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*(\\.\\d{0,2})?")) { // Vain numerot ja max 2 desimaalia
                return change;
            }
            return null;
        };

        TextFormatter<String> priceFormatter = new TextFormatter<>(filter);
        TextFormatter<String> capacityFormatter = new TextFormatter<>(filter);
        eventPriceField.setTextFormatter(priceFormatter);
        eventCapacityField.setTextFormatter(capacityFormatter);
    }

    @FXML
    private void handleSelectImageAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                Image image = new Image(new FileInputStream(selectedFile));
                eventImageView.setImage(image);
            } catch (FileNotFoundException e) {
                eventImageError.setText("Image not found");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleAddEventAction(ActionEvent event) {
        Image eventImage = eventImageView.getImage();
        String eventName = eventNameField.getText();
        String eventDescription = eventDescriptionField.getText();
        String eventType = eventTypeComboBox.getValue();
        LocalDate eventDate = eventDatePicker.getValue();
        String eventLocation = eventLocationComboBox.getValue();
        String eventCapacity = eventCapacityField.getText();
        String eventPrice = eventPriceField.getText();

        boolean isValid = true;

        if (eventName.isEmpty()) {
            eventNameError.setText("Event name is required");
            isValid = false;
        } else {
            eventNameError.setText("");
        }

        if (eventDescription.isEmpty()) {
            eventDescriptionError.setText("Event description is required");
            isValid = false;
        } else {
            eventDescriptionError.setText("");
        }

        if (eventType == null || eventTypeComboBox.getValue().isEmpty()) {
            eventTypeError.setText("Event type is required");
            isValid = false;
        } else {
            eventTypeError.setText("");
        }

        if (eventDate == null) {
            eventDateError.setText("Event date is required");
            isValid = false;
        } else {
            eventDateError.setText("");
        }

        if (eventLocation == null || eventLocationComboBox.getValue().isEmpty()) {
            eventLocationError.setText("Event location is required");
            isValid = false;
        } else {
            eventLocationError.setText("");
        }

        if (eventCapacity.isEmpty()) {
            eventCapacityError.setText("Event capacity is required");
            isValid = false;
        } else {
            eventCapacityError.setText("");
        }

        if (eventPrice.isEmpty()) {
            eventPriceError.setText("Event price is required");
            isValid = false;
        } else {
            eventPriceError.setText("");
        }

        if(isValid) {
            try {
                // TODO: change eventPrice to minPrice and maxPrice
                // TODO: send image as well
                // TODO: check if its possible to return boolean from the createEvent method (now returns only false/value)
                boolean isEventCreated = EventController.createEvent(eventName, eventDate.toString(), eventLocation, eventDescription, eventCapacity, eventPrice, eventType);
                if (isEventCreated) {
                    System.out.println("Event created successfully");
                    // Close the new event window
                    Stage stage = (Stage) addEventButton.getScene().getWindow();
                    stage.close();
                } else {
                    System.out.println("Event creation failed");
                    addEventError.setText("Event creation failed");
                }
            } catch (Exception e) {
                System.out.println("Event creation failed");
                addEventError.setText("Event creation failed");
                e.printStackTrace();
            }
        }
    }
}