package controller.api;

import model.Category;
import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import utils.SessionManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryControllerTest {

    @BeforeAll
    static void setUp() {
        // Log in a user to get a valid token for authentication
        UserController.login("testuser@example.com", "password");
        User user = SessionManager.getInstance().getUser();
        assertNotNull(user);
    }

    @Test
    void getAllCategories() {
        List<Category> categories = CategoryController.getAllCategories();
        assertNotNull(categories);
    }

    @Test
    void getCategoryById() {
        Category category = CategoryController.getCategoryById("1");
        assertNotNull(category);
    }

}