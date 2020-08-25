package com.logan.budget;

import android.content.Context;

import com.logan.moneytracking.DateObject;
import com.logan.moneytracking.JsonHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

public class BudgetManager {

    JsonHandler jsonHandler;
    Calendar c;
    JSONObject all_budgets;

    public  BudgetManager(){

    }

    public BudgetManager(JsonHandler js){
        jsonHandler = js;
        c = Calendar.getInstance();
    }

    Budget budget;
    JSONObject current_budget;

    public void set_jsonHandler(JsonHandler js){
        jsonHandler = js;
    }

    public boolean select_budget(String s) {
        if(name_taken(s)){
            try {
                current_budget = find_budget(s);
                budget = new Budget(current_budget);
                update_spending();
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean budget_ended(DateObject given_date){
        if(given_date.greaterThan(budget.end_date)){
            return true;
        }
        return false;
    }

    public boolean before_started(DateObject given_date){
        if(given_date.before(budget.start_date)){
            return true;
        }
        return false;
    }

    //Returns a string value of the integer week diference between the budget start and the given date
    public String get_startDiff(DateObject d){
        String s = "";

        s += budget.start_date.weekDifference(d) - 1;

        return s;
    }

    public void set_new_budget(String name, float amount, DateObject start_date, DateObject end_date){
        //Sets a new budget for the dates between start_date and end_date
        budget = new Budget(name, amount, start_date, end_date);
    }
    public float get_week_budget(int year, int week){
        //Returns the budget for each week from the start date to the end date
        if(jsonHandler != null){
            //return budget.get_weekly_budget() - jsonHandler.week_spending(c.get(Calendar.YEAR), c.get(Calendar.WEEK_OF_YEAR));
            return budget.get_weekly_budget() - jsonHandler.week_spending(year, week);
        }
        return budget.get_weekly_budget();
    }

    public float get_week_full_budget(int year, int week){
        //Returns the full budget for each week from the start date to the end date
        return budget.get_weekly_budget();
    }

    public float get_original_week_budget(){
        return budget.get_original_weekly_budget();
    }

    public float get_day_budget(DateObject get_date, float weeks_spending){
        //Returns the budget for the date given on that one day
        float days_left = 7f;
        float return_total = budget.get_current_week() - weeks_spending / days_left;
        return_total = (float)((int)return_total * 100) / 100f;
        return return_total;
    }

    public float get_current_week_budget(DateObject get_date){
        //Returns the budget for the week of the given date
        //Returns the budget for each week from the start date to the end date
        Budget b = null;
        try {
            b = new Budget(current_budget);
            update_spending();
        } catch (JSONException e) {
            return 0f;
        }
        if(jsonHandler != null){
            return b.get_weekly_budget();
        }
        return b.get_weekly_budget();
    }

    public void update_spending(){
        float spent = 0f;
        int week_diff = budget.start_date.weekDifference(budget.end_date);
        DateObject c_date = new DateObject(budget.start_date);
        for(int i = 0; i < week_diff; i++){
            spent += jsonHandler.week_spending(c_date.getYear(), c_date.getWeek());
            c_date.update_week(1);
        }

        budget.update_spending(spent);
        try {
            current_budget.put("left_budget", spent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public float get_weekSpending(DateObject c_date){
        return jsonHandler.week_spending(c_date.getYear(), c_date.getWeek());
    }

    public float get_totalBudget(){
        return budget.getTotal_budget();
    }

    public float get_remainingBudget(){
        return budget.get_remainingBudget();
    }

    public int get_weeks_left(){
        return budget.time_left();
    }

    public String[] get_budgetNames(){
        try {
            JSONArray array = all_budgets.getJSONArray("budgets");
            String[] all_s = new String[array.length()];
            for(int i = 0; i < array.length(); i++){
                all_s[i] = array.getJSONObject(i).getString("name");
            }

            return all_s;
        } catch (JSONException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    //Returns JSONObject of the budget with the selected name or null if it does not exist
    public JSONObject find_budget(String s) throws JSONException {
        JSONObject jsonObject = null;
        JSONArray array = all_budgets.getJSONArray("budgets");
        for(int i = 0; i < array.length(); i++){
            if(array.getJSONObject(i).getString("name").equals(s)){
                System.out.println("FOUND " + array.getJSONObject(i).toString());
                return array.getJSONObject(i);
            }
        }
        return jsonObject;
    }


    public boolean name_taken(String s){
        try {
            System.out.println(all_budgets.toString());
            JSONArray budget_arr = all_budgets.getJSONArray("budgets");
            if(budget_arr.length() == 0){
                return false;
            }
            for(int i = 0; i < budget_arr.length(); i++){
                if(budget_arr.getJSONObject(i).getString("name").equals(s)){
                    return true;
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean is_empty(){

        if(all_budgets.toString().equals("{\"budgets\":[]}")){
            return true;
        }

        return false;
    }

    public boolean update_budget(String json_name, String up_value){

        try {
            current_budget.put(json_name, up_value);
            budget = new Budget(current_budget);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update_budget(String json_name, DateObject up_value){

        try {
            current_budget.put(json_name, new JSONObject(up_value.toString()));
            budget = new Budget(current_budget);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Increase a budgets total value with an incoming payment
    public boolean add_to_total_budget(String budget_name, String json_name, String up_value){

        try {
            current_budget = find_budget(budget_name);

            float f = Float.parseFloat(current_budget.getString(json_name)) + Float.parseFloat(up_value);
            int x = (int)(f * 100);
            f = x / 100f;

            current_budget.put(json_name, String.valueOf(f));
            budget = new Budget(current_budget);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete_budget(String given_name){

        try {
            JSONArray j_array = all_budgets.getJSONArray("budgets");
            for(int i = 0; i < j_array.length(); i++)
            {
                if(j_array.getJSONObject(i).getString("name").equals(given_name)){
                    j_array.remove(i);
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean is_file(Context context){
        String path = context.getFilesDir().getAbsolutePath() + "/" + "budgetStorage.json";
        File file = new File(path);
        return file.exists();
    }

    public void create_file(Context context){
        String FILENAME = "budgetStorage.json";
        String default_json = "{\"budgets\":[]}";

        try {
            all_budgets = new JSONObject(default_json);
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(default_json.getBytes());
            fos.close();
        }catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public boolean save_UpdateBudget(Context context){

        String FILENAME = "budgetStorage.json";
        String jsonString = budget.getString();

        try {

            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(all_budgets.toString().getBytes());
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean save_budget(Context context){

        String FILENAME = "budgetStorage.json";
        String jsonString = budget.getString();

        try {

            JSONObject jsonObj = new JSONObject(jsonString);
            all_budgets.getJSONArray("budgets").put(jsonObj);

            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(all_budgets.toString().getBytes());
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String load_budget(Context context){
        String FILENAME = "budgetStorage.json";
        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            all_budgets = new JSONObject(sb.toString());
            return sb.toString();
        } catch (FileNotFoundException fileNotFound) {
            return null;
        } catch (IOException ioException) {
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
