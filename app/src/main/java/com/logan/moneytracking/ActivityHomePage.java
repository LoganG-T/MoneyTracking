package com.logan.moneytracking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.logan.R;
import com.logan.budget.BudgetActivity;
import com.logan.budget.BudgetActivityMain;
import com.logan.budget.BudgetEditActivity;
import com.logan.budget.BudgetManager;
import com.logan.budget.BudgetViewActivity;

public class ActivityHomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
    }

    public void View_Budgets(View view){
        Intent intent = new Intent(this, BudgetActivityMain.class);

        startActivity(intent);
    }

    public void View_Totals(View view) {
        Intent intent = new Intent(this, NotesActivity.class);

        startActivity(intent);
    }

    public void View_Payments(View view){
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }

}
