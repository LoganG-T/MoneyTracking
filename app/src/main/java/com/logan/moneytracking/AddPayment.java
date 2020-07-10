package com.logan.moneytracking;

import android.app.Activity;
import android.graphics.Color;
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

import java.util.ArrayList;
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
        totalText = (TextView)activity.findViewById(R.id.txt_weekTotal);
        index_list = new ArrayList<IndexInfo>();
        day_counts = new int[7];
        delete_counts = new int[7];
        notesColours.Load_Data();
    }

    private Activity activity;
    final JsonHandler jsonHandler;

    private String cur_sym = "£";
    private TextView totalText;
    private EditText text;
    private EditText notesText;
    private SpinnerDay spinnerDay;
    private boolean plus = true;
    private ArrayList<IndexInfo> index_list;
    private int[] day_counts;
    private int[] delete_counts;
    private float temp_total = 0;

    NotesColours notesColours = new NotesColours(activity.getApplicationContext());


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
        index_list = new ArrayList<IndexInfo>();
        day_counts = new int[7];
        delete_counts = new int[7];
        System.out.println("Load another week " + json_week.toString());
        NotesFunctions nf = new NotesFunctions();
        nf.Add_WeekNotes(json_week);
        temp_total = nf.Get_TotalSpending();
        totalText.setText(cur_sym + String.valueOf(temp_total));
        /*NotesColours notesColours = new NotesColours(activity.getApplicationContext());
        notesColours.Load_Data();*/
        for(int i = 0; i < json_week.length(); i++){
            JSONArray day_spending = json_week.getJSONObject(i).getJSONObject("day_spending").getJSONArray("spending");
            JSONArray day_notes = json_week.getJSONObject(i).getJSONObject("day_spending").getJSONArray("notes");
            for(int d_i = 0; d_i < day_spending.length(); d_i++){
                LinearLayout cur_layout = new_layout(i, json_week.getJSONObject(i).getString("weekday"), l_layout);
                TextView pay_view = cur_layout.findViewWithTag("Text");
                pay_view.setText(day_spending.getString(d_i));
                TextView note_view = cur_layout.findViewWithTag("Notes");
                note_view.setText(day_notes.getString(d_i));
                NotesColours.Colour_Data ns = notesColours.Get_Colour(day_notes.getString(d_i));
                cur_layout.setBackgroundColor(Color.rgb(ns.getR(),ns.getG(),ns.getB()));
                TextView day_view = cur_layout.findViewWithTag("Day");
                day_view.setText(json_week.getJSONObject(i).getString("weekday"));
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
                LinearLayout new_layout = new_layout(layout.getChildCount() + 1, current_date.getDay(), layout);
                System.out.println("LAYOUT ID " + new_layout.getId());
                TextView pay_view = new_layout.findViewWithTag("Text");
                pay_view.setText(payment_string);
                TextView note_view = new_layout.findViewWithTag("Notes");
                note_view.setText(notes);
                TextView day_view = new_layout.findViewWithTag("Day");
                day_view.setText(current_date.getDay());
                //add_notes_layout(notes, layout.getChildCount(), layout);
                String add_string = payment_toString(payment_string, notes);
                jsonHandler.Json_add_spending(add_string);
                SaveLoad sl = new SaveLoad();
                sl.Save_Data(activity.getApplicationContext(), jsonHandler);

                temp_total += Float.parseFloat(payment_string);
                totalText.setText(cur_sym + String.valueOf(temp_total));

                NotesColours.Colour_Data ns = notesColours.Get_Colour(notes);
                new_layout.setBackgroundColor(Color.rgb(ns.getR(),ns.getG(),ns.getB()));


                text.setVisibility(View.GONE);
                notesText.setVisibility(View.GONE);

                //payment_counter++;
                ImageButton ib = (ImageButton)activity.findViewById(R.id.imageButton);
                ib.setImageResource(R.drawable.add_image);
                activity.findViewById(R.id.spinDay).setVisibility(View.INVISIBLE);
            }
        }
    }

    private String payment_toString(String payment, String note){
        current_date.update_month();
        return "{ \"year\":" + current_date.getYear() + ", \"week\": " + current_date.getWeek() +",\"month\": \"" + current_date.getMonth() +
                "\", \"weekday\":" + current_date.getDay() + ", \"amount\":[" + payment + "],\"notes\":[" + note + "] }";
    }

    private LinearLayout new_layout(int id, String wday, LinearLayout layout){
        final String weekday = wday;
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
        Space space_zero = new Space(activity);
        space_zero.setLayoutParams(param);

        TextView notesTextView = new TextView(activity);
        TextView dayTextView = new TextView(activity);
        Button b = new Button(activity);
        b.setText("Delete");
        //b.setId(id);
        //textView.setId(id);
        newLayout.setId(id);

        IndexInfo ii = new IndexInfo(id, day_counts[current_date.getDayInt(wday) - 1], wday);
        day_counts[current_date.getDayInt(wday) - 1] += 1;
        index_list.add(ii);

        b.setTag("Button");
        textView.setTag("Text");
        notesTextView.setTag("Notes");
        dayTextView.setTag("Day");

        b.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //String jsonStringNum = Integer.toString();
                int i = newLayout.getId();
                System.out.println("AFTER 2 LAYOUT ID " + i);
                removePayment(v, i);
            };
        });

        newLayout.addView(dayTextView);
        newLayout.addView(space_zero);
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
        display_old_notes(notesText);
        notesText.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(notesText);
    }

    private void display_old_notes(TextView textView){
        PaymentNotesSpinner notesSpinner = new PaymentNotesSpinner(activity, activity.getApplicationContext());

        notesSpinner.setTextView(textView);


        try {
            NotesFunctions nf = new NotesFunctions();
            nf.Add_YearNotes(jsonHandler.get_year(2020));
            notesSpinner.set_NotesFunc(nf);
            ArrayList<String> s = nf.Get_NotesNames();
            s.remove("All");
            notesSpinner.Set_AllNotes(s);

            notesSpinner.spinner_setup(R.id.spin_allNotes);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    void removePayment(View view, int id){

        LinearLayout layout = (LinearLayout) activity.findViewById(id);

        //TextView tv = (TextView)layout.getChildAt(1);
        //float f = Float.parseFloat(tv.getText().toString());
        //add_to_total(-f);

        if(layout == null){
            return;
        }
        layout.removeAllViews();
        int c_id = 0;
        for(int i = 0; i < index_list.size(); i++){
            if(index_list.get(i).Get_Index() == id){
                c_id = i;
                break;
            }
        }

        //{ "year":2020, "week":1, "weekday":"Monday", "index":1}
        String remove_string = "{ \"year\":" + current_date.getYear() +", \"week\":" + current_date.getWeek() +", \"weekday\":\"" + index_list.get(c_id).Get_Day()
                + "\", \"index\":" + (index_list.get(c_id).Get_ArrayPos() - delete_counts[current_date.getDayInt(index_list.get(c_id).Get_Day()) - 1]) + " }";
        delete_counts[current_date.getDayInt(index_list.get(c_id).Get_Day()) - 1] += 1;
        index_list.remove(c_id);
        System.out.println(remove_string);
        try {
            float deleted_amount = jsonHandler.delete_spending(remove_string);
            SaveLoad sl = new SaveLoad();
            sl.Save_Data(activity.getApplicationContext(), jsonHandler);
            temp_total -= deleted_amount;
            totalText.setText(cur_sym + String.valueOf(temp_total));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private class IndexInfo{
        int index_pos;
        int array_pos;
        String day;

        public IndexInfo(int i, int a, String s){
            index_pos = i;
            array_pos = a;
            day = s;
        }

        public int Get_Index(){
            return index_pos;
        }

        public int Get_ArrayPos(){
            return array_pos;
        }

        public String Get_Day(){
            return  day;
        }

    }

}
