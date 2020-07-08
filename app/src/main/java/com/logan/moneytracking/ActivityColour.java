package com.logan.moneytracking;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.logan.R;

public class ActivityColour extends AppCompatActivity {

    ImageView show_colour;
    int r = 0;
    int g = 0;
    int b = 0;
    boolean text = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_colours);

        show_colour = findViewById(R.id.imageView);
        Set_Colour_Bars(R.id.seek_red,R.id.seek_green,R.id.seek_blue);
    }

    private void Set_Colour_Bars(int r_id, int g_id, int b_id){
        SeekBar seek_red = findViewById(r_id);
        seek_red.setMax(255);
        seek_red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub
                r = progress;
                System.out.println(progress + " COLOUR PROGRESS");
                Update_Colour();

            }
        });

        SeekBar seek_green = findViewById(g_id);
        seek_green.setMax(255);
        seek_green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub
                g = progress;
                Update_Colour();

            }
        });

        SeekBar seek_blue = findViewById(b_id);
        seek_blue.setMax(255);
        seek_blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub
                b = progress;
                Update_Colour();

            }
        });
    }

    public void Update_Colour(){
        show_colour.setColorFilter(Color.rgb(r, g, b));
        TextView tv = findViewById(R.id.text_red);
        tv.setBackgroundColor(Color.rgb(r,g,b));
        if(! text && (r + g + b < 350)){
            tv.setTextColor(Color.rgb(255,255,255));
            text = true;
        }else if(text && (r + g + b >= 350)){
            tv.setTextColor(Color.rgb(0,0,0));
            text = false;
        }
    }

}
