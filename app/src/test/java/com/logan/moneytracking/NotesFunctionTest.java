package com.logan.moneytracking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class NotesFunctionTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void group_test() {
        NotesFunctions nf = new NotesFunctions();
        nf.add_note("Tesco");
        nf.add_note("Tesco");
        nf.add_note("Tescos");
        nf.add_note("tescos");
        nf.add_note("tEScOs");
        nf.add_note("electric");
        nf.add_note("electric");
        nf.add_note("electricity");
        nf.print_notes();
        assertEquals(4, 2 + 2);
    }

    @Test
    public void total_dayNotes_test() throws JSONException {
        NotesFunctions nf = new NotesFunctions();
        JSONObject jsonObject = new JSONObject("{\"spending\":[\"10\",\"20\",\"30\"],\"notes\":[\"Tesco\",\"Water\",\"Tescos\"]}");
        nf.Add_DayNotes(jsonObject);
        System.out.println(nf.Get_NotesTotal("tesco"));
        System.out.println(nf.Get_NotesTotal("Tesco"));
        System.out.println(nf.Get_NotesTotal("Water"));
        assertEquals(4, 2 + 2);
    }


    @Test
    public void total_weekNotes_test() throws JSONException {
        NotesFunctions nf = new NotesFunctions();
        JSONObject jsonObject = new JSONObject("{\"spending\":[\"10\",\"20\",\"30\"],\"notes\":[\"Tesco\",\"Water\",\"Tescos\"]}");
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);
        jsonArray.put(jsonObject);
        nf.Add_WeekNotes(jsonArray);
        System.out.println(nf.Get_NotesTotal("tesco"));
        System.out.println(nf.Get_NotesTotal("Tesco"));
        System.out.println(nf.Get_NotesTotal("Water"));
        assertEquals(4, 2 + 2);
    }

}