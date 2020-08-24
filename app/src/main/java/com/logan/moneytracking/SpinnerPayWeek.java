package com.logan.moneytracking;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

public class SpinnerPayWeek extends SpinnerTotals {

    public SpinnerPayWeek(Activity a, Context c, DateObject d){
        activity = a;
        main_context = c;
        current_date = d;
    }

    //Call spinner_setup(int given_year, int given_spinner)


    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        current_date.setWeek(position + 1);
    }

}