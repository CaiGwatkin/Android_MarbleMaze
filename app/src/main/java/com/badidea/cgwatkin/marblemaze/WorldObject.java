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
     * @return True if collision occurred.
     */
    boolean collision(float x, float y, float r);

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
