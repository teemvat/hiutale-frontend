package controller.ui;

import controller.api.CategoryController;
import controller.api.EventController;
import controller.api.LocationController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Category;
import model.Event;
import model.Location;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class NewEventController {

    @FXML private ImageView eventImageView;
    @FXML private TextField titleField, descriptionField, capacityField, startTimeField, endTimeField, priceField;
    @FXML private ComboBox<String> categoriesComboBox, locationComboBox;
    @FXML private DatePicker startDatePicker, endDatePicker;
    @FXML private Label imageError, titleError, descriptionError, categoriesError, startDateError, endDateError, locationError, capacityError, priceError, addEventError, startTimeError, endTimeError;
    @FXML private Button selectImageButton, addEventButton;
    @FXML private FlowPane categoryFlowPane;

    private File eventImage, placeholderImage;
    private final ObservableList<String> selectedCategories = FXCollections.observableArrayList();
    private final List<Category> allCategories = new ArrayList<>();

    @FXML
    private void initialize() {
        configureInputFormatters();
        loadCategories();
        loadLocations();
        eventImage = placeholderImage = new File("/pictures/placeholder_event.jpg");
        setupCategorySelection();
    }

    private void configureInputFormatters() {
        UnaryOperator<TextFormatter.Change> filter = change -> change.getControlNewText().matches("\\d*(\\.\\d{0,2})?") ? change : null;
        priceField.setTextFormatter(new TextFormatter<>(filter));
        capacityField.setTextFormatter(new TextFormatter<>(filter));
        startTimeField.setTextFormatter(new TextFormatter<>(filter));
        endTimeField.setTextFormatter(new TextFormatter<>(filter));
    }

    private void loadCategories() {
        allCategories.addAll(CategoryController.getAllCategories());
        categoriesComboBox.getItems().addAll(allCategories.stream().map(Category::getName).toList());
    }

    private void setupCategorySelection() {
        categoriesComboBox.setOnAction(event -> {
            String selectedCategory = categoriesComboBox.getValue();
            if (selectedCategory != null && !selectedCategories.contains(selectedCategory)) {
                selectedCategories.add(selectedCategory);
            }
        });
    }

    private void addCategoryTag(String category) {
        Label tag = new Label(category);
        Button removeBtn = new Button("X");
        removeBtn.setOnAction(event -> {
            selectedCategories.remove(category);
            categoryFlowPane.getChildren().removeAll(tag, removeBtn);
        });
        //tag.setStyle("-fx-background-color: lightblue; -fx-padding: 5px");
        categoryFlowPane.getChildren().addAll(tag, removeBtn);
    }

    private String[] getCategoryIds() {
        return selectedCategories.stream()
                .map(name -> allCategories.stream()
                        .filter(c -> c.getName().equals(name))
                        .findFirst()
                        .orElse(null))
                .filter(Objects::nonNull)
                .map(c -> String.valueOf(c.getId()))
                .toArray(String[]::new);
    }

    private void loadLocations() {
        List<Location> locations = LocationController.getAllLocations();
        locationComboBox.getItems().addAll(locations.stream().map(Location::getName).toList());
    }

    @FXML
    private void selectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            eventImage = selectedFile;
            try {
                eventImageView.setImage(new Image(new FileInputStream(eventImage)));
            } catch (FileNotFoundException e) {
                eventImage = placeholderImage;
                imageError.setText("Image not found");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void addEvent(ActionEvent event) {
        if (!validateInput()) return;

        try {
            Event createdEvent = EventController.createEvent(
                    //eventImage,
                    titleField.getText(),
                    descriptionField.getText(),
                    locationComboBox.getValue(),
                    capacityField.getText(),
                    getCategoryIds()[0],    // TODO poista indeksi sitten kun api on päivitetty
                    startDatePicker.getValue().toString(),
                    endDatePicker.getValue().toString(),
                    startTimeField.getText(),
                    endTimeField.getText(),
                    Double.parseDouble(priceField.getText())
            );

            if (createdEvent != null) {
                System.out.println("Event created successfully");
                ((Stage) addEventButton.getScene().getWindow()).close();
            } else {
                addEventError.setText("Event creation failed.");
            }
        } catch (Exception e) {
            addEventError.setText("Error: Event creation failed.");
        }
    }

    private boolean validateInput() {
        boolean isValid = true;
        isValid &= validateField(titleField, titleError, "Event name is required");
        isValid &= validateField(descriptionField, descriptionError, "Event description is required");
        isValid &= validateField(categoriesComboBox, categoriesError, "Event type is required");
        isValid &= validateField(locationComboBox, locationError, "Event location is required");
        isValid &= validateField(startDatePicker, startDateError, "Start date is required");
        isValid &= validateField(endDatePicker, endDateError, "End date is required");
        isValid &= validateField(startTimeField, startTimeError, "Start time is required");
        isValid &= validateField(endTimeField, endTimeError, "End time is required");
        isValid &= validateField(capacityField, capacityError, "Event capacity is required");
        isValid &= validateField(priceField, priceError, "Event price is required");
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
}