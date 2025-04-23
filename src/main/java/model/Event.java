package model;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The Event class represents an event with various attributes such as title, description,
 * location, capacity, organizer, categories, status, date, time, price, and counts for
 * attendance and favorites. It provides methods to manipulate and retrieve event details.
 */
public class Event {
    // Unique identifier for the event
    private String id;
    // Title of the event
    private String title;
    // Description of the event
    private String description;
    // ID of the location where the event is held
    private String locationId;
    // Maximum capacity of attendees for the event
    private int capacity;
    // ID of the organizer of the event
    private String organizerId;
    // List of category IDs associated with the event
    private final List<Integer> eventCategoryIds;
    // Status of the event
    private String status;
    // Start date of the event
    private String startDate;
    // End date of the event
    private String endDate;
    // Start time of the event
    private String start;
    // End time of the event
    private String end;
    // Price of the event
    private double price;
    // Number of attendees for the event
    private int attendanceCount;
    // Number of times the event has been marked as a favorite
    private int favouriteCount;
    // Image associated with the event
    private String image;

    /**
     * Constructs a new Event object with the specified attributes.
     *
     * @param eventId          The unique identifier for the event.
     * @param eventTitle       The title of the event.
     * @param eventDescription The description of the event.
     * @param eventLocationId  The ID of the location where the event is held.
     * @param eventCapacity    The maximum capacity of attendees for the event.
     * @param eventOrganizerId The ID of the organizer of the event.
     * @param eventCategories  The array of category IDs associated with the event.
     * @param startDate        The start date of the event.
     * @param endDate          The end date of the event.
     * @param startTime        The start time of the event.
     * @param endTime          The end time of the event.
     * @param eventPrice       The price of the event.
     * @param attendeeCount    The number of attendees for the event.
     * @param favouriteCount   The number of times the event has been marked as a favorite.
     */
    public Event(
            String eventId,
            String eventTitle,
            String eventDescription,
            String eventLocationId,
            String eventCapacity,
            String eventOrganizerId,
            String[] eventCategories,
            String startDate,
            String endDate,
            String startTime,
            String endTime,
            double eventPrice,
            int attendeeCount,
            int favouriteCount) {
        this.id = eventId;
        this.title = eventTitle;
        this.description = eventDescription;
        this.locationId = eventLocationId;
        this.capacity = Integer.parseInt(eventCapacity);
        this.organizerId = eventOrganizerId;
        this.eventCategoryIds = new ArrayList<>();
        for (String category : eventCategories) {
            this.eventCategoryIds.add(Integer.valueOf(category.trim()));
        }
        this.status = "Default";
        this.startDate = startDate;
        this.endDate = endDate;
        this.start = startTime;
        this.end = endTime;
        this.price = eventPrice;
        this.attendanceCount = attendeeCount;
        this.favouriteCount = favouriteCount;
        this.image = null;
    }

    /**
     * Reformats the start and end date-time for backend processing.
     */
    public void reformatDateForBe() {
        this.start = this.startDate + "T" + this.start + ":00";
        this.end = this.endDate + "T" + this.end + ":00";
    }

    /**
     * Reformats the start and end date-time for frontend display.
     */
    public void reformatDateForFe() {
        this.startDate = this.start.split("T")[0];
        this.endDate = this.end.split("T")[0];
        this.start = getTime(this.start);
        this.end = getTime(this.end);
    }

    /**
     * Extracts and formats the time from a given date-time string.
     *
     * @param input The input date-time string.
     * @return The formatted time string.
     */
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

    // Getters and setters for various attributes of the Event class

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

    /**
     * Retrieves the list of category IDs associated with the event.
     *
     * @return A list of category IDs.
     */
    public List<Integer> getCategories() {
        if (eventCategoryIds == null) {
            return new ArrayList<>();
        }
        return eventCategoryIds;
    }

    /**
     * Sets the category IDs for the event.
     *
     * @param eventCategoryIds A comma-separated string of category IDs.
     */
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

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    /**
     * Returns a string representation of the Event object.
     *
     * @return A string containing the event's details.
     */
    @Override
    public String toString() {
        return "Event{"
                + "id='"
                + id
                + '\''
                + ", title='"
                + title + '\''
                + ", description='"
                + description
                + '\''
                + ", locationId='"
                + locationId
                + '\''
                + ", capacity="
                + capacity
                + ", organizerId='"
                + organizerId
                + '\''
                + ", eventCategoryIds="
                + eventCategoryIds
                + ", status='"
                + status
                + '\''
                + ", startDate='"
                + startDate
                + '\''
                + ", endDate='"
                + endDate
                + '\''
                + ", start='"
                + start
                + '\''
                + ", end='"
                + end
                + '\''
                + ", price="
                + price
                + ", attendanceCount="
                + attendanceCount
                + ", favouriteCount="
                + favouriteCount
                + ", image="
                + image
                + '}';
    }
}