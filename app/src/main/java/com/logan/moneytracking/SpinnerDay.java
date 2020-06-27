package com.logan.moneytracking;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SpinnerDay extends SpinnerTotals {

    public SpinnerDay(Activity a, Context c, DateObject d){
        activity = a;
        main_context = c;
        current_date = d;
    }

    public void spinner_setup(int given_spinner){

        String[] days = new String[7];
        for(int i = 1; i <= days.length; i++){
            current_date.setDay(i);
            days[i - 1] = current_date.getDay();
        }
        current_date.setDay("Monday");

        Spinner spinner = (Spinner) activity.findViewById(given_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(main_context,
                android.R.layout.simple_spinner_item, days);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        //Need to minus 1 because it is zero-index'd
        spinner.setSelection(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        //  System.out.println("POS " + position);
        //load_page.setCurrent_week(position + 1);
        current_date.setDay(position + 1);
        //load_page.set_dates();
    }

}