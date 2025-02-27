package controller.ui;

import controller.api.EventController;
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
import model.Event;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.function.UnaryOperator;

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
    private TextField minPriceField;

    @FXML
    private TextField maxPriceField;

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
    private FlowPane listViewPane;

    @FXML
    private Label monDateLabel;

    @FXML
    private Label tueDateLabel;

    @FXML
    private Label wedDateLabel;

    @FXML
    private Label thuDateLabel;

    @FXML
    private Label friDateLabel;

    @FXML
    private Label satDateLabel;

    @FXML
    private Label sunDateLabel;

    @FXML
    private VBox monVBox;

    @FXML
    private VBox tueVBox;

    @FXML
    private VBox wedVBox;

    @FXML
    private VBox thuVBox;

    @FXML
    private VBox friVBox;

    @FXML
    private VBox satVBox;

    @FXML
    private VBox sunVBox;

    @FXML
    private void initialize() {
        // TODO: tutki onko tämä alustus tarpeellinen
        // Initialize ChoiceBox items
        //sortChoiceBox.setItems(FXCollections.observableArrayList("Aakkosjärjestys", "Päivämäärän mukaan"));

        // Add a TextFormatter to ensure only numbers can be typed in the price fields
        configurePriceFields();

        // Load events for the calendar view
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = getStartOfWeek(today);
        updateCalendarLabels(startOfWeek);
        loadEventsForWeek(startOfWeek);

        // Load events for the list view
        loadEventCards();

        // Add listener to DatePicker
        datePicker.setOnAction(this::handleDatePickerAction);
    }

    private void configurePriceFields() {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*(\\.\\d{0,2})?")) { // Vain numerot ja max 2 desimaalia
                return change;
            }
            return null;
        };
        TextFormatter<String> minFormatter = new TextFormatter<>(filter);
        TextFormatter<String> maxFormatter = new TextFormatter<>(filter);
        minPriceField.setTextFormatter(minFormatter);
        maxPriceField.setTextFormatter(maxFormatter);
    }

    private LocalDate getStartOfWeek(LocalDate date) {
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    private void updateCalendarLabels(LocalDate startOfWeek) {
        monDateLabel.setText(startOfWeek.toString());
        tueDateLabel.setText(startOfWeek.plusDays(1).toString());
        wedDateLabel.setText(startOfWeek.plusDays(2).toString());
        thuDateLabel.setText(startOfWeek.plusDays(3).toString());
        friDateLabel.setText(startOfWeek.plusDays(4).toString());
        satDateLabel.setText(startOfWeek.plusDays(5).toString());
        sunDateLabel.setText(startOfWeek.plusDays(6).toString());
    }

    private void loadEventsForWeek(LocalDate startOfWeek) {

        // Placeholder functions: add 10 event cards to each day

//        for (int i = 0; i < 10; i++) {
//            try {
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/eventbox.fxml"));
//                Parent eventCard = fxmlLoader.load();
//                monVBox.getChildren().add(eventCard);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        for (int i = 0; i < 10; i++) {
//            try {
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/eventbox.fxml"));
//                Parent eventCard = fxmlLoader.load();
//                tueVBox.getChildren().add(eventCard);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        for (int i = 0; i < 10; i++) {
//            try {
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/eventbox.fxml"));
//                Parent eventCard = fxmlLoader.load();
//                wedVBox.getChildren().add(eventCard);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        for (int i = 0; i < 10; i++) {
//            try {
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/eventbox.fxml"));
//                Parent eventCard = fxmlLoader.load();
//                thuVBox.getChildren().add(eventCard);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        for (int i = 0; i < 10; i++) {
//            try {
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/eventbox.fxml"));
//                Parent eventCard = fxmlLoader.load();
//                friVBox.getChildren().add(eventCard);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        for (int i = 0; i < 10; i++) {
//            try {
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/eventbox.fxml"));
//                Parent eventCard = fxmlLoader.load();
//                satVBox.getChildren().add(eventCard);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        for (int i = 0; i < 10; i++) {
//            try {
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/eventbox.fxml"));
//                Parent eventCard = fxmlLoader.load();
//                sunVBox.getChildren().add(eventCard);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        // Real function

        List<Event> events = EventController.getAllEvents();
        clearCalendar();
        for (Event event : events) {
            LocalDate eventDate = LocalDate.parse(event.getDate());
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/eventbox.fxml"));
                Parent eventBox = fxmlLoader.load();
                EventBoxController controller = fxmlLoader.getController();
                controller.setEventData(event);

                if (eventDate.equals(startOfWeek)) {
                    monVBox.getChildren().add(eventBox);
                } else if (eventDate.equals(startOfWeek.plusDays(1))) {
                    tueVBox.getChildren().add(eventBox);
                } else if (eventDate.equals(startOfWeek.plusDays(2))) {
                    wedVBox.getChildren().add(eventBox);
                } else if (eventDate.equals(startOfWeek.plusDays(3))) {
                    thuVBox.getChildren().add(eventBox);
                } else if (eventDate.equals(startOfWeek.plusDays(4))) {
                    friVBox.getChildren().add(eventBox);
                } else if (eventDate.equals(startOfWeek.plusDays(5))) {
                    satVBox.getChildren().add(eventBox);
                } else if (eventDate.equals(startOfWeek.plusDays(6))) {
                    sunVBox.getChildren().add(eventBox);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadEventCards() {

        // Placeholder function: add 20 event cards

//        for (int i = 0; i < 20; i++) {
//            try {
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/eventcard.fxml"));
//                Parent eventCard = fxmlLoader.load();
//                listViewPane.getChildren().add(eventCard);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        // Real function

        List<Event> events = EventController.getAllEvents();
        listViewPane.getChildren().clear();

        for (Event event : events) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/eventcard.fxml"));
                Parent eventCard = fxmlLoader.load();
                EventCardController controller = fxmlLoader.getController();
                controller.setEventData(event);
                listViewPane.getChildren().add(eventCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleSearchAction(ActionEvent event) {
        String searchQuery = searchField.getText();
        String date = datePicker.getValue() != null ? datePicker.getValue().toString() : "";
        String eventType = eventTypeComboBox.getValue();
        String location = locationComboBox.getValue();
        String organizer = organizerComboBox.getValue();
        String minPrice = minPriceField.getText();
        String maxPrice = maxPriceField.getText();

        // Filter events based on search criteria
        List<Event> events = EventController.searchEvents(searchQuery, eventType, date, location, minPrice, maxPrice, organizer);

        // Update list view
        listViewPane.getChildren().clear();
        for (Event ev : events) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/eventcard.fxml"));
                Parent eventCard = fxmlLoader.load();
                EventCardController controller = fxmlLoader.getController();
                controller.setEventData(ev);
                listViewPane.getChildren().add(eventCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Update calendar view
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = getStartOfWeek(today);
        updateCalendarWithFilteredEvents(startOfWeek, events);
    }

    private void updateCalendarWithFilteredEvents(LocalDate startOfWeek, List<Event> events) {
        clearCalendar();
        for (Event event : events) {
            LocalDate eventDate = LocalDate.parse(event.getDate());
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/eventbox.fxml"));
                Parent eventBox = fxmlLoader.load();
                EventBoxController controller = fxmlLoader.getController();
                controller.setEventData(event);

                if (eventDate.equals(startOfWeek)) {
                    monVBox.getChildren().add(eventBox);
                } else if (eventDate.equals(startOfWeek.plusDays(1))) {
                    tueVBox.getChildren().add(eventBox);
                } else if (eventDate.equals(startOfWeek.plusDays(2))) {
                    wedVBox.getChildren().add(eventBox);
                } else if (eventDate.equals(startOfWeek.plusDays(3))) {
                    thuVBox.getChildren().add(eventBox);
                } else if (eventDate.equals(startOfWeek.plusDays(4))) {
                    friVBox.getChildren().add(eventBox);
                } else if (eventDate.equals(startOfWeek.plusDays(5))) {
                    satVBox.getChildren().add(eventBox);
                } else if (eventDate.equals(startOfWeek.plusDays(6))) {
                    sunVBox.getChildren().add(eventBox);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleDatePickerAction(ActionEvent event) {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate != null) {
            LocalDate startOfWeek = getStartOfWeek(selectedDate);
            updateCalendarLabels(startOfWeek);
            loadEventsForWeek(startOfWeek);
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
    }

    @FXML
    private void handleProfileAction(ActionEvent event) {
        // Open the profile window
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/profile.fxml"));
            Parent newEventRoot = fxmlLoader.load();
            Stage newEventStage = new Stage();
            newEventStage.setTitle("User profile");
            newEventStage.setScene(new Scene(newEventRoot));
            newEventStage.initModality(Modality.WINDOW_MODAL);
            newEventStage.initOwner(profileButton.getScene().getWindow());
            newEventStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddEventAction(ActionEvent event) {
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
        String selectedSortMethod = sortChoiceBox.getValue();
        sortEvents(selectedSortMethod);
    }

    private void sortEvents(String sortMethod) {
        List<Event> events = EventController.getAllEvents();

        switch (sortMethod) {
            case "Aakkosjärjestys":
                events.sort(Comparator.comparing(Event::getTitle));
                break;
            case "Päivämäärän mukaan":
                events.sort(Comparator.comparing(Event::getDate));
                break;
        }

        listViewPane.getChildren().clear();
        for (Event event : events) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/eventcard.fxml"));
                Parent eventCard = fxmlLoader.load();
                EventCardController controller = fxmlLoader.getController();
                controller.setEventData(event);
                listViewPane.getChildren().add(eventCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearCalendar() {
        monVBox.getChildren().clear();
        tueVBox.getChildren().clear();
        wedVBox.getChildren().clear();
        thuVBox.getChildren().clear();
        friVBox.getChildren().clear();
        satVBox.getChildren().clear();
        sunVBox.getChildren().clear();
    }
}
