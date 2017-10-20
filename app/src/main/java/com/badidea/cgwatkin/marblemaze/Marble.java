package com.badidea.cgwatkin.marblemaze;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Marble class
 *
 * The main object in the world, which the user controls.
 */
class Marble {
    /**
     * The marble's position, radius and velocity values.
     */
    private double mX, mY, mR, mVX, mVY;

    /**
     * Velocity modifier on collision.
     */
    private double k = 0.3;

    /**
     * Marble constructor
     *
     * @param x Position in x plane.
     * @param y Position in y plane.
     * @param vX Velocity in x plane.
     * @param vY Velocity in y plane.
     * @param r Radius.
     */
    Marble(int x, int y, double vX, double vY, double r) {
        mX = x;
        mY = y;
        mR = r;
        mVX = vX;
        mVY = vY;
    }

    /**
     * Draws marble on view.
     *
     * @param c The canvas.
     * @param p The paint.
     */
    void draw(Canvas c, Paint p) {
        c.save();
        c.translate((float) mX, (float) mY);
        c.drawCircle(0, 0, (float) mR, p);
        c.restore();
    }

    /**
     * Move the marble based on change in time and current gravity.
     *
     * @param dT Difference in time.
     * @param gX Gravity in x plane.
     * @param gY Gravity in y plane.
     * @param w Width of canvas.
     * @param h Height of canvas.
     * @return Type of hit.
     */
    HitType move(double dT, double gX, double gY, double w, double h, ArrayList<WorldObject> worldObjects) {
        mVX = updateVelocity(mVX, dT, gX);
        mVY = updateVelocity(mVY, dT, gY);
        double x = linearMovement(mX, mVX, dT);
        double y = linearMovement(mY, mVY, dT);
        if (!worldObjects.isEmpty()) {
            for (WorldObject wo: worldObjects) {
                if (wo.collision(x, y, mR, mVX, mVY)) {
                    mX = x;
                    mY = y;
                    if (wo.isGoal()) {
                        return HitType.TARGET;
                    } else if (wo.isHole()) {
                        return HitType.HOLE;
                    } else {
                        if (((WallObject) wo).isHorizontal()) {
                            reverseVY();
                        }
                        else {
                            reverseVX();
                        }
                        x = linearMovement(mX, mVX, dT);
                        y = linearMovement(mY, mVY, dT);
                        mX = x;
                        mY = y;
                        return HitType.WALL;
                    }
                }
            }
        }
        boolean bc = false;
        if (boundaryCollision(x, w)) {
            reverseVX();
            x = linearMovement(mX, mVX, dT);
            bc = true;
        }
        if (boundaryCollision(y, h)) {
            reverseVY();
            y = linearMovement(mY, mVY, dT);
            bc = true;
        }
        if (bc) {
            mX = x;
            mY = y;
            return HitType.BOUNDARY;
        }
        mX = x;
        mY = y;
        return HitType.NONE;
    }

    /**
     * Reverse velocity in x plane.
     */
    private void reverseVX() {
        mVX = -mVX * k;
    }

    /**
     * Reverse velocity in y plane.
     */
    private void reverseVY() {
        mVY = -mVY * k;
    }

    /**
     * Reverse velocity in both x and y planes.
     */
    private void bounce() {
        reverseVX();
        reverseVY();
    }

    /**
     * Calculate next linear position.
     *
     * @param coordinate The coordinate.
     * @param v The velocity.
     * @param dT The time difference.
     * @return The new coordinate.
     */
    private double linearMovement(double coordinate, double v, double dT) {
        return coordinate + (v * dT * 100);
    }

    /**
     * Calculate the new velocity based on gravity.
     *
     * @param v The current velocity.
     * @param dT The time difference.
     * @param g The gravity modifier.
     * @return The new velocity.
     */
    private double updateVelocity(double v, double dT, double g) {
        return v + (dT * g * 10);
    }

    /**
     * Calculate if the marble is touching the boundary.
     *
     * @param coordinate The coordinate.
     * @param boundary The boundary value.
     * @return True if boundary collision occurred.
     */
    private boolean boundaryCollision(double coordinate, double boundary) {
        return coordinate < mR || coordinate > boundary - mR;
    }
}

/**
 * Hit types.
 */
enum HitType {
    NONE,
    BOUNDARY,
    WALL,
    TARGET,
    HOLE
}
