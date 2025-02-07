package controller.ui;

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
    private DatePicker eventDatePicker;

    @FXML
    private ComboBox<String> eventLocationComboBox;

    @FXML
    private ComboBox<String> eventTypeComboBox;

    @FXML
    private TextField eventPriceField;

    @FXML
    private Label eventNameError;

    @FXML
    private Label eventDateError;

    @FXML
    private Label eventLocationError;

    @FXML
    private Label eventTypeError;

    @FXML
    private Label eventPriceError;

    @FXML
    private Button selectImageButton;

    @FXML
    private Button addEventButton;

    @FXML
    private void initialize() {
        // Add a TextFormatter to ensure only numbers can be typed in the price field

        // Vaihtoehto 1
        //eventPriceField.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));

        // Vaihtoehto 2
        configurePriceField();
    }

    private void configurePriceField() {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*(\\.\\d{0,2})?")) { // Vain numerot ja max 2 desimaalia
                return change;
            }
            return null;
        };

        TextFormatter<String> formatter = new TextFormatter<>(filter);
        eventPriceField.setTextFormatter(formatter);
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
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleAddEventAction(ActionEvent event) {
        String eventName = eventNameField.getText();
        LocalDate eventDate = eventDatePicker.getValue();
        String eventLocation = eventLocationComboBox.getValue();
        String eventType = eventTypeComboBox.getValue();
        String eventPrice = eventPriceField.getText();

        if (eventName.isEmpty()) {
            eventNameError.setText("Event name is required");
        } else {
            eventNameError.setText("");
        }

        if (eventDate == null) {
            eventDateError.setText("Event date is required");
        } else {
            eventDateError.setText("");
        }

        if (eventLocation == null || eventLocationComboBox.getValue().isEmpty()) {
            eventLocationError.setText("Event location is required");
        } else {
            eventLocationError.setText("");
        }

        if (eventType == null || eventTypeComboBox.getValue().isEmpty()) {
            eventTypeError.setText("Event type is required");
        } else {
            eventTypeError.setText("");
        }

        if (eventPrice.isEmpty()) {
            eventPriceError.setText("Event price is required");
        } else {
            eventPriceError.setText("");
        }

        if(!eventName.isEmpty() && eventDate != null && eventLocation != null && eventType != null && !eventPrice.isEmpty()) {
            // Placeholder function for adding an event
            System.out.println("Add event button clicked");
            // Close the new event window
            Stage stage = (Stage) addEventButton.getScene().getWindow();
            stage.close();
        }
    }
}
