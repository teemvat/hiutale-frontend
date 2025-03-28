package controller.ui;

import app.Main;
import controller.api.CategoryController;
import controller.api.EventController;
import controller.api.LocationController;
import controller.api.UserController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Category;
import model.Event;
import model.Location;
import model.User;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.UnaryOperator;

import java.io.IOException;
import java.util.stream.Collectors;

public class HomeController {

    @FXML private TextField searchField, minPriceField, maxPriceField;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<User> organizerComboBox;
    @FXML private ComboBox<Location> locationComboBox;
    @FXML private ComboBox<Category> eventTypeComboBox;
    @FXML private Button searchButton, resetButton, profileButton, addEventButton;
    @FXML private ChoiceBox<String> sortChoiceBox;
    @FXML private FlowPane listViewPane;
    @FXML private Label monDateLabel, tueDateLabel, wedDateLabel, thuDateLabel, friDateLabel, satDateLabel, sunDateLabel;
    @FXML private Label[] dayLabels;
    @FXML private VBox monVBox, tueVBox, wedVBox, thuVBox, friVBox, satVBox, sunVBox;
    @FXML private VBox[] dayBoxes;

    private List<Event> cachedEvents = new ArrayList<>();

    private enum SortType {
        ALPHABETICAL, DATE
    }

    private static final Map<String, SortType> SORT_MAP = Map.of(
            Main.bundle.getString("sort.alphabetical"), SortType.ALPHABETICAL,
            Main.bundle.getString("sort.date"), SortType.DATE
    );

    @FXML
    private void initialize() {
        List<Event> allEvents = EventController.getAllEvents();
        cachedEvents = Optional.ofNullable(allEvents).orElse(new ArrayList<>());

        dayLabels = new Label[]{monDateLabel, tueDateLabel, wedDateLabel, thuDateLabel, friDateLabel, satDateLabel, sunDateLabel};
        dayBoxes = new VBox[]{monVBox, tueVBox, wedVBox, thuVBox, friVBox, satVBox, sunVBox};
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
        eventTypeComboBox.setConverter(new StringConverter<Category>() {
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

        organizerComboBox.setConverter(new StringConverter<User>() {
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

    private void configurePriceFields() {
        // Add a TextFormatter to ensure only numbers can be typed in the price fields
        UnaryOperator<TextFormatter.Change> filter = change ->
                change.getControlNewText().matches("\\d*(\\.\\d{0,2})?") ? change : null;

        minPriceField.setTextFormatter(new TextFormatter<>(filter));
        maxPriceField.setTextFormatter(new TextFormatter<>(filter));
    }

    private void populateEventTypeComboBox() {
        List<Category> categories = CategoryController.getAllCategories();
//        List<String> categoryNames = categories.stream()
//                .map(Category::getName)
//                .collect(Collectors.toList());
        eventTypeComboBox.getItems().clear();
        eventTypeComboBox.getItems().addAll(categories);
    }

    private void populateLocationComboBox() {
        List<Location> locations = LocationController.getAllLocations();
        locationComboBox.getItems().clear();
        locationComboBox.getItems().addAll(locations);
    }

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

    private LocalDate getStartOfWeek(LocalDate date) {
        if (date == null) {
            return LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        }
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    private void updateCalendar(LocalDate startOfWeek) {
        for (int i = 0; i < 7; i++) {
            dayLabels[i].setText(startOfWeek.plusDays(i).toString());
        }

        loadEventsForWeek(startOfWeek);
    }

    private void loadEventsForWeek(LocalDate startOfWeek) {
        clearCalendar();
        if (!cachedEvents.isEmpty()) {
            cachedEvents.forEach(event -> addEventToCalendar(event, startOfWeek));
        }
    }

    private void addEventToCalendar(Event event, LocalDate startOfWeek) {
        try {
            String eventDateStr = event.getDate();
            if (eventDateStr == null || eventDateStr.isEmpty()) {
                throw new DateTimeParseException("Date is null or empty", eventDateStr, 0);
            }
            LocalDate eventDate = LocalDate.parse(eventDateStr);
            int dayOffset = (int) ChronoUnit.DAYS.between(startOfWeek, eventDate);

            if (dayOffset >= 0 && dayOffset < 7) {
                dayBoxes[dayOffset].getChildren().add(loadFXML("/fxml/eventbox.fxml", event));
            }
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format for event: " + event.getTitle());
        }
    }

    private void loadEventCards() {
        listViewPane.getChildren().clear();
        if (cachedEvents.isEmpty()) {
            listViewPane.getChildren().add(new Label(Main.bundle.getString("no.events")));
        } else {
            cachedEvents.forEach(event -> listViewPane.getChildren().add(loadFXML("/fxml/eventcard.fxml", event)));
        }
    }

    private Parent loadFXML(String resource, Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resource), Main.bundle);
            Parent root = loader.load();

            if (resource.equals("/fxml/eventcard.fxml")) {
                EventCardController controller = loader.getController();
                controller.setEventData(event);
            } else if (resource.equals("/fxml/eventbox.fxml")) {
                EventBoxController controller = loader.getController();
                controller.setEventData(event);
            }

            return root;
        } catch (IOException e) {
            e.printStackTrace();
            return new Label("Error loading event");
        }
    }

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

    private void updateListView(List<Event> events) {
        listViewPane.getChildren().clear();
        if (events.isEmpty()) {
            listViewPane.getChildren().add(new Label(Main.bundle.getString("no.events")));
        } else {
            events.forEach(event -> listViewPane.getChildren().add(loadFXML("/fxml/eventcard.fxml", event)));
        }
    }

    private void updateCalendarView(List<Event> events) {
        clearCalendar();
        if (!events.isEmpty()) {
            events.forEach(event -> addEventToCalendar(event, getStartOfWeek(datePicker.getValue() != null ? datePicker.getValue() : LocalDate.now())));
        }
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

        // Reload all events
        updateListView(cachedEvents);
        updateCalendarView(cachedEvents);
    }

    @FXML
    private void handleProfileAction() {
        openNewWindow("/fxml/profile.fxml", Main.bundle.getString("profile.title"), profileButton);
    }

    @FXML
    private void handleAddEventAction() {
        openNewWindow("/fxml/newevent.fxml", Main.bundle.getString("new.event.title"), addEventButton);
    }

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
            e.printStackTrace();
        }
    }

    private void sortEvents(SortType sortMethod) {
        cachedEvents.sort(switch (sortMethod) {
            case ALPHABETICAL -> Comparator.comparing(Event::getTitle);
            case DATE -> Comparator.comparing(Event::getDate);
        });
        updateListView(cachedEvents);
    }

    @FXML
    private void handleSortAction() {
        Optional.ofNullable(SORT_MAP.get(sortChoiceBox.getValue())).ifPresent(this::sortEvents);
    }

    private void clearCalendar() {
        Arrays.stream(dayBoxes).forEach(vbox -> vbox.getChildren().clear());
    }

    private void updateEventList(){
        cachedEvents = EventController.getAllEvents();
        updateListView(cachedEvents);
        updateCalendarView(cachedEvents);
    }
}
