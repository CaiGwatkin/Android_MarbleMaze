package com.badidea.cgwatkin.marblemaze;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import java.util.ArrayList;

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
    void move(float dT, float gX, float gY, float w, float h, ArrayList<WorldObject> worldObjects) {
        mVX = updateVelocity(mVX, dT, gX);
        mVY = updateVelocity(mVY, dT, gY);
        float x = linearMovement(mX, mVX, dT);
        float y = linearMovement(mY, mVY, dT);
        boolean bc = false;
        if (boundaryCollision(x, w)) {
            mVX = -mVX * k;
            x = linearMovement(mX, mVX, dT);
            bc = true;
        }
        if (boundaryCollision(y, h)) {
            mVY = -mVY * k;
            y = linearMovement(mY, mVY, dT);
            bc = true;
        }
        if (bc) {
            mX = x;
            mY = y;
            return;
        }
        if (!worldObjects.isEmpty()) {
            for (WorldObject wo: worldObjects) {
                if (wo.collision(x, y, mR)) {
                    switch (wo.side(x, y, mR)) {
                        case LEFT:
                            reverseVX();
                            break;
                        case TOP_LEFT:
                            bounce();
                            break;
                        case TOP:
                            reverseVY();
                            break;
                        case TOP_RIGHT:
                            bounce();
                            break;
                        case RIGHT:
                            reverseVX();
                            break;
                        case BOTTOM_RIGHT:
                            bounce();
                            break;
                        case BOTTOM:
                            reverseVY();
                            break;
                        case BOTTOM_LEFT:
                            bounce();
                            break;
                        default:
                            bounce();
                            break;
                    }
                    x = linearMovement(mX, mVX, dT);
                    y = linearMovement(mY, mVY, dT);
                    break;
                }
            }
        }
        mX = x;
        mY = y;
    }

    private void reverseVX() {
        mVX = -mVX * k;
    }

    private void reverseVY() {
        mVY = -mVY * k;
    }

    private void bounce() {
        mVX = -mVX * k;
        mVY = -mVY * k;
    }

    /**
     * Calculate next linear position.
     *
     * @param coordinate The coordinate.
     * @param v The velocity.
     * @param dT The time difference.
     * @return The new coordinate.
     */
    private float linearMovement(float coordinate, float v, float dT) {
        return coordinate + (v * dT * 100);
    }

    /**
     * Calculate the new velocity based on gravity.
     *
     * @param v The current velocity.
     * @param dT The time difference.
     * @param g The gravity modifier.
     * @return The new velocity.
     */
    private float updateVelocity(float v, float dT, float g) {
        return v + (dT * g * 10);
    }

    /**
     * Calculate if the marble is touching the boundary.
     *
     * @param coordinate The coordinate.
     * @param boundary The boundary value.
     * @return True if boundary collision occurred.
     */
    private boolean boundaryCollision(float coordinate, float boundary) {
        return coordinate < mR || coordinate > boundary - mR;
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
