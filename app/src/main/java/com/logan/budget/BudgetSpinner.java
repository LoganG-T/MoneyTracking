package com.logan.budget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;


public class BudgetSpinner extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Activity activity;
    Context main_context;
    BudgetManager budgetManager;
    String[] names;

    BudgetEditActivity editActivity = null;

    public BudgetSpinner(Activity a, Context c, BudgetManager bm){
        activity = a;
        main_context = c;
        budgetManager = bm;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("DROP WORK");

    }

    public void spinner_setup(int given_spinner){

        names = budgetManager.get_budgetNames();
        if(names.length == 0){
            return;
        }

        Spinner spinner = (Spinner) activity.findViewById(given_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(main_context,
                android.R.layout.simple_spinner_item, names);

        //select_dialog_multichoice
        //simple_spinner_item
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        spinner.setSelection(0);
    }

    public void Set_EditActivity(BudgetEditActivity b){
        editActivity = b;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        //Must set budget name
        budgetManager.select_budget(names[position]);
        if(editActivity != null){
            editActivity.Display_Budget(names[position]);
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
