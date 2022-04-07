package com.matthewyeung35.recipebookapp.objects;

public class Ingredient {
    float amount;
    String food;

    public Ingredient(float amount, String food) {
        this.amount = amount;
        this.food = food;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
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
