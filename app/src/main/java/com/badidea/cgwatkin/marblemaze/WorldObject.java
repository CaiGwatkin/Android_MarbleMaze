package com.badidea.cgwatkin.marblemaze;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * World Object class
 *
 * Extended from to create world objects.
 */
abstract class WorldObject {
    /**
     * The object's position.
     */
    int mX, mY;

    /**
     * WorldObject constructor
     *
     * @param x Position in x plane (top left if object.
     * @param y Position in y plane.
     */
    WorldObject(int x, int y) {
        mX = x;
        mY = y;
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
     * @return The CollisionSide of this object the marble is on.
     */
    abstract public CollisionSide side(float x, float y, float r);

    /**
     * Returns true if object is goal.
     *
     * @return True if object is goal.
     */
    abstract public boolean isGoal();

    /**
     * Returns true if object is hole.
     *
     * @return True if object is hole.
     */
    abstract public boolean isHole();

    /**
     * Returns true if object is object.
     *
     * @return True if object is object.
     */
    abstract public boolean isObject();
}

/**
 * The different sides of an object the marble can be on.
 */
enum CollisionSide {
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
