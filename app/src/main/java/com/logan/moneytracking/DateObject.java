package com.logan.moneytracking;

import android.provider.CalendarContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class DateObject {

    public DateObject(){
        day = "Monday";
        month = "January";
        week = 0;
        year = 0;
    }
    public DateObject(Calendar c){
        setDay(c.get(Calendar.DAY_OF_WEEK) - 1);
        month = getMonthString(c.get(Calendar.MONTH));
        week = c.get(Calendar.WEEK_OF_YEAR);
        year = c.get(Calendar.YEAR);
    }
    public DateObject(JSONObject json) throws JSONException {
        day = json.getString("day");
        month = json.getString("month");
        week = json.getInt("week");
        year = json.getInt("year");
    }
    public DateObject(String d, int w, int y){
        day = d;
        week = w;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,y);
        calendar.set(Calendar.WEEK_OF_YEAR, w);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        month = getMonthString(calendar.get(Calendar.MONTH));
        year = y;
    }
    public DateObject(int d, int w, int y){
        setDay(d);
        week = w;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,y);
        calendar.set(Calendar.WEEK_OF_YEAR, w);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        month = getMonthString(calendar.get(Calendar.MONTH));
        year = y;
    }

    public DateObject(DateObject d){
        day = d.getDay();
        week = d.getWeek();
        month = d.getMonth();
        year = d.getYear();
    }

    public boolean greaterThan(DateObject given_date){
        if(year > given_date.getYear()){
            return true;
        }
        else if(year < given_date.getYear()){
            return false;
        } else{
            //years are equal
            if(week > given_date.getWeek()){
                return true;
            }
            else if(week < given_date.getWeek()){
                return false;
            }
            else{
                //Weeks are the same
                return false;
            }
        }
    }

    public boolean before(DateObject given_date){
        if(year < given_date.getYear()){
            return true;
        }
        else if(year > given_date.getYear()){
            return false;
        } else{
            //years are equal
            if(week < given_date.getWeek()){
                return true;
            }
            else if(week > given_date.getWeek()){
                return false;
            }
            else{
                //Weeks are the same
                return false;
            }
        }
    }

    public int weekDifference(DateObject given_date){
        if(year == given_date.getYear()){
            if(week > given_date.getWeek()){
                return (week - given_date.getWeek()) + 1;
            }else{
                return (given_date.getWeek() - week) + 1;
            }
        }else{
            return 1;
        }
    }

    public void addWeek(int amount){
        week++;

        if(week >= 53){
            week = 0;
            year++;
        }
    }

    String day;
    String month;
    int week;
    int year;

    public String getDay(){
        return day;
    }

    public int getWeek() {
        return week;
    }

    public String getMonth() {
        return month;
    }

    //Returns 0-based current month as int
    public int getMonthInt() {
        switch (month){
            case("January"):
                return 0;
            case("February"):
                return 1;
            case("March"):
                return 2;
            case("April"):
                return 3;
            case("May"):
                return 4;
            case("June"):
                return 5;
            case("July"):
                return 6;
            case("August"):
                return 7;
            case("September"):
                return 8;
            case("October"):
                return 9;
            case("November"):
                return 10;
            case("December"):
                return 11;

        }
        return 0;
    }
    public String getMonthString(int value) {
        switch (value){
            case(0):
                return "January";
            case(1):
                return "February";
            case(2):
                return "March";
            case(3):
                return "April";
            case(4):
                return "May";
            case(5):
                return "June";
            case(6):
                return "July";
            case(7):
                return "August";
            case(8):
                return "September";
            case(9):
                return "October";
            case(10):
                return "November";
            case(11):
                return "December";

        }
        return "January";
    }

    public int getYear() {
        return year;
    }

    public void setDay(String day) {
        this.day = day;
    }
    public void setDay(int day_index) {
        switch(day_index){
            case(1):
                this.day = "Monday";
                break;
            case(2):
                this.day = "Tuesday";
                break;
            case(3):
                this.day = "Wednesday";
                break;
            case(4):
                this.day = "Thursday";
                break;
            case(5):
                this.day = "Friday";
                break;
            case(6):
                this.day = "Saturday";
                break;
            case(7):
                this.day = "Sunday";
                break;
            default:
                this.day = "Sunday";
        }
    }

    public void setMonth(String m) {
        month = m;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString(){
        String r_string = "{";

        r_string += "\"day\":\"" + day + "\",";
        r_string += "\"month\":\"" + month + "\",";
        r_string += "\"week\":" + week + ",";
        r_string += "\"year\":" + year + "";

        r_string += "}";
        return r_string;
    }
}
