package com.logan.moneytracking;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.logan.R;

public class PaymentNotesSpinner extends NotesSpinner {

    public PaymentNotesSpinner(Activity a, Context c){
        activity = a;
        main_context = c;
    }

    TextView textView;

    public void setTextView(TextView tv){
        textView = tv;
    }


    public void update_output(){
        textView.setText(all_notes.get(latest_position));
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        latest_position = position;
        update_output();
    }

}
