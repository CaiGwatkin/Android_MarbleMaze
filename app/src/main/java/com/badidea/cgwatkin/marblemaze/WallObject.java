package com.badidea.cgwatkin.marblemaze;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

class WallObject implements WorldObject {

    /**
     * Line end coordinates.
     */
    private int mX1, mY1, mX2, mY2;

    /**
     * Constructor
     *
     * @param x1 Point 1 x coordinate.
     * @param y1 Point 1 y coordinate.
     * @param x2 Point 2 x coordinate.
     * @param y2 Point 2 y coordinate.
     */
    WallObject(int x1, int y1, int x2, int y2) {
        mX1 = x1;
        mY1 = y1;
        mX2 = x2;
        mY2 = y2;
    }

    /**
     * Draws object on view.
     *
     * @param c The canvas.
     * @param p The paint.
     */
    public void draw(Canvas c, Paint p) {
        c.drawLine(mX1, mY1, mX2, mY2, p);
    }

    /**
     * Tests if the marble collides with this object.
     *
     * @param x Marble's centre's x coordinate.
     * @param y Marble's centre's y coordinate.
     * @param r Marble's radius.
     * @return True if collision occurred.
     */
    public boolean collision(float x, float y, float r) {
        return lineCircle(mX1, mY1, mX2, mY2, x, y, r);
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
        return mX1 == mX2;
    }

    /* *****************************************************************************************************************
     * Following sampled from http://www.jeffreythompson.org/collision-detection/line-circle.php
     */
    // LINE/CIRCLE
    private boolean lineCircle(float x1, float y1, float x2, float y2, float cx, float cy, float r) {

        // is either end INSIDE the circle?
        // if so, return true immediately
        if (pointCircle(x1, y1, cx, cy, r) || pointCircle(x2, y2, cx, cy, r)) {
            return true;
        }

        // get length of the line
        float len = distance(x1, y1, x2, y2);

        // get dot product of the line and circle
        float dot = ( ((cx - x1) * (x2 - x1)) + ((cy - y1) * (y2 - y1)) ) / (float) Math.pow(len, 2);

        // find the closest point on the line
        float closestX = x1 + (dot * (x2 - x1));
        float closestY = y1 + (dot * (y2 - y1));

        Log.d("closestX", String.valueOf(closestX));
        Log.d("closestY", String.valueOf(closestY));
        Log.d("       X", String.valueOf(cx));
        Log.d("       Y", String.valueOf(cy));

//        // is this point actually on the line segment?
//        // if so keep going, but if not, return false
//        boolean onSegment = linePoint(x1, y1, x2, y2, closestX, closestY);
//        if (!onSegment) {
//            return false;
//        }

        // get distance to closest point
        float distance = distance(closestX, closestY, cx, cy);
        return distance <= r;
    }


    // POINT/CIRCLE
    private boolean pointCircle(float px, float py, float cx, float cy, float r) {

        // get distance between the point and circle's center
        float distance = distance(px, py, cx, cy);

        // if the distance is less than the circle's
        // radius the point is inside!
        return distance <= r;
    }


    // LINE/POINT
    private boolean linePoint(float x1, float y1, float x2, float y2, float px, float py) {

        // get distance from the point to the two ends of the line
        float d1 = distance(px, py, x1, y1);
        float d2 = distance(px, py, x2, y2);

        // get the length of the line
        float lineLen = distance(x1, y1, x2, y2);

        // since floats are so minutely accurate, add
        // a little buffer zone that will give collision
        float buffer = 0.1f;    // higher # = less accurate

        // if the two distances are equal to the line's
        // length, the point is on the line!
        // note we use the buffer here to give a range,
        // rather than one #
        return (d1 + d2 >= lineLen - buffer && d1 + d2 <= lineLen + buffer);
    }
    /* *****************************************************************************************************************
     * End sample
     */

    /**
     * Returns the distance between two points.
     *
     * @param x1 Point 1 x coordinate.
     * @param y1 Point 1 y coordinate.
     * @param x2 Point 2 x coordinate.
     * @param y2 Point 2 y coordinate.
     * @return Distance between two points.
     */
    private float distance(float x1, float y1, float x2, float y2) {
        float distX = x1 - y1;
        float distY = x2 - y2;
//        return (float) Math.sqrt( (distX*distX) + (distY*distY) );
        return (float) Math.hypot( distX, distY );
    }
}
