package com.logan.moneytracking;

import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;

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
            WaitThread t = new WaitThread(notesSpinner, year_notes);
            t.start();
            t.join();

            notesSpinner.set_NotesFunc(year_notes);
            notesSpinner.spinner_setup(R.id.notes_spinner);
            System.out.println(year_notes.Get_NotesTotal("electric"));
            System.out.println("TESCO " + year_notes.Get_NotesTotal("Tesco"));

            Calendar c = Calendar.getInstance();
            current_date = new DateObject("Monday", c.get(Calendar.WEEK_OF_YEAR), c.get(Calendar.YEAR));

            SpinnerMonth spinnerMonth = new SpinnerMonth(this, getApplicationContext(), current_date);
            spinnerMonth.spinner_setup(R.id.n_monthSpin);
            SpinnerYear spinnerYear = new SpinnerYear(this, getApplicationContext(), current_date);
            spinnerYear.spinner_setup(current_date.getYear(), R.id.n_yearSpin);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

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

    public void confirm_dates(View view) {
        NotesFunctions nf = new NotesFunctions();
        try {
            JSONObject year_obj = js.get_year(current_date.getYear());
            if(year_obj == null){
                return;
            }
            int i_count = year_obj.getJSONArray("week").length();
            JSONArray year_arr = year_obj.getJSONArray("days");
            for (int i = 0; i < i_count; i++) {
                nf.Add_MonthNotes(year_arr.getJSONArray(i), current_date.getMonth());
            }
            notesSpinner.set_NotesFunc(nf);
            notesSpinner.update_output();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
