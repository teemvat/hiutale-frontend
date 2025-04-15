package controller.ui;

import app.Main;
import controller.api.CategoryController;
import controller.api.EventController;
import controller.api.LocationController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Category;
import model.Event;
import model.Location;
import utils.FormValidator;

/**
 * Controller class for the "New Event" view.
 * Handles the creation of new events, including input validation,
 * category and location selection, and image upload.
*/
public class NewEventController {

  @FXML private ImageView eventImageView;
  @FXML private TextField titleField;
  @FXML private TextField descriptionField;
  @FXML private TextField capacityField;
  @FXML private TextField startTimeField;
  @FXML private TextField endTimeField;
  @FXML private TextField priceField;
  @FXML private ComboBox<Category> categoriesComboBox;
  @FXML private ComboBox<Location> locationComboBox;
  @FXML private DatePicker startDatePicker;
  @FXML private DatePicker endDatePicker;
  @FXML private Label imageError;
  @FXML private Label titleError;
  @FXML private Label descriptionError;
  @FXML private Label categoriesError;
  @FXML private Label startDateError;
  @FXML private Label endDateError;
  @FXML private Label locationError;
  @FXML private Label capacityError;
  @FXML private Label priceError;
  @FXML private Label addEventError;
  @FXML private Label startTimeError;
  @FXML private Label endTimeError;
  @FXML private Button selectImageButton;
  @FXML private Button addEventButton;
  @FXML private FlowPane categoryFlowPane;

  private File eventImage = new File("/pictures/placeholder_event.jpg");
  private final File placeholderImage = new File("/pictures/placeholder_event.jpg");
  private final ObservableList<Category> selectedCategories = FXCollections.observableArrayList();
  private final List<Category> allCategories = new ArrayList<>();
  private final List<Location> allLocations = new ArrayList<>();
  Logger logger = Logger.getLogger(getClass().getName());

  /**
   * Initializes the controller.
   * Configures input formatters, loads categories and locations,
   * sets up category selection, and initializes the placeholder image.
  */
  @FXML
  private void initialize() {
    configureInputFormatters();
    loadCategories();
    loadLocations();
    setupCategorySelection();
    setComboBoxConverters();
  }

  /**
   * Sets up custom converters for the categories and location ComboBoxes.
   * Converts between objects and their string representations.
  */
  private void setComboBoxConverters() {

    categoriesComboBox.setConverter(new StringConverter<>() {
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

    locationComboBox.setConverter(new StringConverter<>() {
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

  /**
   * Configures input formatters for numeric fields to allow only valid numeric input.
   * Ensures fields like price, capacity, and time accept proper formats.
  */
  private void configureInputFormatters() {
    UnaryOperator<TextFormatter.Change> filter = change -> change.getControlNewText().matches("\\d*(\\.\\d{0,2})?") ? change : null;
    priceField.setTextFormatter(new TextFormatter<>(filter));
    capacityField.setTextFormatter(new TextFormatter<>(filter));
    startTimeField.setTextFormatter(new TextFormatter<>(filter));
    endTimeField.setTextFormatter(new TextFormatter<>(filter));
  }

  /**
   * Loads all available categories from the CategoryController
   * and populates the categories ComboBox.
  */
  private void loadCategories() {
    allCategories.addAll(CategoryController.getAllCategories());
    categoriesComboBox.getItems().addAll(allCategories);
  }

  /**
   * Sets up the category selection mechanism.
   * Adds selected categories to a list and displays them as tags in the UI.
  */
  private void setupCategorySelection() {
    categoriesComboBox.setOnAction(event -> {
      Category selectedCategory = categoriesComboBox.getSelectionModel().getSelectedItem();
      if (selectedCategory != null && !selectedCategories.contains(selectedCategory)) {
        selectedCategories.add(selectedCategory);
        addCategoryTag(selectedCategory);
      }
    });
  }

  /**
   * Adds a tag for a selected category to the UI.
   * Allows the user to remove the category by clicking the "X" button.
   *
   * @param category The category to add as a tag.
  */
  private void addCategoryTag(Category category) {
    Label tag = new Label(category.getName());
    Button removeBtn = new Button("X");
    removeBtn.setOnAction(event -> {
      selectedCategories.remove(category);
      categoryFlowPane.getChildren().removeAll(tag, removeBtn);
    });
    categoryFlowPane.getChildren().addAll(tag, removeBtn);
  }

  /**
   * Retrieves the IDs of all selected categories.
   *
   * @return An array of selected category IDs.
  */
  private String[] getCategoryIds() {
    return selectedCategories.stream()
            .map(Category::getId)
            .toArray(String[]::new);
  }

  /**
   * Loads all available locations from the LocationController
   * and populates the location ComboBox.
  */
  private void loadLocations() {
    allLocations.addAll(LocationController.getAllLocations());
    locationComboBox.getItems().addAll(allLocations);
  }

  /**
   * Retrieves the ID of the selected location.
   *
   * @return The ID of the selected location, or null if no location is selected.
  */
  private String getSelectedLocationId() {
    Location selectedLocation = locationComboBox.getSelectionModel().getSelectedItem();
    return (selectedLocation != null) ? selectedLocation.getId() : null;
  }

  /**
   * Opens a file chooser dialog to allow the user to select an image for the event.
   * Updates the event image view with the selected image.
  */
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
        imageError.setText(Main.getBundle().getString("image.error"));
        logger.info("Error with image selection: " + e.getMessage());
      }
    }
  }

  /**
   * Handles the creation of a new event.
   * Validates user input, collects event details, and sends the data to the EventController.
   * Closes the window upon successful event creation.
  */
  @FXML
  private void addEvent() {
    if (!validateInput()) {
      return;
    }

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
      logger.info("New event created: " + createdEvent);
      ((Stage) addEventButton.getScene().getWindow()).close();
    } catch (Exception e) {
      addEventError.setText(Main.getBundle().getString("event.creation.error"));
    }
  }

  /**
   * Validates user input for all required fields.
   * Displays error messages for invalid or empty fields.
   *
   * @return true if all fields are valid, false otherwise.
  */
  private boolean validateInput() {
    return Stream.of(
        validateField(titleField, titleError),
        validateField(descriptionField, descriptionError),
        validateField(categoriesComboBox, categoriesError),
        validateField(locationComboBox, locationError),
        validateField(startDatePicker, startDateError),
        validateField(endDatePicker, endDateError),
        validateField(startTimeField, startTimeError),
        validateField(endTimeField, endTimeError),
        validateField(capacityField, capacityError),
        validateField(priceField, priceError)
    ).allMatch(valid -> valid);
  }

  private boolean validateField(Control field, Label errorLabel) {
    boolean isValid = !(field instanceof TextField textField && textField.getText().isEmpty()
                     || field instanceof ComboBox<?> comboBox && comboBox.getValue() == null
                     || field instanceof DatePicker datePicker && datePicker.getValue() == null);

    errorLabel.setText(isValid ? "" : Main.getBundle().getString("empty.field"));
    return isValid;
  }
}
