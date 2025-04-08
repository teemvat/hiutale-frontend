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


public class EventController {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(File.class, new FileTypeAdapter())
            .create();
    private static final List<Event> events = new ArrayList<>();

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
                    System.out.println("Invalid minPrice value: " + maxPrice);
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

    public static void deleteEvent(String eventId) {
        sendHttpRequest("DELETE", "/events/delete/" + eventId, "");
        events.removeIf(event -> event.getId().equals(eventId));
        ImageController.deleteImage(eventId);
    }

    public static Event getEvent(String eventId) {
        for (Event event : events) {
            if (event.getId().equals(eventId)) {
                event.setImage(ImageController.getImageUrl(event.getId()));
                return event;
            }
        }
        return null;
    }

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
