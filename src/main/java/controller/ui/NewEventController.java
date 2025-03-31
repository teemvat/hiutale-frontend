package controller.ui;

import app.Main;
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
import javafx.util.StringConverter;
import model.Category;
import model.Event;
import model.Location;
import model.User;

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
    @FXML private ComboBox<Category> categoriesComboBox;
    @FXML private ComboBox<Location> locationComboBox;
    @FXML private DatePicker startDatePicker, endDatePicker;
    @FXML private Label imageError, titleError, descriptionError, categoriesError, startDateError, endDateError, locationError, capacityError, priceError, addEventError, startTimeError, endTimeError;
    @FXML private Button selectImageButton, addEventButton;
    @FXML private FlowPane categoryFlowPane;

    private File eventImage, placeholderImage;
    private final ObservableList<Category> selectedCategories = FXCollections.observableArrayList();
    private final List<Category> allCategories = new ArrayList<>();
    private final List<Location> allLocations = new ArrayList<>();

    @FXML
    private void initialize() {
        configureInputFormatters();
        loadCategories();
        loadLocations();
        eventImage = placeholderImage = new File("/pictures/placeholder_event.jpg");
        setupCategorySelection();

        setComboBoxConverters();

    }

    private void setComboBoxConverters() {
        categoriesComboBox.setConverter(new StringConverter<Category>() {
            @Override
            public String toString(Category category) {
                return (category == null) ? "" : category.getName();
            }

            @Override
            public Category fromString(String string) {
                return categoriesComboBox.getItems().stream()
                        .filter(category -> category.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        locationComboBox.setConverter(new StringConverter<Location>() {
            @Override
            public String toString(Location location) {
                return (location == null) ? "" : location.getName();
            }

            @Override
            public Location fromString(String string) {
                return locationComboBox.getItems().stream()
                        .filter(location -> location.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
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
        categoriesComboBox.getItems().addAll(allCategories);
    }

    private void setupCategorySelection() {
        categoriesComboBox.setOnAction(event -> {
            Category selectedCategory = categoriesComboBox.getSelectionModel().getSelectedItem();
            if (selectedCategory != null && !selectedCategories.contains(selectedCategory)) {
                selectedCategories.add(selectedCategory);
                addCategoryTag(selectedCategory);
            }
        });
    }

    private void addCategoryTag(Category category) {
        Label tag = new Label(category.getName());
        Button removeBtn = new Button("X");
        removeBtn.setOnAction(event -> {
            selectedCategories.remove(category);
            categoryFlowPane.getChildren().removeAll(tag, removeBtn);
        });
        categoryFlowPane.getChildren().addAll(tag, removeBtn);
    }

    private String[] getCategoryIds() {
        return selectedCategories.stream()
                .map(Category::getId)
                .toArray(String[]::new);
    }

    private void loadLocations() {
        allLocations.addAll(LocationController.getAllLocations());
        locationComboBox.getItems().addAll(allLocations);
    }

    private String getSelectedLocationId() {
        Location selectedLocation = locationComboBox.getSelectionModel().getSelectedItem();
        return (selectedLocation != null) ? selectedLocation.getId() : null;    }

    @FXML
    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            eventImage = selectedFile;
            try {
                eventImageView.setImage(new Image(new FileInputStream(eventImage)));
            } catch (FileNotFoundException e) {
                eventImage = placeholderImage;
                imageError.setText(Main.bundle.getString("image.error"));
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void addEvent() {
        if (!validateInput()) return;

        try {
            Event createdEvent = EventController.createEvent(
                    titleField.getText(),
                    descriptionField.getText(),
                    getSelectedLocationId(),
                    capacityField.getText(),
                    getCategoryIds(),
                    startDatePicker.getValue().toString(),
                    endDatePicker.getValue().toString(),
                    startTimeField.getText(),
                    endTimeField.getText(),
                    Double.parseDouble(priceField.getText()),
                    eventImage
            );
            System.out.println("NewEventController, created event: " + createdEvent);

            if (createdEvent != null) {
                System.out.println("Event created successfully");
                ((Stage) addEventButton.getScene().getWindow()).close();
            } else {
                addEventError.setText(Main.bundle.getString("event.creation.error"));
            }
        } catch (Exception e) {
            addEventError.setText(Main.bundle.getString("event.creation.error"));
            System.out.println("Title: " + titleField.getText() + " Description: " + descriptionField.getText() + " Location: " + getSelectedLocationId() + " Capacity: " + capacityField.getText() + " Categories: " + getCategoryIds() + " Start date: " + startDatePicker.getValue().toString() + " End date: " + endDatePicker.getValue().toString() + " Start time: " + startTimeField.getText() + " End time: " + endTimeField.getText() + " Price: " + priceField.getText());
        }
    }

    private boolean validateInput() {
        boolean isValid = true;
        isValid &= validateField(titleField, titleError, Main.bundle.getString("empty.field"));
        isValid &= validateField(descriptionField, descriptionError, Main.bundle.getString("empty.field"));
        isValid &= validateField(categoriesComboBox, categoriesError, Main.bundle.getString("empty.field"));
        isValid &= validateField(locationComboBox, locationError, Main.bundle.getString("empty.field"));
        isValid &= validateField(startDatePicker, startDateError, Main.bundle.getString("empty.field"));
        isValid &= validateField(endDatePicker, endDateError, Main.bundle.getString("empty.field"));
        isValid &= validateField(startTimeField, startTimeError, Main.bundle.getString("empty.field"));
        isValid &= validateField(endTimeField, endTimeError, Main.bundle.getString("empty.field"));
        isValid &= validateField(capacityField, capacityError, Main.bundle.getString("empty.field"));
        isValid &= validateField(priceField, priceError, Main.bundle.getString("empty.field"));
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