package com.badidea.cgwatkin.marblemaze;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class MarbleMazeActivity extends Activity implements SensorEventListener {
    /**
     * The refresh rate/delay values.
     */
    static long REFRESH_RATE = 60;
    static long REFRESH_DELAY = 1000 / REFRESH_RATE;

    /**
     * Thread handler allow screen refresh after set delay.
     */
    private final Handler mHandler = new Handler();

    /**
     * The start time of the refresh runnable.
     */
    private long startTime;

    /**
     * The refresh runnable. Updates the view every refresh delay and forces view refresh.
     */
    private Runnable mRefresh = new Runnable() {
        @Override
        public void run() {
            long dT = System.currentTimeMillis() - startTime;
            startTime = System.currentTimeMillis();
            mMarbleView.update(dT / 1000f);
            mMarbleView.postInvalidate();
            long timeTaken = System.currentTimeMillis() - startTime;
            mHandler.postDelayed(this, REFRESH_DELAY - timeTaken);
        }
    };

    /**
     * Sensor manager.
     */
    private SensorManager mSensorManager;

    /**
     * Accelerometer sensor.
     */
    private Sensor mAccelerometer;

    /**
     * The marble view being displayed.
     */
    private MarbleView mMarbleView;

    /**
     * Set content view to marble puzzle. Find marble view.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_marble_puzzle);
        mMarbleView = (MarbleView) findViewById(R.id.marble_view);
        mMarbleView.post(new Runnable() {
            @Override
            public void run() {
                mMarbleView.makeMarble();
            }
        });
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    /**
     * Set activity to fullscreen.
     * Restart updating marble view when app resumed.
     */
    @Override
    public void onResume() {
        super.onResume();
        mMarbleView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        startTime = System.currentTimeMillis();
        mHandler.post(mRefresh);
    }

    /**
     * Stop updating marble view when paused.
     */
    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        mHandler.removeCallbacks(mRefresh);
    }

    /**
     * Sets the gravity values of the marble view when sensor values change.
     *
     * @param event The sensor event.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        mMarbleView.setGravity(-event.values[0], event.values[1]);
    }

    /**
     * Do nothing.
     *
     * @param sensor The sensor.
     * @param accuracy The new accuracy value.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}
