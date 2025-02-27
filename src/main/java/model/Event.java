package model;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private String id;
    private String title;
    private String description;
    private String locationId;
    private int capacity;
    private String organizerId;
    private List<String> categories;
    private String status;
    private String date;
    private String start;
    private String end;
    private double price;
    private int attendanceCount;
    private int favouriteCount;

    // for new event and getting events from database
    public Event(String eventId, String eventTitle, String eventDescription, String eventLocationId, String eventCapacity, String eventOrganizerId, String eventCategories, String eventDate, String startTime, String endTime, double eventPrice, int attendeeCount, int favouriteCount) {
        this.id = eventId;
        this.title = eventTitle;
        this.description = eventDescription;
        this.locationId = eventLocationId;
        this.capacity = Integer.parseInt(eventCapacity);
        this.organizerId = eventOrganizerId;

        this.categories = new ArrayList<>(); //todo: tää kusee, fiksaa!
        for (String category : eventCategories.split(",")) {
            this.categories.add(category.trim());
        }
        this.status = "Default";
        this.date = eventDate;
        this.start = startTime;
        this.end = endTime;
        this.price = eventPrice;
        this.attendanceCount = attendeeCount;
    }

    // for editing events
    public Event(String eventId, String eventTitle, String eventDescription, String eventLocationId, String eventCapacity, String eventCategories, String eventDate, String startTime, String endTime, double eventPrice) {
        this.id = eventId;
        this.title = eventTitle;
        this.description = eventDescription;
        this.locationId = eventLocationId;
        this.capacity = Integer.parseInt(eventCapacity);
        this.categories.clear();
        this.categories.add(eventCategories);
        this.date = eventDate;
        this.start = startTime;
        this.end = endTime;
        this.price = eventPrice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    public String[] getCategories() {
        return categories.toArray(new String[0]);
    }

    public void setCategories(String categories) {
        this.categories.clear();
        for (String category : categories.split(",")) {
            this.categories.add(category.trim());
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAttendanceCount() {
        return attendanceCount;
    }

    public void setAttendanceCount(int attendanceCount) {
        this.attendanceCount = attendanceCount;
    }

    public int getFavouriteCount() {
        return favouriteCount;
    }

    public void setFavouriteCount(int favouriteCount) {
        this.favouriteCount = favouriteCount;
    }
}
