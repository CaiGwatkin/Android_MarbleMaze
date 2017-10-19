package com.badidea.cgwatkin.marblemaze;

import android.os.Handler;

/**
 * Refresh World class
 *
 * Refreshes game world until stopped.
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
    private MarbleView mWorldView;

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
     * Refresh World constructor.
     *
     * @param marbleView The world to refresh.
     * @param handler The thread handler to post to.
     */
    RefreshWorld(MarbleView marbleView, Handler handler) {
        mWorldView = marbleView;
        mHandler = handler;
        paused = true;
    }

    @Override
    public void run() {
        if (!paused) {
            long dT = System.currentTimeMillis() - mStartTime;
            mStartTime = System.currentTimeMillis();
            mWorldView.update(dT / 1000f);
            mWorldView.postInvalidate();
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
