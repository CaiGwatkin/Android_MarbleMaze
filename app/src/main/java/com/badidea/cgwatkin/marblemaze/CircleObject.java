package com.badidea.cgwatkin.marblemaze;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Circle Object abstract class
 *
 * For extending from to create circular world objects.
 *
 * Implements World Object interface.
 */
abstract class CircleObject implements WorldObject {

    /**
     * The object's position.
     */
    private int mX, mY, mR;

    /**
     * CircleObject constructor
     *
     * @param x Position of centre in x plane (top left if object.
     * @param y Position of centre in y plane.
     * @param r Radius.
     */
    CircleObject(int x, int y, int r) {
        mX = x;
        mY = y;
        mR = r;
    }

    /**
     * Draws object on view.
     *
     * @param c The canvas.
     * @param p The paint.
     */
    @Override
    public void draw(Canvas c, Paint p) {
        c.save();
        c.translate(mX, mY);
        c.drawCircle(0, 0, mR, p);
        c.restore();
    }

    /**
     * Tests if the marble collides with this object.
     *
     * @param x Marble's centre's x coordinate.
     * @param y Marble's centre's y coordinate.
     * @param r Marble's radius.
     * @param vX Marble's velocity in x plane.
     * @param vY Marble's velocity in y plane.
     * @return true if collision occurred.
     */
    @Override
    public boolean collision(double x, double y, double r, double vX, double vY) {
        return ((mX - x) * (mX - x) + (mY - y) * (mY - y)) < ((mR + r) * (mR + r));
    }

    /**
     * Returns true if object is goal.
     *
     * @return true if object is goal.
     */
    @Override
    abstract public boolean isGoal();

    /**
     * Returns true if object is hole.
     *
     * @return true if object is hole.
     */
    @Override
    abstract public boolean isHole();

    /**
     * Returns true if object is wall.
     *
     * @return true if object is wall.
     */
    @Override
    abstract public boolean isWall();
}
