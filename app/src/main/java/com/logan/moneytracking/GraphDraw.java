package com.logan.moneytracking;

import android.content.Context;
import android.graphics.Canvas;
//import android.graphics.Color;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.HashMap;

//Example https://guides.codepath.com/android/Basic-Painting-with-Views
public class GraphDraw extends View {

    private final int paintColor = Color.BLACK;
    int width;
    int height;
    boolean changed = true;
    HashMap<String, Float> percents;
    double[] percent_sums;
    String[] names;
    // defines paint and canvas
    private Paint drawPaint;

    public GraphDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*
        setFocusable(true);
        setFocusableInTouchMode(true);
         */
        setupPaint();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;


    }

    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (changed) {
            System.out.println("DRAW NOW");
            canvas.drawCircle(width / 2, width / 4, width / 4, drawPaint);
            //drawPaint.setColor(Color.GREEN);
            int x_center = width / 2;
            int y_center = width / 4;
            int circle_diameter = width / 2;
            for (int y = (width / 4) - (width / 4); y < (width / 4) + (width / 4); y++) {
                for (int x = (width / 2) - (width / 4); x < (width / 2) + (width / 4); x++) {
                    if ((x - x_center) * (x - x_center) + (y - y_center) * (y - y_center) <= circle_diameter / 2 * circle_diameter / 2) {
                        drawPaint.setColor(Chart_Colours(x,y,x_center, y_center));
                        canvas.drawPoint(x, y, drawPaint);
                    }
                }
            }
            changed = false;
        }
    }

    public void Set_PieChart(HashMap<String, Float> given_percents, String[] given_names){
        percents = given_percents;
        names = given_names;
        percent_sums = new double[names.length];
        percent_sums[0] = 0;
        for(int i = 1; i < names.length; i++){
            percent_sums[i] = percent_sums[i - 1] + ((double)percents.get(names[i]) * 3.6);
        }
        changed = true;
    }

    public int Chart_Colours(int x, int y, int c_x, int c_y){
        if(percents == null){
            return Color.BLACK;
        }
        int[] c_array = new int[6];

        c_array[0] = Color.rgb(255,255,255);
        c_array[1] = Color.rgb(255,0,0);
        c_array[2] = Color.rgb(0,255,0);
        c_array[3] = Color.rgb(0,0,255);
        c_array[4] = Color.rgb(0,255,255);
        c_array[5] = Color.rgb(255,0,255);
        double f = Math.toDegrees(Math.atan2(y - c_y, x - c_x));
        if (f < 0) {
            f += 360;
        }
        //3.6 is 1% of 360 -> degrees in circle
        for(int i = 0; i < percents.size();i++){
            if(i == 0){
                if(f >= percent_sums[i] && f < (3.6 * (percents.get(names[i])))){
                    return c_array[i];
                }
            }else{
                if(f >= percent_sums[i] && f < (3.6 * (percent_sums[i - 1] + percents.get(names[i])))){
                    return c_array[i];
                }
            }
        }
        return c_array[5];
    }
}
