package com.badidea.cgwatkin.marblemaze;

import android.graphics.Canvas;
import android.graphics.Paint;

abstract class WorldObject {
    /**
     * The object's position.
     */
    int mX, mY, mW, mH;

    /**
     * WorldObject constructor
     *
     * @param x Position in x plane (top left if object.
     * @param y Position in y plane.
     */
    WorldObject(int x, int y) {
        mX = x;
        mY = y;
        mW = 0;
        mH = 0;
    }

    /**
     * Get x coordinate.
     *
     * @return x coordinate.
     */
    int getX() {
        return mX;
    }

    /**
     * Get y coordinate.
     *
     * @return y coordinate.
     */
    int getY() {
        return mY;
    }

    /**
     * Get width value.
     *
     * @return Width.
     */
    int getW() {
        return mW;
    }

    /**
     * Get height value.
     *
     * @return Height.
     */
    int getH() {
        return mH;
    }

    /**
     * Draws object on view.
     *
     * @param c The canvas.
     * @param p The paint.
     */
    abstract void draw(Canvas c, Paint p);

    /**
     * Tests if the marble collides with this object.
     *
     * @param x Marble's centre's x coordinate.
     * @param y Marble's centre's y coordinate.
     * @param r Marble's radius.
     * @return True if collision occurred.
     */
    abstract public boolean collision(float x, float y, float r);

    /**
     * Finds to which side of this object the marble is.
     *
     * @param x Marble's centre's x coordinate.
     * @param y Marble's centre's y coordinate.
     * @param r Marble's radius.
     * @return The Side of this object the marble is on.
     */
    abstract public Side side(float x, float y, float r);
}

/**
 * The different sides of an object the marble can be on.
 */
enum Side {
    NONE,
    LEFT,
    TOP_LEFT,
    TOP,
    TOP_RIGHT,
    RIGHT,
    BOTTOM_RIGHT,
    BOTTOM,
    BOTTOM_LEFT
}
