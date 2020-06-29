package com.logan.moneytracking;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class SpinnerTotals extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public SpinnerTotals(Activity a, Context c){
        activity = a;
        main_context = c;
    }

    public SpinnerTotals(){
        activity = null;
        main_context = null;
    }

    public Activity activity;
    public Context main_context;
    public DateObject current_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("DROP WORK");
    }

    public void spinner_setup(int given_year, int given_spinner){
        Calendar calendar = Calendar.getInstance();
        int this_week = calendar.get(Calendar.WEEK_OF_YEAR);
        if(calendar.get(Calendar.DAY_OF_WEEK) == 1){
            this_week -= 1;
        }
        //Janurary is 0th month, December is 11th month
        calendar.set(given_year, 0, 1);
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);

        int first_monday = 1;
        for(int i = 0; i < 7; i++){
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            //Sunday is 1, Monday is 2,...,Saturday is 7
            if(calendar.get(Calendar.DAY_OF_WEEK) == 2){
                System.out.println("FIRST DAY = " + calendar.get(Calendar.DAY_OF_WEEK) + " " + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.DAY_OF_YEAR));
                first_monday =  calendar.get(Calendar.DAY_OF_MONTH);
                break;
            }
        }

        String[] drop_weeks = new String[calendar.getActualMaximum(Calendar.WEEK_OF_YEAR)];
        for(int i = 0; i < drop_weeks.length; i++){
            drop_weeks[i] = calendar.get(Calendar.DAY_OF_MONTH) + " " + get_month(calendar.get(Calendar.MONTH)) + " - ";
            calendar.add(Calendar.DAY_OF_WEEK, 6);
            drop_weeks[i] += calendar.get(Calendar.DAY_OF_MONTH) + " " + get_month(calendar.get(Calendar.MONTH));
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }

        Spinner spinner = (Spinner) activity.findViewById(given_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(main_context,
                android.R.layout.simple_spinner_item, drop_weeks);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        //Need to minus 1 because it is zero-index'd
        current_date.setWeek(this_week - 1);
        System.out.println("CURRENT WEEK SET TO " + this_week);
        spinner.setSelection(this_week - 1);
    }

    public String get_month(int month){
        switch(month){
            case 0:
                return "Jan";
            case 1:
                return "Feb";
            case 2:
                return "Mar";
            case 3:
                return "Apr";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "Aug";
            case 8:
                return "Sep";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            case 11:
                return "Dec";
        }
        return "December";

    }
    public String get_full_month(int month){
        switch(month){
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
        }
        return "December";

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        //  System.out.println("POS " + position);
        //load_page.setCurrent_week(position + 1);
        //load_page.set_dates();
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}