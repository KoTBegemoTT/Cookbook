package com.example.cookbook.models;

public class Ingredient {
    String title, amount;

    public Ingredient(String title, String amount) {
        this.title = title;
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public String getAmount() {
        return amount;
    }
}
