package model;

public class Category {
    private final int categoryId;
    private final String name;
    private final String description;

    public Category(int id, String name, String description) {
        this.categoryId = id;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return categoryId + "";
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name;
    }
}
