package com.logan.moneytracking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.logan.R;

import org.json.JSONException;

import java.util.Calendar;

public class TotalActivity extends AppCompatActivity {

    JsonHandler jsonHandler;
    TotalHandler totalHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        SaveLoad sl = new SaveLoad();
        sl.Load_Data(getApplicationContext(), "storage.json");

        try {
            jsonHandler = new JsonHandler(getApplicationContext(), "storage.json", sl);
            Calendar calendar = Calendar.getInstance();
            DateObject dateObject = new DateObject(calendar.get(Calendar.DAY_OF_WEEK), calendar.get(Calendar.WEEK_OF_YEAR), calendar.get(Calendar.YEAR));
            totalHandler = new TotalHandler(TotalActivity.this , jsonHandler, dateObject);
            SpinnerDay dt = new SpinnerDay(TotalActivity.this, this, dateObject);
            dt.spinner_setup(R.id.day_spin);
            SpinnerWeek st = new SpinnerWeek(TotalActivity.this, this, dateObject);
            st.spinner_setup(2020, R.id.week_spin);
            SpinnerYear dd = new SpinnerYear(TotalActivity.this, this, dateObject);
            dd.spinner_setup(2020, R.id.year_spin);
            SpinnerMonthOld spinnerMonth = new SpinnerMonthOld(TotalActivity.this, this, totalHandler);
            spinnerMonth.spinner_setup(R.id.month_total);
            total_display();
        } catch (JSONException e) {
            //Large error here the program is no good
        }
        //String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        //TextView textView = findViewById(R.id.textView);
        //textView.setText(message);
    }

    /*
        public void load_total_buttons(final TotalHandler totalHandler){
        Button day_button = (Button)activity.findViewById(R.id.day_total);
        day_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                totalHandler.get_day_total(current_day, current_week, current_year);
            }
        });
    }
     */

    private void total_display(){
        TextView textView = (TextView)findViewById(R.id.totalDisplay);
        try {
            textView.setText(String.valueOf(totalHandler.get_day_total("Monday", 1 ,2020)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void day_click(View view){
        TextView textView = (TextView)findViewById(R.id.totalDisplay);
        try {
            textView.setText(String.valueOf(totalHandler.get_day_total()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void week_click(View view){
        TextView textView = (TextView)findViewById(R.id.weekTotalDisplay);
        try {
            textView.setText(String.valueOf(totalHandler.get_week_total()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
