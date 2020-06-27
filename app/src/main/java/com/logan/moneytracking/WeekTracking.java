package com.logan.moneytracking;

import java.util.ArrayList;

public class WeekTracking {

    ArrayList<WeekData> date;

    public void setDate(ArrayList<WeekData> given_data){
        date = given_data;
    }

    public void add_data(WeekData given_data){
        date.add(given_data);
    }

    public String toString(){
        return "{\"date\":" + date + "}";
    }
}
