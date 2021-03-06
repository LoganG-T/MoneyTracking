package com.logan.moneytracking;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.logan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class NotesActivity extends AppCompatActivity {

    NotesSpinner notesSpinner;
    DateObject current_date;
    JsonHandler js;
    GraphDraw g_draw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        js = new JsonHandler(getApplicationContext(), "storage.json");
        NotesFunctions year_notes = new NotesFunctions();
        try {
            JSONObject year_obj = js.get_year(2020);
            int i_count = year_obj.getJSONArray("week").length();
            JSONArray year_arr = year_obj.getJSONArray("days");
            for(int i = 0; i < i_count; i++){
                year_notes.Add_WeekNotes(year_arr.getJSONArray(i));
            }
            notesSpinner = new NotesSpinner(this, getApplicationContext());

            //Creating thread to wait on the spinner data to be loaded before set
            WaitThread t = new WaitThread(notesSpinner, year_notes);
            t.start();
            t.join();

            notesSpinner.set_NotesFunc(year_notes);
            notesSpinner.spinner_setup(R.id.notes_spinner);

            Calendar c = Calendar.getInstance();
            current_date = new DateObject(c.get(Calendar.DAY_OF_WEEK), c.get(Calendar.WEEK_OF_YEAR), c.get(Calendar.YEAR));

            SpinnerMonth spinnerMonth = new SpinnerMonth(this, getApplicationContext(), current_date);
            spinnerMonth.spinner_setup(R.id.n_monthSpin);
            SpinnerYear spinnerYear = new SpinnerYear(this, getApplicationContext(), current_date);
            spinnerYear.spinner_setup(current_date.getYear(), R.id.n_yearSpin);

            g_draw = findViewById(R.id.graph_draw);

            g_draw.setLayoutParams(new LinearLayout.LayoutParams((g_draw.width/4) + 10,(g_draw.width/4) + 10));


            update_pageData();

        } catch (JSONException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    //Thread class to wait for the full noteSpinner data to be obtained before trying to load the spinner
    private class WaitThread extends Thread{
        NotesSpinner notesSpinner;
        NotesFunctions year_notes;
        WaitThread(NotesSpinner ns, NotesFunctions nf){
            notesSpinner = ns;
            year_notes = nf;
        }

        public void run(){
            notesSpinner.Set_AllNotes(year_notes.Get_NotesNames());
        }
    }

    public void update_pageData(){
        NotesFunctions nf = new NotesFunctions();
        try {
            JSONObject year_obj = js.get_year(current_date.getYear());
            if(year_obj == null){
                //return;
            }else {
                int i_count = year_obj.getJSONArray("week").length();
                JSONArray year_arr = year_obj.getJSONArray("days");
                for (int i = 0; i < i_count; i++) {
                    nf.Add_MonthNotes(year_arr.getJSONArray(i), current_date.getMonth());
                }
            }
            notesSpinner.set_NotesFunc(nf);
            notesSpinner.Set_AllNotes(nf.Get_NotesNames());
            notesSpinner.update_output();

            if(g_draw != null){
                g_draw.Set_PieChart(nf.Get_All_Percents(), nf.Get_NotesNames_Array());
            }

            LinearLayout gLegend = findViewById(R.id.lin_gLegends);
            gLegend.setBackgroundColor(Color.rgb(5,5,5));
            gLegend.removeAllViews();
            for(int i = 0; i < nf.Get_NotesNames_Array().length; i++){
                TextView textView = new TextView(getApplicationContext());
                textView.setText(nf.Get_NotesNames_Array()[i]);
                textView.setTextColor(g_draw.Get_Percent_Color(i));
                gLegend.addView(textView);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void confirm_dates(View view) {
        update_pageData();
        g_draw.Draw_Again();
    }


}
