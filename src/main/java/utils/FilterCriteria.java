package utils;

import controller.api.LocationController;
import controller.api.UserController;
import java.util.Arrays;
import model.Event;

public class FilterCriteria {
  private final String searchQuery;
  private final String date;
  private final String eventType;
  private final String location;
  private final String organizer;
  private final String minPrice;
  private final String maxPrice;

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
    return (searchQuery.isEmpty() || event.getTitle().toLowerCase().contains(searchQuery.toLowerCase()))
            && (date.isEmpty() || event.getDate().equals(date))
            && (eventType == null || Arrays.asList(event.getCategories()).contains(eventType))
            && (location == null || LocationController.getLocationById(event.getLocationId()).getName().equals(location))
            && (organizer == null || UserController.getUser(Integer.parseInt(event.getOrganizerId())).getUsername().equals(organizer));
  }
}
