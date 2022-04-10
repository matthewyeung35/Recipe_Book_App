package com.matthewyeung35.recipebookapp.objects;

import java.util.ArrayList;

public class Recipe {
    int id;
    String name;
    byte[] image;
    int serving;
    ArrayList<Ingredient> ingredients;
    String shortDesc;
    String steps;
    String comments;
    boolean favourite;

    public Recipe(int id, String name, byte[] image, int serving, ArrayList<Ingredient> ingredients, String shortDesc, String steps, String comments, boolean favourite) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.serving = serving;
        this.ingredients = ingredients;
        this.shortDesc = shortDesc;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
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

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
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
