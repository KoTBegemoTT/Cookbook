package com.example.cookbook.api.models;

public class Preview {
    String name;
    String mobile_image;
    String ingredients;
    String steps;
    String dish;
    String rating;

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return mobile_image;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public String getDish() {
        return dish;
    }

    public String getRating() {
        return rating;
    }
}
