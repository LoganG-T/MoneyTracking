package com.logan.moneytracking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotesFunctions {

    String inc_all = "All";

    public NotesFunctions(){
        spending_notes = new ArrayList<SpendingCount>();
        total_spending = 0f;
    }

    ArrayList<SpendingCount> spending_notes;
    float total_spending;

    public void load_note_count(){

    }

    public void save_note_count(){

    }

    public void remove_note(String s){
        for(int i = 0; i < spending_notes.size(); i++){
            if(spending_notes.get(i).check_note(s)){
                spending_notes.get(i).reduce_count();
                break;
            }
        }
    }

    public void group_notes(){

    }

    public void add_note(String s){
        //Go through all notes in this json object and group by the notes
        //Reduce the current word by 1 incase it ends in s
        boolean b = false;
        for(int i = 0; i < spending_notes.size(); i++){
            if(spending_notes.get(i).check_note(s)){
                b = true;
                spending_notes.get(i).add_count();
                break;
            }
        }
        if(! b){
            SpendingCount sc = new SpendingCount(s);
            spending_notes.add(sc);
        }
    }
    public void add_note(String s, float f){
        //Go through all notes in this json object and group by the notes
        //Reduce the current word by 1 incase it ends in s
        boolean b = false;
        for(int i = 0; i < spending_notes.size(); i++){
            if(spending_notes.get(i).check_note(s)){
                b = true;
                spending_notes.get(i).add_count(f);
                break;
            }
        }
        if(! b){
            SpendingCount sc = new SpendingCount(s, f);
            spending_notes.add(sc);
        }
        total_spending += f;
    }

    public void print_notes(){
        for(int i = 0; i < spending_notes.size(); i++){
            spending_notes.get(i).print();
        }
    }

    public ArrayList<SpendingCount> get_allNotes(){
        return spending_notes;
    }
    /*
    {"spending":[],"notes":[]}
     */
    public void Add_DayNotes(JSONObject given_json) throws JSONException {
        ArrayList<String> spending = new ArrayList<String>();
        JSONArray notes_arr = given_json.getJSONArray("notes");
        JSONArray spend_arr = given_json.getJSONArray("spending");
        for(int i = 0; i < notes_arr.length(); i++){
            add_note(notes_arr.getString(i), Float.valueOf(spend_arr.getString(i)));
        }
    }

    public float Get_NotesTotal(String given_note){
        if(given_note.equals(inc_all)){
            return total_spending;
        }
        for(int i = 0; i < spending_notes.size(); i++){
            if(spending_notes.get(i).check_note(given_note)){
                return spending_notes.get(i).getSpending();
            }
        }
        return 0f;
    }

    public float Get_TotalSpending(){
        return total_spending;
    }
    /*
    [{"weekday":"Monday","month":"December","day_spending":{"spending":[],"notes":[]}},
    {"weekday":"Wednesday","month":"January","day_spending":{"spending":["15.5"],"notes":["Tesco"]}}]
     */
    public void Add_WeekNotes(JSONArray given_json) throws JSONException {
        for(int i = 0; i < given_json.length(); i++){
            Add_DayNotes(given_json.getJSONObject(i).getJSONObject("day_spending"));
        }
    }

    public void Add_MonthNotes(JSONArray given_json, String given_month) throws JSONException {
        for(int i = 0; i < given_json.length(); i++){
            if(given_json.getJSONObject(i).getString("month").equals(given_month)) {
                Add_DayNotes(given_json.getJSONObject(i).getJSONObject("day_spending"));
            }
        }
    }


    public String[] Get_NotesNames(){
        String[] s = new String[spending_notes.size() + 1];
        s[0] = inc_all;
        for(int i = 0; i < spending_notes.size(); i++){
            s[i + 1] = spending_notes.get(i).getNote();
        }
        return s;
    }


    private class SpendingCount{
        String note;
        int count;
        float total_spent;
        public SpendingCount(String n){
            n = n.toLowerCase();
            n = n.substring(0, 1).toUpperCase() + n.substring(1);
            note = n;
            count = 1;
            total_spent = 0f;
        }
        public SpendingCount(String n, float spent){
            n = n.toLowerCase();
            n = n.substring(0, 1).toUpperCase() + n.substring(1);
            note = n;
            count = 1;
            total_spent = spent;
        }

        public void print(){
            System.out.println(note + " " + count);
        }

        public void add_count(){
            count++;
        }
        public void add_count(float spent){
            count++;
            total_spent += spent;
        }

        public void reduce_count(){
            count--;
        }
        public String getNote(){
            return note;
        }
        public float getSpending(){
            return total_spent;
        }

        public boolean check_note(String s){
            s = s.toLowerCase();
            if(s.substring(s.length() - 1).equals("s")){
                s = s.substring(0, s.length() - 1);
            }
            boolean x = s.contains(note.toLowerCase()) || note.toLowerCase().contains(s);
            return x;
        }
    }
}
