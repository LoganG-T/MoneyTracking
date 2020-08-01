package com.logan.moneytracking;

import android.content.Context;
import android.graphics.Color;

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

public class NotesColours {

    String FILENAME = "n_colours.json";
    Context context;
    JSONObject note_data;

    public NotesColours(Context c){
        context = c;
    }

    public boolean Save(String note, int r, int g, int b){
        //delete_file(context);
        Colour_Data cd = new Colour_Data(r, g, b);
        Note_Colour noteColour = new Note_Colour(note, cd);
        System.out.println(note + " JSON COLOUR DATA");

        try {
            JSONObject jsonObject = new JSONObject(Load());
            if(Check_Existing(note, jsonObject)){
                JSONArray jsonArray = jsonObject.getJSONArray("notes");
                for (int i = 0; i < jsonArray.length(); i++){
                    if(jsonArray.getJSONObject(i).getString("note_name").equals(note)){
                        jsonArray.getJSONObject(i).put("note_colour", new JSONObject(cd.toString()));
                        break;
                    }
                }
            }else {
                jsonObject.getJSONArray("notes").put(new JSONObject(noteColour.toString()));
            }
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            System.out.println(jsonObject.toString() + " JSON COLOUR DATA");

            fos.write(jsonObject.toString().getBytes());
            fos.close();
            //save now

            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public String Load(){
        if(Check_File()){
            try {
                FileInputStream fis = context.openFileInput(FILENAME);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            New_File();
        }
        return "{notes:[]}";
    }

    public void Load_Data(){
        try {
            note_data = new JSONObject(Load());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Colour_Data Get_Colour(String s){
        Colour_Data cd = new Colour_Data(255,255,255);

        JSONArray jsonArray = null;
        try {
            jsonArray = note_data.getJSONArray("notes");
            for(int i = 0; i < jsonArray.length(); i++){
                if(jsonArray.getJSONObject(i).getString("note_name").equals(s)){
                    return new Colour_Data(jsonArray.getJSONObject(i).getJSONObject("note_colour").getInt("r"),
                            jsonArray.getJSONObject(i).getJSONObject("note_colour").getInt("g"),
                            jsonArray.getJSONObject(i).getJSONObject("note_colour").getInt("b"));
                }
            }
        } catch (JSONException e) {
        }


        return cd;
    }

    public boolean Check_Existing(String s, JSONObject jsonObject){
        JSONArray jsonArray = null;
        try {
            jsonArray = jsonObject.getJSONArray("notes");

        for (int i = 0; i < jsonArray.length(); i++){
            if(jsonArray.getJSONObject(i).getString("note_name").equals(s)){
                return true;
            }
        }
        } catch (JSONException e) {
        e.printStackTrace();
    }

        return false;
    }

    public boolean Check_File(){
        String path = context.getFilesDir().getAbsolutePath() + "/" + FILENAME;
        File file = new File(path);
        return file.exists();
    }

    public boolean New_File(){

        //String default_json = "{\"date\":[{\"week\":[1, 2],\"year\": 2020,\"days\":[[{\"weekday\": \"Monday\",\"day_spending\":{\"spending\":[\"10.00\", \"20.00\"],\"notes\": [\"Takeaway\", \"Take-away\"]}}],[{\"weekday\":\"Monday\",\"day_spending\":{\"spending\": [\"30.00\", \"40.00\"],\"notes\":[\"Takeaway3\",\"Take - away4\"]}}]]}]}";
        String default_json = "{notes:[]}";
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(default_json.getBytes());
            fos.close();
            return true;
        }catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void delete_file(Context c){
        c.deleteFile(FILENAME);
        System.out.println("Deleted");
    }


    private class Note_Colour{
        public String note_name;
        public Colour_Data note_colour;

        public Note_Colour(String s, Colour_Data d){
            note_name = s;
            note_colour = d;
        }

        public String toString(){
            String s = "{";
            s += "\"note_name\":\"" + note_name + "\",";
            s += "\"note_colour\":" + note_colour.toString();
            s += "}";

            return s;
        }
    }

    public class Colour_Data{
        public int r;
        public int g;
        public int b;

        public Colour_Data(int g_r, int g_g, int g_b){
            r = g_r;
            g = g_g;
            b = g_b;
        }

        public String toString(){
            String s = "{";
            s += "\"r\":" + r + ",";
            s += "\"g\":" + g + ",";
            s += "\"b\":" + b;
            s += "}";

            return s;
        }

        public int getR(){
            return r;
        }
        public int getG(){
            return g;
        }
        public int getB(){
            return b;
        }

        public boolean isPure(){
            if((r >= 250 && g >= 250 && b >= 250) || r <= 5 && g <= 5 && b <= 5){
                return true;
            }
            return false;
        }

    }
}
