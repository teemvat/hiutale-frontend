package model;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LocationTest {
    static Location location = new Location("1", "name", "address", "city", "postalCode");

    @Order(2)
    @Test
    void getId() {
        assertEquals("1", location.getId());
    }

    @Order(3)
    @Test
    void setId() {
        location.setId("2");
        assertEquals("2", location.getId());
    }

    @Order(4)
    @Test
    void getName() {
        assertEquals("name", location.getName());
    }

    @Order(5)
    @Test
    void setName() {
        location.setName("newName");
        assertEquals("newName", location.getName());
    }

    @Order(6)
    @Test
    void getAddress() {
        assertEquals("address", location.getAddress());
    }

    @Order(7)
    @Test
    void setAddress() {
        location.setAddress("newAddress");
        assertEquals("newAddress", location.getAddress());
    }

    @Order(8)
    @Test
    void getCity() {
        assertEquals("city", location.getCity());
    }

    @Order(9)
    @Test
    void setCity() {
        location.setCity("newCity");
        assertEquals("newCity", location.getCity());
    }

    @Order(10)
    @Test
    void getPostalCode() {
        assertEquals("postalCode", location.getPostalCode());
    }

    @Order(11)
    @Test
    void setPostalCode() {
        location.setPostalCode("newPostalCode");
        assertEquals("newPostalCode", location.getPostalCode());
    }

    @Order(1)
    @Test
    void testToString() {
        assertEquals("name", location.toString());
    }
}