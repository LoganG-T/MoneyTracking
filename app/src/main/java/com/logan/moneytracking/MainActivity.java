package com.logan.moneytracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logan.R;
import com.logan.budget.BudgetActivity;
import com.logan.budget.BudgetActivityMain;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    JsonHandler jsonHandler;
    SaveLoad sl;

    public MainActivity(){
        jsonHandler = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sl = new SaveLoad();


        /*
        add_pay_layout("10", 1, (LinearLayout) findViewById(R.id.payment_layout));
        add_pay_layout("11", 2, (LinearLayout) findViewById(R.id.payment_layout));
        add_pay_layout("12", 3, (LinearLayout) findViewById(R.id.payment_layout));
        add_pay_layout("13", 4, (LinearLayout) findViewById(R.id.payment_layout));
        add_pay_layout("14", 5, (LinearLayout) findViewById(R.id.payment_layout));
        add_pay_layout("15", 6, (LinearLayout) findViewById(R.id.payment_layout));
         */

        boolean isFilePresent = sl.check_file(getApplicationContext(), "storage.json");
        if(isFilePresent) {
            String jsonString = sl.Load_Data(getApplicationContext(), "storage.json");
            System.out.println("FILE PRESENT");
            System.out.println(jsonString);

            load_app_data(sl);

            //do the json parsing here and do the rest of functionality of app
        } else {
            boolean isFileCreated = sl.Create_Data(getApplicationContext(), "storage.json", "");
            if(isFileCreated) {
                load_app_data(sl);
                //proceed with storing the first todo  or show ui
            } else {
                //show error or try again.
            }
        }
    }

    void load_app_data(SaveLoad sl){
        //Below 3 segements add given json string data to the existing data and then save the data into memory

        try {
            jsonHandler = new JsonHandler(getApplicationContext(), "storage.json", sl);


            final LoadPage loadPage = new LoadPage(MainActivity.this, jsonHandler);

            DropDown dd = new DropDown(MainActivity.this, this, loadPage);
            dd.main_call_this(2020);

            loadPage.load_plus_button();

            loadPage.load_day_buttons(loadPage);

        } catch (JSONException e) {
            e.printStackTrace();
            //Big error restart the program again and display error message
        }

        //jsonHandler.Json_add_spending("{ \"year\":2020, \"week\":1, \"weekday\":\"Monday\", \"amount\":[\"1.00\",\"2.00\",\"3.00\"], \"notes\":[\"x\",\"y\",\"z\"] }");
        //sl.Save_Data(getApplicationContext(), jsonHandler);


        //sl.delete_file(getApplicationContext(), "storage.json");
    }


    public void launchBudget(View view) {
        Intent intent = new Intent(this, BudgetActivityMain.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        //intent.putExtra("jsKey", jsonHandler);
        //sl.Save_Data(getApplicationContext(), jsonHandler);

        startActivity(intent);
    }

}
