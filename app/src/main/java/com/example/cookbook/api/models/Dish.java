package com.example.cookbook.api.models;

public class Dish {
    String name;
    String calorie;
    String source;
    String mobile_image;
    String vote;
    String description;
    String ingredients_name;
    String ingredients_number;
    String recipe;
    String rating;

    public String getName() {
        return name;
    }

    public String getCalorie() {
        return calorie;
    }

    public String getSource() {
        return source;
    }

    public String getImageUrl() {
        return mobile_image;
    }

    public String getVote() {
        return vote;
    }

    public String getDescription() {
        return description;
    }

    public String getIngredients_name() {
        return ingredients_name;
    }

    public String getIngredients_number() {
        return ingredients_number;
    }

    public String getRecipe() {
        return recipe;
    }

    public String getRating() {
        return rating;
    }
}
