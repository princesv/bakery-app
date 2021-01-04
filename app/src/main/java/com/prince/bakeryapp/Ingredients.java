package com.prince.bakeryapp;

import java.util.List;

public class Ingredients {
    List<Double> quantity;
    List<String> measure;
    List<String> ingredient;

    public Ingredients(List<Double> quantity, List<String> measure, List<String> ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public List<Double> getQuantity() {
        return quantity;
    }

    public void setQuantity(List<Double> quantity) {
        this.quantity = quantity;
    }

    public List<String> getMeasure() {
        return measure;
    }

    public void setMeasure(List<String> measure) {
        this.measure = measure;
    }

    public List<String> getIngredient() {
        return ingredient;
    }

    public void setIngredient(List<String> ingredient) {
        this.ingredient = ingredient;
    }
}
