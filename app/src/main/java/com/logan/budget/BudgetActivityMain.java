package com.logan.budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.logan.R;
import com.logan.moneytracking.NotesActivity;
import com.logan.moneytracking.TotalActivity;

public class BudgetActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_main);
    }

    public void new_budget(View view){
        Intent intent = new Intent(this, BudgetActivity.class);

        startActivity(intent);
    }

    public void view_budgets(View view){
        Intent intent = new Intent(this, BudgetViewActivity.class);

        startActivity(intent);
    }

    public void edit_budgets(View view){
        BudgetManager budgetManager = new BudgetManager();
        if(! budgetManager.is_file(getApplicationContext())){
            Intent intent = new Intent(this, BudgetActivity.class);

            startActivity(intent);
        }else {
            budgetManager.load_budget(getApplicationContext());
            if(budgetManager.is_empty()){
                Intent intent = new Intent(this, BudgetActivity.class);

                startActivity(intent);
            }else {

                Intent intent = new Intent(this, BudgetEditActivity.class);

                startActivity(intent);
            }
        }
    }

    public void view_totals(View view) {
        Intent intent = new Intent(this, TotalActivity.class);

        startActivity(intent);
    }

    public void notes_totals(View view){
        Intent intent = new Intent(this, NotesActivity.class);

        startActivity(intent);
    }

}
