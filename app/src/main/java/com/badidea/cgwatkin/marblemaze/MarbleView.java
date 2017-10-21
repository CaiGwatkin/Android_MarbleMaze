package com.badidea.cgwatkin.marblemaze;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class MarbleView extends View {

    private int wallWidth;

    private int canvasWidth, canvasHeight;

    /**
     * Context.
     */
    private Context mContext;

    /**
     * Observer, for callback to activity.
     */
    private Observer mObserver;

    /**
     * The paint objects to colour etc. the marble.
     */
    private Paint mPaintMarble, mPaintWall, mPaintGoal, mPaintHole;

    /**
     * Gravity values.
     */
    private double mGX = 0, mGY = 9.8;

    /**
     * The marble being displayed.
     */
    private Marble mMarble;

    /**
     * The objects in the world.
     */
    private ArrayList<WorldObject> mWorldObjects;

    /**
     * Marble View constructor.
     *
     * @param context The context this was generated from.
     * @param attrs Any attributes for the view.
     */
    public MarbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        this.post(new Runnable() {
            @Override
            public void run() {
                createWorld();
            }
        });
    }

    /**
     * Set the observer of this view.
     *
     * @param observer The observer.
     */
    public void setSuccessObserver(Observer observer){
        mObserver = observer;
    }

    /**
     * Defines paints to be used for objects.
     */
    private void setPaint() {
        mPaintMarble = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintMarble.setColor(ResourcesCompat.getColor(mContext.getResources(), R.color.marble, null));
        mPaintMarble.setStyle(Paint.Style.FILL);
        mPaintMarble.setAntiAlias(true);

        mPaintWall = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintWall.setColor(ResourcesCompat.getColor(mContext.getResources(), R.color.wall, null));
        mPaintWall.setStrokeWidth(wallWidth);
        mPaintWall.setStyle(Paint.Style.STROKE);
        mPaintWall.setAntiAlias(true);

        mPaintGoal = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintGoal.setColor(ResourcesCompat.getColor(mContext.getResources(), R.color.target, null));
        mPaintGoal.setStyle(Paint.Style.FILL);
        mPaintGoal.setAntiAlias(true);

        mPaintHole = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintHole.setColor(ResourcesCompat.getColor(mContext.getResources(), R.color.hole, null));
        mPaintHole.setStyle(Paint.Style.FILL);
        mPaintHole.setAntiAlias(true);
    }

    /**
     * Creates world with marble, objects, target and hole.
     */
    public void createWorld() {
        canvasWidth = getWidth();
        canvasHeight = getHeight();
        wallWidth = 8;
        int radius = canvasWidth / 30;
        int maxVelocity = radius * 2;
        int distanceBetweenWalls = maxVelocity * 2;
        int width = (canvasWidth / distanceBetweenWalls) * distanceBetweenWalls;
        int height = (canvasHeight / distanceBetweenWalls) * distanceBetweenWalls;
        int xPadding = (canvasWidth - width) / 2;
        int yPadding = (canvasHeight - height) / 2;

        mMarble = new Marble(canvasWidth - xPadding - distanceBetweenWalls / 2,
                canvasHeight - yPadding - distanceBetweenWalls / 2, mGX, mGY, radius, maxVelocity);
        mWorldObjects = mObserver.createWorldObjects(canvasWidth, canvasHeight, wallWidth, radius, distanceBetweenWalls,
                xPadding, yPadding);
        setPaint();
    }

    /**
     * Draws the marble.
     *
     * @param c The canvas.
     */
    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        if (mMarble != null) {
            mMarble.draw(c, mPaintMarble);
        }
        if (!mWorldObjects.isEmpty()) {
            for (WorldObject wo: mWorldObjects) {
                Paint p = mPaintWall;
                if (!wo.isWall()) {
                    if (wo.isGoal()) {
                        p = mPaintGoal;
                    }
                    else if (wo.isHole()) {
                        p = mPaintHole;
                    }
                }
                wo.draw(c, p);
            }
        }
    }

    /**
     * Sets the gravity values.
     *
     * @param gX Gravity in x plane.
     * @param gY Gravity in y plane.
     */
    public void setGravity(double gX, double gY) {
        mGX = Math.min(gX, 9.8);
        mGY = Math.min(gY, 9.8);
    }

    /**
     * Updates the location and velocity of the marble.
     *
     * @param dT Difference in time.
     */
    public void update(double dT) {
        if (mMarble != null) {
            HitType hit = mMarble.move(dT, mGX, mGY, canvasWidth, canvasHeight, mWorldObjects);
            switch (hit) {
                case GOAL:
                    success();
                    break;
                case HOLE:
                    failure();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Goal has been collided with.
     */
    private void success() {
        if (mObserver != null) {
            mObserver.success();
        }
        else {
            ((Activity) getContext()).finish();
        }
    }

    /**
     * Hole has been collided with.
     */
    private void failure() {
        if (mObserver != null) {
            mObserver.failure();
        }
        else {
            ((Activity) getContext()).finish();
        }
    }
}
