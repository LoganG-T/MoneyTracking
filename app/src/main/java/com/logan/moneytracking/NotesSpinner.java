package com.logan.moneytracking;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.logan.R;

import java.util.ArrayList;


public class NotesSpinner implements AdapterView.OnItemSelectedListener{

    public NotesSpinner(){
        activity = null;
        main_context = null;
    }

    public NotesSpinner(Activity a, Context c){
        activity = a;
        main_context = c;
    }

    public Activity activity;
    public Context main_context;
    ArrayAdapter<String> adapter = null;
    ArrayList<String> all_notes;
    NotesFunctions notesFunctions;
    String cur_symb = "£";
    int latest_position;


    public void spinner_setup(int given_spinner){

        Spinner spinner = (Spinner) activity.findViewById(given_spinner);
        adapter = new ArrayAdapter<String>(main_context,
                android.R.layout.simple_spinner_item, all_notes);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        //Need to minus 1 because it is zero-index'd
        spinner.setSelection(0);
    }

    public void update_notes(){
        if(adapter != null) {
            adapter.clear();
            adapter.addAll(all_notes);
        }
    }

    public void Set_AllNotes(ArrayList<String> s){
        all_notes = s;
        update_notes();
    }

    public void set_NotesFunc(NotesFunctions nf){
        notesFunctions = nf;
    }


    public void update_output(){
        TextView textView = (TextView)activity.findViewById(R.id.note_display);
        textView.setText(cur_symb + String.valueOf(notesFunctions.Get_NotesTotal(all_notes.get(latest_position))));
        TextView textView2 = (TextView)activity.findViewById(R.id.note_total);
        textView2.setText("out of " + cur_symb + String.valueOf(notesFunctions.Get_TotalSpending()));

        TextView textPercent = (TextView)activity.findViewById(R.id.note_percent);
        float perc = notesFunctions.Get_NotesTotal(all_notes.get(latest_position)) / notesFunctions.Get_TotalSpending();
        perc *= 10000;
        perc = (int)perc;
        perc = (perc / 100f);
        textPercent.setText(String.valueOf(perc) + "% of spending");
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        //  System.out.println("POS " + position);
        //load_page.setCurrent_week(position + 1);
        //load_page.set_dates();
        latest_position = position;
        update_output();
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
