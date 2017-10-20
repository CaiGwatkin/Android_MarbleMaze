package com.badidea.cgwatkin.marblemaze;

import android.graphics.Canvas;
import android.graphics.Paint;

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

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    public int getR() {
        return mR;
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
     * @return true if collision occurred.
     */
    @Override
    public boolean collision(float x, float y, float r) {
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
