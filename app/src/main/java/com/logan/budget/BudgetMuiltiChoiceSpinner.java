package com.logan.budget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.logan.moneytracking.BudgetMultiChoice;
import com.logan.moneytracking.BudgetMultiChoiceAdapter;

import java.util.ArrayList;

public class BudgetMuiltiChoiceSpinner extends AppCompatActivity{

    Activity activity;
    Context main_context;
    BudgetManager budgetManager;
    String[] names;

    ArrayList<BudgetMultiChoice> budget_list;

    BudgetEditActivity editActivity = null;

    public BudgetMuiltiChoiceSpinner(Activity a, Context c, BudgetManager bm){
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

        //Need to minus 1 because it is zero-index'd
        Spinner spinner = (Spinner) activity.findViewById(given_spinner);

        budget_list = new ArrayList<>();
        BudgetMultiChoice top_selection = new BudgetMultiChoice();
        top_selection.setTitle("Confirm");
        top_selection.setSelected(false);
        budget_list.add(top_selection);

        for (int i = 0; i < names.length; i++) {
            BudgetMultiChoice budget_choice = new BudgetMultiChoice();
            budget_choice.setTitle(names[i]);
            budget_choice.setSelected(false);
            budget_list.add(budget_choice);
        }
        BudgetMultiChoiceAdapter myAdapter = new BudgetMultiChoiceAdapter(main_context, 0,
                budget_list);
        spinner.setAdapter(myAdapter);

        //spinner.setSelection(0);
    }


    public ArrayList<String> Chosen_Budgets(){
        ArrayList<String> return_strings = new ArrayList<String>();

        for(int i = 1; i < names.length + 1; i++){
            if(budget_list.get(i).isSelected()){
                return_strings.add(budget_list.get(i).getTitle());
            }
        }

        return return_strings;
    }
}
