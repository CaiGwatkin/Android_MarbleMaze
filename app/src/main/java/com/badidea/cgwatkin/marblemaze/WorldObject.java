package com.badidea.cgwatkin.marblemaze;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * World Object class
 *
 * Extended from to create world objects.
 */
interface WorldObject {

    /**
     * Draws object on view.
     *
     * @param c The canvas.
     * @param p The paint.
     */
    void draw(Canvas c, Paint p);

    /**
     * Tests if the marble collides with this object.
     *
     * @param x Marble's centre's x coordinate.
     * @param y Marble's centre's y coordinate.
     * @param r Marble's radius.
     * @param vX Marble's velocity in x plane.
     * @param vY Marble's velocity in y plane.
     * @return True if collision occurred.
     */
    boolean collision(double x, double y, double r, double vX, double vY);

    /**
     * Returns true if object is goal.
     *
     * @return True if object is goal.
     */
    boolean isGoal();

    /**
     * Returns true if object is hole.
     *
     * @return True if object is hole.
     */
    boolean isHole();

    /**
     * Returns true if object is object.
     *
     * @return True if object is object.
     */
    boolean isWall();
}
