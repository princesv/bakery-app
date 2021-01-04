package com.prince.bakeryapp;

import java.util.List;

public class RecipeList {
    List<Integer> ids;
    List<String> names;
    List<String> ingredients;
    List<String> steps;
    List<Integer> servings;

    public RecipeList(List<Integer> ids, List<String> names, List<String> ingredients, List<String> steps, List<Integer> servings) {
        this.ids = ids;
        this.names = names;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public List<Integer> getServings() {
        return servings;
    }

    public void setServings(List<Integer> servings) {
        this.servings = servings;
    }
}
