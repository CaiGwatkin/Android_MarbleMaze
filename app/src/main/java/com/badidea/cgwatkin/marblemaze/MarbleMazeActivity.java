package com.badidea.cgwatkin.marblemaze;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
                return MarbleMazeActivity.this.createWorldObjects(canvasWidth, canvasHeight, wallWidth, radius, distanceBetweenWalls,
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
        restartWorld();
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

    /**
     * Save state between runs.
     *
     * @param savedInstanceState The saved state.
     */
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
    private ArrayList<WorldObject> createWorldObjects(int canvasWidth, int canvasHeight, int wallWidth, int radius,
                                                      int distanceBetweenWalls, int xPadding, int yPadding) {
        switch (worldNumber) {
            case 1:
                return createWorld1(canvasWidth, canvasHeight, wallWidth, radius, distanceBetweenWalls,
                        xPadding, yPadding);
            case 2:
                return createWorld2(canvasWidth, canvasHeight, wallWidth, radius, distanceBetweenWalls,
                        xPadding, yPadding);
            case 3:
                return createWorld3(canvasWidth, canvasHeight, wallWidth, radius, distanceBetweenWalls,
                        xPadding, yPadding);
            case 4:
                return createWorld4(canvasWidth, canvasHeight, wallWidth, radius, distanceBetweenWalls,
                        xPadding, yPadding);
            case 5:
                return createWorld5(canvasWidth, canvasHeight, wallWidth, radius, distanceBetweenWalls,
                        xPadding, yPadding);
            default:
                return createWorld1(canvasWidth, canvasHeight, wallWidth, radius, distanceBetweenWalls,
                        xPadding, yPadding);
        }
    }

    /**
     * Creates world objects for world 1.
     *
     * Introduce user to goal.
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
    private ArrayList<WorldObject> createWorld1(int canvasWidth, int canvasHeight, int wallWidth, int radius,
                                                int distanceBetweenWalls, int xPadding, int yPadding) {
        return new ArrayList<>();
    }

    /**
     * Creates world objects for world 2.
     *
     * Introduce user to walls.
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
    private ArrayList<WorldObject> createWorld2(int canvasWidth, int canvasHeight, int wallWidth, int radius,
                                                int distanceBetweenWalls, int xPadding, int yPadding) {
        ArrayList<WorldObject> worldObjects = new ArrayList<>();


        // Walls for square in centre (l, t, r, b)
        worldObjects.add(new WallObject(
                xPadding + distanceBetweenWalls, canvasHeight / 2 - yPadding / 2,
                xPadding + distanceBetweenWalls, canvasHeight / 2 + yPadding / 2, wallWidth
        ));
        worldObjects.add(new WallObject(
                xPadding + distanceBetweenWalls, canvasHeight / 2 - yPadding / 2,
                canvasWidth - xPadding - distanceBetweenWalls, canvasHeight / 2 - yPadding / 2, wallWidth
        ));
        worldObjects.add(new WallObject(
                canvasWidth - xPadding - distanceBetweenWalls, canvasHeight / 2 - yPadding / 2,
                canvasWidth - xPadding - distanceBetweenWalls, canvasHeight / 2 + yPadding / 2, wallWidth
        ));
        worldObjects.add(new WallObject(
                xPadding + distanceBetweenWalls, canvasHeight / 2 + yPadding / 2,
                canvasWidth - xPadding - distanceBetweenWalls, canvasHeight / 2 + yPadding / 2, wallWidth
        ));


        return worldObjects;
    }

    /**
     * Creates world objects for world 3.
     *
     * Introduce user to holes.
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
    private ArrayList<WorldObject> createWorld3(int canvasWidth, int canvasHeight, int wallWidth, int radius,
                                                int distanceBetweenWalls, int xPadding, int yPadding) {
        ArrayList<WorldObject> worldObjects = new ArrayList<>();


        // Add 4 holes, diagonally across middle of area
        worldObjects.add(new HoleObject(
                xPadding + distanceBetweenWalls / 2 + distanceBetweenWalls,
                yPadding + distanceBetweenWalls / 2 + distanceBetweenWalls * 7, radius
        ));
        worldObjects.add(new HoleObject(
                xPadding + distanceBetweenWalls / 2 + distanceBetweenWalls * 2,
                yPadding + distanceBetweenWalls / 2 + distanceBetweenWalls * 6, radius
        ));
        worldObjects.add(new HoleObject(
                xPadding + distanceBetweenWalls / 2 + distanceBetweenWalls * 3,
                yPadding + distanceBetweenWalls / 2 + distanceBetweenWalls * 5, radius
        ));
        worldObjects.add(new HoleObject(
                xPadding + distanceBetweenWalls / 2 + distanceBetweenWalls * 4,
                yPadding + distanceBetweenWalls / 2 + distanceBetweenWalls * 4, radius
        ));



        return worldObjects;
    }

    /**
     * Creates world objects for world 4.
     *
     * Combination of world 2 and 3.
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
    private ArrayList<WorldObject> createWorld4(int canvasWidth, int canvasHeight, int wallWidth, int radius,
                                                int distanceBetweenWalls, int xPadding, int yPadding) {
        ArrayList<WorldObject> worldObjects = new ArrayList<>();


        // Add 4 holes, diagonally across middle of area
        worldObjects.add(new HoleObject(
                xPadding + distanceBetweenWalls / 2 + distanceBetweenWalls,
                yPadding + distanceBetweenWalls / 2 + distanceBetweenWalls * 4, radius
        ));
        worldObjects.add(new HoleObject(
                xPadding + distanceBetweenWalls / 2 + distanceBetweenWalls * 2,
                yPadding + distanceBetweenWalls / 2 + distanceBetweenWalls * 3, radius
        ));
        worldObjects.add(new HoleObject(
                xPadding + distanceBetweenWalls / 2 + distanceBetweenWalls * 3,
                yPadding + distanceBetweenWalls / 2 + distanceBetweenWalls * 2, radius
        ));
        worldObjects.add(new HoleObject(
                xPadding + distanceBetweenWalls / 2 + distanceBetweenWalls * 4,
                yPadding + distanceBetweenWalls / 2 + distanceBetweenWalls, radius
        ));


        // Walls for square in centre (l, t, r, b)
        worldObjects.add(new WallObject(
                xPadding + distanceBetweenWalls, yPadding + distanceBetweenWalls * 10,
                xPadding + distanceBetweenWalls, yPadding + distanceBetweenWalls * 11, wallWidth
        ));
        worldObjects.add(new WallObject(
                xPadding + distanceBetweenWalls, yPadding + distanceBetweenWalls * 10,
                canvasWidth - xPadding - distanceBetweenWalls, yPadding + distanceBetweenWalls * 10, wallWidth
        ));
        worldObjects.add(new WallObject(
                canvasWidth - xPadding - distanceBetweenWalls, yPadding + distanceBetweenWalls * 10,
                canvasWidth - xPadding - distanceBetweenWalls, yPadding + distanceBetweenWalls * 11, wallWidth
        ));
        worldObjects.add(new WallObject(
                xPadding + distanceBetweenWalls, yPadding + distanceBetweenWalls * 11,
                canvasWidth - xPadding - distanceBetweenWalls, yPadding + distanceBetweenWalls * 11, wallWidth
        ));



        return worldObjects;
    }

    /**
     * Creates world objects for world 5.
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
    private ArrayList<WorldObject> createWorld5(int canvasWidth, int canvasHeight, int wallWidth, int radius,
                                                int distanceBetweenWalls, int xPadding, int yPadding) {
        ArrayList<WorldObject> worldObjects = new ArrayList<>();


        // Walls for square down from top just left of centre (l, r, b)
        worldObjects.add(new WallObject(
                xPadding + distanceBetweenWalls * 2, yPadding,
                xPadding + distanceBetweenWalls * 2, yPadding + distanceBetweenWalls * 5,
                wallWidth
        ));
        worldObjects.add(new WallObject(
                xPadding + distanceBetweenWalls * 3, yPadding,
                xPadding + distanceBetweenWalls * 3, yPadding + distanceBetweenWalls * 5,
                wallWidth
        ));
        worldObjects.add(new WallObject(
                xPadding + distanceBetweenWalls * 2, yPadding + distanceBetweenWalls * 5,
                xPadding + distanceBetweenWalls * 3, yPadding + distanceBetweenWalls * 5,
                wallWidth
        ));


        // Walls for square up from bottom just right of centre (l, t, r)
        worldObjects.add(new WallObject(
                xPadding + distanceBetweenWalls * 3, yPadding + distanceBetweenWalls * 7,
                xPadding + distanceBetweenWalls * 3, canvasHeight - yPadding,
                wallWidth
        ));
        worldObjects.add(new WallObject(
                xPadding + distanceBetweenWalls * 3, yPadding + distanceBetweenWalls * 7,
                xPadding + distanceBetweenWalls * 4, yPadding + distanceBetweenWalls * 7,
                wallWidth
        ));
        worldObjects.add(new WallObject(
                xPadding + distanceBetweenWalls * 4, yPadding + distanceBetweenWalls * 7,
                xPadding + distanceBetweenWalls * 4, canvasHeight - yPadding,
                wallWidth
        ));


        // Add 4 holes, around gap in center wall
        worldObjects.add(new HoleObject(
                xPadding + distanceBetweenWalls / 2 + distanceBetweenWalls * 2,
                yPadding + distanceBetweenWalls / 2 + distanceBetweenWalls * 5,
                radius
        ));
        worldObjects.add(new HoleObject(
                xPadding + distanceBetweenWalls / 2 + distanceBetweenWalls * 2,
                yPadding + distanceBetweenWalls / 2 + distanceBetweenWalls * 7,
                radius
        ));
        worldObjects.add(new HoleObject(
                xPadding + distanceBetweenWalls / 2 + distanceBetweenWalls * 4,
                yPadding + distanceBetweenWalls / 2 + distanceBetweenWalls * 6,
                radius
        ));
        worldObjects.add(new HoleObject(
                xPadding + distanceBetweenWalls / 2 + distanceBetweenWalls * 4,
                yPadding + distanceBetweenWalls / 2 + distanceBetweenWalls * 7,
                radius
        ));


        return worldObjects;
    }
}
