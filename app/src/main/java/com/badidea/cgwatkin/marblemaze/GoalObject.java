package com.badidea.cgwatkin.marblemaze;

/**
 * Goal Object class
 *
 * For goals in the world.
 * User succeeds if marble collides with this.
 *
 * Extends Circle Object, which implements World Object interface.
 */
class GoalObject extends CircleObject {

    /**
     * GoalObject constructor
     *
     * @param x Position of centre in x plane (top left if object.
     * @param y Position of centre in y plane.
     * @param r Radius.
     */
    GoalObject(int x, int y, int r) {
        super(x, y, r);
    }

    /**
     * Returns true if object is goal.
     *
     * @return true
     */
    @Override
    public boolean isGoal() {
        return true;
    }

    /**
     * Returns true if object is hole.
     *
     * @return false
     */
    @Override
    public boolean isHole() {
        return false;
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
