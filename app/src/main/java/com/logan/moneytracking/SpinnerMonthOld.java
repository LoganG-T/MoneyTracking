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

public class SpinnerMonthOld extends SpinnerTotals {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("DROP WORK");

    }

    public SpinnerMonthOld(Activity a, Context c, TotalHandler t) throws JSONException {
        activity = a;
        main_context = c;
        totalHandler = t;
        textView = (TextView)activity.findViewById(R.id.monthTotalDisplay);
        textView.setText(String.valueOf(totalHandler.get_month_total(get_full_month(1))));
    }
    TotalHandler totalHandler;
    TextView textView;

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
        spinner.setSelection(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        //  System.out.println("POS " + position);
        //load_page.setCurrent_week(position + 1);
        try {
            if(textView != null) {
                textView.setText(String.valueOf(totalHandler.get_month_total(get_full_month(position))));
                System.out.println(String.valueOf(totalHandler.get_month_total(get_full_month(position))));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //load_page.set_dates();
    }

}