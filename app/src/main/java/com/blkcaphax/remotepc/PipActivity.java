package com.blkcaphax.remotepc;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class PipActivity extends AppCompatActivity {
    private Point point;
    private Point pointLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pip);
        point = new Point(0,0);
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        pointLayout = new Point();
        pointLayout.x = layoutParams.x;
        pointLayout.y = layoutParams.y;
        this.update(point);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
                point.x =(int) event.getRawX();
                point.y = (int) event.getRawY();
                WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
                pointLayout.x = layoutParams.x;
                pointLayout.y = layoutParams.y;
                break;
            case MotionEvent.ACTION_MOVE:
                Point temp = new Point();
                temp.x = (int)event.getRawX() - point.x + pointLayout.x;
                temp.y = (int)event.getRawY() - point.y + pointLayout.y;
                this.update(temp);
                break;
            default:

        }
        return super.onTouchEvent(event);
    }

    private void update(Point point){
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        Log.d("point", "onTouchEvent: "+point.toString());
        Log.d("point", "update: "+layoutParams.x+" "+layoutParams.y);
        layoutParams.x = point.x;
        layoutParams.height = 700;
        layoutParams.width = 600;
        layoutParams.y = point.y;
        this.getWindow().setAttributes(layoutParams);
    }
}
