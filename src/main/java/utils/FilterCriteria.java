package utils;

import controller.api.LocationController;
import controller.api.UserController;
import model.Event;

import java.util.Arrays;

public class FilterCriteria {
    private String searchQuery;
    private String date;
    private String eventType;
    private String location;
    private String organizer;
    private String minPrice;
    private String maxPrice;

    public FilterCriteria(String searchQuery, String date, String eventType, String location, String organizer, String minPrice, String maxPrice) {
        this.searchQuery = searchQuery;
        this.date = date;
        this.eventType = eventType;
        this.location = location;
        this.organizer = organizer;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public boolean matches(Event event) {
        return (searchQuery.isEmpty() || event.getTitle().toLowerCase().contains(searchQuery.toLowerCase())) &&
                (date.isEmpty() || event.getDate().equals(date)) &&
                (eventType == null || Arrays.asList(event.getCategories()).contains(eventType)) &&
                (location == null || LocationController.getLocationById(event.getLocationId()).getName().equals(location)) &&
                (organizer == null || UserController.getUser(Integer.parseInt(event.getOrganizerId())).getUsername().equals(organizer));
    }
}
