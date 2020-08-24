package com.logan.moneytracking;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;

public class SpinnerMonthDays extends SpinnerTotals {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public SpinnerMonthDays(Activity a, Context c, DateObject d) throws JSONException {
        activity = a;
        main_context = c;
        current_date = d;
    }

    boolean month_req = false;
    Calendar c;
    int r_id;
    ArrayAdapter<String> adapter;
    int spin;
    int last_selected = 0;

    public void update_days(){
        if(adapter != null) {
            adapter.clear();
            adapter.addAll(get_days());
        }
    }

    public void set_select(){
        update_date(last_selected);
    }

    public ArrayList<String> get_days(){
        c = Calendar.getInstance();
        c.set(Calendar.YEAR, current_date.getYear());
        c.set(Calendar.MONTH, current_date.getMonthInt());
        c.set(Calendar.DAY_OF_MONTH, 1);

        int days_month = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        ArrayList<String> days = new ArrayList<String>();
        for(int i = 1; i <= days_month; i++){
            days.add(Integer.toString(i));
        }
        return days;
    }


    //Given a spinner r.id as int and given the days of the month to add as drop-down values
    public void spinner_setup(int given_spinner){
        spin = given_spinner;

        ArrayList<String> days = get_days();
        Spinner spinner = (Spinner) activity.findViewById(given_spinner);
        adapter = new ArrayAdapter<String>(main_context,
                android.R.layout.simple_spinner_item, days);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void update_date(int position){
        c.set(Calendar.YEAR, current_date.getYear());
        c.set(Calendar.MONTH, current_date.getMonthInt());
        c.set(Calendar.DAY_OF_MONTH, position + 1);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK);
        day_of_week--;
        if(day_of_week == 0){
            day_of_week = 7;
            current_date.setWeek(c.get(Calendar.WEEK_OF_YEAR) - 1);
        }
        else{
            current_date.setWeek(c.get(Calendar.WEEK_OF_YEAR));
        }
        current_date.setDay(day_of_week);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        update_date(position);

        last_selected = position;

        if(month_req){
            activity.findViewById(r_id).setVisibility(View.VISIBLE);
        }
    }

}