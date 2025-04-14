package controller.ui;

import app.Main;
import controller.api.CategoryController;
import controller.api.EventController;
import controller.api.LocationController;
import controller.api.UserController;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Category;
import model.Event;
import model.Location;
import model.User;

/**
 * Controller for managing the Home view.
 * This class handles the display of events, filtering, sorting, and user interactions
 * such as searching, resetting filters, and navigating to other views.
*/
public class HomeController {

  @FXML private TextField searchField;
  @FXML private TextField minPriceField;
  @FXML private TextField maxPriceField;
  @FXML private DatePicker datePicker;
  @FXML private ComboBox<User> organizerComboBox;
  @FXML private ComboBox<Location> locationComboBox;
  @FXML private ComboBox<Category> eventTypeComboBox;
  @FXML private Button searchButton;
  @FXML private Button resetButton;
  @FXML private Button profileButton;
  @FXML private Button addEventButton;
  @FXML private ChoiceBox<String> sortChoiceBox;
  @FXML private FlowPane listViewPane;
  @FXML private Label monDateLabel;
  @FXML private Label tueDateLabel;
  @FXML private Label wedDateLabel;
  @FXML private Label thuDateLabel;
  @FXML private Label friDateLabel;
  @FXML private Label satDateLabel;
  @FXML private Label sunDateLabel;
  @FXML private Label[] dayLabels;
  @FXML private VBox monBox;
  @FXML private VBox tueBox;
  @FXML private VBox wedBox;
  @FXML private VBox thuBox;
  @FXML private VBox friBox;
  @FXML private VBox satBox;
  @FXML private VBox sunBox;
  @FXML private VBox[] dayBoxes;

  private List<Event> cachedEvents = new ArrayList<>();

  private enum SortType {
    ALPHABETICAL, DATE
  }

  private static final Map<String, SortType> SORT_MAP = Map.of(
          Main.bundle.getString("sort.alphabetical"), SortType.ALPHABETICAL,
          Main.bundle.getString("sort.date"), SortType.DATE
  );

  /**
   * Initializes the Home view by setting up the calendar, loading events, and configuring UI components.
  */
  @FXML
  private void initialize() {
    List<Event> allEvents = EventController.getAllEvents();
    cachedEvents = Optional.ofNullable(allEvents).orElse(new ArrayList<>());

    dayLabels = new Label[]{monDateLabel, tueDateLabel, wedDateLabel, thuDateLabel, friDateLabel, satDateLabel, sunDateLabel};
    dayBoxes = new VBox[]{monBox, tueBox, wedBox, thuBox, friBox, satBox, sunBox};
    LocalDate today = LocalDate.now();
    updateCalendar(getStartOfWeek(today));
    datePicker.setOnAction(event -> updateCalendar(getStartOfWeek(datePicker.getValue())));

    configurePriceFields();

    loadEventCards();
    populateEventTypeComboBox();
    populateLocationComboBox();
    populateOrganizerComboBox();

    setComboBoxConverters();

    // Set sorting options
    sortChoiceBox.setItems(FXCollections.observableArrayList(
            Main.bundle.getString("sort.alphabetical"),
            Main.bundle.getString("sort.date")
    ));

    // Set default sorting option
    sortChoiceBox.setValue(Main.bundle.getString("sort.alphabetical"));
  }

