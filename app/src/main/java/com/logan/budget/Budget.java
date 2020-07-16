package com.logan.budget;

import android.provider.CalendarContract;

import com.logan.moneytracking.DateObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class Budget {
    String name;
    float total_budget;
    float left_budget;//The amount spent of the total budget so far -> 50 if £50 has been spent
    DateObject start_date;
    DateObject end_date;
    int term_time;//Weeks total for the budget
    int weeks_gone = 0;
    boolean ended = false;

    public Budget(String s, float f, DateObject start, DateObject end){
        name = s;
        total_budget = f;
        left_budget = 0;
        start_date = start;
        end_date = end;
        System.out.println("START " + start.getYear() + " " + start.getMonth() + "(" + start.getWeek() + ")" + " "+ start.getDay());
        System.out.println("END " + end.getYear() + " " + end.getMonth() + "(" + end.getWeek() + ")" + " " + end.getDay());
        if(start_date.getYear() == end_date.getYear()){
            //Make sunday the same week not week starter
            term_time = (end_date.getWeek() - start_date.getWeek()) + 1;
        }else{
            term_time = calculate_term_time(start_date, end_date);
        }
        System.out.println("WEEKS ARE " +term_time);
    }

    public Budget(JSONObject json) throws JSONException {
        name = json.getString("name");
        total_budget = Float.valueOf(json.getString("total_budget"));
        left_budget = Float.valueOf(json.getString("left_budget"));
        start_date = new DateObject(json.getJSONObject("start_date"));
        end_date = new DateObject(json.getJSONObject("end_date"));
        term_time = json.getInt("term_time");
        weeks_gone = json.getInt("weeks_gone");
        update_week_auto();
    }

    /*public void recieve(float amount){
        left_budget -= amount;
    }*/

    public void update_spending(float amount){
        left_budget = amount;
    }

    public void spend(float amount){
        left_budget += amount;
    }

    public void set_term(int t){
        if(t <= 0){
            t = 1;
        }
        term_time = t;
    }

    public void update_week(){
        weeks_gone += 1;
    }

    public void update_week_auto(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,start_date.getYear());
        c.set(Calendar.WEEK_OF_YEAR,start_date.getWeek());
        Calendar c2 = Calendar.getInstance();
        int weeks = c2.get(Calendar.WEEK_OF_YEAR);
        if(c2.get(Calendar.YEAR) == start_date.getYear()){
            //Same year
            weeks_gone = c2.get(Calendar.WEEK_OF_YEAR) - start_date.getWeek();
        }
        else{
            //different years
        }
        //weeks_gone += 1;
    }

    public float get_remainingBudget(){
        return total_budget - left_budget;
    }

    public float getTotal_budget(){
        return total_budget;
    }

    public float get_original_weekly_budget(){
        if(term_time <= 1){
            return (int)(total_budget / (float)weeks_gone * 100);
        }
        int x = (int)(total_budget / (float)(term_time + weeks_gone) * 100);
        return (float)x / 100f;
    }

    public float get_weekly_budget(){
        //returns the termly payments for the budget
        if(term_time <= 1){
            return total_budget;
        }
        int x = (int)(total_budget / (float)term_time * 100);
        return (float)x / 100f;
    }

    public float get_last_week(){
        //returns the last amount of money to add the total up if there was spare change
        int x = (int)(total_budget / (float)term_time * 100);
        float num = (float)x / 100f;
        return total_budget - (num * (term_time - 1));
    }

    public float get_current_week(){
        //returns the termly payments for the budget
        int week = (term_time - weeks_gone);
        if(week <= 1){
            return get_current_last();
        }else {
            int x = (int) ((total_budget - left_budget) / (float) week * 100);
            return (float) x / 100f;
        }
    }

    public int time_left(){
        return term_time - weeks_gone;
    }

    public String getString(){
        return "{\"name\": \"" + name + "\", \"total_budget\":\"" +total_budget + "\",\"left_budget\":\"" + left_budget + "\"" +
                ",\"start_date\":" + start_date.toString() + ",\"end_date\":" + end_date.toString() + "," +
                "\"term_time\":" + term_time + ", \"weeks_gone\":" + weeks_gone + "}";
    }



    private float get_current_last(){
        //returns the last amount of money to add the total up if there was spare change
        return total_budget - left_budget;
    }

    private int calculate_term_time(DateObject start, DateObject end){
        return start.weekDifference(end);
    }

}
