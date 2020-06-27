package com.logan.moneytracking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeekData {
    int year;
    ArrayList<Integer> week;
    ArrayList<WeekdayData[]> days;

    public void setYear(int given_year){
        year = given_year;
    }
    public void setWeek(ArrayList<Integer> given_week_nums){
        week = given_week_nums;
    }
    public void setWeek(JSONArray given_week_nums) throws JSONException {
        week = new ArrayList<Integer>();
        for(int i = 0; i < given_week_nums.length(); i++){
            week.add(given_week_nums.getInt(i));
        }
    }
    public void setDays(ArrayList<WeekdayData[]> given_data){
        days = given_data;
    }
    public void setDays(JSONArray given_data) throws JSONException {
        days = new ArrayList<WeekdayData[]>();
        for(int i = 0; i < given_data.length(); i++){
            WeekdayData[] new_week = new WeekdayData[7];
            for(int w = 0; w < 7; w++){
                new_week[0] = new WeekdayData();
                new_week[0].set_Weekday(given_data.getJSONObject(i).get("weekday").toString());
                new_week[0].set_Month(given_data.getJSONObject(i).get("month").toString());
                new_week[0].set_Spending(given_data.getJSONObject(i).getJSONObject("day_spending"));
            }
            days.add(new_week);
        }
    }

    public int getYear(){
        return year;
    }

    public ArrayList<Integer> getWeek() {
        return week;
    }

    public ArrayList<WeekdayData[]> getDays() {
        return days;
    }

    private String get_days_string(){

        String days_string = "";
        for(int i = 0; i < days.size(); i++){
            int week_count = 0;
            if(days.get(i) != null){
                days_string += "[";
            }
            for(int j = 0; j < 7; j++) {
                WeekdayData d = days.get(i)[j];
                if(d != null) {
                    if(week_count > 0){
                        days_string += ",";
                    }
                    days_string += days.get(i)[j].toString();
                    week_count += 1;
                }
            }
            if(days.get(i) != null){
                days_string += "]";
            }
        }

        return days_string;
    }

    public String toString(){
        //{ \"year\":2020, \"week\":[1]," +
        //                " days:[ {\"Monday\" : {\"spending\" :[20.00], \"notes\":[\"Takeaway\"] } } ]" +
        //                " }
        return "{\"year\":" + year + ",\"week\":" + week.toString() + ", \"days\":" + get_days_string() + "}";
    }
}
