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

    public String getAmountStr(){
        // return a string value of amount. if it's a full number, return without the .0
        String [] arrayOfFloatInput = String.valueOf(amount).split("\\.");
        if (arrayOfFloatInput[1].equals("0")){
            return arrayOfFloatInput[0];
        }else{
            return String.valueOf(amount);
        }
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
