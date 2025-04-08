package controller.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Event;
import utils.FileTypeAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static utils.ApiConnector.sendHttpRequest;

/**
 * The EventController class provides methods to manage events, including creating, retrieving,
 * searching, and deleting events. It handles event-related operations such as uploading images.
 */
public class EventController {
    // Gson configured with custom FileTypeAdapter for JSON serialization and deserialization
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(File.class, new FileTypeAdapter())
            .create();
    // A list to store all events
    private static final List<Event> events = new ArrayList<>();

    /**
     * Creates a new event and uploads an associated image.
     *
     * @param title       The title of the event.
     * @param description The description of the event.
     * @param locationId  The ID of the event's location.
     * @param capacity    The capacity of the event.
     * @param categories  An array of category IDs associated with the event.
     * @param startDate   The start date of the event.
     * @param endDate     The end date of the event.
     * @param startTime   The start time of the event.
     * @param endTime     The end time of the event.
     * @param price       The price of the event.
     * @param image       The image file associated with the event.
     * @return The newly created Event object.
     */
    public static Event createEvent(String title,
                                    String description,
                                    String locationId,
                                    String capacity,
                                    String[] categories,
                                    String startDate,
                                    String endDate,
                                    String startTime,
                                    String endTime,
                                    double price,
                                    File image) {
        Event event = new Event(
                null,
                title,
                description,
                locationId,
                capacity,
                null,
                categories,
                startDate,
                endDate,
                startTime,
                endTime,
                price,
                0,
                0);
        event.reformatDateForBe();
        System.out.println("EventController, created event: " + event);
        String requestBody = gson.toJson(event);
        System.out.println("EventController, request: " + requestBody);
        String response = sendHttpRequest("POST", "/events/create", requestBody);
        System.out.println("EventController, response: " + response);
        Event newEvent = gson.fromJson(response, Event.class);
        System.out.println("EventController, new event: " + newEvent);
        ImageController.uploadImage(Long.parseLong(newEvent.getId()), image);

        events.add(newEvent);
        return newEvent;
    }

    /**
     * Retrieves all events from the server and updates the local event list.
     *
     * @return A list of all Event objects.
     */
    public static List<Event> getAllEvents() {
        events.clear();

        String response = sendHttpRequest("GET", "/events/all", "");
        System.out.println("Response: " + response);
        List<Event> allEvents = gson.fromJson(response, new TypeToken<List<Event>>() {
        }.getType());
        events.addAll(allEvents);
        for (Event event : events) {
            event.reformatDateForFe();
            String imageUrl = ImageController.getImageUrl(event.getId());
            if (imageUrl != null && !imageUrl.isEmpty()) {
                System.out.println("Image found for event ID: " + event.getId());
                event.setImage(imageUrl);
            } else {
                System.out.println("No image found for event ID: " + event.getId());
            }
        }
        return allEvents;
    }

    /**
     * Searches for events based on various criteria.
     *
     * @param query       The search query for event titles.
     * @param category    The category ID to filter events.
     * @param date        The date to filter events.
     * @param location    The location ID to filter events.
     * @param minPrice    The minimum price to filter events.
     * @param maxPrice    The maximum price to filter events.
     * @param organizerId The organizer ID to filter events.
     * @return A list of Event objects matching the search criteria.
     */
    public static List<Event> searchEvents(
            String query,
            String category,
            String date,
            String location,
            String minPrice,
            String maxPrice,
            String organizerId
    ) {
        List<Event> searchResults = new ArrayList<>();

        for (Event event : events) {
            if (query != null && !event.getTitle().toLowerCase().contains(query.toLowerCase())) {
                System.out.println("Skipping event due to query: " + event.getTitle());
                continue;
            }
            System.out.println("Category: " + category);
            System.out.println("event category: " + event.getCategories());
            if (category != null
                    && !category.isEmpty()
                    && event.getCategories() != null
                    && !event.getCategories().contains(Integer.parseInt(category))
            ) {
                System.out.println("Skipping event due to category: " + event.getTitle());
                continue;
            }
            if (date != null
                    && !date.isEmpty()
                    && !event.getDate().equals(date)
            ) {
                System.out.println("Skipping event due to date: " + event.getTitle());
                continue;
            }
            if (location != null
                    && (!event.getLocationId().equalsIgnoreCase(location))
            ) {
                System.out.println("Skipping event due to location: " + event.getTitle());
                continue;
            }
            if (minPrice != null
                    && !minPrice.trim().isEmpty()
            ) {
                try {
                    double minPriceValue = Double.parseDouble(minPrice);
                    if (event.getPrice() < minPriceValue) {
                        System.out.println("Skipping event due to minPrice: " + event.getTitle());
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid minPrice value: " + minPrice);
                }
            }
            if (maxPrice != null
                    && !maxPrice.isEmpty()
            ) {
                try {
                    double maxPriceValue = Double.parseDouble(maxPrice);
                    if (event.getPrice() > maxPriceValue) {
                        System.out.println("Skipping event due to minPrice: " + event.getTitle());
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid minPrice value: " + minPrice);
                }
            }
            if (organizerId != null
                    && (!event.getOrganizerId().equalsIgnoreCase(organizerId))
            ) {
                System.out.println("Skipping event due to organizerId: " + event.getTitle());
                continue;
            }
            searchResults.add(event);
        }
        return searchResults;
    }

    /**
     * Deletes an event by its ID and removes it from the local event list.
     *
     * @param eventId The ID of the event to delete.
     */
    public static void deleteEvent(String eventId) {
        sendHttpRequest("DELETE", "/events/delete/" + eventId, "");
        events.removeIf(event -> event.getId().equals(eventId));
        ImageController.deleteImage(eventId);
    }

    /**
     * Retrieves a specific event by its ID.
     *
     * @param eventId The ID of the event to retrieve.
     * @return The Event object with the specified ID, or null if not found.
     */
    public static Event getEvent(String eventId) {
        for (Event event : events) {
            if (event.getId().equals(eventId)) {
                event.setImage(ImageController.getImageUrl(event.getId()));
                return event;
            }
        }
        return null;
    }

    /**
     * Retrieves all events organized by a specific organizer.
     *
     * @param organizerId The ID of the organizer.
     * @return A list of Event objects organized by the specified organizer.
     */
    public static List<Event> getEventsByOrganizer(String organizerId) {
        List<Event> organizerEvents = new ArrayList<>();
        for (Event event : events) {
            if (event.getOrganizerId().equals(organizerId)) {
                organizerEvents.add(event);
            }
        }
        return organizerEvents;
    }
}