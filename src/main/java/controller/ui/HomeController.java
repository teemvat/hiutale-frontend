package controller.ui;

import controller.api.CategoryController;
import controller.api.EventController;
import controller.api.LocationController;
import controller.api.UserController;
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
import model.Category;
import model.Event;
import model.Location;
import model.User;
import utils.FilterCriteria;

import java.time.DayOfWeek;
import java.time.LocalDate;
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
    @FXML private ComboBox<String> eventTypeComboBox, locationComboBox, organizerComboBox;
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
            "Aakkosjärjestys", SortType.ALPHABETICAL,
            "Päivämäärän mukaan", SortType.DATE
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
        List<String> categoryNames = categories.stream()
                .map(Category::getName)
                .collect(Collectors.toList());
        eventTypeComboBox.getItems().addAll(categoryNames);
    }

    private void populateLocationComboBox() {
        List<Location> locations = LocationController.getAllLocations();
        List<String> locationNames = locations.stream()
                .map(Location::getName)
                .collect(Collectors.toList());
        locationComboBox.getItems().addAll(locationNames);
    }

    private void populateOrganizerComboBox() {
        List<Event> allEvents = EventController.getAllEvents();
        Set<String> organizerIds = allEvents.stream()
                .map(Event::getOrganizerId)
                .collect(Collectors.toSet());

        List<String> organizerNames = organizerIds.stream()
                .map(id -> UserController.getUser(Integer.parseInt(id)).getUsername())
                .collect(Collectors.toList());

        organizerComboBox.getItems().addAll(organizerNames);
    }

    private LocalDate getStartOfWeek(LocalDate date) {
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
        if (cachedEvents.isEmpty()) {
            listViewPane.getChildren().add(new Label("No events found for this week"));
        } else {
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
            listViewPane.getChildren().add(new Label("No events found"));
        } else {
            cachedEvents.forEach(event -> listViewPane.getChildren().add(loadFXML("/fxml/eventcard.fxml", event)));
        }
    }

    private Parent loadFXML(String resource, Event event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resource));
            Parent node = fxmlLoader.load();

            if (resource.equals("/fxml/eventcard.fxml")) {
                EventCardController controller = fxmlLoader.getController();
                controller.setEventData(event);
            } else if (resource.equals("/fxml/eventbox.fxml")) {
                EventBoxController controller = fxmlLoader.getController();
                controller.setEventData(event);
            }

            return node;
        } catch (IOException e) {
            e.printStackTrace();
            return new Label("Error loading event");
        }
    }

    @FXML
    private void handleSearchAction(ActionEvent event) {
        List<Event> filteredEvents = EventController.searchEvents(
                searchField.getText(),
                datePicker.getValue() != null ? datePicker.getValue().toString() : "",
                eventTypeComboBox.getValue(),
                locationComboBox.getValue(),
                organizerComboBox.getValue(),
                minPriceField.getText(),
                maxPriceField.getText()
                );
        updateListView(filteredEvents);
    }

    private void updateListView(List<Event> events) {
        listViewPane.getChildren().clear();
        if (events.isEmpty()) {
            listViewPane.getChildren().add(new Label("No events found"));
        } else {
            events.forEach(event -> listViewPane.getChildren().add(loadFXML("/fxml/eventcard.fxml", event)));
        }
    }

    @FXML
    private void handleResetAction(ActionEvent event) {
        // Reset all fields
        searchField.clear();
        datePicker.setValue(LocalDate.now());
        eventTypeComboBox.getSelectionModel().clearSelection();
        locationComboBox.getSelectionModel().clearSelection();
        organizerComboBox.getSelectionModel().clearSelection();
        minPriceField.clear();
        maxPriceField.clear();
    }

    @FXML
    private void handleProfileAction(ActionEvent event) {
        openNewWindow("/fxml/profile.fxml", "User Profile", profileButton);
    }

    @FXML
    private void handleAddEventAction(ActionEvent event) {
        openNewWindow("/fxml/newevent.fxml", "Add New Event", addEventButton);
    }

    private void openNewWindow(String resource, String title, Button ownerButton) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(resource));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(ownerButton.getScene().getWindow());
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
    private void handleSortAction(ActionEvent event) {
        Optional.ofNullable(SORT_MAP.get(sortChoiceBox.getValue())).ifPresent(this::sortEvents);
    }

    private void clearCalendar() {
        Arrays.stream(dayBoxes).forEach(vbox -> vbox.getChildren().clear());
    }
}
