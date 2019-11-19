package com.intraposition.buzcartsdk.Compass;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;


import com.kircherelectronics.fsensor.filter.averaging.LowPassFilter;
import com.kircherelectronics.fsensor.filter.averaging.MeanFilter;
import com.kircherelectronics.fsensor.filter.averaging.MedianFilter;
import com.kircherelectronics.fsensor.filter.gyroscope.OrientationGyroscope;
import com.kircherelectronics.fsensor.filter.gyroscope.fusion.complimentary.OrientationFusedComplimentary;
import com.kircherelectronics.fsensor.filter.gyroscope.fusion.kalman.OrientationFusedKalman;
import com.kircherelectronics.fsensor.util.rotation.RotationUtil;

import org.apache.commons.math3.complex.Quaternion;

import static android.content.Context.SENSOR_SERVICE;

public class Compass implements SensorEventListener {

    private static final String TAG = Compass.class.getSimpleName();

    private enum Mode {
        GYROSCOPE_ONLY,
        COMPLIMENTARY_FILTER,
        KALMAN_FILTER;
    }

    private enum FilterMode {

        MEDIAN,
        MEAN,
        LOWPASS
    }

    private SensorManager mSensorManager = null;

    private OrientationGyroscope orientationGyroscope;
    private OrientationFusedComplimentary orientationComplimentaryFusion;
    private OrientationFusedKalman orientationKalmanFusion;

    private float[] fusedOrientation = new float[3];

    private float[] rotation = new float[3];
    private float[] acceleration = new float[4];
    private float[] magnetic = new float[3];

    private boolean hasAcceleration = false;
    private boolean hasMagnetic = false;

    private MeanFilter meanFilter;

    private MeanFilter directionFilter;

    private MedianFilter medianFilter;

    private LowPassFilter lowPassFilter;

    private FilterMode mode = FilterMode.MEAN;

    private Mode computeMode = Mode.COMPLIMENTARY_FILTER;

    private Averager averager;

    private MapRotator mapRotator;

    private Float angleCorrection;

    private long lastUpdate;

    public Compass(Context context, Integer bufferLen, Integer samplingRate, Float angleCorrection) {

        this.angleCorrection = angleCorrection;

        averager = new Averager(bufferLen, samplingRate);

        orientationGyroscope = new OrientationGyroscope();

        orientationComplimentaryFusion = new OrientationFusedComplimentary();

        orientationKalmanFusion = new OrientationFusedKalman();

        meanFilter = new MeanFilter();

        directionFilter = new MeanFilter();

        medianFilter = new MedianFilter();

        lowPassFilter = new LowPassFilter();

        lastUpdate = System.currentTimeMillis();

        // get sensorManager and initialise sensor listeners
        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
    }


    // This function registers sensor listeners for the accelerometer, magnetometer and gyroscope.
    private void initListeners() {
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_FASTEST);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void start() {
        initListeners();
    }

    public void stop() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        switch (event.sensor.getType()) {
            case Sensor.TYPE_GYROSCOPE:
                // Android reuses events, so you probably want a copy
                System.arraycopy(event.values, 0, rotation, 0, event.values.length);
                calculateOrientation(event);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                // Android reuses events, so you probably want a copy
                System.arraycopy(event.values, 0, magnetic, 0, event.values.length);
                hasMagnetic = true;
                break;
            case Sensor.TYPE_ACCELEROMETER:
                System.arraycopy(event.values, 0, acceleration, 0, event.values.length);
                hasAcceleration = true;
                break;
        }


    }

    private void calculateOrientation(SensorEvent event) {

        switch (computeMode) {
            case GYROSCOPE_ONLY:
                if (!orientationGyroscope.isBaseOrientationSet()) {
                    orientationGyroscope.setBaseOrientation(Quaternion.IDENTITY);
                } else {
                    fusedOrientation = orientationGyroscope.calculateOrientation(rotation, event.timestamp);
                }

                break;
            case COMPLIMENTARY_FILTER:
                if (!orientationComplimentaryFusion.isBaseOrientationSet()) {
                    if (hasAcceleration && hasMagnetic) {
                        orientationComplimentaryFusion.setBaseOrientation(RotationUtil.getOrientationVectorFromAccelerationMagnetic(acceleration, magnetic));
                    }
                } else {
                    fusedOrientation = orientationComplimentaryFusion.calculateFusedOrientation(rotation, event.timestamp, acceleration, magnetic);
//                    Log.d("kbk", "Process Orientation Fusion Complimentary: " + Arrays.toString(fusedOrientation));
                }

                break;
            case KALMAN_FILTER:

                if (!orientationKalmanFusion.isBaseOrientationSet()) {
                    if (hasAcceleration && hasMagnetic) {
                        orientationKalmanFusion.setBaseOrientation(RotationUtil.getOrientationVectorFromAccelerationMagnetic(acceleration, magnetic));
                    }
                } else {
                    fusedOrientation = orientationKalmanFusion.calculateFusedOrientation(rotation, event.timestamp, acceleration, magnetic);
                }
                break;
        }

        switch (mode) {
            case LOWPASS:
                fusedOrientation = lowPassFilter.filter(fusedOrientation);
                break;
            case MEAN:
                fusedOrientation = meanFilter.filter(fusedOrientation);
                break;
            case MEDIAN:
                fusedOrientation = medianFilter.filter(fusedOrientation);
                break;
        }
        float heading = ((float) Math.toDegrees(fusedOrientation[2]) + 360) % 360;
        averager.update(heading);
        if (mapRotator != null) {
            if (System.currentTimeMillis() - lastUpdate > 100) {
                Log.d("MapRot", "azimuth (deg): " + heading);
                lastUpdate = System.currentTimeMillis();
                mapRotator.rotate(heading);
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

