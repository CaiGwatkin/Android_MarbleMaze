package com.badidea.cgwatkin.marblemaze;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MarbleView extends View {
    /**
     * The number of marbles to be generated.
     */
    static int NUM_BUBBLES = 1;

    /**
     * The paint objects to colour etc. the marble.
     */
    static Paint mPaint;
    static {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.argb(255, 255, 0, 0));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);
        mPaint.setAntiAlias(true);
    }

    /**
     * The marble being displayed.
     */
    private Marble mMarble;

    /**
     * Gravity values.
     */
    private float mGX, mGY;

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
        mMarble = new Marble(0, 0, 0, 0, 0);
    }

    /**
     * Draws the marble.
     *
     * @param c The canvas.
     */
    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        mMarble.draw(c, mPaint);
    }

    public void makeMarble() {
        mMarble = new Marble(getWidth() / 2, getHeight() / 2, 0, 0, 100);
    }

    /**
     * Updates the location and velocity of the marble.
     *
     * @param dT Difference in time.
     */
    public void update(float dT) {
        mMarble.move(dT, mGX, mGY, getWidth(), getHeight());
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


//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            mT = true;
//            mTx = (int) event.getX();
//            mTy = (int) event.getY();
//
//            synchronized (mMarbleList) {
//                Iterator<Marble> i = mMarbleList.iterator();
//                while (i.hasNext()) {
//                    Marble b = i.next();
//                    if (mMarble.inside(mTx, mTy)) {
//                        mTouchedMarble=b;
//                        mTouchedMarble.stop();
//                        //  mMarble.reverse();//i.remove();
//                        break;
//                    }
//                }
//            }
//
//            return true;
//        }
//        if (event.getAction() == MotionEvent.ACTION_UP) {
//            mT = false;
//            mTouchedMarble=null;
//            return true;
//        }
//
//        if (event.getAction() == MotionEvent.ACTION_MOVE) {
//            mTx = (int) event.getX();
//            mTy = (int) event.getY();
//            if(mTouchedMarble!=null)
//                mTouchedMarble.move(mTx,mTy);
//            return true;
//        }
//
//
//        return false;
//    }
}
