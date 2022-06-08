package com.example.cookbook.models;

public class Dish {
    String img, title, ingredientsCount, stepsCount;
    Float starRating;

    public Dish(String img, String title, String ingredientsCount, String stepsCount, Float starRating) {
        this.img = img;
        this.title = title;
        this.ingredientsCount = ingredientsCount;
        this.stepsCount = stepsCount;
        this.starRating = starRating;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIngredientsCount() {
        return ingredientsCount;
    }

    public void setIngredientsCount(String ingredientsCount) {
        this.ingredientsCount = ingredientsCount;
    }

    public String getStepsCount() {
        return stepsCount;
    }

    public void setStepsCount(String stepsCount) {
        this.stepsCount = stepsCount;
    }

    public Float getStarRating() {
        return starRating;
    }

    public void setStarRating(Float starRating) {
        this.starRating = starRating;
    }
}
