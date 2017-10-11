package com.badidea.cgwatkin.marblemaze;

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
    static Paint mPaintMarble, mPaintObject;
    static {
        mPaintMarble = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintMarble.setColor(Color.argb(255, 255, 0, 0));
        mPaintMarble.setStyle(Paint.Style.STROKE);
        mPaintMarble.setStrokeWidth(8);
        mPaintMarble.setAntiAlias(true);

        mPaintObject = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintObject.setColor(Color.argb(255, 0, 255, 0));
        mPaintObject.setStyle(Paint.Style.STROKE);
        mPaintObject.setStrokeWidth(8);
        mPaintObject.setAntiAlias(true);
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
                wo.draw(c, mPaintObject);
            }
        }
    }

    /**
     * Updates the location and velocity of the marble.
     *
     * @param dT Difference in time.
     */
    public void update(float dT) {
        if (mMarble != null) {
            mMarble.move(dT, mGX, mGY, getWidth(), getHeight(), mWorldObjects);
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
}
