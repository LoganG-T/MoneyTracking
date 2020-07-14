package com.logan.moneytracking;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SpendingData {
    ArrayList<Float> amount;
    ArrayList<String> name;

    public SpendingData(){
        amount = new ArrayList<Float>();
        name = new ArrayList<String>();
    }

    public SpendingData(ArrayList<Float> given_amount, ArrayList<String> given_name){
        amount = given_amount;
        name = given_name;
    }

    public SpendingData(Float[] given_amount, String[] given_name){
        amount = new ArrayList<Float>();
        name = new ArrayList<String>();
        for (int i = 0; i < given_amount.length; i++) {
            amount.add(given_amount[i]);
            name.add(given_name[i]);
        }
    }

    public SpendingData(JSONArray given_amount, JSONArray given_name) throws JSONException {
        amount = new ArrayList<Float>();
        name = new ArrayList<String>();
        for (int i = 0; i < given_amount.length(); i++) {
            amount.add((float)given_amount.getDouble(i));
            name.add(given_name.getString(i));
        }
    }

    public ArrayList<Float> getAmount(){
        return amount;
    }

    public void setAmount(ArrayList<Float> given_amount){
        amount = given_amount;
    }

    public ArrayList<String> getName(){
        return name;
    }

    public void setName(ArrayList<String> given_name){
        name = given_name;
    }

    public String nameString(){
        String s = "";
        s += "[";
        for(int i = 0; i < name.size(); i++){
            s += "\"" + name.get(i) + "\"";
            if(i < name.size() - 1){
                s += ",";
            }
        }
        s += "]";
        return s;
    }

    public String toString(){

        return "{ \"spending\" :" + amount.toString() + ", \"notes\" : " + nameString() + "}";
    }
}