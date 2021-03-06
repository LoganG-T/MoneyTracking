package com.logan.budget;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.logan.R;
import com.logan.moneytracking.DateObject;
import com.logan.moneytracking.JsonHandler;
import com.logan.moneytracking.SaveLoad;

import org.json.JSONException;

import java.util.Calendar;

public class BudgetViewActivity extends AppCompatActivity {

    BudgetManager budgetManager;
    Calendar c;
    String cur_symb = "£";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_view);

        c = Calendar.getInstance();
        JsonHandler jsonHandler = null;
        try {
            SaveLoad sl = new SaveLoad();
            jsonHandler = new JsonHandler(this,"storage.json" , sl);
            budgetManager = new BudgetManager(jsonHandler);
            //Budget manager check if it exists
            if(budgetManager.is_file(getApplicationContext())){
                budgetManager.load_budget(this);
            }else{
                budgetManager.create_file(getApplicationContext());
            }

            if(budgetManager.is_empty()){
                Spinner spinner = (Spinner)findViewById(R.id.sp_budgetNames);
                spinner.setVisibility(View.INVISIBLE);
            }else {
                BudgetSpinner budgetSpinner = new BudgetSpinner(BudgetViewActivity.this, this, budgetManager);
                budgetSpinner.spinner_setup(R.id.sp_budgetNames);
            }
            set_currentDate();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void view_selected(View view){
        if(! budgetManager.is_empty()) {
            c = Calendar.getInstance();
            select_budget(view);
        }
    }

    private void set_currentDate(){
        String s = "";
        TextView textWeek = (TextView) findViewById(R.id.txt_selectedWeek);
        c.set(Calendar.DAY_OF_WEEK,2);
        DateObject dateObject = new DateObject(c);
        s += c.get(Calendar.DAY_OF_MONTH) + " " + dateObject.getMonth() + "-";
        Calendar c2 = (Calendar)c.clone();
        if(s.length() > 8){
            s+="\n";
        }
        c2.add(Calendar.DAY_OF_WEEK, 6);
        s += c2.get(Calendar.DAY_OF_MONTH);

        DateObject dateObject2 = new DateObject(c2);
        s += " " + dateObject2.getMonth();
        textWeek.setText(s + " " + dateObject.getYear());

    }

    public void select_budget(View view){
        set_currentDate();

        //Check if the date is before or after the budgets chosen start/end dates
        if(budgetManager.budget_ended(new DateObject(c))){
            System.out.println("BUDGET OVER");
            TextView textView = (TextView) findViewById(R.id.txt_remoney);
            textView.setText("Budget has already ended, view by week?");
            textView.setTextColor(Color.rgb(0,0,0));
        }else if(budgetManager.before_started(new DateObject(c))){
            TextView textView = (TextView) findViewById(R.id.txt_remoney);
            DateObject d = new DateObject(c);
            d.setDay("Monday");
            textView.setText("Budget starts in " + budgetManager.get_startDiff(d) + " weeks.");
            textView.setTextColor(Color.rgb(0,0,0));
        }
        //Date is between the allowed budgets dates
        else {
            DateObject d = new DateObject(c);
            DateObject current_date = new DateObject(Calendar.getInstance());
            String pay_display = "";
            TextView textView = (TextView) findViewById(R.id.txt_remoney);
            float f = 0;
            //displayed date is before the current week so display spent data
            if(d.before(current_date)){
                float temp_f = budgetManager.get_weekSpending(d);
                pay_display = Float.toString( temp_f) + " spent";
                pay_display += "  " + budgetManager.get_original_week_budget();
                if(temp_f > budgetManager.get_original_week_budget()){
                    textView.setTextColor(Color.rgb(255,50,50));
                }else{
                    textView.setTextColor(Color.rgb(50, 186, 86));
                }
            }else if(budgetManager.get_remainingBudget() <= 0){
                pay_display = "0 Budget has all been spent.";
                textView.setTextColor(Color.rgb(255,50,50));
            }
            //chosen date is current or future and allowed so display the allowed remaining budget spending
            else {
                f = budgetManager.get_current_week_budget(d);
                pay_display = Float.toString((int)(f * 100) / 100f);
                pay_display += "  " + budgetManager.get_original_week_budget();

            }
            textView.setText(cur_symb + pay_display);
            if(f < 0){
                textView.setTextColor(Color.rgb(255,0,0));
            }
            if(f > 0){
                textView.setTextColor(Color.rgb(0,200,0));
            }
            TextView textView_total = (TextView) findViewById(R.id.txt_total);
            textView_total.setText(cur_symb + Float.toString(budgetManager.get_totalBudget()));
            TextView textView_left = (TextView) findViewById(R.id.txt_tLeft);
            textView_left.setText(cur_symb + Float.toString(budgetManager.get_remainingBudget()));
            TextView textWeeks_left = (TextView) findViewById(R.id.txt_wksLeft);
            textWeeks_left.setText(Integer.toString(budgetManager.get_weeks_left()));
        }
    }

    public void prev_week(View view){
        if(! budgetManager.is_empty()) {
            c.add(Calendar.WEEK_OF_YEAR, -1);
            select_budget(view);
        }
    }

    public void next_week(View view){
        if(! budgetManager.is_empty()) {
            c.add(Calendar.WEEK_OF_YEAR, 1);
            select_budget(view);
        }
    }

}
