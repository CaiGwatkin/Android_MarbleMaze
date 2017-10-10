package com.badidea.cgwatkin.marblemaze;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Marble {
    /**
     * The marble's position, radius and velocity values.
     */
    private float mX, mY, mR, mVX, mVY;

    /**
     * Velocity modifier on collision.
     */
    private float k = 0.3f;

    /**
     * Marble constructor
     *
     * @param x Position in x plane.
     * @param y Position in y plane.
     * @param vX Velocity in x plane.
     * @param vY Velocity in y plane.
     * @param r Radius.
     */
    Marble(int x, int y, float vX, float vY, float r) {
        mX = x;
        mY = y;
        mR = r;
        mVX = vX;
        mVY = vY;
    }

    /**
     * Draws marble on view.
     *
     * @param c The canvas.
     * @param p The paint.
     */
    void draw(Canvas c, Paint p) {
        c.save();
        c.translate(mX, mY);
        c.drawCircle(0, 0, mR, p);
        float sr = 2f * mR / 3f;
        c.drawArc(new RectF(-sr, -sr, sr, sr), 300, 30, false, p);
        c.restore();
    }

    /**
     * Move the marble based on change in time and current gravity.
     *
     * @param dT Difference in time.
     * @param gX Gravity in x plane.
     * @param gY Gravity in y plane.
     * @param w Width of canvas.
     * @param h Height of canvas.
     */
    void move(float dT, float gX, float gY, float w, float h) {
        mX += mVX * dT * 100;
        if (mX < mR || mX > w - mR) {
            mX -= mVX * dT * 100;
            mVX = -mVX * k;
        }
        mY += mVY * dT * 100;
        if (mY < mR || mY > h - mR) {
            mY -= mVY * dT * 100;
            mVY = -mVY * k;
        }
        mVX += dT * gX * 10;
        mVY += dT * gY * 10;
    }

//    void move(float x, float y) {
//        mX = x;
//        mY = y;
//    }

//    private float getX() {
//        return mX;
//    }
//
//    private float getR() {
//        return mR;
//    }
//
//    private float getY() {
//        return mY;
//    }

//    /**
//     * Update marble velocity on boundary intersection.
//     *
//     * @param boundaryW Width of boundary.
//     * @param boundaryH Height of boundary.
//     */
//    void manageBoundaryIntersection(float boundaryW, float boundaryH) {
//        if (mX < mR || mX > boundaryW - mR) {
//            mVX = -mVX;
//        }
//        if (mY < mR || mY > boundaryH - mR) {
//            mVY = -mVY;
//        }
//    }

//    void manageMarbleIntersection(Marble b, Marble touchedMarble) {
//        float bX = b.getX();
//        float bY = b.getY();
//        float bR = b.getR();
//        float d = (mX - bX) * (mX - bX) + (mY - bY) * (mY - bY);
//        if (d < (mR + bR) * (mR + bR)) {
//            d = (float) Math.sqrt(d);
//            float dx = (bX - mX) / d;
//            float dy = (bY - mY) / d;
//            float displacement = (mR + bR) - d;
//            if (this != touchedMarble) {
//                this.bounce(dx * displacement, dy * displacement);
//            }
//            if (b != touchedMarble) {
//                b.bounce(-dx * displacement, -dy * displacement);
//            }
//        }
//    }

//    private void bounce(float dx,float dy) {
//        mVX -= k * dx;
//        mVY -= k * dy;
//        mVX *=0.9f;
//        mVY *=0.9f;
//    }
//
//    void stop() {
//        mVX =0;
//        mVY =0;
//    }


//    boolean inside(float x, float y) {
//        return (((x - mX) * (x - mX) + (y - mY) * (y - mY)) < mR * mR);
//    }
}