  private void setComboBoxConverters() {
    eventTypeComboBox.setConverter(new StringConverter<>() {
      @Override
      public String toString(Category category) {
        return (category == null) ? "" : category.getName();
      }

      @Override
      public Category fromString(String string) {
        return eventTypeComboBox.getItems().stream()
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

    organizerComboBox.setConverter(new StringConverter<>() {
      @Override
      public String toString(User organizer) {
        return (organizer == null) ? "" : organizer.getUsername();
      }

      @Override
      public User fromString(String string) {
        return organizerComboBox.getItems().stream()
                .filter(organizer -> organizer.getUsername().equals(string))
                .findFirst()
                .orElse(null);
        }
    });
  }

  /**
   * Configures the price fields to accept only numeric input with up to two decimal places.
  */
  private void configurePriceFields() {
    UnaryOperator<TextFormatter.Change> filter = change ->
            change.getControlNewText().matches("\\d*(\\.\\d{0,2})?") ? change : null;

    minPriceField.setTextFormatter(new TextFormatter<>(filter));
    maxPriceField.setTextFormatter(new TextFormatter<>(filter));
  }

  /**
   * Populates the event type combo box with all available categories.
  */
  private void populateEventTypeComboBox() {
    List<Category> categories = CategoryController.getAllCategories();
    eventTypeComboBox.getItems().clear();
    eventTypeComboBox.getItems().addAll(categories);
  }

  /**
   * Populates the location combo box with all available locations.
  */
  private void populateLocationComboBox() {
    List<Location> locations = LocationController.getAllLocations();
    locationComboBox.getItems().clear();
    locationComboBox.getItems().addAll(locations);
  }

  /**
   * Populates the organizer combo box with all unique organizers from the events.
  */
  private void populateOrganizerComboBox() {
    List<Event> allEvents = EventController.getAllEvents();
    Set<String> organizerIds = allEvents.stream()
            .map(Event::getOrganizerId)
            .collect(Collectors.toSet());

    List<User> organizers = new ArrayList<>();
    for (String id : organizerIds) {
      User user = UserController.getUser(Integer.parseInt(id));
      if (user != null) {
        organizers.add(user);
      }
    }

    // Clear previous data to avoid conflicts
    organizerComboBox.getItems().clear();
    organizerComboBox.getItems().addAll(organizers);
  }

  /**
   * Gets the start of the week (Monday) for the given date.
   *
   * @param date The date for which to find the start of the week.
   * @return The LocalDate representing the start of the week.
  */
  private LocalDate getStartOfWeek(LocalDate date) {
    if (date == null) {
      return LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }
    return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
  }

  /**
   * Updates the calendar view to display the week starting from the given date.
   *
   * @param startOfWeek The start date of the week to display.
  */
  private void updateCalendar(LocalDate startOfWeek) {
    for (int i = 0; i < 7; i++) {
      dayLabels[i].setText(startOfWeek.plusDays(i).toString());
    }

    loadEventsForWeek(startOfWeek);
  }

  /**
   * Loads events for the week starting from the given date and displays them in the calendar.
   *
   * @param startOfWeek The start date of the week to load events for.
  */
  private void loadEventsForWeek(LocalDate startOfWeek) {
    clearCalendar();
    if (!cachedEvents.isEmpty()) {
      cachedEvents.forEach(event -> addEventToCalendar(event, startOfWeek));
    }
  }

  /**
   * Adds an event to the calendar on the appropriate day based on its date.
   *
   * @param event The event to add to the calendar.
   * @param startOfWeek The start date of the week to determine the day offset.
  */
  private void addEventToCalendar(Event event, LocalDate startOfWeek) {
    try {
      String eventDateStr = event.getDate();
      if (eventDateStr == null || eventDateStr.isEmpty()) {
        throw new DateTimeParseException("Date is null or empty", eventDateStr, 0);
      }
      LocalDate eventDate = LocalDate.parse(eventDateStr);
      int dayOffset = (int) ChronoUnit.DAYS.between(startOfWeek, eventDate);

      if (dayOffset >= 0 && dayOffset < 7) {
        dayBoxes[dayOffset].getChildren().add(loadFxml("/fxml/event_box.fxml", event));
      }
    } catch (DateTimeParseException e) {
      System.err.println("Invalid date format for event: " + event.getTitle());
    }
  }

  /**
   * Loads event cards into the list view pane.
  */
  private void loadEventCards() {
    listViewPane.getChildren().clear();
    if (cachedEvents.isEmpty()) {
      listViewPane.getChildren().add(new Label(Main.bundle.getString("no.events")));
    } else {
      cachedEvents.forEach(event -> listViewPane.getChildren().add(loadFxml("/fxml/event_card.fxml", event)));
    }
  }

  /**
   * Loads an FXML file and sets the event data for the corresponding controller.
   *
   * @param resource The path to the FXML file.
   * @param event The event data to set in the controller.
   * @return The loaded Parent node.
  */
  private Parent loadFxml(String resource, Event event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(resource), Main.bundle);
      Parent root = loader.load();

      if (resource.equals("/fxml/event_card.fxml")) {
        EventCardController controller = loader.getController();
        controller.setEventData(event);
      } else if (resource.equals("/fxml/event_box.fxml")) {
        EventBoxController controller = loader.getController();
        controller.setEventData(event);
      }

      return root;
    } catch (IOException e) {
      System.err.println("Error loading new FXML file " + e.getMessage());
      e.printStackTrace(System.err);
      return new Label("Error loading event");
    }
  }

  /**
   * Handles the search action by filtering events based on the selected criteria.
  */
  @FXML
  private void handleSearchAction() {
    Category selectedCategory = eventTypeComboBox.getSelectionModel().getSelectedItem();
    System.out.println("Selected category: " + selectedCategory);
    Location selectedLocation = locationComboBox.getSelectionModel().getSelectedItem();
    User selectedOrganizer = organizerComboBox.getSelectionModel().getSelectedItem();

    String categoryId = (selectedCategory != null) ? selectedCategory.getId() : null;
    String locationId = (selectedLocation != null) ? selectedLocation.getId() : null;
    String organizerId = (selectedOrganizer != null) ? selectedOrganizer.getIdString() : null;

    List<Event> filteredEvents = EventController.searchEvents(
            searchField.getText(),
            categoryId,
            datePicker.getValue() != null ? datePicker.getValue().toString() : "",
            locationId,
            minPriceField.getText(),
            maxPriceField.getText(),
            organizerId
    );

    updateListView(filteredEvents);
    updateCalendarView(filteredEvents);
  }

  /**
   * Updates the list view with the given list of events.
   *
   * @param events The list of events to display in the list view.
  */
  private void updateListView(List<Event> events) {
    listViewPane.getChildren().clear();
    if (events.isEmpty()) {
      listViewPane.getChildren().add(new Label(Main.bundle.getString("no.events")));
    } else {
      events.forEach(event -> listViewPane.getChildren().add(loadFxml("/fxml/event_card.fxml", event)));
    }
  }

  /**
   * Updates the calendar view with the given list of events.
   *
   * @param events The list of events to display in the calendar.
  */
  private void updateCalendarView(List<Event> events) {
    clearCalendar();
    if (!events.isEmpty()) {
      events.forEach(event -> addEventToCalendar(event, getStartOfWeek(datePicker.getValue() != null ? datePicker.getValue() : LocalDate.now())));
    }
  }

  /**
   * Handles the reset action by clearing all filters and reloading all events.
  */
  @FXML
  private void handleResetAction() {
    searchField.clear();
    datePicker.setValue(null);
    eventTypeComboBox.getSelectionModel().clearSelection();
    locationComboBox.getSelectionModel().clearSelection();
    organizerComboBox.getSelectionModel().clearSelection();
    minPriceField.clear();
    maxPriceField.clear();

    updateListView(cachedEvents);
    updateCalendarView(cachedEvents);
  }

  /**
   * Handles the profile button action by opening the profile view.
  */
  @FXML
  private void handleProfileAction() {
    openNewWindow("/fxml/profile.fxml", Main.bundle.getString("profile.title"), profileButton);
  }

  /**
   * Handles the add event button action by opening the new event creation view.
  */
  @FXML
  private void handleAddEventAction() {
    openNewWindow("/fxml/new_event.fxml", Main.bundle.getString("new.event.title"), addEventButton);
  }

  /**
   * Opens a new window with the specified FXML resource and title.
   *
   * @param resource The path to the FXML file.
   * @param title The title of the new window.
   * @param ownerButton The button that owns the new window.
  */
  private void openNewWindow(String resource, String title, Button ownerButton) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(resource), Main.bundle);
      Parent root = loader.load();
      Stage stage = new Stage();
      stage.setTitle(title);
      stage.setScene(new Scene(root));
      stage.initModality(Modality.WINDOW_MODAL);
      stage.initOwner(ownerButton.getScene().getWindow());
      stage.setOnHiding(event -> updateEventList());
      stage.showAndWait();
    } catch (IOException e) {
      System.err.println("Error opening new window " + e.getMessage());
      e.printStackTrace(System.err);
    }
  }

  /**
   * Sorts the cached events based on the selected sorting method.
   *
   * @param sortMethod The sorting method to apply.
  */
  private void sortEvents(SortType sortMethod) {
    cachedEvents.sort(switch (sortMethod) {
      case ALPHABETICAL -> Comparator.comparing(Event::getTitle);
      case DATE -> Comparator.comparing(Event::getDate);
    });
    updateListView(cachedEvents);
  }

  /**
   * Handles the sort action by applying the selected sorting method.
  */
  @FXML
  private void handleSortAction() {
    Optional.ofNullable(SORT_MAP.get(sortChoiceBox.getValue())).ifPresent(this::sortEvents);
  }

  /**
   * Clears all events from the calendar view.
  */
  private void clearCalendar() {
    Arrays.stream(dayBoxes).forEach(vbox -> vbox.getChildren().clear());
  }

  /**
   * Updates the event list by reloading all events and updating the views.
  */
  private void updateEventList() {
    cachedEvents = EventController.getAllEvents();
    updateListView(cachedEvents);
    updateCalendarView(cachedEvents);
  }
}
