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
    private List<Integer> eventCategoryIds;
    private String status;
    private String date;
    private String startTime;
    private String endTime;
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
        this.eventCategoryIds = new ArrayList<>();
        for (String category : eventCategories.split(",")) {
            this.eventCategoryIds.add(Integer.valueOf(category.trim()));
        }
        this.status = "Default";
        this.date = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = eventPrice;
        this.attendanceCount = attendeeCount;
    }

    // for editing events only
    public Event(String eventId, String eventTitle, String eventDescription, String eventLocationId, String eventCapacity, String eventCategories, String eventDate, String startTime, String endTime, double eventPrice) {
        this.id = eventId;
        this.title = eventTitle;
        this.description = eventDescription;
        this.locationId = eventLocationId;
        this.capacity = Integer.parseInt(eventCapacity);
        this.eventCategoryIds = new ArrayList<>();
        this.eventCategoryIds.add(Integer.valueOf(eventCategories));
        this.date = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = eventPrice;
    }

    public void reformatDateForBE() {
        this.startTime = this.date + "T" + this.startTime;
        this.endTime = this.date + "T" + this.endTime;
    }

    public void reformatDateForFE() {
        this.date = this.startTime.split("T")[0];
        this.startTime = this.startTime.split("T")[1];
        this.endTime = this.endTime.split("T")[1];
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

    public List<Integer> getCategories() {
        if (eventCategoryIds == null) {
            return new ArrayList<>();
        }
        return eventCategoryIds;
    }

    public void setEventCategoryIds(String eventCategoryIds) {
        this.eventCategoryIds.clear();
        for (String category : eventCategoryIds.split(",")) {
            this.eventCategoryIds.add(Integer.valueOf(category.trim()));
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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
