package com.vivek.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.vivek.musicplayer.helper.Song;
import com.vivek.musicplayer.player.PlayerState;
import com.vivek.musicplayer.utils.ImageViewUtil;

import java.util.ArrayList;
import java.util.Locale;


import de.hdodenhof.circleimageview.CircleImageView;

public class PlayerActivity extends AppCompatActivity {

    // transition properties
    private static String PROPNAME_SCREENLOCATION_LEFT = "rsspace:location:left";
    private static String PROPNAME_SCREENLOCATION_TOP = "rsspace:location:top";
    private static String PROPNAME_WIDTH = "rsspace:width";
    private static String PROPNAME_HEIGHT = "rsspace:height";

    // Animation constants
    private static final AccelerateDecelerateInterpolator DEFAULT_INTERPOLATOR = new AccelerateDecelerateInterpolator();
    private static final int DEFAULT_DURATION = 300;

    // Bundle that will contain the transition start values
    private Bundle mStartValues;
    // Bundle that will contain the transition end values
    final private Bundle mEndValues = new Bundle();

    CircleImageView circularSongArt;
    ImageView playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_down);

        playButton = findViewById(R.id.playPauseButtonPlayer);
        circularSongArt = findViewById(R.id.playerSongArt);

        extractViewInfoFromBundle(getIntent());
        onUiReady();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(PlayerState.isMusicPlaying()){
            playButton.setImageResource(R.drawable.ic_pause_button_player);
        } else {
            playButton.setImageResource(R.drawable.ic_play_button_player);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void playButtonClicked(View view) {
        if(PlayerState.isMusicPlaying()){
            //Pause the music and set the icon to play
            PlayerState.setMusicPlaying(false);
            playButton.setImageResource(R.drawable.ic_play_button_player);
        } else {
            // Play the music and set the icon to pause
            PlayerState.setMusicPlaying(true);
            playButton.setImageResource(R.drawable.ic_pause_button_player);
        }
    }


    @Override
    public void onBackPressed() {
        // run the exit animation
        runExitAnimation();
    }


    private void extractViewInfoFromBundle(Intent intent) {
        mStartValues = intent.getBundleExtra(MainActivity.VIEW_INFO_EXTRA);
    }

    private void onUiReady() {
        circularSongArt.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                // remove previous listener
                circularSongArt.getViewTreeObserver().removeOnPreDrawListener(this);
                // prep the scene
                prepareScene();
                // run the animation
                runEnterAnimation();
                return true;
            }
        });
    }

    /**
     * This method preps the scene. Captures the end values, calculates deltas with start values and
     * reposition the view in the target layout
     */
    private void prepareScene() {
        // do the first capture to scale the image
        captureScaleValues(mEndValues, circularSongArt);

        // calculate the scale factors
        float scaleX = scaleDelta(mStartValues, mEndValues, PROPNAME_WIDTH);
        float scaleY = scaleDelta(mStartValues, mEndValues, PROPNAME_HEIGHT);

        // scale the image
        circularSongArt.setScaleX(scaleX);
        circularSongArt.setScaleY(scaleY);

        // as scaling the image will change the top and left coordinates, we need to re-capture
        // the values to proper figure out the translation deltas w.r.t. to start view
        captureScreenLocationValues(mEndValues, circularSongArt);

        int deltaX = translationDelta(mStartValues, mEndValues, PROPNAME_SCREENLOCATION_LEFT);
        int deltaY = translationDelta(mStartValues, mEndValues, PROPNAME_SCREENLOCATION_TOP);
        // finally, translate the end view to where the start view was
        circularSongArt.setTranslationX(deltaX);
        circularSongArt.setTranslationY(deltaY);
    }

    private void runEnterAnimation() {
        // We can now make it visible
        circularSongArt.setVisibility(View.VISIBLE);
        // finally, run the animation
        circularSongArt.animate()
                .setDuration(DEFAULT_DURATION)
                .setInterpolator(DEFAULT_INTERPOLATOR)
                .scaleX(1f)
                .scaleY(1f)
                .translationX(0)
                .translationY(0)
                .start();
    }



    private static void captureScaleValues(@NonNull Bundle b, @NonNull View view) {
        if (view instanceof ImageView) {
            int[] size = ImageViewUtil.getDisplayedImageLocation((CircleImageView) view);
            b.putInt(PROPNAME_WIDTH, size[2]);
            b.putInt(PROPNAME_HEIGHT, size[3]);
        } else {
            b.putInt(PROPNAME_WIDTH, view.getWidth());
            b.putInt(PROPNAME_HEIGHT, view.getHeight());
        }
    }

    private static void captureScreenLocationValues(@NonNull Bundle b, @NonNull View view) {
        if (view instanceof ImageView) {
            int[] size = ImageViewUtil.getDisplayedImageLocation((ImageView) view);
            b.putInt(PROPNAME_SCREENLOCATION_LEFT, size[0]);
            b.putInt(PROPNAME_SCREENLOCATION_TOP, size[1]);
        } else {
            int[] screenLocation = new int[2];
            view.getLocationOnScreen(screenLocation);
            b.putInt(PROPNAME_SCREENLOCATION_LEFT, screenLocation[0]);
            b.putInt(PROPNAME_SCREENLOCATION_TOP, screenLocation[1]);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                runExitAnimation();
                break;
        }
        return true;
    }

    /**
     * Call this method to run the exit transition
     */
    private void runExitAnimation() {
        // re-calculate deltas
        int deltaX = translationDelta(mStartValues, mEndValues, PROPNAME_SCREENLOCATION_LEFT);
        int deltaY = translationDelta(mStartValues, mEndValues, PROPNAME_SCREENLOCATION_TOP);
        float scaleX = scaleDelta(mStartValues, mEndValues, PROPNAME_WIDTH);
        float scaleY = scaleDelta(mStartValues, mEndValues, PROPNAME_HEIGHT);

        circularSongArt.animate()
                .setDuration(DEFAULT_DURATION)
                .setInterpolator(DEFAULT_INTERPOLATOR)
                .scaleX(scaleX)
                .scaleY(scaleY)
                .translationX(deltaX)
                .translationY(deltaY)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        overridePendingTransition(0, 0);
                    }
                }).start();
    }

    /**
     * Helper method to calculate the scale delta given start and end values
     *
     * @param startValues  start values {@link Bundle}
     * @param endValues    end values {@link Bundle}
     * @param propertyName property name
     * @return scale delta value
     */
    private float scaleDelta(
            @NonNull Bundle startValues,
            @NonNull Bundle endValues,
            @NonNull String propertyName) {

        int startValue = startValues.getInt(propertyName);
        int endValue = endValues.getInt(propertyName);
        float delta = (float) startValue / endValue;

        //Timber.d(String.format(Locale.US, "%s: startValue = %d, endValue = %d, delta = %f", propertyName, startValue, endValue, delta));

        return delta;
    }

    /**
     * Helper method to calculate the translation deltas given start and end values
     *
     * @param startValues  start values {@link Bundle}
     * @param endValues    end values {@link Bundle}
     * @param propertyName property name
     * @return translation delta between start and end values
     */
    private int translationDelta(
            @NonNull Bundle startValues,
            @NonNull Bundle endValues,
            @NonNull String propertyName) {

        int startValue = startValues.getInt(propertyName);
        int endValue = endValues.getInt(propertyName);
        int delta = startValue - endValue;

        //Timber.d(String.format(Locale.US, "%s: startValue = %d, endValue = %d, delta = %d", propertyName, startValue, endValue, delta));

        return delta;
    }
}
