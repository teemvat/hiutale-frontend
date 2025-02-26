package model;

import java.time.LocalDate;

public class Event {
    private String eventId;
    private String eventTitle;
    private String eventDescription;
    private String eventLocationId;
    private int eventCapacity;
    private String eventOrganizerId;
    private String eventCategories;
    private String eventStatus;
    private LocalDate eventDate;
    private String startTime;
    private String endTime;
    private double eventPrice;
    private int attendeeCount;
    private int favouriteCount;

    // for new event and getting events from database
    public Event(String eventId, String eventTitle, String eventDescription, String eventLocationId, String eventCapacity, String eventOrganizerId, String eventCategories, LocalDate eventDate, String startTime, String endTime, double eventPrice, int attendeeCount, int favouriteCount) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.eventLocationId = eventLocationId;
        this.eventCapacity = Integer.parseInt(eventCapacity);
        this.eventOrganizerId = eventOrganizerId;
        this.eventCategories = eventCategories;
        this.eventStatus = "Default";
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventPrice = eventPrice;
        this.attendeeCount = attendeeCount;
    }

    // for editing events
    public Event(String eventId, String eventTitle, String eventDescription, String eventLocationId, String eventCapacity, String eventCategories, LocalDate eventDate, String startTime, String endTime, double eventPrice) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.eventLocationId = eventLocationId;
        this.eventCapacity = Integer.parseInt(eventCapacity);
        this.eventCategories = eventCategories;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventPrice = eventPrice;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventLocationId() {
        return eventLocationId;
    }

    public void setEventLocationId(String eventLocationId) {
        this.eventLocationId = eventLocationId;
    }

    public int getEventCapacity() {
        return eventCapacity;
    }

    public void setEventCapacity(int eventCapacity) {
        this.eventCapacity = eventCapacity;
    }

    public String getEventOrganizerId() {
        return eventOrganizerId;
    }

    public void setEventOrganizerId(String eventOrganizerId) {
        this.eventOrganizerId = eventOrganizerId;
    }

    public String getEventCategories() {
        return eventCategories;
    }

    public void setEventCategories(String eventCategories) {
        this.eventCategories = eventCategories;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
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

    public double getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(double eventPrice) {
        this.eventPrice = eventPrice;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public int getAttendeeCount() {
        return attendeeCount;
    }

    public void setAttendeeCount(int attendeeCount) {
        this.attendeeCount = attendeeCount;
    }

    public int getFavouriteCount() {
        return favouriteCount;
    }

    public void setFavouriteCount(int favouriteCount) {
        this.favouriteCount = favouriteCount;
    }
}
