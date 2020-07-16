package com.logan.moneytracking;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class JsonHandler {

    private String file_name;
    private String json_string;
    private SaveLoad sl;

    private JSONObject current_json;

    public JsonHandler(Context context, String given_file){
        file_name = given_file;
        sl = new SaveLoad();
        json_string = sl.Load_Data(context, given_file);
        try {
            current_json = new JSONObject(json_string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JsonHandler(Context context, String given_file, SaveLoad sl) throws JSONException {
        file_name = given_file;
        json_string = sl.Load_Data(context, given_file);
        current_json = new JSONObject(json_string);
    }

    public JsonHandler() throws JSONException {
        json_string = "{\"date\":[{\"week\":[1,2],\"year\":2020,\"days\":[[{\"weekday\":\"Monday\",\"day_spending\":" +
                "{\"spending\":[10,20],\"notes\":[\"Takeaway\",\"Take-away\"]}}],[{\"weekday\":\"Monday\",\"day_spending\":" +
                "{\"spending\":[30,40],\"notes\":[\"Takeaway3\",\"Take-away4\"]}}]]}]}";
        current_json = new JSONObject(json_string);
    }

    public int Week_of_year(Date given_date){

        Calendar c = Calendar.getInstance();
        c.setTime(given_date);
        //int year = c.get(Calendar.YEAR);
        int week_of_year = c.get(Calendar.WEEK_OF_YEAR);

        return week_of_year;
    }

    public boolean Json_add_spending(String given_json){
    //Format for the json is {"date" : [{"year" : 2020, "week" : [1], "days":[[{"day":"Monday", "day_spending":{"amount":[], "notes":[]} }] ]} ] }
        //With each array index in "week" corresponding to the index in days for that week

        //Given json is a json string of the year, week number and the days_spending information to add
        //Example given_json format is { "year":2020, "week":1, "weekday":"Monday", "amount":[1,2,3], "notes"["x","y","z"] }
        try {
            JSONObject json_obj = new JSONObject(given_json);

            //JSONObject current_json = new JSONObject(json_string);

            //Accessing the year
            JSONObject selected_obj = get_year(json_obj.getInt("year"));
            //Unhandled thrown exception from get year above

            if(selected_obj == null){
                selected_obj = add_new_year(json_obj.getInt("year"));
            }

            //Accessing the week / week-index position
            JSONArray week_array = selected_obj.getJSONArray("week");
            int week_index = -1;
            for(int i = 0; i < week_array.length(); i++){
                if(week_array.getInt(i) == json_obj.getInt("week")){
                    week_index = i;
                    break;
                }
            }
            if(week_index == -1){
                JSONArray weeks = selected_obj.getJSONArray("week");
                JSONArray weeks_spending = selected_obj.getJSONArray("days");
                JSONArray new_data = new JSONArray();
                WeekdayData wdd = new WeekdayData(json_obj.getString("weekday"), json_obj.getString("month"));
                /*wdd.set_Weekday(json_obj.getString("weekday"));
                wdd.set_Month(json_obj.getString("month"));*/
                SpendingData spendingData = new SpendingData(json_obj.getJSONArray("amount"), json_obj.getJSONArray("notes"));
                wdd.set_Spending(spendingData);
                new_data.put(new JSONObject(wdd.toString()));
                weeks_spending.put(new_data);
                weeks.put(json_obj.getInt("week"));
            }else {
                //Accessing the weekday of the week required
;
                JSONArray weeks_spending = selected_obj.getJSONArray("days").getJSONArray(week_index);
                JSONObject spending_data = null;
                for (int i = 0; i < weeks_spending.length(); i++) {

                    if (weeks_spending.getJSONObject(i).getString("weekday").equals(json_obj.getString("weekday"))) {
                        //Add spending
                        spending_data = weeks_spending.getJSONObject(i).getJSONObject("day_spending");
                        JSONArray amount_ar = json_obj.getJSONArray("amount");
                        JSONArray notes_ar = json_obj.getJSONArray("notes");
                        JSONArray current_amount = spending_data.getJSONArray("spending");
                        JSONArray current_notes = spending_data.getJSONArray("notes");
                        for (int ar = 0; ar < amount_ar.length(); ar++) {
                            current_amount.put(amount_ar.getString(ar));
                            current_notes.put(notes_ar.getString(ar));
                        }
                        spending_data.put("spending", current_amount);
                        spending_data.put("notes", current_notes);

                        //SpendingData current_spending = new SpendingData(spending_data.getJSONArray("amount"), spending_data.getJSONArray("notes"));
                        break;
                    }
                }

                if (spending_data == null) {
                    //insert new weekday data
                    WeekdayData wdd = new WeekdayData(json_obj.getString("weekday"), json_obj.getString("month"));
                    SpendingData spendingData = new SpendingData(json_obj.getJSONArray("amount"), json_obj.getJSONArray("notes"));
                    wdd.set_Spending(spendingData);
                    weeks_spending.put(new JSONObject(wdd.toString()));
                }
            }


            json_string = current_json.toString();

            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String get_jsonString(){

        json_string = current_json.toString();
        return json_string;
    }

    //Returns spending data as json of the 'c_day' string of a weekday or null if it does not exist
    public JSONObject get_chosen_day(String c_day, int c_week, int c_year){
        JSONObject return_json = null;
        try {
            JSONArray days = get_week(c_year, c_week);
            if(days == null){
                return null;
            }
            for(int i = 0; i < days.length(); i++){
                if(days.getJSONObject(i).getString("weekday").equals(c_day)){
                    return days.getJSONObject(i).getJSONObject("day_spending");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return return_json;
    }

    public boolean add_new_day(int year, int week, String day) throws JSONException {

        JSONArray json_week = get_week(year, week);
        if(json_week == null){
            json_week = add_new_week(year, week);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, week);
        calendar.set(Calendar.DAY_OF_WEEK, day_int(day));
        int month = calendar.get(Calendar.MONTH) + 1;
        WeekdayData wd = new WeekdayData(day, month_string(month));
        json_week.put(new JSONObject(wd.toString()));

        return true;
    }

    int day_int(String day){
        switch(day){
            case("Sunday"):
                return 1;
            case("Monday"):
                return 2;
            case("Tuesday"):
                return 3;
            case("Wednesday"):
                return 4;
            case("Thursday"):
                return 5;
            case("Friday"):
                return 6;
            case("Saturday"):
                return 7;
            default:
                return 2;
        }
    }

    String month_string(int month){
        switch(month){
            case(1):
                return "January";
            case(2):
                return "February";
            case(3):
                return "March";
            case(4):
                return "April";
            case(5):
                return "May";
            case(6):
                return "June";
            case(7):
                return "July";
            case(8):
                return "August";
            case(9):
                return "September";
            case(10):
                return "October";
            case(11):
                return "November";
            case(12):
                return "December";
            default:
                return "December";
        }
    }

    private boolean Edit_order(){

        return false;
    }

    private boolean Arrange_order(){

        return false;
    }

    //returns -1 if the year does not exist
    public int get_year_index(int year){
        try {
            //JSONObject current_json = new JSONObject(json_string);
            for (int i = 0; i < current_json.getJSONArray("date").length(); i++){
                JSONObject x = new JSONObject(current_json.getJSONArray("date").get(i).toString());
                if(x.getInt("year") == year){
                    return i;
                }
            }

        }
        catch(JSONException e){
            return -1;
        }
        return -1;
    }

    public JSONObject get_year(int year) throws JSONException {
        JSONObject selected_obj = null;
        //JSONArray date_json = new JSONObject(json_string).getJSONArray("date");
        JSONArray date_json = current_json.getJSONArray("date");
        //Accessing the year
        for(int i = 0; i < date_json.length(); i++){
            if(date_json.getJSONObject(i).getInt("year") == year){
                selected_obj = date_json.getJSONObject(i);
                break;
            }
        }
        return selected_obj;
    }

    //Returns the index of the given week in the array or -1 if the week does not exist
    public int get_week_index(int year, int week) throws JSONException {
        JSONObject year_obj = get_year(year);
        if(year_obj == null){
            return -1;
        }
        JSONArray week_array = year_obj.getJSONArray("week");
        for(int i = 0; i < week_array.length(); i++){
            if(week_array.getInt(i) == week) {
                return i;
            }
        }
        return -1;
    }

    //Returns a JSONArray of the weekdays in the chosen week of the year -> or null if it does not exist
    public JSONArray get_week(int year, int week) throws JSONException {
        JSONObject return_obj = null;
        int week_index = get_week_index(year, week);
        if(week_index < 0){
            return null;
        }

        JSONObject year_obj = get_year(year);
        JSONArray week_obj = year_obj.getJSONArray("days").getJSONArray(week_index);

        return week_obj;
    }

    //Given a year and a week of that year, adds the information to the stored json file
    public JSONArray add_new_week(int year, int week) throws JSONException {
        JSONObject year_obj = get_year(year);//put int week, put {} days
        JSONArray week_obj = year_obj.getJSONArray("week");
        week_obj.put(week);
        //year_obj.put("week", week);
        JSONArray new_week = new JSONArray();
        JSONArray new_days = year_obj.getJSONArray("days");
        new_days.put(new_week);

        return new_week;
    }

    public JSONObject add_new_year(int year) throws JSONException {
        JSONObject json_year = new JSONObject("{\"week\":[],\"year\":" + year + ",\"days\":[]}");
        current_json.getJSONArray("date").put(json_year);

        return json_year;
    }

    public float week_spending(int year, int week){
        try {
            float total = 0f;
            JSONArray jsonArray = get_week(year, week);
            if(jsonArray == null){
                return 0f;
            }
            for(int i = 0; i < jsonArray.length(); i++){
                JSONArray day_spending = jsonArray.getJSONObject(i).getJSONObject("day_spending").getJSONArray("spending");
                for(int d = 0; d < day_spending.length(); d++){
                    total += (float)day_spending.getDouble(d);
                }
            }
            return total;

        } catch (JSONException e) {
            return 0f;
        }

    }

    //{ "year":2020, "week":1, "weekday":"Monday", "index":1 }
    public float delete_spending(String given_string) throws JSONException {

        float ret_f = 0f;
        JSONObject given_json = new JSONObject(given_string);

        JSONObject day_obj = get_chosen_day(given_json.getString("weekday"), given_json.getInt("week"), given_json.getInt("year"));

        int index = given_json.getInt("index");

        ret_f = (float)day_obj.getJSONArray("spending").getDouble(index);

        day_obj.getJSONArray("spending").remove(index);
        day_obj.getJSONArray("notes").remove(index);
        if(day_obj.getJSONArray("spending").length() == 0){
            remove_chosen_day(given_json.getString("weekday"), given_json.getInt("week"), given_json.getInt("year"));
        }


        return ret_f;
    }

    private boolean remove_chosen_day(String c_day, int c_week, int c_year){
        JSONObject return_json = null;
        try {
            JSONArray days = get_week(c_year, c_week);
            if(days == null){
                return false;
            }
            for(int i = 0; i < days.length(); i++){
                if(days.getJSONObject(i).getString("weekday").equals(c_day)){
                    days.remove(i);
                    return true;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

}


/*
{
"date":
	[
		{
		"week":[1,2],
		"year":2020,
		"days":
		[
			{
			"weekday":"Monday",
			"day_spending":
				{
				"spending":[10,20],
				"notes":["Takeaway","Take-away"]
				}
			},
			{
			"weekday":"Monday",
			"day_spending":
				{
				"spending":[30,40],
				"notes":["Takeaway3","Take-away4"]
				}
			}
		]
		}
	]
}

 */