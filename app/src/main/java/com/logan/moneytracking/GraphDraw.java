package com.logan.moneytracking;

import android.content.Context;
import android.graphics.Canvas;
//import android.graphics.Color;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import java.util.HashMap;

//Example https://guides.codepath.com/android/Basic-Painting-with-Views
public class GraphDraw extends SurfaceView implements SurfaceHolder.Callback {

    private final int paintColor = Color.BLACK;
    int width;
    int height;
    boolean changed = true;
    boolean empty = false;
    HashMap<String, Float> percents;
    float[] percent_sums;
    String[] names;
    int[] c_array;

    SurfaceHolder holder;
    // defines paint and canvas
    private Paint drawPaint;

    public GraphDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();

        holder.addCallback(this);
        setupPaint();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        //Default colours for if the user has not set up personalized colours
        c_array = new int[12];

        c_array[0] = Color.rgb(255,61,61);
        c_array[1] = Color.rgb(255,118,33);
        c_array[2] = Color.rgb(255,151,15);
        c_array[3] = Color.rgb(255,179,15);
        c_array[4] = Color.rgb(255,211,13);
        c_array[5] = Color.rgb(255,233,0);
        c_array[6] = Color.rgb(113, 225, 11);
        c_array[7] = Color.rgb(77, 199, 37);
        c_array[8] = Color.rgb(52, 179, 113);
        c_array[9] = Color.rgb(52, 119, 186);
        c_array[10] = Color.rgb(149, 91, 194);
        c_array[11] = Color.rgb(224, 74, 177);
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

    public void Set_PieChart(HashMap<String, Float> given_percents, String[] given_names){
        if(given_names == null || given_names.length == 0){
            empty = true;
            return;
        }
        percents = given_percents;
        names = given_names;
        NotesColours notesColours = new NotesColours(getContext());
        notesColours.Load_Data();
        for(int i = 0; i < names.length; i++){
            NotesColours.Colour_Data cd = notesColours.Get_Colour(names[i]);
            if(! cd.isPure()){
                c_array[i] = Color.rgb(cd.getR(),cd.getG(),cd.getB());
            }
        }
        percent_sums = new float[names.length];
        percent_sums[0] = 0;
        for(int i = 1; i < names.length; i++){
            percent_sums[i] = percent_sums[i - 1] + (percents.get(names[i - 1]) * 3.6f);
        }

        changed = true;
    }

    public void Draw_Again(){
        surfaceCreated(holder);
    }

    public int Chart_Colours(int x, int y, int c_x, int c_y){
        if(percents == null){
            return Color.BLACK;
        }

        double f = Math.toDegrees(Math.atan2(y - c_y, x - c_x));
        if (f < 0) {
            f += 360;
        }
        //3.6 is 1% of 360 -> degrees in circle
        for(int i = 0; i < percents.size();i++){
            if(f >= percent_sums[i] && f < percent_sums[i] +(3.6 * percents.get(names[i]))){
                return Get_Percent_Color(i);
            }
        }
        return Get_Percent_Color(0);
    }

    public int Get_Percent_Color(int index){
        if(index >= c_array.length){
            return c_array[0];
        }

        return c_array[index];
    }

    //https://riptutorial.com/android/example/13004/surfaceview-with-drawing-thread example
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(empty){
            Canvas canvas = holder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            holder.unlockCanvasAndPost(canvas);
            empty = false;
        }
        if (changed) {
            Canvas canvas = holder.lockCanvas();

            int x_center = (width / 8) + 5;
            int y_center = (width / 8) + 5;
            int circle_diameter = width / 4;
            for (int y = 0; y < y_center + y_center; y++) {
                for (int x = x_center - x_center; x < x_center + x_center; x++) {
                    if ((x - x_center) * (x - x_center) + (y - y_center) * (y - y_center) <= circle_diameter / 2 * circle_diameter / 2) {
                        drawPaint.setColor(Chart_Colours(x,y,x_center, y_center));
                        canvas.drawPoint(x, y, drawPaint);
                    }
                }
            }
            changed = false;
            holder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
