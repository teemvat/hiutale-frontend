package model;

public class Location {
    private String locationId;
    private String name;
    private String address;
    private String city;
    private String postalCode;

    public Location(String locationId, String name, String address, String city, String postalCode) {
        this.locationId = locationId;
        this.name = name;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
