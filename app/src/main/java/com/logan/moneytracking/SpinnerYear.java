package com.logan.moneytracking;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SpinnerYear extends SpinnerTotals {

    public SpinnerYear(Activity a, Context c, DateObject d){
        activity = a;
        main_context = c;
        current_date = d;
    }
    int start_year;
    int r_id;

    public void set_month_req(int r){
        r_id = r;
    }

    public void spinner_setup(int given_year, int given_spinner){
        start_year = given_year - 20;

        String[] years = new String[41];
        for(int i = 0; i < years.length; i++){
            years[i] = String.valueOf(given_year - 20 + i);
        }

        Spinner spinner = (Spinner) activity.findViewById(given_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(main_context,
                android.R.layout.simple_spinner_item, years);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        spinner.setSelection((int)(years.length / 2f));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        current_date.setYear(start_year + position);
        if(r_id != 0) {
            activity.findViewById(r_id).setVisibility(View.VISIBLE);
        }
    }

}