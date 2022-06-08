package com.example.cookbook.models;

public class RecipeStep {
    String description;
    String image;

    public RecipeStep(String stepImage, String stepDescription) {
        this.description = stepDescription;
        this.image = stepImage;
    }

    public String getStepDescription() {
        return description;
    }

    public String getStepImage() {
        return image;
    }
}
