package com.badidea.cgwatkin.marblemaze;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

class RectObject extends WorldObject {
    /**
     * The rectangle object that describes the
     */
    private Rect mRect;
    
    private int mW, mH;

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

//    public int side(float x, float y) {
//        if ()
//    }
}
