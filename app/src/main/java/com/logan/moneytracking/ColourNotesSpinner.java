package com.logan.moneytracking;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

public class ColourNotesSpinner extends NotesSpinner {

    public ColourNotesSpinner(Activity a, Context c){
        activity = a;
        main_context = c;
    }

    String note;


    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        latest_position = position;
        note = all_notes.get(position);
    }
    public String getNote(){
        return  note;
    }

}
