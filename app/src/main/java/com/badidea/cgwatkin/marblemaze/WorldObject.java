package com.badidea.cgwatkin.marblemaze;

import android.graphics.Canvas;
import android.graphics.Paint;

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

    int getX() {
        return mX;
    }

    /**
     * Draws object on view.
     *
     * @param c The canvas.
     * @param p The paint.
     */
    abstract void draw(Canvas c, Paint p);
}
