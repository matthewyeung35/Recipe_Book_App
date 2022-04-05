package com.matthewyeung35.recipebookapp;

public class Ingredient {
    int amount;
    String food;

    public Ingredient(int amount, String food) {
        this.amount = amount;
        this.food = food;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    @Override
    public String toString() {

        return "Ingredient{" +
                "amount=" + amount +
                ", food='" + food + '\'' +
                '}';
    }
}
