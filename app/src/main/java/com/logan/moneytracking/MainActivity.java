package com.logan.moneytracking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.logan.R;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    JsonHandler jsonHandler;
    SaveLoad sl;
    SpinnerPayWeek spinnerWeek;
    DateObject dateObject;
    AddPayment addPayment;

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


            //final LoadPage loadPage = new LoadPage(MainActivity.this, jsonHandler);


            //DropDown dd = new DropDown(MainActivity.this, this, loadPage);
            //dd.main_call_this(2020);

            Calendar calendar = Calendar.getInstance();
            dateObject = new DateObject(calendar);

            addPayment = new AddPayment(MainActivity.this, jsonHandler, dateObject);

            spinnerWeek = new SpinnerPayWeek(this, getApplicationContext(), dateObject);
            spinnerWeek.spinner_setup(dateObject.getYear(), R.id.spinner1);


            SpinnerYear spinnerYear = new SpinnerYear(this, getApplicationContext(), dateObject);
            spinnerYear.spinner_setup(dateObject.getYear(), R.id.spin_year);

            addPayment.load_plus_button();

            //loadPage.load_plus_button();

            //loadPage.load_day_buttons(loadPage);

            Load_Week(dateObject.getWeek() + 1);
        } catch (JSONException e) {
            e.printStackTrace();
            //Big error restart the program again and display error message
        }

        //jsonHandler.Json_add_spending("{ \"year\":2020, \"week\":1, \"weekday\":\"Monday\", \"amount\":[\"1.00\",\"2.00\",\"3.00\"], \"notes\":[\"x\",\"y\",\"z\"] }");
        //sl.Save_Data(getApplicationContext(), jsonHandler);


        //sl.delete_file(getApplicationContext(), "storage.json");
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
        System.out.println("LOAD WEEK " + new_week);
        JSONArray jsonArray = jsonHandler.get_week(dateObject.getYear(), new_week);
        addPayment.load_another_week(jsonArray, (LinearLayout) findViewById(R.id.payment_layout));
    }

    public void Spending_Option_Yes(View view){
        Spinner s = findViewById(R.id.spin_spending_budgets);
        s.setVisibility(View.VISIBLE);
        Button b = findViewById(R.id.btn_spending_confirm);
        b.setVisibility(View.VISIBLE);
    }

    public void Spending_Option_No(View view){
        Button b = findViewById(R.id.btn_spending_confirm);
        b.setVisibility(View.VISIBLE);
    }

    public void Confirm_Spending_Option(View view){
        Button b = findViewById(R.id.btn_spending_confirm);
        b.setVisibility(View.INVISIBLE);
        addPayment.Confirm_Spending_Options();
    }

}
