package com.matthewyeung35.recipebookapp.objects;

import java.util.ArrayList;

public class Recipe {
    int id;
    String name;
    //TODO implent image method
    String image;
    int serving;
    ArrayList<Ingredient> ingredients;
    String shotDesc;
    String steps;
    String comments;
    boolean favourite;

    public Recipe(int id, String name, String image, int serving, ArrayList<Ingredient> ingredients, String shotDesc, String steps, String comments, boolean favourite) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.serving = serving;
        this.ingredients = ingredients;
        this.shotDesc = shotDesc;
        this.steps = steps;
        this.comments = comments;
        this.favourite = favourite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getServing() {
        return serving;
    }

    public void setServing(int serving) {
        this.serving = serving;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getShotDesc() {
        return shotDesc;
    }

    public void setShotDesc(String shotDesc) {
        this.shotDesc = shotDesc;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
}
