package model;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
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
    private String startDate;
    private String endDate;
    private String start;
    private String end;
    private double price;
    private int attendanceCount;
    private int favouriteCount;

    // for new event and getting events from database
    public Event(String eventId, String eventTitle, String eventDescription, String eventLocationId, String eventCapacity, String eventOrganizerId, String eventCategories, String startDate, String endDate, String startTime, String endTime, double eventPrice, int attendeeCount, int favouriteCount) {
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
        this.startDate = startDate;
        this.endDate = endDate;
        this.start = startTime;
        this.end = endTime;
        this.price = eventPrice;
        this.attendanceCount = attendeeCount;
    }

    public void reformatDateForBE() {
        this.start = this.startDate + "T" + this.start + ":00";
        this.end = this.endDate + "T" + this.end + ":00";
    }

    public void reformatDateForFE() {
        this.startDate = this.start.split("T")[0];
        this.endDate = this.end.split("T")[0];
        this.start = getTime(this.start);
        this.end = getTime(this.end);
    }

    private String getTime(String input) {
        String formattedTime;
        if (input.contains("T")) { // Full timestamp
            OffsetDateTime dateTime = OffsetDateTime.parse(input);
            formattedTime = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else { // Just time
            LocalTime time = LocalTime.parse(input, DateTimeFormatter.ofPattern("HH:mm"));
            formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return formattedTime;
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

    public String getDate() {
        return startDate;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public double getPrice() {
        return price;
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
