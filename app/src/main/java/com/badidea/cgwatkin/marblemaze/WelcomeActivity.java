package com.badidea.cgwatkin.marblemaze;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class WelcomeActivity extends Activity {

    /**
     * Stores the welcome messages for the content view.
     */
    static ArrayList<Integer> mWelcomeMessages;
    static {
        mWelcomeMessages = new ArrayList<>();
        mWelcomeMessages.add(R.string.welcome1);
        mWelcomeMessages.add(R.string.welcome2);
        mWelcomeMessages.add(R.string.welcome3);
    }

    /**
     * The position of the next welcome message in mWelcomeMessages array.
     */
    private Integer mNextWelcomeMessage;

    /**
     * Thread handler to handle changing welcome messages.
     */
    private final Handler mNextHandler = new Handler();

    /**
     * The view containing the welcome message.
     */
    private TextView mWelcomeTextView;

    /**
     * The runnable that starts the transition to the next welcome message.
     */
    private final Runnable mNextRunnable = new Runnable() {
        @Override
        public void run() {
            nextWelcomeMessage();
        }
    };

    /**
     * Create view for activity.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);
        mWelcomeTextView = (TextView) findViewById(R.id.welcome_text);
        mNextWelcomeMessage = 0;
    }

    /**
     * Go to next welcome message after onCreate() has finished.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        delayedNext(1000);
    }

    /**
     * Set activity to fullscreen.
     * Restart updating marble view when app resumed.
     */
    @Override
    public void onResume() {
        super.onResume();
        findViewById(android.R.id.content).setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * Begins runnable to display next welcome message after delay.
     *
     * @param delayMillis The millisecond delay.
     */
    private void delayedNext(int delayMillis) {
        mNextHandler.removeCallbacks(mNextRunnable);
        mNextHandler.postDelayed(mNextRunnable, delayMillis);
    }

    /**
     * Go to next welcome message.
     */
    private void nextWelcomeMessage() {
        int resourceInt = mWelcomeMessages.get(mNextWelcomeMessage);
        mNextWelcomeMessage++;
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();

        if (mNextWelcomeMessage < mWelcomeMessages.size()) {
            final Integer duration = 1500;
            final AnimatorListenerAdapter listener = new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mWelcomeTextView.animate()
                                    .setInterpolator(interpolator)
                                    .alpha(0f)
                                    .setDuration(duration);
                        }
                    };
            pulseText(mWelcomeTextView, resourceInt, 40, interpolator, duration, listener);
            delayedNext(3000);
        }
        else if (mNextWelcomeMessage == mWelcomeMessages.size()) {
            final AnimatorListenerAdapter listener = new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    findViewById(R.id.welcome_continue).animate()
                            .setInterpolator(interpolator)
                            .alpha(1f)
                            .setDuration(1500);
                }
            };
            pulseText(mWelcomeTextView, resourceInt, 70, interpolator, 2000, listener);
        }
    }

    /**
     * Pulses the content view.
     *
     * Starts an animation that increases the alpha of the content view. Optionally a listener can
     * be used to decrease the
     *
     * @param textView The text view to be pulsed.
     * @param resourceInt The position of the next welcome message in mWelcomeMessages array.
     * @param textSize The size of the text.
     * @param interpolator The interpolator to use.
     * @param duration The animation duration.
     * @param listener An optional listener (to decrease alpha, therefore pulse).
     */
    private void pulseText(final TextView textView, final Integer resourceInt,
                           final Integer textSize, final Interpolator interpolator,
                           final Integer duration, AnimatorListenerAdapter listener) {
        textView.setText(resourceInt);
        textView.setTextSize(textSize);
        textView.animate()
                .setInterpolator(interpolator)
                .alpha(1f)
                .setDuration(duration)
                .setListener(listener);
    }

    /**
     * Start app when touch event occurs.
     *
     * @param event The touch event
     * @return true
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        setContentView(R.layout.world_picker);
        ((WorldPickerGridView) findViewById(R.id.world_picker)).init();
        return true;
    }
}
