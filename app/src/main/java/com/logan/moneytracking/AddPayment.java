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

import java.util.Calendar;

public class AddPayment {

    public AddPayment(Activity new_activity, JsonHandler js, DateObject d){

        activity = new_activity;
        jsonHandler = js;
        Calendar calendar = Calendar.getInstance();
        current_date = d;
        spinnerDay = new SpinnerDay(activity, activity.getApplicationContext(), current_date);
        spinnerDay.spinner_setup(R.id.spinDay);
        activity.findViewById(R.id.spinDay).setVisibility(View.INVISIBLE);
    }

    private Activity activity;
    final JsonHandler jsonHandler;

    private EditText text;
    private EditText notesText;
    private SpinnerDay spinnerDay;
    private boolean plus = true;
    private int payment_counter = 0;

    private DateObject current_date;

    public void load_plus_button(){
        ImageButton b = (ImageButton) activity.findViewById(R.id.imageButton);
        b.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                addPayment(v);
            }
        });
    }

    public void load_another_week(JSONArray json_week, LinearLayout l_layout) throws JSONException {
        l_layout.removeAllViews();
        if(json_week == null){
            return;
        }
        System.out.println("Load another week " + json_week.toString());
        for(int i = 0; i < json_week.length(); i++){
            JSONArray day_spending = json_week.getJSONObject(i).getJSONObject("day_spending").getJSONArray("spending");
            JSONArray day_notes = json_week.getJSONObject(i).getJSONObject("day_spending").getJSONArray("notes");
            for(int d_i = 0; d_i < day_spending.length(); d_i++){
                LinearLayout cur_layout = new_layout(i, l_layout);
                TextView pay_view = cur_layout.findViewWithTag("Text");
                pay_view.setText(day_spending.getString(d_i));
                TextView note_view = cur_layout.findViewWithTag("Notes");
                note_view.setText(day_notes.getString(d_i));
            }

        }
    }

    private void addPayment(View view){

        if(plus) {
            plus = !plus;
            LinearLayout layout = (LinearLayout) activity.findViewById(R.id.textLayout);

            text = new EditText(activity);
            text.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            layout.addView(text);
            display_notes();

            activity.findViewById(R.id.spinDay).setVisibility(View.VISIBLE);

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
                activity.findViewById(R.id.spinDay).setVisibility(View.INVISIBLE);
            }
        }
    }

    private String payment_toString(String payment, String note){
        return "{ \"year\":" + current_date.getYear() + ", \"week\": " + current_date.getWeek() +
                ", \"weekday\":" + current_date.getDay() + ", \"amount\":[" + payment + "],\"notes\":[" + note + "] }";
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
        String remove_string = "{ \"year\":" + current_date.getYear() +", \"week\":" + current_date.getWeek() +", \"weekday\":\"" + current_date.getDay()
                + "\", \"index\":" + id + " }";
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
