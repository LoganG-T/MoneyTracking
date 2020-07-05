package com.logan.moneytracking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.logan.R;
import com.logan.budget.BudgetActivity;
import com.logan.budget.BudgetActivityMain;
import com.logan.budget.BudgetEditActivity;
import com.logan.budget.BudgetManager;
import com.logan.budget.BudgetViewActivity;

import org.json.JSONException;

import java.util.Calendar;

public class ActivityHomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        SaveLoad sl = new SaveLoad();
        JsonHandler js;

        boolean isFilePresent = sl.check_file(getApplicationContext(), "storage.json");
        if(isFilePresent) {
            String jsonString = sl.Load_Data(getApplicationContext(), "storage.json");
            System.out.println("FILE PRESENT");
            System.out.println(jsonString);

            //do the json parsing here and do the rest of functionality of app
        } else {
            boolean isFileCreated = sl.Create_Data(getApplicationContext(), "storage.json", "");
            if(isFileCreated) {
                //proceed with storing the first todo  or show ui
            } else {
                //show error or try again.
            }
        }

        try {
            js = new JsonHandler(getApplicationContext(), "storage.json", sl);
            Calendar c = Calendar.getInstance();
            c.setFirstDayOfWeek(Calendar.MONDAY);
            float f = js.week_spending(c.get(Calendar.YEAR), c.get(Calendar.WEEK_OF_YEAR));
            TextView textView = (TextView)findViewById(R.id.txt_homeTotal);
            textView.setText("You have spent " + cur_sym + f +" this week");
            System.out.println(c.get(Calendar.WEEK_OF_YEAR));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    String cur_sym = "£";

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
