package com.logan.moneytracking;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SaveLoad {

    //private Context context;

    public void Save_Data(){

    }

    //Returns null if there is no week data in the json string, otherwise returns the week data of that year
    private WeekData extract_year_data(String json_string){
        try {
            JSONObject jsonObj = new JSONObject(json_string);
            WeekData wd = new WeekData();
            wd.setYear(jsonObj.getInt("year"));
            wd.setWeek(jsonObj.getJSONArray("week"));
            wd.setDays(jsonObj.getJSONArray("days"));

            return wd;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean Save_Data(Context context, JsonHandler jsonHandler){
        String FILENAME = "storage.json";
        String jsonString = jsonHandler.get_jsonString();

        try {
            JSONObject jsonObj = new JSONObject(jsonString);

            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(jsonObj.toString().getBytes());
            fos.close();

            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }


    /*private boolean delete(int val){
        String FILENAME = "storage.json";
        String jsonString = Load_Data(getApplicationContext(), FILENAME);
        TextView tv = (TextView) findViewById(R.id.textView12);
        tv.setText("Delete start");
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONObject weeksNum = new JSONObject(jsonString).getJSONObject("week_values").getJSONObject(chosen_week);
            int oldAmount = weeksNum.getInt("amount");
            weeksNum.put("amount", oldAmount - 1);
            JSONArray x = weeksNum.getJSONArray("values");
            x.remove(val);
            weeksNum.put("values", x);
            jsonObj.getJSONObject("week_values").put(chosen_week, weeksNum);
            FileOutputStream fos = getApplicationContext().openFileOutput(FILENAME,Context.MODE_PRIVATE);
            fos.write(jsonObj.toString().getBytes());
            fos.close();
            tv.setText("Saved");
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }*/

    public String Load_Data(Context context, String file_name){
        try {
            FileInputStream fis = context.openFileInput(file_name);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (FileNotFoundException fileNotFound) {
            return null;
        } catch (IOException ioException) {
            return null;
        }
    }

    public boolean Create_Data(Context context, String file_name, String json_data){

        //String default_json = "{\"date\":[{\"week\":[1, 2],\"year\": 2020,\"days\":[[{\"weekday\": \"Monday\",\"day_spending\":{\"spending\":[\"10.00\", \"20.00\"],\"notes\": [\"Takeaway\", \"Take-away\"]}}],[{\"weekday\":\"Monday\",\"day_spending\":{\"spending\": [\"30.00\", \"40.00\"],\"notes\":[\"Takeaway3\",\"Take - away4\"]}}]]}]}";
        String default_json = "{\"date\":[{\"week\":[],\"year\": 2020,\"days\":[]}]}";
        try {
            FileOutputStream fos = context.openFileOutput(file_name, Context.MODE_PRIVATE);
            if (json_data != null) {
                if(json_data == "" || json_data == "{}"){
                    fos.write(default_json.getBytes());
                    System.out.println("DEFAULT USED " + default_json);
                }else {
                    fos.write(json_data.getBytes());
                    System.out.println("DATA USED " + json_data);
                }
            }
            fos.close();
            return true;
        }catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
//https://stackoverflow.com/questions/40168601/android-how-to-save-json-data-in-a-file-and-retrieve-it
    public boolean check_file(Context context, String file_name){
        String path = context.getFilesDir().getAbsolutePath() + "/" + file_name;
        File file = new File(path);
        return file.exists();
    }

    public void delete_file(Context c, String s){
        c.deleteFile(s);
        System.out.println("Deleted");
    }
}
