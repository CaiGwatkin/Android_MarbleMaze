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
    /**
     * The radius of the marble to be displayed.
     */
    static int RADIUS = 40;

    static int DEFAULT_OBJECT_SIZE = 200;

    /**
     * Context.
     */
    private Context mContext;

    /**
     * SuccessObserver, for callback to activity.
     */
    private SuccessObserver mSuccessObserver;

    /**
     * The paint objects to colour etc. the marble.
     */
    private Paint mPaintMarble, mPaintObject, mPaintGoal, mPaintHole;

    /**
     * Gravity values.
     */
    private float mGX, mGY;

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
        mGX = 0;
        mGY = 9.8f;
        mWorldObjects = new ArrayList<>();
        setPaint();
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
    public void setSuccessObserver(SuccessObserver observer){
        mSuccessObserver = observer;
    }

    /**
     * Defines paints to be used for objects.
     */
    private void setPaint() {
        mPaintMarble = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintMarble.setColor(ResourcesCompat.getColor(mContext.getResources(), R.color.marble, null));
        mPaintMarble.setStyle(Paint.Style.FILL);
        mPaintMarble.setAntiAlias(true);

        mPaintObject = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintObject.setColor(ResourcesCompat.getColor(mContext.getResources(), R.color.wall, null));
        mPaintObject.setStyle(Paint.Style.FILL);
        mPaintObject.setAntiAlias(true);

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
        mMarble = new Marble(getWidth() - RADIUS, getHeight() - RADIUS, 0, 0, RADIUS);
        mWorldObjects.add(new RectObject(getWidth() / 2 - DEFAULT_OBJECT_SIZE / 2,
                getHeight() / 2 - DEFAULT_OBJECT_SIZE / 2,
                DEFAULT_OBJECT_SIZE, DEFAULT_OBJECT_SIZE));
        mWorldObjects.add(new GoalObject(RADIUS, RADIUS, RADIUS));
        mWorldObjects.add(new HoleObject(getWidth() - RADIUS, RADIUS, RADIUS));
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
                Paint p = mPaintObject;
                if (!wo.isObject()) {
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
    public void setGravity(float gX, float gY) {
        mGX = gX;
        mGY = gY;
    }

    /**
     * Updates the location and velocity of the marble.
     *
     * @param dT Difference in time.
     */
    public void update(float dT) {
        if (mMarble != null) {
            HitType hit = mMarble.move(dT, mGX, mGY, getWidth(), getHeight(), mWorldObjects);
            switch (hit) {
                case NONE:
                    break;
                case TARGET:
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
        if (mSuccessObserver != null) {
            mSuccessObserver.success();
        }
        else {
            ((Activity) getContext()).finish();
        }
    }

    /**
     * Hole has been collided with.
     */
    private void failure() {
        if (mSuccessObserver != null) {
            mSuccessObserver.failure();
        }
        else {
            ((Activity) getContext()).finish();
        }
    }
}
