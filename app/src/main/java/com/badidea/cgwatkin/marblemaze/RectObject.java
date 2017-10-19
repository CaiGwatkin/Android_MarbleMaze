package com.badidea.cgwatkin.marblemaze;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Rect Object class
 *
 * Used for rectangle objects in world.
 */
class RectObject extends WorldObject {
    /**
     * The object's position.
     */
    int mW, mH;

    /**
     * The rectangle object that describes the
     */
    private Rect mRect;

    /**
     * SquareObject constructor
     *
     * @param x Position in x plane.
     * @param y Position in y plane.
     * @param w Width in x plane.
     * @param h Height in y plane.
     */
    RectObject(int x, int y, int w, int h) {
        super(x, y);
        mRect = new Rect(0, 0, w, h);
        mW = w;
        mH = h;
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
        c.translate(mX, mY);
        c.drawRect(mRect, p);
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
        float distX = Math.abs(x - mX - mW / 2);
        float distY = Math.abs(y - mY - mH / 2);
        if ((distX > (mW / 2 + r)) || (distY > (mH / 2 + r))) {
            return false;
        }
        if ((distX <= (mW/2)) || (distY <= (mH/2))) {
            return true;
        }
        float dX = distX - mW / 2;
        float dY = distY - mH / 2;
        return (dX * dX + dY * dY <= r * r);
    }

    /**
     * Finds to which side of this object the marble is.
     *
     * @param x Marble's centre's x coordinate.
     * @param y Marble's centre's y coordinate.
     * @param r Marble's radius.
     * @return The CollisionSide of this object the marble is on.
     */
    @Override
    public CollisionSide side(float x, float y, float r) {
        int maxX = mX + mW;
        int maxY = mY + mH;
        if (x < mX) {
            if (y < mY) {
                return CollisionSide.TOP_LEFT;
            }
            else if (y > maxY) {
                return CollisionSide.BOTTOM_LEFT;
            }
            else {
                return CollisionSide.LEFT;
            }
        }
        else if (x > maxX) {
            if (y < mY) {
                return CollisionSide.TOP_RIGHT;
            }
            else if (y > maxY) {
                return CollisionSide.BOTTOM_RIGHT;
            }
            else {
                return CollisionSide.RIGHT;
            }
        }
        else if (y < mY) {
            return CollisionSide.TOP;
        }
        else if (y > maxY) {
            return CollisionSide.BOTTOM;
        }
        return CollisionSide.NONE;
    }

    /**
     * Returns true if object is target.
     *
     * @return True if object is target.
     */
    @Override
    public boolean isGoal() {
        return false;
    }

    /**
     * Returns true if object is hole.
     *
     * @return True if object is hole.
     */
    @Override
    public boolean isHole() {
        return false;
    }

    /**
     * Returns true if object is object.
     *
     * @return True
     */
    @Override
    public boolean isObject() {
        return true;
    }
}
