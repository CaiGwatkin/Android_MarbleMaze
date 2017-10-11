package com.badidea.cgwatkin.marblemaze;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

class RectObject extends WorldObject {
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
}
