package com.logan.moneytracking;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TotalHandler {
    final JsonHandler jsonHandler;
    Activity activity;
    DateObject current_date;

    public TotalHandler(Activity a, JsonHandler js, DateObject d){
        activity = a;
        jsonHandler = js;
        current_date = d;
    }

    public float get_day_total(String c_day, int c_week, int c_year) throws JSONException {
        float total = 0f;
        JSONObject jsonObject = jsonHandler.get_chosen_day(c_day, c_week, c_year);
        if(jsonObject == null){
            return total;
        }
        JSONArray ar = jsonObject.getJSONArray("spending");
        for(int i = 0; i < ar.length(); i++){
            total += Float.parseFloat(ar.getString(i));
        }


        return total;
    }
    public float get_day_total() throws JSONException {
        float total = 0;
        JSONObject jsonObject = jsonHandler.get_chosen_day(current_date.day, current_date.week, current_date.year);
        if(jsonObject == null){
            return 0f;
        }
        System.out.println(jsonObject.toString());
        JSONArray ar = jsonObject.getJSONArray("spending");
        for(int i = 0; i < ar.length(); i++){
            total += Float.parseFloat(ar.getString(i));
        }

        return total;
    }
    public float get_week_total() throws JSONException {
        float total = 0;
        JSONArray jsonWeek = jsonHandler.get_week(current_date.year, current_date.week);
        if(jsonWeek == null){
            return 0f;
        }
        for(int i = 0; i < jsonWeek.length(); i++){
            //JSONArray day_total = jsonWeek.getJSONObject(i).getJSONArray("spending");
            String day_s = jsonWeek.getJSONObject(i).getString("weekday");
            /*for(int c_day = 0; c_day < day_total.length(); c_day++){
                total += Float.parseFloat(day_total.getString(c_day));
            }*/
            total += get_day_total(day_s, current_date.week, current_date.year);
        }


        return total;
    }
    public float get_month_total(String month) throws JSONException {
        float total = 0;
        JSONObject year_obj = jsonHandler.get_year(current_date.year);
        if(year_obj == null){
            return 0f;
        }
        JSONArray all_days = year_obj.getJSONArray("days");
        for(int i = 0; i < all_days.length(); i++){
            for(int t = 0; t < all_days.getJSONArray(i).length(); t++){
                if(all_days.getJSONArray(i).getJSONObject(t).getString("month").equals(month)){
                    total += add_spending(all_days.getJSONArray(i).getJSONObject(t).getJSONObject("day_spending"));
                }
            }
        }


        return total;
    }
    public float get_year_total(){
        float total = 0;


        return total;
    }
    public float get_all_total() {
        float total = 0;


        return total;
    }

    public String return_date(){
        return current_date.getDay() + " " + String.valueOf(current_date.getWeek()) + " " + String.valueOf(current_date.getYear());
    }

    private float add_spending(JSONObject given_json) throws JSONException {
        float total = 0f;

        for(int i = 0; i < given_json.getJSONArray("spending").length(); i++){
            total += Float.parseFloat(given_json.getJSONArray("spending").getString(i));
        }

        return total;
    }

}
