package com.codecollapse.ccvideoviewer.CCVideoViewer;

import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.constraintlayout.widget.ConstraintLayout;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.codecollapse.ccvideoviewer.CCVideoViewer.CCVideoViewer.isOuterLayoutVisible;


public class CCAnimation {

    public static void FadeInAnimation(ConstraintLayout constraintLayout) {

        constraintLayout.setVisibility(VISIBLE);
        isOuterLayoutVisible = true;
        Log.d("LayoutAnimation", "isOuterLayoutVisible: " + isOuterLayoutVisible);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(constraintLayout, "alpha", 0, 1f);
        fadeIn.setDuration(500);
        fadeIn.start();
    }
    public static void FadeOutAnimation(final ConstraintLayout constraintLayout) {

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(200);
        fadeOut.setDuration(200);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isOuterLayoutVisible = false;
                Log.d("LayoutAnimation", "isOuterLayoutVisible: " + isOuterLayoutVisible);
                constraintLayout.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        constraintLayout.startAnimation(fadeOut);
    }
}
