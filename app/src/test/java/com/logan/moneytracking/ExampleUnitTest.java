package com.logan.moneytracking;

import android.provider.CalendarContract;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void json_week_test() throws JSONException {
        JsonHandler js = new JsonHandler();
        Date d = new Date();
        System.out.println(js.Week_of_year(d));
        assertNotEquals(-1, js.Week_of_year(d));
    }

    @Test
    public void json_week0_test() throws JSONException {
        JsonHandler js = new JsonHandler();
        Date d = new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime();
        System.out.println(js.Week_of_year(d));
        assertEquals(1, js.Week_of_year(d));
    }

    //@Test
    public void json_add_test() throws JSONException {
        JsonHandler js = new JsonHandler();
        String json_string = "{\"week\" : \"Hello\"}";
        assertTrue(js.Json_add_spending(json_string));
    }

    @Test
    public void json_test_year() throws JSONException {
        JsonHandler js = new JsonHandler();
        String full = get_json_string_tu();
        js.Json_add_spending(full);
        assertEquals(js.get_year_index(2020), 0);
    }

    @Test
    public void json_test_noyear() throws JSONException {
        JsonHandler js = new JsonHandler();
        String full = get_json_string_tu();
        js.Json_add_spending(full);
        assertEquals(js.get_year_index(2030), -1);
    }

    @Test
    public void test_add_error_year() throws JSONException {
        JsonHandler js = new JsonHandler();
        String full = get_json_string_tu();
        boolean x = js.Json_add_spending("{ \"year\":3020, \"week\":1, \"weekday\":\"Monday\", \"amount\":[1,2,3], \"notes\":[\"x\",\"y\",\"z\"] }");
        assertFalse(x);
    }

    @Test
    public void json_test_add_new_day() throws JSONException {
        JsonHandler js = new JsonHandler();
        String full = get_json_string_tu();
        //js.Json_add_date(full);
        assertTrue(js.Json_add_spending(full));
    }

    @Test
    public void json_test_add_existing_day() throws JSONException {
        JsonHandler js = new JsonHandler();
        String full = get_json_string_mon();
        //js.Json_add_date(full);
        assertTrue(js.Json_add_spending(full));
    }

    @Test
    public void json_get_year_test() throws JSONException {
        JsonHandler js = new JsonHandler();
        String full = get_json_string_mon();
        System.out.println(js.get_year(2020).toString());
        assertEquals(1,1);
    }

    @Test
    public void test_week_index() throws JSONException {
        JsonHandler js = new JsonHandler();
        String full = get_json_string_mon();
        assertEquals(js.get_week_index(2020,1),0);
        assertEquals(js.get_week_index(2020,10),-1);
    }

    @Test
    public void test_week() throws JSONException {
        JsonHandler js = new JsonHandler();
        String full = get_json_string_mon();
        System.out.println(js.get_week(2020,1).toString());
        assertEquals(js.get_week_index(2020,1),0);
    }

    @Test
    public void test_chosen_day() throws JSONException {
        JsonHandler js = new JsonHandler();
        String full = get_json_string_mon();
        System.out.println(js.get_chosen_day("Monday",1, 2020).toString());
        assertEquals(js.get_week_index(2020,1),0);
    }

    @Test
    public void test_add_day() throws JSONException {
        JsonHandler js = new JsonHandler();
        String full = get_json_string_mon();
        assertTrue(js.add_new_day(2020,1,"Wednesday"));
        assertTrue(js.Json_add_spending("{ \"year\":2020, \"week\":1, \"weekday\":\"Wednesday\", \"amount\":[1,2,3], \"notes\":[\"x\",\"y\",\"z\"] }"));
    }

    public String get_json_string_mon(){

        return "{ \"year\":2020, \"week\":1, \"weekday\":\"Monday\", \"amount\":[1,2,3], \"notes\":[\"x\",\"y\",\"z\"] }";
    }

    public String get_json_string_tu(){

        return "{ \"year\":2020, \"week\":1, \"weekday\":\"Tuesday\", \"amount\":[1,2,3], \"notes\":[\"x\",\"y\",\"z\"] }";
    }

    public String get_json_full(){
        ArrayList<Float> f = new ArrayList<Float>();
        f.add(10f);
        f.add(20f);
        ArrayList<String> s = new ArrayList<String>();
        s.add("Takeaway");
        s.add("Take-away");
        SpendingData sd = new SpendingData(f, s);
        sd.toString();
        WeekdayData[] wdd = new WeekdayData[7];
        wdd[0] = new WeekdayData();
        wdd[0].set_Weekday("Monday");
        wdd[0].set_Spending(sd);
        //WeekdayData[] wdd2 = new WeekdayData[7];
        wdd[1] = new WeekdayData();
        wdd[1].set_Weekday("Tuesday");
        wdd[1].set_Spending(sd);
        WeekData weekData = new WeekData();
        weekData.setYear(2020);
        ArrayList<Integer> weekList = new ArrayList<Integer>();
        weekList.add(1);
        weekList.add(2);
        ArrayList<WeekdayData[]> days = new ArrayList<WeekdayData[]>();
        days.add(wdd);
        //days.add(wdd2);
        weekData.setWeek(weekList);
        weekData.setDays(days);
        ArrayList<WeekData> week_list = new ArrayList<WeekData>();
        week_list.add(weekData);
        WeekTracking full = new WeekTracking();
        full.setDate(week_list);
        //System.out.println(sd.toString());
        //System.out.println(wdd.toString());
        //System.out.println(weekData.toString());
        //System.out.println(full.toString());
        return full.toString();
    }
}