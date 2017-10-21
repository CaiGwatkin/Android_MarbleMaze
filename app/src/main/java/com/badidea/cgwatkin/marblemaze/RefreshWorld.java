package com.badidea.cgwatkin.marblemaze;

import android.os.Handler;

/**
 * Refresh World class
 *
 * Refreshes game world until paused.
 */
class RefreshWorld implements Runnable {

    /**
     * The refresh rate/delay values.
     */
    private static long REFRESH_RATE = 60;
    private static long REFRESH_DELAY = 1000 / REFRESH_RATE;

    /**
     * Marble world view to be refreshed.
     */
    private MarbleView mMarbleView;

    /**
     * Thread handler allow screen refresh after set delay.
     */
    private Handler mHandler;

    /**
     * Whether the runnable should pause or not.
     */
    private boolean paused;

    /**
     * The start time of the refresh runnable.
     */
    private long mStartTime;

    /**
     * Constructor.
     *
     * @param marbleView The world to refresh.
     * @param handler The thread handler to post to.
     */
    RefreshWorld(MarbleView marbleView, Handler handler) {
        mMarbleView = marbleView;
        mHandler = handler;
        paused = true;
    }

    /**
     * Update Marble View, redraw.
     * Recursive.
     */
    @Override
    public void run() {
        if (!paused) {
            long dT = System.currentTimeMillis() - mStartTime;
            mStartTime = System.currentTimeMillis();
            mMarbleView.update(dT / 1000f);
            mMarbleView.postInvalidate();
            long timeTaken = System.currentTimeMillis() - mStartTime;
            mHandler.postDelayed(this, REFRESH_DELAY - timeTaken);
        }
    }

    /**
     * @param startTime Start time.
     */
    RefreshWorld setStartTime(long startTime) {
        mStartTime = startTime;
        return this;
    }

    /**
     * Pauses refresh.
     */
    void pause() {
        paused = true;
    }

    /**
     * Un-pauses refresh.
     */
    void unPause() {
        paused = false;
    }
}
