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
            canvas.drawCircle(width / 2, width / 4, width / 4, drawPaint);
            //drawPaint.setColor(Color.GREEN);
            int x_center = width / 2;
            int y_center = width / 4;
            int circle_diameter = width / 2;
            for (int y = (width / 4) - (width / 4); y < (width / 4) + (width / 4); y++) {
                for (int x = (width / 2) - (width / 4); x < (width / 2) + (width / 4); x++) {
                    if ((x - x_center) * (x - x_center) + (y - y_center) * (y - y_center) <= circle_diameter / 2 * circle_diameter / 2) {
                        double f = Math.toDegrees(Math.atan2(y - y_center, x - x_center));
                        /*if((y - y_center < 0) && (x - x_center < 0)){
                            f += 180;
                        }*/
                        if (f < 0) {
                            f += 360;
                        }
                        if (f >= 0 && f < 90) {
                            drawPaint.setColor(Color.RED);
                        } else if (f >= 90 && f < 180) {
                            drawPaint.setColor(Color.GREEN);
                        } else if (f >= 180 && f < 270) {
                            drawPaint.setColor(Color.BLUE);
                        } else if (f >= 270 && f < 360) {
                            drawPaint.setColor(Chart_Colours());
                        }
                        canvas.drawPoint(x, y, drawPaint);
                    }
                }
            }
            changed = false;
        }
    }

    public void Set_PieChart(HashMap<String, Float> given_percents){
        percents = given_percents;
        changed = true;
    }

    public int Chart_Colours(){
        int[] c_array = new int[6];

        c_array[0] = Color.rgb(255,255,255);
        c_array[1] = Color.rgb(255,255,255);
        c_array[2] = Color.rgb(255,255,255);
        c_array[3] = Color.rgb(255,255,255);
        c_array[4] = Color.rgb(255,255,255);
        c_array[5] = Color.rgb(255,255,255);
        float f = 0;
        if(f >= 0 && f < 90){
            return c_array[0];
        }else if(f >= 90 && f < 180){
            drawPaint.setColor(Color.GREEN);
        }else if(f >= 180 && f < 270){
            drawPaint.setColor(Color.BLUE);
        }else if(f >= 270 && f < 360){
            drawPaint.setColor(Color.YELLOW);
        }
        return c_array[0];
    }
}
