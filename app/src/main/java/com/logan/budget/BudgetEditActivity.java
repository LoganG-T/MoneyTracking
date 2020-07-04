package com.logan.budget;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.logan.R;
import com.logan.moneytracking.DateObject;
import com.logan.moneytracking.JsonHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class BudgetEditActivity extends AppCompatActivity {

    BudgetManager budgetManager;
    JsonHandler jsonHandler;
    int et_budName = R.id.bd_budgetName;
    int et_budAmount = R.id.bd_amount;

    boolean start = true;

    long set_date_milli = 0;
    long start_date_milli = 0;
    long end_date_milli = 0;

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


        CalendarView calendarView = (CalendarView)findViewById(R.id.calendarView);
        calendarView.setFirstDayOfWeek(2);//Sunday is 1 Monday is 2 etc
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, day);
                set_date_milli = c.getTimeInMillis();
            }
        });
        LinearLayout calendarBox = (LinearLayout)findViewById(R.id.layout_calenderBox);
        calendarBox.setVisibility(View.INVISIBLE);

    }

    //{"name":"Budget","total_budget":"500.0","left_budget":"0.0","start_date":{"day":"Monday","month":"June","week":23,"year":2020},"end_date":{"day":"Tuesday","month":"September","week":36,"year":2020},"term_time":14,"weeks_gone":0}
    public void Display_Budget(String given_name){
        try {
            JSONObject budget_json = budgetManager.find_budget(given_name);
            TextView t_name = (TextView)findViewById(et_budName);
            t_name.setText(budget_json.getString("name"));
            TextView t_num = (TextView)findViewById(et_budAmount);
            t_num.setText(budget_json.getString("total_budget"));

            DateObject dateObject = new DateObject(budget_json.getJSONObject("start_date"));
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, dateObject.getYear());
            c.set(Calendar.WEEK_OF_YEAR, dateObject.getWeek());
            c.set(Calendar.DAY_OF_WEEK, dateObject.getCalDayInt());
            start_date_milli = c.getTimeInMillis();
            Edit_DateDisplay(R.id.txt_startDate, c.get(Calendar.DAY_OF_MONTH), true);

            DateObject dateEnd = new DateObject(budget_json.getJSONObject("end_date"));
            Calendar c_e = Calendar.getInstance();
            c_e.set(Calendar.YEAR, dateEnd.getYear());
            c_e.set(Calendar.WEEK_OF_YEAR, dateEnd.getWeek());
            c_e.set(Calendar.DAY_OF_WEEK, dateEnd.getCalDayInt());
            end_date_milli = c_e.getTimeInMillis();
            Edit_DateDisplay(R.id.txt_endDate, c_e.get(Calendar.DAY_OF_MONTH), false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void Edit_StartDate(View view){
        Open_DateCalendar();
        start = true;
    }

    public void Edit_EndDate(View view){
        Open_DateCalendar();
        start = false;
    }

    public void Close_DateCalendar(View view){
        LinearLayout calendarBox = (LinearLayout)findViewById(R.id.layout_calenderBox);
        calendarBox.setVisibility(View.INVISIBLE);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.layout_Data);
        linearLayout.setVisibility(View.VISIBLE);
        if(start){
            start_date_milli = set_date_milli;
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(start_date_milli);
            Edit_DateDisplay(R.id.txt_startDate, c.get(Calendar.DAY_OF_MONTH), true);
        }else{
            end_date_milli = set_date_milli;
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(end_date_milli);
            Edit_DateDisplay(R.id.txt_endDate, c.get(Calendar.DAY_OF_MONTH), false);
        }
    }

    private void Open_DateCalendar(){
        LinearLayout calendarBox = (LinearLayout)findViewById(R.id.layout_calenderBox);
        calendarBox.setVisibility(View.VISIBLE);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.layout_Data);
        linearLayout.setVisibility(View.INVISIBLE);
    }

    private void Edit_DateDisplay(int given_id, int month_day, boolean is_start){
        Calendar c_s = Calendar.getInstance();
        if(is_start) {
            c_s.setTimeInMillis(start_date_milli);
        }else{
            c_s.setTimeInMillis(end_date_milli);
        }
        DateObject dateObject = new DateObject(c_s);

        TextView t_date = (TextView)findViewById(given_id);
        t_date.setText(dateObject.getDay() + " " + month_day + " " + dateObject.getMonth() + " " + dateObject.getYear());
    }

    public void Confirm_Edit(View view){
        Calendar c_s = Calendar.getInstance();
        Calendar c_e = Calendar.getInstance();
        c_s.setTimeInMillis(start_date_milli);
        c_e.setTimeInMillis(end_date_milli);
        System.out.println(set_date_milli + " YEAR " + c_s.get(Calendar.YEAR) + " MONTH " + c_s.get(Calendar.MONTH) + " DAY " + c_s.get(Calendar.DAY_OF_MONTH));
        TextView t_name = (TextView)findViewById(et_budName);
        TextView t_value= (TextView)findViewById(et_budAmount);

        DateObject start = new DateObject(c_s);

        DateObject end = new DateObject(c_e);

        boolean x = budgetManager.update_budget("name", t_name.getText().toString());
        x = x && budgetManager.update_budget("total_budget", t_value.getText().toString());
        x = x && budgetManager.update_budget("start_date", start);
        x = x && budgetManager.update_budget("end_date", end);
        if(x) {
            budgetManager.save_UpdateBudget(getApplicationContext());
            //Small confirmation displayed to user
            Toast.makeText(getApplicationContext(), "Edit saved", Toast.LENGTH_LONG).show();
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
