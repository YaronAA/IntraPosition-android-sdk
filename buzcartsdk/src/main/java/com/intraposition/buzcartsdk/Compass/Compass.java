package com.intraposition.buzcartsdk.Compass;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import static android.content.Context.SENSOR_SERVICE;

public class Compass implements SensorEventListener {

    private static final String TAG = Compass.class.getSimpleName();

    private SensorManager mSensorManager = null;

    private Sensor mRotationVectorSensor;
    private final float[] mRotationMatrix = new float[16];

    private Averager averager;

    private MapRotator mapRotator;

    private Float angleCorrection;

    private long lastUpdate;

    public Compass(Context context, Integer bufferLen, Integer samplingRate, Float angleCorrection) {

        this.angleCorrection = angleCorrection;
        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);

        mRotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        averager = new Averager(bufferLen, samplingRate);

        lastUpdate = System.currentTimeMillis();

        // get sensorManager and initialise sensor listeners
//        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);

        mRotationMatrix[ 0] = 1;
        mRotationMatrix[ 4] = 1;
        mRotationMatrix[ 8] = 1;
        mRotationMatrix[12] = 1;

    }


    // This function registers sensor listeners for the accelerometer, magnetometer and gyroscope.
    public void initListeners() {
        mSensorManager.registerListener(this, mRotationVectorSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void start() {
        initListeners();
    }

    public void stop() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // we received a sensor event. it is a good practice to check
        // that we received the proper event
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            // convert the rotation-vector to a 4x4 matrix. the matrix
            // is interpreted by Open GL as the inverse of the
            // rotation-vector, which is what we want.
            float[] orientation = new float[3];
            SensorManager.getRotationMatrixFromVector(mRotationMatrix , event.values);
            SensorManager.getOrientation(mRotationMatrix, orientation);
            float azimuth = (float)((float)Math.toDegrees(orientation[0]) + 360.0) % 360;
            float pitch = (float)Math.toDegrees(orientation[1]);
            float roll = (float)Math.toDegrees(orientation[2]);
            averager.update(azimuth);
            if (mapRotator != null) {
                if (System.currentTimeMillis() - lastUpdate > 100) {
                    Log.d("MapRot", "azimuth (deg): " + azimuth);
                    lastUpdate = System.currentTimeMillis();
                    mapRotator.rotate(azimuth);
                }
            }

        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public double[] getOrientationBuffer() {
        return averager.getOrientationBuffer();
    }


    public void setOnCompassEventListener(CompassEventListener listener) {
        mapRotator = new MapRotator(listener);
        mapRotator.setCorrection(this.angleCorrection);
    }
}

