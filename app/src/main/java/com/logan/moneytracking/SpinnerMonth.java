package com.logan.moneytracking;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.logan.R;

import org.json.JSONException;

import java.util.Calendar;

public class SpinnerMonth extends SpinnerTotals {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public SpinnerMonth(Activity a, Context c, DateObject d) throws JSONException {
        activity = a;
        main_context = c;
        current_date = d;
    }

    //boolean month_req = false;
    int r_id;
    SpinnerMonthDays day_spin;

    public void set_month_req(int r){
        r_id = r;
    }

    public void set_day_spinner(SpinnerMonthDays s){
        day_spin = s;
    }


    public void spinner_setup(int given_spinner){

        String[] months = new String[12];
        for(int i = 0; i < months.length; i++){
            months[i] = get_month(i);
        }

        Spinner spinner = (Spinner) activity.findViewById(given_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(main_context,
                android.R.layout.simple_spinner_item, months);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        //Need to minus 1 because it is zero-index'd
        spinner.setSelection(current_date.getMonthInt());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        //  System.out.println("POS " + position);
        //load_page.setCurrent_week(position + 1);
        //load_page.set_dates();

        current_date.setMonth(get_full_month(position));


        if(r_id != 0){
            day_spin.set_select();
            activity.findViewById(r_id).setVisibility(View.VISIBLE);
            day_spin.update_days();
        }

    }

}