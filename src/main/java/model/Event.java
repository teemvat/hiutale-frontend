package model;

public class Event {
    private String eventTitle;
    private String eventDescription;
    private String eventLocationId;
    private int eventCapacity;
    private String eventOrganizer;
    private String eventCategories;
    private String eventStatus;
    private String eventDate;
    private String eventTime;
    private double eventPrice;

    public Event(String eventTitle, String eventDescription, String eventLocationId, String eventCapacity, String eventOrganizer, String eventCategories, String eventStatus, String eventDate, String eventTime, double eventPrice) {
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.eventLocationId = eventLocationId;
        this.eventCapacity = Integer.parseInt(eventCapacity);
        this.eventOrganizer = eventOrganizer;
        this.eventCategories = eventCategories;
        this.eventStatus = eventStatus;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
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

    public String getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(String eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
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

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public double getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(double eventPrice) {
        this.eventPrice = eventPrice;
    }
}
