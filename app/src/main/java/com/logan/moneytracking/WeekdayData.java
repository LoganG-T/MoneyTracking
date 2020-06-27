package com.logan.moneytracking;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeekdayData {
    String weekday;
    String month;
    //Maybe save the month of the day in here? and maybe even the date as well?
    SpendingData spending;

    public WeekdayData(){

    }

    public WeekdayData(String day){
        weekday = day;
        spending = new SpendingData();
    }
    public WeekdayData(String day, String given_month){
        weekday = day;
        month = given_month;
        spending = new SpendingData();
    }

    public String get_Weekday(){
        return weekday;
    }

    public void set_Weekday(String given_weekday){
        weekday = given_weekday;
    }
    public String get_Month(){
        return month;
    }

    public void set_Month(String given_month){
        weekday = given_month;
    }

    public SpendingData get_Spending(){
        return spending;
    }

    public void set_Spending(SpendingData given_spending){
        spending = given_spending;
    }
    public void set_Spending(JSONObject spending) throws JSONException {
        ArrayList<Float> f = new ArrayList<Float>();
        ArrayList<String> s = new ArrayList<String>();
        for(int i = 0; i < spending.getJSONArray("spending").length(); i++){
            f.add((float)spending.getJSONArray("spending").getDouble(i));
            s.add(spending.getJSONArray("spending").getString(i));
        }
        //Is this check neccesary?
        if(f.size() > 0 && s.size() > 0){
            SpendingData new_spend = new SpendingData(f, s);
        }
    }

    public String toString(){

        //{\"Monday\" : {\"spending\" :[20.00], \"notes\":[\"Takeaway\"] } } ] }
        return "{ \"weekday\":"  + "\"" + weekday + "\", \"month\":\"" + month + "\", \"day_spending\" :" + spending.toString() + "}";
    }

}
