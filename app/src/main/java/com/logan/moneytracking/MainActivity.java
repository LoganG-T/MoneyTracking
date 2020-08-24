package com.logan.moneytracking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.logan.R;
import com.logan.budget.BudgetManager;
import com.logan.budget.BudgetMuiltiChoiceSpinner;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    JsonHandler jsonHandler;
    SaveLoad sl;
    SpinnerPayWeek spinnerWeek;
    DateObject dateObject;
    AddPayment addPayment;

    BudgetMuiltiChoiceSpinner budgetSpinner;

    public MainActivity(){
        jsonHandler = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sl = new SaveLoad();

        boolean isFilePresent = sl.check_file(getApplicationContext(), "storage.json");
        if(isFilePresent) {
            String jsonString = sl.Load_Data(getApplicationContext(), "storage.json");
            System.out.println("FILE PRESENT");
            System.out.println(jsonString);

            load_app_data(sl);

        } else {
            boolean isFileCreated = sl.Create_Data(getApplicationContext(), "storage.json", "");
            if(isFileCreated) {
                load_app_data(sl);
            } else {
                //show error or try again.
            }
        }
    }

    void load_app_data(SaveLoad sl){
        //Below 3 segements add given json string data to the existing data and then save the data into memory

        try {
            jsonHandler = new JsonHandler(getApplicationContext(), "storage.json", sl);

            Calendar calendar = Calendar.getInstance();
            dateObject = new DateObject(calendar);

            addPayment = new AddPayment(MainActivity.this, jsonHandler, dateObject);

            spinnerWeek = new SpinnerPayWeek(this, getApplicationContext(), dateObject);
            spinnerWeek.spinner_setup(dateObject.getYear(), R.id.spinner1);


            SpinnerYear spinnerYear = new SpinnerYear(this, getApplicationContext(), dateObject);
            spinnerYear.spinner_setup(dateObject.getYear(), R.id.spin_year);

            addPayment.load_plus_button();

            Load_Week(dateObject.getWeek() + 1);
        } catch (JSONException e) {
            e.printStackTrace();
            //Big error restart the program again and display error message
        }
    }

    public void Next_Week(View view){

        Spinner spin = (Spinner) findViewById(R.id.spinner1);
        dateObject.update_week(1);
        spin.setSelection(dateObject.getWeek() - 1, false);

        try {

            Load_Week(dateObject.getWeek());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void Prev_Week(View view){
        Spinner spin = (Spinner) findViewById(R.id.spinner1);
        dateObject.update_week(-1);
        spin.setSelection(dateObject.getWeek() - 1, false);

        try {

            Load_Week(dateObject.getWeek());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Load_Week(int new_week) throws JSONException {
        JSONArray jsonArray = jsonHandler.get_week(dateObject.getYear(), new_week);
        addPayment.load_another_week(jsonArray, (LinearLayout) findViewById(R.id.payment_layout));
    }


    //Start of incoming budgets options

    public void Spending_Option_Yes(View view){
        Spinner s = findViewById(R.id.spin_spending_budgets);
        s.setVisibility(View.VISIBLE);
        Button b = findViewById(R.id.btn_spending_confirm);
        b.setVisibility(View.VISIBLE);

        BudgetManager budgetManager = new BudgetManager(jsonHandler);
        if(budgetManager.is_file(getApplicationContext())) {
            budgetManager.load_budget(getApplicationContext());
        }else{
            budgetManager.create_file(getApplicationContext());
        }

        budgetSpinner = new BudgetMuiltiChoiceSpinner(this, getApplicationContext(), budgetManager);

        budgetSpinner.spinner_setup(R.id.spin_spending_budgets);
    }

    public void Spending_Option_No(View view){
        Button b = findViewById(R.id.btn_spending_confirm);
        b.setVisibility(View.VISIBLE);
    }

    //Selects the chosen budgets from the drop-down tick spinner and adds the incoming payment to the selects budgets totals
    public void Confirm_Spending_Option(View view){
        Button b = findViewById(R.id.btn_spending_confirm);
        b.setVisibility(View.INVISIBLE);

        ArrayList<String> display_budgets = budgetSpinner.Chosen_Budgets();

        if(display_budgets.size() > 0) {

            BudgetManager budgetManager = new BudgetManager(jsonHandler);
            if(budgetManager.is_file(getApplicationContext())) {
                budgetManager.load_budget(getApplicationContext());
            }else{
                budgetManager.create_file(getApplicationContext());
            }

            for (int i = 0; i < display_budgets.size(); i++) {
                boolean x = budgetManager.add_to_total_budget(display_budgets.get(i), "total_budget", addPayment.Get_Latest_Incoming());

                if (x) {
                    budgetManager.save_UpdateBudget(getApplicationContext());
                }
            }

            //Small confirmation displayed to user
            String s = "Budget";

            if (display_budgets.size() > 1) {
                s += "s";
            }

            Toast.makeText(getApplicationContext(), s + " updated", Toast.LENGTH_LONG).show();
        }

        addPayment.Confirm_Spending_Options();
    }

    //End of incoming budgets options

}
