package com.logan.moneytracking;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.logan.moneytracking.SpinnerTotals;
import com.logan.moneytracking.TotalActivity;
import com.logan.moneytracking.TotalHandler;

public class SpinnerWeek extends SpinnerTotals {

    public SpinnerWeek(Activity a, Context c, DateObject d){
        activity = a;
        main_context = c;
        current_date = d;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        //  System.out.println("POS " + position);
        //load_page.setCurrent_week(position + 1);
        current_date.setWeek(position + 1);
        //load_page.set_dates();
    }

}