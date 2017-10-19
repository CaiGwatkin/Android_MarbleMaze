package com.badidea.cgwatkin.marblemaze;

/**
 * SuccessObserver interface
 *
 * Used in custom view to callback to activity.
 */
interface SuccessObserver {
    void success();
    void failure();
}
