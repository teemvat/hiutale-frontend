package controller.ui;

import controller.api.EventController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Event;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.function.UnaryOperator;

public class NewEventController {

    @FXML private ImageView eventImageView;
    @FXML private TextField eventNameField, eventDescriptionField, eventCapacityField, startTimeField, endTimeField, eventPriceField;
    @FXML private ComboBox<String> eventTypeComboBox, eventLocationComboBox;
    @FXML private DatePicker eventDatePicker;
    @FXML private Label eventImageError, eventNameError, eventDescriptionError, eventTypeError, eventDateError, eventLocationError, eventCapacityError, eventPriceError, addEventError, timeError;
    @FXML private Button selectImageButton, addEventButton;

    @FXML
    private void initialize() {
        configureInputFormatters();
    }

    private void configureInputFormatters() {
        UnaryOperator<TextFormatter.Change> filter = change -> change.getControlNewText().matches("\\d*(\\.\\d{0,2})?") ? change : null;
        eventPriceField.setTextFormatter(new TextFormatter<>(filter));
        eventCapacityField.setTextFormatter(new TextFormatter<>(filter));
        startTimeField.setTextFormatter(new TextFormatter<>(filter));
        endTimeField.setTextFormatter(new TextFormatter<>(filter));
    }

    @FXML
    private void selectEventImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                eventImageView.setImage(new Image(new FileInputStream(selectedFile)));
            } catch (FileNotFoundException e) {
                eventImageError.setText("Image not found");
                System.err.println("Error: Image file not found.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void addEvent(ActionEvent event) {
        if (!validateInput()) return;

        try {
            Event createdEvent = EventController.createEvent(
                    // eventImageView.getImage(),   // TODO
                    eventNameField.getText(),
                    eventDescriptionField.getText(),
                    eventLocationComboBox.getValue(),
                    eventCapacityField.getText(),
                    eventTypeComboBox.getValue(),
                    eventDatePicker.getValue().toString(),
                    startTimeField.getText(),
                    endTimeField.getText(),
                    Double.parseDouble(eventPriceField.getText())
            );

            if (createdEvent != null) {
                System.out.println("Event created successfully");
                ((Stage) addEventButton.getScene().getWindow()).close();
            } else {
                showError("Event creation failed");
            }
        } catch (Exception e) {
            showError("Event creation failed");
            System.err.println("Error: Event creation failed.");
            e.printStackTrace();
        }
    }

    private boolean validateInput() {
        boolean isValid = true;
        isValid &= validateField(eventNameField, eventNameError, "Event name is required");
        isValid &= validateField(eventDescriptionField, eventDescriptionError, "Event description is required");
        isValid &= validateField(eventTypeComboBox, eventTypeError, "Event type is required");
        isValid &= validateField(eventLocationComboBox, eventLocationError, "Event location is required");
        isValid &= validateField(eventDatePicker, eventDateError, "Event date is required");
        isValid &= validateField(eventCapacityField, eventCapacityError, "Event capacity is required");
        isValid &= validateField(eventPriceField, eventPriceError, "Event price is required");

        if (startTimeField.getText().isEmpty() || endTimeField.getText().isEmpty()) {
            timeError.setText("Time information is required");
            isValid = false;
        } else {
            timeError.setText("");
        }
        return isValid;
    }

    private boolean validateField(Control field, Label errorLabel, String errorMessage) {
        boolean isValid = true;
        if (field instanceof TextField && ((TextField) field).getText().isEmpty()) isValid = false;
        if (field instanceof ComboBox && ((ComboBox<?>) field).getValue() == null) isValid = false;
        if (field instanceof DatePicker && ((DatePicker) field).getValue() == null) isValid = false;

        errorLabel.setText(isValid ? "" : errorMessage);
        return isValid;
    }

    private void showError(String message) {
        addEventError.setText(message);
        System.err.println("Error: " + message);
    }
}