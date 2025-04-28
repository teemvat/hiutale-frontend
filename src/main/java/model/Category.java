package model;

/**
 * The Category class represents a category with an ID, name, and description.
 * It provides methods to retrieve the category's details and a string representation.
 */
public class Category {
    // The unique identifier for the category
    private final int categoryId;
    // The name of the category
    private final String name;
    // A brief description of the category
    private final String description;

    /**
     * Constructs a new Category object with the specified ID, name, and description.
     *
     * @param id          The unique identifier for the category.
     * @param name        The name of the category.
     * @param description A brief description of the category.
     */
    public Category(int id, String name, String description) {
        this.categoryId = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Retrieves the ID of the category as a string.
     *
     * @return The category ID as a string.
     */
    public int getId() {
        return categoryId;
    }

    /**
     * Retrieves the name of the category.
     *
     * @return The name of the category.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the description of the category.
     *
     * @return The description of the category.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns a string representation of the category, which is its name.
     *
     * @return The name of the category.
     */
    @Override
    public String toString() {
        return name;
    }
}