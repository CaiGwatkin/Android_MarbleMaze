package com.badidea.cgwatkin.marblemaze;

import android.graphics.Canvas;
import android.graphics.Paint;

class WallObject implements WorldObject {

    /**
     * Line end coordinates.
     */
    private int mX1, mY1, mX2, mY2, mW;

    /**
     * Constructor
     *
     * @param x1 Point 1 x coordinate.
     * @param y1 Point 1 y coordinate.
     * @param x2 Point 2 x coordinate.
     * @param y2 Point 2 y coordinate.
     * @param w Wall width in pixels.
     */
    WallObject(int x1, int y1, int x2, int y2, int w) {
        mX1 = x1;
        mY1 = y1;
        mX2 = x2;
        mY2 = y2;
        mW = w;
    }

    /**
     * Draws object on view.
     *
     * @param c The canvas.
     * @param p The paint.
     */
    public void draw(Canvas c, Paint p) {
        int halfW = mW / 2;
        if (isHorizontal()) {
            c.drawLine(mX1 - halfW, mY1, mX2 + halfW, mY2, p);
        }
        else {
            c.drawLine(mX1, mY1 - halfW, mX2, mY2 + halfW, p);
        }
    }

    /**
     * Tests if the marble collides with this object.
     *
     * @param cX Marble's centre's x coordinate.
     * @param cY Marble's centre's y coordinate.
     * @param cR Marble's radius.
     * @param cVX Marble's velocity in x plane.
     * @param cVY Marble's velocity in y plane.
     * @return True if collision occurred.
     */
    public boolean collision(double cX, double cY, double cR, double cVX, double cVY) {
//        return lineCircle(mX1, mY1, mX2, mY2, x, y, r);
        if (endPointCollision(mX1, mY1, cX, cY, cR) || endPointCollision(mX2, mY2, cX, cY, cR)) {
            return true;
        }
        cR = cR - 0.1;    // buffer
        if (isHorizontal()) {
            // Horizontal
            if (cVY > 0 && cY < mY1) {
                // Increasing y
                return cY > mY1 - mW / 2 - cR && cX >= mX1 && cX <= mX2;
            }
            else if (cY > mY1) {
                // Increasing x
                return cY < mY1 + mW / 2 + cR && cX >= mX1 && cX <= mX2;
            }
            return false;
        }
        else {
            // Vertical
            if (cVX > 0 && cX < mX1) {
                // Increasing y
                return cX > mX1 - mW / 2 - cR && cY >= mY1 && cY <= mY2;
            }
            else if (cX > mX1) {
                // Increasing x
                return cX < mX1 + mW / 2 + cR && cY >= mY1 && cY <= mY2;
            }
            return false;
        }
    }

    /**
     * Returns true if object is goal.
     *
     * @return false
     */
    public boolean isGoal() {
        return false;
    }

    /**
     * Returns true if object is hole.
     *
     * @return false
     */
    public boolean isHole() {
        return false;
    }

    /**
     * Returns true if object is object.
     *
     * @return true
     */
    public boolean isWall() {
        return true;
    }

    /**
     * Returns true if wall is horizontal.
     *
     * @return true if wall is horizontal.
     */
    boolean isHorizontal() {
        return mY1 == mY2;
    }

    /**
     * Returns true if collision on end-point.
     *
     * @param pX End point's x coordinate.
     * @param pY End point's y coordinate.
     * @param cX Circle's x coordinate.
     * @param cY Circle's y coordinate.
     * @param cR Circle's radius.
     * @return true if collision
     */
    private boolean endPointCollision(double pX, double pY, double cX, double cY, double cR) {
        double d = distance(pX, pY, cX, cY);
        return d <= cR;
    }

    /**
     * Returns the distance between two points.
     *
     * @param x1 Point 1 x coordinate.
     * @param y1 Point 1 y coordinate.
     * @param x2 Point 2 x coordinate.
     * @param y2 Point 2 y coordinate.
     * @return Distance between two points.
     */
    private double distance(double x1, double y1, double x2, double y2) {
        double distX = x1 - x2;
        double distY = y1 - y2;
        return Math.hypot( distX, distY );
    }
}
