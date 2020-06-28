package com.logan.moneytracking;

import android.app.Activity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.logan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Calendar;

public class LoadPage {

    public LoadPage(Activity new_activity, JsonHandler js){

        activity = new_activity;
        jsonHandler = js;
        current_date = new DateObject();
    }

    private Activity activity;
    final JsonHandler jsonHandler;

    private EditText text;
    private EditText notesText;
    private boolean plus = true;
    private int payment_counter = 0;

    private DateObject current_date;
    private int current_year = 2020;
    private int current_week = 1;//7 is February
    private String current_day = "Monday";

    /*public void load_day_buttons(final LoadPage loadPage){
        loadPage.load_another_day(current_day, (LinearLayout) activity.findViewById(R.id.payment_layout));
        Button b = (Button)activity.findViewById(R.id.day_1);
        b.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //LoadPage lp = loadPage;
                JsonHandler js = jsonHandler;
                current_day = "Monday";

                loadPage.load_another_day("Monday", (LinearLayout) activity.findViewById(R.id.payment_layout));
            }
        });
        Button b_tues = (Button)activity.findViewById(R.id.day_2);
        b_tues.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                LoadPage lp = loadPage;
                JsonHandler js = jsonHandler;
                current_day = "Tuesday";

                lp.load_another_day("Tuesday", (LinearLayout) activity.findViewById(R.id.payment_layout));
            }
        });
        Button b_wed = (Button)activity.findViewById(R.id.day_3);
        b_wed.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                LoadPage lp = loadPage;
                JsonHandler js = jsonHandler;
                current_day = "Wednesday";

                lp.load_another_day("Wednesday", (LinearLayout) activity.findViewById(R.id.payment_layout));
            }
        });

        Button b_thur = (Button)activity.findViewById(R.id.day_4);
        b_thur.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                LoadPage lp = loadPage;
                JsonHandler js = jsonHandler;
                current_day = "Thursday";

                lp.load_another_day("Thursday", (LinearLayout) activity.findViewById(R.id.payment_layout));
            }
        });
        Button b_fri = (Button)activity.findViewById(R.id.day_5);
        b_fri.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                LoadPage lp = loadPage;
                JsonHandler js = jsonHandler;
                current_day = "Friday";

                lp.load_another_day("Friday", (LinearLayout) activity.findViewById(R.id.payment_layout));
            }
        });
        Button b_sat = (Button)activity.findViewById(R.id.day_6);
        b_sat.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                LoadPage lp = loadPage;
                JsonHandler js = jsonHandler;
                current_day = "Saturday";

                lp.load_another_day("Saturday", (LinearLayout) activity.findViewById(R.id.payment_layout));
            }
        });
        Button b_sun = (Button)activity.findViewById(R.id.day_7);
        b_sun.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                LoadPage lp = loadPage;
                JsonHandler js = jsonHandler;
                current_day = "Sunday";

                lp.load_another_day("Sunday", (LinearLayout) activity.findViewById(R.id.payment_layout));
            }
        });
    }*/

