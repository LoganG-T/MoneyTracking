package com.logan.budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.logan.R;
import com.logan.moneytracking.DateObject;
import com.logan.moneytracking.JsonHandler;
import com.logan.moneytracking.SaveLoad;
import com.logan.moneytracking.SpinnerMonth;
import com.logan.moneytracking.SpinnerMonthDays;
import com.logan.moneytracking.SpinnerYear;

import org.json.JSONException;

import java.util.Calendar;

public class BudgetActivity extends AppCompatActivity {

    JsonHandler jsonHandler;
    DateObject start_date;
    DateObject end_date;
    int current_year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        SaveLoad sl = new SaveLoad();
        sl.Load_Data(getApplicationContext(), "storage.json");

        try {
            jsonHandler = new JsonHandler(getApplicationContext(), "storage.json", sl);
            Calendar calendar = Calendar.getInstance();
            current_year = calendar.get(Calendar.YEAR);
            start_date = new DateObject(calendar.get(Calendar.DAY_OF_WEEK), calendar.get(Calendar.WEEK_OF_YEAR), current_year);
            end_date = new DateObject(calendar.get(Calendar.DAY_OF_WEEK), calendar.get(Calendar.WEEK_OF_YEAR) + 1, current_year);

            //Set the spinners for the start day, month year
            set_year_spinner(R.id.start_year, R.id.start_month, start_date);
            set_year_spinner(R.id.end_year, R.id.end_month, end_date);

            set_month_spinner(R.id.start_month, R.id.start_day, start_date);
            set_month_spinner(R.id.end_month, R.id.end_day, end_date);

            //Spinner spin = (Spinner) findViewById(R.id.start_month);

        } catch (JSONException e) {
            //Large error here the program is no good
        }
    }

    private void set_year_spinner(int r_id, int month_id, DateObject given_date){
        SpinnerYear spinnerYear = new SpinnerYear(BudgetActivity.this, getApplicationContext(), given_date);
        spinnerYear.spinner_setup(current_year, r_id);
        spinnerYear.set_month_req(month_id);
    }

    private void set_month_spinner(int r_id, int day_id, DateObject given_date) throws JSONException {
        SpinnerMonth spinnerMonth = new SpinnerMonth(BudgetActivity.this, getApplicationContext(), given_date);
        spinnerMonth.spinner_setup(r_id);
        spinnerMonth.set_month_req(day_id);
        Spinner spin = (Spinner) findViewById(r_id);
        spin.setVisibility(View.INVISIBLE);

        SpinnerMonthDays spinnerDays = new SpinnerMonthDays(BudgetActivity.this, getApplicationContext(), given_date);
        spinnerMonth.set_day_spinner(spinnerDays);
        spinnerDays.spinner_setup(day_id);
        findViewById(day_id).setVisibility(View.INVISIBLE);
    }

    public void confirm_button(View view){
        TextView textAmount = (TextView)findViewById(R.id.bd_week_amount);
        TextView textLeft = (TextView)findViewById(R.id.bd_weeks_left);
        EditText editText = (EditText)findViewById(R.id.bd_editBudget);
        EditText editName = (EditText)findViewById(R.id.bd_budgetName);
        if(editName.getText().toString().isEmpty()){
            //Display error saying it cannot be empty
            System.out.println("NO EMPTY ALLOWED");
            return;
        }

        if(editText.getText().toString().isEmpty()){
            //Display error saying cannot be empty
            return;
        }
        Float amount = 0f;

        RadioGroup rg = (RadioGroup) findViewById(R.id.rdg_budget);

        int rd_id = rg.getCheckedRadioButtonId();
        switch (rd_id){
            case R.id.rd_total:
                amount = ((int)Float.parseFloat(editText.getText().toString()) * 100 ) / 100f;
                break;
            case R.id.rd_week:
                amount = ((int)Float.parseFloat(editText.getText().toString()) * 100 ) / 100f;
                amount *= (end_date.weekDifference(start_date));
                break;
        }
        BudgetManager budgetManager = new BudgetManager(jsonHandler);


        if(! budgetManager.is_file(this)){
            budgetManager.create_file(this);
            System.out.println("File created");
        }else{
            budgetManager.load_budget(this);
        }

        if(budgetManager.name_taken(editName.getText().toString())){
            //Display error here
            System.out.println("Name taken");
            return;
        }

        budgetManager.set_new_budget(editName.getText().toString(), amount, start_date, end_date);



        budgetManager.set_jsonHandler(jsonHandler);
        textAmount.setText(Float.toString(budgetManager.get_week_budget(start_date.getYear(), start_date.getWeek())));
        textLeft.setText(Integer.toString(budgetManager.get_weeks_left()));
        budgetManager.save_budget(this);
        System.out.println("Saved");
        System.out.println(budgetManager.load_budget(this));

        //System.out.println(budgetManager.);

    }

}
