package model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EventTest {
    static Event event;

    @BeforeAll
    static void setUp() {
        event = new Event("1", "title", "description", "1", "1", "1", new String[]{"1"}, "2021-01-01", "2021-01-01", "00:00", "00:00", 0.0, 0, 0);
    }

    @Order(26)
    @Test
    void reformatDateForBE() {
        String expectedStart = "2021-01-01T00:00:00";
        String expectedEnd = "2021-01-01T00:00:00";
        event.reformatDateForBE();
        assertEquals(expectedStart, event.getStart());
        assertEquals(expectedEnd, event.getEnd());
    }

//    @Disabled
//    @Order(27)
//    @Test
//    // tutki mikä mättää
//    void reformatDateForFE() {
//        String expectedStartDate = "2021-01-01";
//        String expectedEndDate = "2021-01-01";
//        String expectedStart = "00:00";
//        String expectedEnd = "00:00";
//        event.reformatDateForFE();
//        assertEquals(expectedStartDate, event.getDate());
//        assertEquals(expectedEndDate, event.getDate());
//        assertEquals(expectedStart, event.getStart());
//        assertEquals(expectedEnd, event.getEnd());
//    }

    @Order(1)
    @Test
    void getTitle() {
        String expected = "title";
        assertEquals(expected, event.getTitle());
    }

    @Order(2)
    @Test
    void setTitle() {
        String expected = "new title";
        event.setTitle(expected);
        assertEquals(expected, event.getTitle());
    }

    @Order(3)
    @Test
    void getDescription() {
        String expected = "description";
        assertEquals(expected, event.getDescription());
    }

    @Order(4)
    @Test
    void setDescription() {
        String expected = "new description";
        event.setDescription(expected);
        assertEquals(expected, event.getDescription());
    }

    @Order(5)
    @Test
    void getLocationId() {
        String expected = "1";
        assertEquals(expected, event.getLocationId());
    }

    @Order(6)
    @Test
    void setLocationId() {
        String expected = "2";
        event.setLocationId(expected);
        assertEquals(expected, event.getLocationId());
    }

    @Order(7)
    @Test
    void getCapacity() {
        int expected = 1;
        assertEquals(expected, event.getCapacity());
    }

    @Order(8)
    @Test
    void setCapacity() {
        int expected = 2;
        event.setCapacity(expected);
        assertEquals(expected, event.getCapacity());
    }

    @Order(9)
    @Test
    void getOrganizerId() {
        String expected = "1";
        assertEquals(expected, event.getOrganizerId());
    }

    @Order(10)
    @Test
    void setOrganizerId() {
        String expected = "2";
        event.setOrganizerId(expected);
        assertEquals(expected, event.getOrganizerId());
    }

    @Order(11)
    @Test
    void getCategories() {
        Integer[] expected = new Integer[]{1};
        assertArrayEquals(expected, event.getCategories().toArray());
    }

    @Order(12)
    @Test
    void setEventCategoryIds() {
        String expected = "2";
        event.setEventCategoryIds(expected);
        Integer[] expectedArray = new Integer[]{2};
        assertArrayEquals(expectedArray, event.getCategories().toArray());
    }

    @Order(13)
    @Test
    void getStatus() {
        String expected = "Default";
        assertEquals(expected, event.getStatus());
    }

    @Order(14)
    @Test
    void getDate() {
        String expected = "2021-01-01";
        assertEquals(expected, event.getDate());
    }

    @Order(15)
    @Test
    void getStart() {
        String expected = "00:00";
        assertEquals(expected, event.getStart());
    }

    @Order(16)
    @Test
    void getEnd() {
        String expected = "00:00";
        assertEquals(expected, event.getEnd());
    }

    @Order(17)
    @Test
    void getPrice() {
        double expected = 0.0;
        assertEquals(expected, event.getPrice());
    }

    @Order(18)
    @Test
    void getId() {
        String expected = "1";
        assertEquals(expected, event.getId());
    }

    @Test
    void setId() {
        String expected = "2";
        event.setId(expected);
        assertEquals(expected, event.getId());
    }

    @Order(19)
    @Test
    void getAttendanceCount() {
        int expected = 0;
        assertEquals(expected, event.getAttendanceCount());
    }

    @Order(20)
    @Test
    void setAttendanceCount() {
        int expected = 1;
        event.setAttendanceCount(expected);
        assertEquals(expected, event.getAttendanceCount());
    }

    @Order(21)
    @Test
    void getFavouriteCount() {
        int expected = 0;
        assertEquals(expected, event.getFavouriteCount());
    }

    @Order(22)
    @Test
    void setFavouriteCount() {
        int expected = 1;
        event.setFavouriteCount(expected);
        assertEquals(expected, event.getFavouriteCount());
    }

    @Order(23)
    @Test
    void setImage() {
        String expected = "image";
        event.setImage(expected);
        assertEquals(expected, event.getImage());
    }

    @Order(24)
    @Test
    void getImage() {
        String expected = "image";
        event.setImage(expected);
        assertEquals(expected, event.getImage());
    }

    @Order(0)
    @Test
    void testToString() {
        String expected = "Event{" +
                "id='1" + '\'' +
                ", title='title" + '\'' +
                ", description='description" + '\'' +
                ", locationId='1" + '\'' +
                ", capacity=1" +
                ", organizerId='1" + '\'' +
                ", eventCategoryIds=[1]" +
                ", status='Default" + '\'' +
                ", startDate='2021-01-01" + '\'' +
                ", endDate='2021-01-01" + '\'' +
                ", start='00:00" + '\'' +
                ", end='00:00" + '\'' +
                ", price=0.0" +
                ", attendanceCount=0" +
                ", favouriteCount=0" +
                ", image=null" +
                '}';
        assertEquals(expected, event.toString());
    }
}