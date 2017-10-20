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
    public boolean collision(double x, double y, double r) {
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
    private boolean lineCircle(double x1, double y1, double x2, double y2, double cx, double cy, double r) {

        // is either end INSIDE the circle?
        // if so, return true immediately
        if (pointCircle(x1, y1, cx, cy, r) || pointCircle(x2, y2, cx, cy, r)) {
            return true;
        }

        // get length of the line
        double len = distance(x1, y1, x2, y2);

        // get dot product of the line and circle
        double dot = ( ((cx - x1) * (x2 - x1)) + ((cy - y1) * (y2 - y1)) ) / Math.pow(len, 2);

        // find the closest point on the line
        double closestX = x1 + (dot * (x2 - x1));
        double closestY = y1 + (dot * (y2 - y1));

        // is this point actually on the line segment?
        // if so keep going, but if not, return false
        boolean onSegment = linePoint(x1, y1, x2, y2, closestX, closestY);
        if (!onSegment) {
            return false;
        }

        // get distance to closest point
        double distance = distance(closestX, closestY, cx, cy);
        return distance <= r;
    }


    // POINT/CIRCLE
    private boolean pointCircle(double px, double py, double cx, double cy, double r) {

        // get distance between the point and circle's center
        double distance = distance(px, py, cx, cy);

        // if the distance is less than the circle's
        // radius the point is inside!
        return distance <= r;
    }


    // LINE/POINT
    private boolean linePoint(double x1, double y1, double x2, double y2, double px, double py) {

        // get distance from the point to the two ends of the line
        double d1 = distance(px, py, x1, y1);
        double d2 = distance(px, py, x2, y2);

        // get the length of the line
        double lineLen = distance(x1, y1, x2, y2);

        // since doubles are so minutely accurate, add
        // a little buffer zone that will give collision
        double buffer = 0.1;    // higher # = less accurate

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
    private double distance(double x1, double y1, double x2, double y2) {
        double distX = x1 - y1;
        double distY = x2 - y2;
//        return (double) Math.sqrt( (distX*distX) + (distY*distY) );
        return Math.hypot( distX, distY );
    }
}
