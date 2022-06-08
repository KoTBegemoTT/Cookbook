package com.example.cookbook.api.models.api_classes;

import android.util.Log;

public class Relation {
    public String rate;
    public Boolean loved;


    public Relation(String rate) {
        this.rate = rate;
        this.loved = false;
    }

    public Relation(Boolean loved) {
        this.rate = null;
        this.loved = loved;
    }

    public Relation() {
        this.rate = null;
        this.loved = false;
    }

    public Relation(Relation relation) {
        this.rate = relation.rate;
        this.loved = relation.loved;
    }


    public void setRelation(Relation relation){
        this.rate = relation.rate;
        this.loved = relation.loved;
        Log.i("T", "NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN");
        Log.i("T", this.loved + "");
    }
}
