package com.logan.moneytracking;

enum Weekday{
    Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday
}

public class SpendingDataOld {
    float amount;
    String name;

    public SpendingDataOld(float given_amount, String given_name){
        amount = given_amount;
        name = given_name;
    }

    public float getAmount(){
        return amount;
    }

    public void setAmount(float given_amount){
        amount = given_amount;
    }

    public String getName(){
        return name;
    }

    public void setName(String given_name){
        name = given_name;
    }
}