    public void load_plus_button(){
        ImageButton b = (ImageButton) activity.findViewById(R.id.imageButton);
        b.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                addPayment(v);
            }
        });
    }

    private void addPayment(View view){

        if(plus) {
            plus = !plus;
            LinearLayout layout = (LinearLayout) activity.findViewById(R.id.textLayout);

            text = new EditText(activity);
            text.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            layout.addView(text);
            display_notes();

            ImageButton ib = (ImageButton)activity.findViewById(R.id.imageButton);
            ib.setImageResource(R.drawable.confirm_action);
        }
        else{

            LinearLayout layout = (LinearLayout) activity.findViewById(R.id.payment_layout);
            String payment_string = text.getText().toString();

            int i = payment_string.indexOf('.');
            if(i > -1) {
                payment_string += "00";
                String s = payment_string.substring(0, i);
                System.out.println(s);
                System.out.println(payment_string);
                String sd = payment_string.substring(i + 1, i + 3);
                System.out.println(s + " " + sd);
                payment_string = s + "." + sd;
            }else{
                payment_string += ".00";
            }
            if(!payment_string.equals("") && !payment_string.equals(".00")){
                plus = !plus;
                //layout.removeView(text);
                String notes = notesText.getText().toString();
                if(notes.equals("") || notes.isEmpty()){
                    notes = "None";
                }
                //add_pay_layout(payment_string, layout.getChildCount(), layout);
                LinearLayout new_layout = new_layout(layout.getChildCount(), layout);
                TextView pay_view = new_layout.findViewWithTag("Text");
                pay_view.setText(payment_string);
                TextView note_view = new_layout.findViewWithTag("Notes");
                note_view.setText(notes);
                //add_notes_layout(notes, layout.getChildCount(), layout);
                String add_string = payment_toString(payment_string, notes);
                jsonHandler.Json_add_spending(add_string);
                SaveLoad sl = new SaveLoad();
                sl.Save_Data(activity.getApplicationContext(), jsonHandler);

                float f = Float.parseFloat(payment_string);
                //add_to_total(f);
                //edit("labels", payment_string);
                text.setVisibility(View.GONE);
                notesText.setVisibility(View.GONE);
                payment_counter++;
                ImageButton ib = (ImageButton)activity.findViewById(R.id.imageButton);
                ib.setImageResource(R.drawable.add_image);
            }
        }
    }

    private String[] get_date(int year, int week){
        String return_dates[] = new String[7];

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.WEEK_OF_YEAR, week);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        /*for(int i = 0; i < 7; i++){
            if(calendar.get(Calendar.DAY_OF_WEEK) == 1){
                break;
            }
        }*/
        for(int i = 0; i < 7; i++){
            int temp_day = calendar.get(Calendar.DAY_OF_MONTH);
            return_dates[i] = String.valueOf(temp_day);
            if(temp_day > 10 && temp_day < 14){
                return_dates[i] += "th";
            }else {
                switch (temp_day % 10) {
                    case (1):
                        return_dates[i] += "st";
                        break;
                    case (2):
                        return_dates[i] += "nd";
                        break;
                    case (3):
                        return_dates[i] += "rd";
                        break;
                    default:
                        return_dates[i] += "th";
                        break;
                }
            }
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }

        return return_dates;
    }


    /*public void set_dates(){
        String[] dates = get_date(current_year, current_week);

        Button b1 = (Button)activity.findViewById(R.id.day_1);
        b1.setText("Monday " + String.valueOf(dates[0]));
        Button b2 = (Button)activity.findViewById(R.id.day_2);
        b2.setText("Tuesday " + String.valueOf(dates[1]));
        Button b3 = (Button)activity.findViewById(R.id.day_3);
        b3.setText("Wednesday " + String.valueOf(dates[2]));
        Button b4 = (Button)activity.findViewById(R.id.day_4);
        b4.setText("Thursday " + String.valueOf(dates[3]));
        Button b5 = (Button)activity.findViewById(R.id.day_5);
        b5.setText("Friday " + String.valueOf(dates[4]));
        Button b6 = (Button)activity.findViewById(R.id.day_6);
        b6.setText("Saturday " + String.valueOf(dates[5]));
        Button b7 = (Button)activity.findViewById(R.id.day_7);
        b7.setText("Sunday " + String.valueOf(dates[6]));

        current_day = "Monday";

        load_another_day("Monday", (LinearLayout) activity.findViewById(R.id.payment_layout));
    }*/

    private String payment_toString(String payment, String note){
        return "{ \"year\":" + current_year + ", \"week\": " + current_week +
                ", \"weekday\":" + current_day + ", \"amount\":[" + payment + "],\"notes\":[" + note + "] }";
    }

    private LinearLayout new_layout(int id, LinearLayout layout){
        final LinearLayout newLayout = new LinearLayout(activity);
        newLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView textView = new TextView(activity);
        //https://stackoverflow.com/questions/4641072/how-to-set-layout-weight-attribute-dynamically-from-code
        Space space = new Space(activity);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );
        space.setLayoutParams(param);

        Space second_space = new Space(activity);
        second_space.setLayoutParams(param);

        TextView notesTextView = new TextView(activity);
        Button b = new Button(activity);
        b.setText("Delete");
        //b.setId(id);
        //textView.setId(id);
        newLayout.setId(id);
        b.setTag("Button");
        textView.setTag("Text");
        notesTextView.setTag("Notes");

        b.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //String jsonStringNum = Integer.toString();
                int i = newLayout.getId();
                removePayment(v, i);
            };
        });

        newLayout.addView(textView);
        newLayout.addView(space);
        newLayout.addView(notesTextView);
        newLayout.addView(second_space);
        newLayout.addView(b);
        layout.addView(newLayout);
        return newLayout;
    }

    /*public void add_pay_layout(String set_text, int id, LinearLayout layout){
        final LinearLayout newLayout = new LinearLayout(activity);
        newLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView textView = new TextView(activity);
        textView.setText(set_text);
        Button b = new Button(activity);
        b.setText("Delete");
        //b.setId(id);
        //textView.setId(id);
        newLayout.setId(id);
        b.setTag("Button");
        textView.setTag("Text");
        b.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //String jsonStringNum = Integer.toString();
                int i = newLayout.getId();
                //removePayment(v, i);
            };
        });
        newLayout.addView(textView);
        newLayout.addView(b);
        layout.addView(newLayout);
    }*/


    /*public void load_another_day(ArrayList<String> s_list, ArrayList<Integer> i_list, LinearLayout l_layout){
        l_layout.removeAllViews();
        for(int i = 0; i < i_list.size(); i++){
            LinearLayout cur_layout = new_layout(i_list.get(i), l_layout);
            TextView pay_view = cur_layout.findViewWithTag("Text");
            pay_view.setText(payment_string);
            TextView note_view = cur_layout.findViewWithTag("Notes");
            note_view.setText(notes);
        }
    }*/

    public void load_another_day(String given_day, LinearLayout l_layout){
        l_layout.removeAllViews();
        TextView t = (TextView) activity.findViewById(R.id.dayDisplay);
        t.setText(given_day.substring(0,3).toLowerCase());
        JSONObject current_obj = jsonHandler.get_chosen_day(given_day, current_week, current_year);
        System.out.println((current_day.toString()));
        if(current_obj != null) {
            try {
                JSONArray spending = current_obj.getJSONArray("spending");
                JSONArray notes = current_obj.getJSONArray("notes");
                for (int i = 0; i < spending.length(); i++) {
                    LinearLayout cur_layout = new_layout(i, l_layout);
                    TextView pay_view = cur_layout.findViewWithTag("Text");
                    pay_view.setText(spending.get(i).toString());
                    TextView note_view = cur_layout.findViewWithTag("Notes");
                    note_view.setText(notes.getString(i));
                    //add_pay_layout(spending.get(i).toString(), i, l_layout);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //This only needs to be called if the user if adding spending data to the new day, otherwise it can be left empty
        else{
            //Create new json for the given day in that week to be added to
            //System.out.println("ADD NEW DAY");
            try {
                jsonHandler.add_new_day(current_year, current_week, current_day);
            } catch (JSONException e) {
                e.printStackTrace();
                //New day not added display why and deal with it
            }
        }
        /*for(int i = 0; i < i_list.size(); i++){
            add_pay_layout(s_list.get(i), i_list.get(i), l_layout);
        }*/
    }

    public void setCurrent_week(int c_week){
        current_week = c_week;
    }

    private void display_notes(){
        LinearLayout layout = (LinearLayout) activity.findViewById(R.id.notesTextLayout);

        notesText = new EditText(activity);
        notesText.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(notesText);
    }


    void removePayment(View view, int id){

        LinearLayout layout = (LinearLayout) activity.findViewById(id);

        TextView tv = (TextView)layout.getChildAt(0);
        float f = Float.parseFloat(tv.getText().toString());
        //add_to_total(-f);

        layout.removeAllViews();

        //{ "year":2020, "week":1, "weekday":"Monday", "index":1}
        String remove_string = "{ \"year\":" + current_year +", \"week\":" + current_week +", \"weekday\":\"" + current_day + "\", \"index\":" + id + " }";
        System.out.println(remove_string);
        try {
            boolean deleted = jsonHandler.delete_spending(remove_string);
            if(deleted){
                SaveLoad sl = new SaveLoad();
                sl.Save_Data(activity.getApplicationContext(), jsonHandler);
                System.out.println(jsonHandler.get_year(2020));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
