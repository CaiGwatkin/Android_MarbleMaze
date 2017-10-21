package com.badidea.cgwatkin.marblemaze;

class HoleObject extends CircleObject {

    /**
     * HoleObject constructor
     *
     * @param x Position of centre in x plane (top left if object.
     * @param y Position of centre in y plane.
     * @param r Radius.
     */
    HoleObject(int x, int y, int r) {
        super(x, y, r);
    }

    /**
     * Returns true if object is goal.
     *
     * @return false
     */
    @Override
    public boolean isGoal() {
        return false;
    }

    /**
     * Returns true if object is hole.
     *
     * @return true
     */
    @Override
    public boolean isHole() {
        return true;
    }

    /**
     * Returns true if object is wall.
     *
     * @return false
     */
    @Override
    public boolean isWall() {
        return false;
    }
}
