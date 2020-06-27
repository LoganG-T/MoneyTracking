package com.logan.moneytracking;

import java.util.ArrayList;

public class DayData {
    Weekday set_day;
    ArrayList<SpendingDataOld> spending_list;
    int total_spending;

    public DayData(Weekday given_day){
        set_day = given_day;
        total_spending = 0;
    }

    public void add_spending(SpendingDataOld new_spend){
        spending_list.add(new_spend);
        total_spending += 1;
    }

    public int getTotal_spending(){
        return total_spending;
    }

    public SpendingDataOld get_spending_at(int index){
        return spending_list.get(index);
    }
}
/*

public class WeekData {
    DayData[] week_array;


    public WeekData(){
        week_array = new DayData[7];
        int count = 0;
        for(Weekday day : Weekday.values()){
            week_array[count] = new DayData(day);
            count++;
        }
    }
}

 */