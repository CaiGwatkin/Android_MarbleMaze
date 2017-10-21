package com.badidea.cgwatkin.marblemaze;

import java.util.ArrayList;

/**
 * Observer interface
 *
 * Used in Marble View to callback to activity.
 */
interface Observer {
    void success();
    void failure();
    ArrayList<WorldObject> createWorldObjects(int canvasWidth, int canvasHeight, int wallWidth, int radius,
                                              int distanceBetweenWalls, int xPadding, int yPadding);
}
