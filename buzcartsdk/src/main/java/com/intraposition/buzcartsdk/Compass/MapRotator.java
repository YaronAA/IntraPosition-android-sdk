package com.intraposition.buzcartsdk.Compass;

import android.util.Log;

import com.intraposition.buzcartsdk.Compass.CompassEventListener;

public class MapRotator {

    private static final int THRESHOLD = 10;

    private Float correction;

    private int count = 0;

    private class Quadrant{

        public int low;

        public int high;

        public int direction;

        public Quadrant(int low, int high, int angle){
            this.low = low;
            this.high = high;
            this.direction = angle;
        }

        public boolean contains(float angle){
            if (this.direction == 0){
                return ((angle >=0 && angle < high) || (angle < 360 && angle >= low));
            }
            return (angle >= low && angle < high);
        }
    }

    private Quadrant [] quadrants = new Quadrant[4];

    private int lastDirection = 0;

    private float lastAngle = 0;

    private CompassEventListener listener;

    public float getCorrection() {
        return correction;
    }

    public void setCorrection(Float correction) {
        this.correction = correction;
    }

    public MapRotator(CompassEventListener listener){
        this.listener = listener;
        quadrants[0] = new Quadrant(315,45, 0);
        quadrants[1] = new Quadrant(45,135, 90);
        quadrants[2] = new Quadrant(135,225,180);
        quadrants[3] = new Quadrant(225,315, 270);
    }

    public void rotate(float angle){
        if (correction == null){
            return;
        }
        angle = (angle + correction.floatValue()) % 360;
//        Log.d("MapRot","angle: " +angle +" last: "+lastAngle+" direction: "+lastDirection+" count: "+count);
        for (Quadrant q :quadrants) {
            if (q.contains(angle) && q.direction != lastDirection  /*&& ( Math.abs(lastAngle - angle) > THRESHOLD)*/ ){
                count +=1;
                int delta = q.direction;// - lastDirection;
                if ( count > 3) {
                    if (listener!=null){
                        listener.onAzimuthChange(q.direction);
                    }
                    lastDirection = q.direction;
                    lastAngle = angle;
                    count = 0;
                    Log.d("MapRot", "new direction: "+ q.direction +" delta: "+delta);
                }
            }
        }
    }
}

