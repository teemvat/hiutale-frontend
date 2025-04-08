package model;

/**
 * The Location class represents a location with attributes ID, name, address, city, and postcode.
 * It provides methods to retrieve and modify these attributes.
 */
public class Location {
    // The unique identifier for the location
    private String id;
    // The name of the location
    private String name;
    // The address of the location
    private String address;
    // The city where the location is situated
    private String city;
    // The postal code of the location
    private String postalCode;

    /**
     * Constructs a new Location object with the specified attributes.
     *
     * @param id         The unique identifier for the location.
     * @param name       The name of the location.
     * @param address    The address of the location.
     * @param city       The city where the location is situated.
     * @param postalCode The postal code of the location.
     */
    public Location(String id, String name, String address, String city, String postalCode) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
    }

    /**
     * Retrieves the ID of the location.
     *
     * @return The unique identifier of the location.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the location.
     *
     * @param id The unique identifier to set for the location.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the location.
     *
     * @return The name of the location.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the location.
     *
     * @param name The name to set for the location.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the address of the location.
     *
     * @return The address of the location.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the location.
     *
     * @param address The address to set for the location.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Retrieves the city of the location.
     *
     * @return The city where the location is situated.
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city of the location.
     *
     * @param city The city to set for the location.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Retrieves the postal code of the location.
     *
     * @return The postal code of the location.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the postal code of the location.
     *
     * @param postalCode The postal code to set for the location.
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Returns a string representation of the location, which is its name.
     *
     * @return The name of the location.
     */
    @Override
    public String toString() {
        return name;
    }
}