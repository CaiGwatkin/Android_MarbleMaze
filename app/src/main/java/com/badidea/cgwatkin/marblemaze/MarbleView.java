package com.badidea.cgwatkin.marblemaze;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
     * The paint objects to colour etc. the marble.
     */
    static Paint mPaintMarble, mPaintObject, mPaintTarget, mPaintHole;
    static {
        mPaintMarble = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintMarble.setColor(Color.argb(255, 0, 255, 153));
        mPaintMarble.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintMarble.setStrokeWidth(8);
        mPaintMarble.setAntiAlias(true);

        mPaintObject = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintObject.setColor(Color.argb(255, 153, 0, 255));
        mPaintObject.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintObject.setStrokeWidth(8);
        mPaintObject.setAntiAlias(true);

        mPaintTarget = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTarget.setColor(Color.argb(255, 0, 102, 255));
        mPaintTarget.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintTarget.setStrokeWidth(8);
        mPaintTarget.setAntiAlias(true);

        mPaintHole = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintHole.setColor(Color.argb(255, 0, 0, 0));
        mPaintHole.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintHole.setStrokeWidth(8);
        mPaintHole.setAntiAlias(true);
    }

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
        mGX = 0;
        mGY = 9.8f;
        mWorldObjects = new ArrayList<>();
        this.post(new Runnable() {
            @Override
            public void run() {
                mMarble = new Marble(getWidth() - RADIUS, getHeight() - RADIUS, 0, 0, RADIUS);
                mWorldObjects.add(new RectObject(getWidth() / 2 - DEFAULT_OBJECT_SIZE / 2,
                        getHeight() / 2 - DEFAULT_OBJECT_SIZE / 2,
                        DEFAULT_OBJECT_SIZE, DEFAULT_OBJECT_SIZE));
                mWorldObjects.add(new TargetObject(RADIUS, RADIUS, RADIUS));
                mWorldObjects.add(new HoleObject(getWidth() - RADIUS, RADIUS, RADIUS));
            }
        });
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
                    if (wo.isTarget()) {
                        p = mPaintTarget;
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

    private void success() {
        ((Activity) getContext()).finish();
    }

    private void failure() {
        ((Activity) getContext()).finish();
    }
}
