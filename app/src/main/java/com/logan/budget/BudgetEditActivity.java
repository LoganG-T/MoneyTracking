package com.logan.budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.logan.R;
import com.logan.moneytracking.JsonHandler;
import com.logan.moneytracking.SaveLoad;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class BudgetEditActivity extends AppCompatActivity {

    BudgetManager budgetManager;
    JsonHandler jsonHandler;
    int et_budName = R.id.bd_budgetName;
    int et_budAmount = R.id.bd_amount;
    int sp_Syear = R.id.start_year;
    int sp_Eyear = R.id.end_year;
    int sp_Smonth = R.id.start_month;
    int sp_Emonth = R.id.end_month;
    int sp_Sday = R.id.start_day;
    int sp_Eday = R.id.end_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_edit);

        jsonHandler  = new JsonHandler(getApplicationContext(), "storage.json");
        budgetManager = new BudgetManager(jsonHandler);
        if(budgetManager.is_file(getApplicationContext())) {
            budgetManager.load_budget(getApplicationContext());
        }else{
            budgetManager.create_file(getApplicationContext());
        }

        BudgetSpinner budgetSpinner = new BudgetSpinner(this, getApplicationContext(), budgetManager);

        budgetSpinner.spinner_setup(R.id.spin_budgetName);
        budgetSpinner.Set_EditActivity(this);

    }

    //{"name":"Budget","total_budget":"500.0","left_budget":"0.0","start_date":{"day":"Monday","month":"June","week":23,"year":2020},"end_date":{"day":"Tuesday","month":"September","week":36,"year":2020},"term_time":14,"weeks_gone":0}
    public void Display_Budget(String given_name){
        try {
            System.out.println(budgetManager.find_budget(given_name).toString());
            JSONObject budget_json = budgetManager.find_budget(given_name);
            TextView t_name = (TextView)findViewById(et_budName);
            t_name.setText(budget_json.getString("name"));
            TextView t_num = (TextView)findViewById(et_budAmount);
            t_num.setText(budget_json.getString("total_budget"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Set_DateSpinners(){

    }

    public void Confirm_Edit(View view){
        TextView t_name = (TextView)findViewById(et_budName);
        boolean x = budgetManager.update_budget("name", t_name.getText().toString());
        if(x) {
            budgetManager.save_UpdateBudget(getApplicationContext());
        }
    }

    public void Delete_Budget(View view){
        TextView t_name = (TextView)findViewById(et_budName);
        boolean x = budgetManager.delete_budget(t_name.getText().toString());
        if(x) {
            budgetManager.save_UpdateBudget(getApplicationContext());
        }
    }

}
