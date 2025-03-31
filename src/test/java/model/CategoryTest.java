package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {
    static Category category;

    @BeforeAll
    static void setUp() {
        category = new Category(1, "name", "description");
    }

    @Test
    void getId() {
        assertEquals("1", category.getId());
    }

    @Test
    void getName() {
        assertEquals("name", category.getName());
    }

    @Test
    void getDescription() {
        assertEquals("description", category.getDescription());
    }

    @Test
    void testToString() {
        assertEquals("name", category.toString());
    }
}