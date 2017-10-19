package com.badidea.cgwatkin.marblemaze;

import android.graphics.Canvas;
import android.graphics.Paint;

class HoleObject extends WorldObject {
    /**
     * The object's position.
     */
    private int mR;

    /**
     * GoalObject constructor
     *
     * @param x Position of centre in x plane (top left if object.
     * @param y Position of centre in y plane.
     * @param r Radius.
     */
    HoleObject(int x, int y, int r) {
        super(x, y);
        mR = r;
    }

    /**
     * Draws object on view.
     *
     * @param c The canvas.
     * @param p The paint.
     */
    @Override
    void draw(Canvas c, Paint p) {
        c.save();
        c.translate(mX,mY);
        c.drawCircle(0,0,mR,p);
        c.restore();
    }

    /**
     * Tests if the marble collides with this object.
     *
     * @param x Marble's centre's x coordinate.
     * @param y Marble's centre's y coordinate.
     * @param r Marble's radius.
     * @return True if collision occurred.
     */
    @Override
    public boolean collision(float x, float y, float r) {
        return ((mX - x) * (mX - x) + (mY - y) * (mY - y)) < ((mR + r) * (mR + r));
    }

    /**
     * Not used with GoalObject.
     *
     * @param x Marble's centre's x coordinate.
     * @param y Marble's centre's y coordinate.
     * @param r Marble's radius.
     * @return null
     */
    @Override
    public CollisionSide side(float x, float y, float r) {
        return null;
    }

    /**
     * Returns true if object is goal.
     *
     * @return False
     */
    @Override
    public boolean isGoal() {
        return false;
    }

    /**
     * Returns true if object is hole.
     *
     * @return True
     */
    @Override
    public boolean isHole() {
        return true;
    }

    /**
     * Returns true if object is object.
     *
     * @return False
     */
    @Override
    public boolean isObject() {
        return false;
    }
}
