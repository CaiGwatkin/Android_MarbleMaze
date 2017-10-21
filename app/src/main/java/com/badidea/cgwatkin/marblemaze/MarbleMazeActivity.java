package com.badidea.cgwatkin.marblemaze;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Marble Maze Activity class
 */
public class MarbleMazeActivity extends Activity implements SensorEventListener {

    /**
     * ActionType type.
     */
    private enum ActionType {
        SUCCESS,
        FAILURE
    }

    private int worldNumber;

    /**
     * Thread handler allow screen refresh after set delay.
     */
    private Handler mHandler = new Handler();

    /**
     * The refresh runnable. Updates the view every refresh delay and forces view refresh.
     */
    private RefreshWorld mRefresh;

    /**
     * Sensor manager.
     */
    private SensorManager mSensorManager;

    /**
     * Accelerometer sensor.
     */
    private Sensor mAccelerometer;

    /**
     * Linear Layout for success_failure/failure message.
     */
    private LinearLayout mSuccessFailureDisplay;

    /**
     * The marble view being displayed.
     */
    private MarbleView mMarbleView;

    /**
     * Set content view to marble puzzle. Find marble view.
     *
     * @param savedInstanceState Saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                worldNumber = 1;
            }
            else {
                worldNumber = extras.getInt("WORLD_NUMBER");
            }
        }
        else {
            worldNumber = savedInstanceState.getInt("WORLD_NUMBER");
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_marble_maze);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSuccessFailureDisplay = (LinearLayout) findViewById(R.id.success_failure_display);
        mMarbleView = (MarbleView) findViewById(R.id.marble_view);
        mMarbleView.setSuccessObserver(new Observer() {
            @Override
            public void success() {
                doSuccessFailure(ActionType.SUCCESS);
            }

            @Override
            public void failure() {
                doSuccessFailure(ActionType.FAILURE);
            }

            @Override
            public ArrayList<WorldObject> createWorldObjects(int canvasWidth, int canvasHeight, int wallWidth,
                                                             int radius, int distanceBetweenWalls, int xPadding,
                                                             int yPadding) {
                return doCreateWorldObjects(canvasWidth, canvasHeight, wallWidth, radius, distanceBetweenWalls,
                        xPadding, yPadding);
            }
        });
        mRefresh = new RefreshWorld(mMarbleView, mHandler);
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mRefresh.unPause();
        mHandler.post(mRefresh.setStartTime(System.currentTimeMillis()));
    }

    /**
     * Stop updating marble view when paused.
     */
    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        mHandler.removeCallbacks(mRefresh);
        mRefresh.pause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("WORLD_NUMBER", worldNumber);
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

    /**
     * Deal with success or failure.
     *
     * @param action Success or failure.
     */
    private void doSuccessFailure(ActionType action) {
        mRefresh.pause();
        mHandler.removeCallbacks(mRefresh);
        View successFailureLayout = LayoutInflater.from(this).inflate(R.layout.success_failure, mSuccessFailureDisplay,
                false);
        int messageResource;
        if (action == ActionType.SUCCESS) {
            messageResource = R.string.success;
            mMarbleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            mSensorManager.unregisterListener(this);
        }
        else {
            messageResource = R.string.failure;
            mMarbleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    restartWorld();
                }
            });
        }
        ((TextView) successFailureLayout.findViewById(R.id.success_failure_message)).setText(messageResource);
        mSuccessFailureDisplay.addView(successFailureLayout);
    }

    /**
     * Restart world.
     */
    private void restartWorld() {
        mMarbleView.createWorld();
        mSuccessFailureDisplay.removeAllViews();
        mMarbleView.setOnClickListener(null);
        mRefresh.unPause();
        mHandler.post(mRefresh.setStartTime(System.currentTimeMillis()));
    }

    /**
     * Returns world objects based on world number.
     *
     * @param canvasWidth Width of canvas.
     * @param canvasHeight Height of canvas.
     * @param wallWidth Width of walls.
     * @param radius Radius of marble.
     * @param distanceBetweenWalls Minimum distance between walls.
     * @param xPadding Padding around board in x plane.
     * @param yPadding Padding around board in y plane.
     * @return An array list of world objects.
     */
    private ArrayList<WorldObject> doCreateWorldObjects(int canvasWidth, int canvasHeight, int wallWidth, int radius,
                                                        int distanceBetweenWalls, int xPadding, int yPadding) {
        switch (worldNumber) {
            case 1:
                return World1(canvasWidth, canvasHeight, wallWidth, radius, distanceBetweenWalls, xPadding, yPadding);
            default:
                return World1(canvasWidth, canvasHeight, wallWidth, radius, distanceBetweenWalls, xPadding, yPadding);
        }
    }

    /**
     *
     *
     * @param canvasWidth Width of canvas.
     * @param canvasHeight Height of canvas.
     * @param wallWidth Width of walls.
     * @param radius Radius of marble.
     * @param distanceBetweenWalls Minimum distance between walls.
     * @param xPadding Padding around board in x plane.
     * @param yPadding Padding around board in y plane.
     * @return An array list of world objects.
     */
    private ArrayList<WorldObject> World1(int canvasWidth, int canvasHeight, int wallWidth, int radius,
                                          int distanceBetweenWalls, int xPadding, int yPadding) {
        ArrayList<WorldObject> worldObjects = new ArrayList<>();
        for (int i = xPadding; i < canvasWidth; i += distanceBetweenWalls) {
            worldObjects.add(new WallObject(i, yPadding, i, canvasHeight - yPadding, wallWidth));
        }
        for (int i = yPadding; i < canvasHeight; i += distanceBetweenWalls) {
            worldObjects.add(new WallObject(xPadding, i, canvasWidth - xPadding, i, wallWidth));
        }
        worldObjects.add(new GoalObject(xPadding + distanceBetweenWalls / 2,
                yPadding + distanceBetweenWalls / 2, radius));
        worldObjects.add(new HoleObject(canvasWidth - xPadding - distanceBetweenWalls / 2,
                yPadding + distanceBetweenWalls / 2, radius));
        return worldObjects;
    }
}
