package com.codecollapse.ccvideoviewer.CCVideoViewer;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import static com.codecollapse.ccvideoviewer.CCVideoViewer.CommonFunctions.HideNavAndStatusBar;
import static com.codecollapse.ccvideoviewer.CCVideoViewer.CommonFunctions.getCurrentDuration;
import static com.codecollapse.ccvideoviewer.CCVideoViewer.CommonFunctions.getScreenBrightness;
import static com.codecollapse.ccvideoviewer.CCVideoViewer.CommonFunctions.getVideoDuration;
import static com.codecollapse.ccvideoviewer.CCVideoViewer.CommonFunctions.setScreenBrightness;

public class CCVideoViewer extends ConstraintLayout {

    private View rootView;
    private VideoView videoView;
    private SeekBar seekBar;
    private CCSeekBar brightnessSeekBar;
    private ImageView imageViewPlayPause;
    private ImageView imageViewVPlayPause;
    private ImageView imageViewRotateRight;
    private ImageView imageViewRotateLeft;
    private TextView textViewDuration;
    private TextView textViewCurrentDuration;
    private TextView textViewPlusTen;
    private TextView textViewMinusTen;
    private TextView textViewVideoTitle;
    private ConstraintLayout constraintLayout;
    private ConstraintLayout outerLayout;
    private String videoDuration;
    private String currentDuration;
    private Uri videoUri;
    private Context mContext;
    private int brightness;
    public static boolean isOuterLayoutVisible;
    private GestureDetector mDetector;
    private Handler handler =  new Handler();

    public CCVideoViewer(Context context) {
        super(context);
        mContext = context;
    }

    public CCVideoViewer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        InitializeControls(context);
    }

    private void InitializeControls(final Context mContext) {
        rootView = inflate(mContext, R.layout.ccvideo_viewer_layout, this);
        videoView = (VideoView) rootView.findViewById(R.id.videoView);
        seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        imageViewPlayPause = rootView.findViewById(R.id.imageViewPlayPause);
        textViewDuration = rootView.findViewById(R.id.textViewDuration);
        textViewCurrentDuration = rootView.findViewById(R.id.textViewCurrentDuration);
        imageViewVPlayPause = rootView.findViewById(R.id.imageViewVPlayPause);
        constraintLayout = rootView.findViewById(R.id.constraintLayout);
        outerLayout = rootView.findViewById(R.id.outerLayout);
        imageViewRotateLeft = rootView.findViewById(R.id.imageViewRotateLeft);
        imageViewRotateRight = rootView.findViewById(R.id.imageViewRotateRight);
        textViewPlusTen = rootView.findViewById(R.id.textViewPlusTen);
        textViewMinusTen = rootView.findViewById(R.id.textViewMinusTen);
        textViewVideoTitle = rootView.findViewById(R.id.textViewVideoTitle);
        brightnessSeekBar = (CCSeekBar) findViewById(R.id.brightnessSeekBar);


        brightness = getScreenBrightness(mContext);
        setScreenBrightness(brightness,mContext);
        brightnessSeekBar.setMax(225);
        brightnessSeekBar.setProgress(brightness);

        mDetector = new GestureDetector(mContext, new LayoutGestureDetector());
        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // pass the events to the gesture detector
                // a return value of true means the detector is handling it
                // a return value of false means the detector didn't
                // recognize the event
                return mDetector.onTouchEvent(event);

            }
        };
        outerLayout.setOnTouchListener(touchListener);

        imageViewPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    imageViewPlayPause.setBackgroundResource(R.drawable.play);
                    imageViewVPlayPause.setBackgroundResource(R.drawable.play);
                    videoView.pause();
                } else {
                    imageViewPlayPause.setBackgroundResource(R.drawable.pause);
                    imageViewVPlayPause.setBackgroundResource(R.drawable.pause);
                    videoView.start();
                    seekBar.postDelayed(onEverySecond, 1000);
                }
            }
        });
        imageViewVPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    imageViewVPlayPause.setBackgroundResource(R.drawable.play);
                    imageViewPlayPause.setBackgroundResource(R.drawable.play);
                    videoView.pause();
                } else {
                    imageViewVPlayPause.setBackgroundResource(R.drawable.pause);
                    imageViewPlayPause.setBackgroundResource(R.drawable.pause);
                    videoView.start();
                    seekBar.postDelayed(onEverySecond, 1000);
                }
            }
        });

        imageViewRotateRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastForward();
            }
        });

        imageViewRotateLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rewind();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    // this is when actually seekbar has been seeked to a new position
                    videoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        brightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setScreenBrightness(progress,mContext);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setMax(videoView.getDuration());
                seekBar.postDelayed(onEverySecond, 1000);
                mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {
                        if (percent < seekBar.getMax()) {
                            Log.d("OnBuffering", "onBufferingUpdate: updating");
                            seekBar.setSecondaryProgress(seekBar.getMax() * percent / 100);

                        }
                    }
                });
            }
        });

    }

    public void setVideoUrl(Uri uri) {
        videoUri = uri;
        videoView.setVideoURI(uri);
        textViewCurrentDuration.setText("00:00");
        textViewDuration.setText(getVideoDuration(videoView.getDuration()));
      //  textViewDuration.setText("00:00" + " / " + getVideoDuration(videoView.getDuration()));
    }

    public void startVideo() {
        imageViewPlayPause.setBackgroundResource(R.drawable.pause);
        imageViewVPlayPause.setBackgroundResource(R.drawable.pause);
        CCAnimation.FadeInAnimation(constraintLayout);
        AutoHideViewTimer();
        HideNavAndStatusBar(mContext);
        videoView.start();
    }

    public void setVideoTitle(String title) {
        textViewVideoTitle.setText(title);
    }

    private Runnable onEverySecond = new Runnable() {

        @Override
        public void run() {

            if (seekBar != null) {
                seekBar.setProgress(videoView.getCurrentPosition());

                videoDuration = getVideoDuration(videoView.getDuration());
                currentDuration = getCurrentDuration(videoView.getCurrentPosition());

                textViewCurrentDuration.setText(currentDuration);
                textViewDuration.setText(videoDuration);

                if(videoView.getCurrentPosition() <= 10000){
                    imageViewRotateLeft.setEnabled(false);
                    ImageViewCompat.setImageTintList(imageViewRotateLeft, ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.md_grey_500)));
                } else {
                    imageViewRotateLeft.setEnabled(true);
                    ImageViewCompat.setImageTintList(imageViewRotateLeft, ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.wight)));
                }

                if(videoView.getCurrentPosition() < videoView.getDuration()-10000){
                    imageViewRotateRight.setEnabled(true);
                    ImageViewCompat.setImageTintList(imageViewRotateRight, ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.wight)));
                }
                else {
                    imageViewRotateRight.setEnabled(false);
                    ImageViewCompat.setImageTintList(imageViewRotateRight, ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.md_grey_500)));
                }

            }

            if (videoView.isPlaying()) {
                seekBar.postDelayed(onEverySecond, 1000);
            }

        }
    };

    private void AutoHideViewTimer(){

        Runnable eventRunnable = new Runnable() {
            public void run() {
                // do something
                Log.d("CheckHandler", "CheckHandlerOne");
                HideView();
            }
        };
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(eventRunnable,10000);
    }

    private void HideView() {
        if (isOuterLayoutVisible) {
            if (constraintLayout.getVisibility() == VISIBLE) {
                constraintLayout.setVisibility(GONE);
            }
        }
    }

    private void HalfRotationRightAnimation() {
        float mAngleToRotate = 90f * 1; // rotate 12 rounds
        final RotateAnimation wheelRotation = new RotateAnimation(0.0f, mAngleToRotate, imageViewRotateRight.getWidth() / 2.0f, imageViewRotateRight.getHeight() / 2.0f);
        wheelRotation.setDuration(400); // rotate 12 rounds in 3 seconds
        wheelRotation.setInterpolator(mContext, android.R.interpolator.accelerate_decelerate);
        imageViewRotateRight.startAnimation(wheelRotation);

        wheelRotation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
                Log.d("RotationActivity", "Rotation started...");
            }

            public void onAnimationEnd(Animation animation) {
                Log.d("RotationActivity", "Rotation ended...");
                // HalfRotationRightReverseAnimation();
            }

            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void HalfRotationLeftAnimation() {
        float mAngleToRotate = -90f * 1; // rotate 12 rounds
        RotateAnimation wheelRotation = new RotateAnimation(0.0f, mAngleToRotate, imageViewRotateLeft.getWidth() / 2.0f, imageViewRotateRight.getHeight() / 2.0f);
        wheelRotation.setDuration(400); // rotate 12 rounds in 3 seconds
        wheelRotation.setInterpolator(mContext, android.R.interpolator.accelerate_decelerate);
        imageViewRotateLeft.startAnimation(wheelRotation);

        wheelRotation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
                Log.d("RotationActivity", "Rotation started...");
            }

            public void onAnimationEnd(Animation animation) {
                Log.d("RotationActivity", "Rotation ended...");
                imageViewRotateLeft.animate().translationX(0).translationY(0).setDuration(400);
            }

            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void AnimateNumberRight() {
        textViewPlusTen.setVisibility(VISIBLE);
        ObjectAnimator anim = ObjectAnimator.ofFloat(textViewPlusTen, "translationX", 40f);
        anim.setDuration(300);

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                textViewPlusTen.setVisibility(GONE);
                textViewPlusTen.animate().translationX(0).translationY(0).setDuration(2000);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
    }

    private void AnimateNumberLeft() {
        textViewMinusTen.setVisibility(VISIBLE);
        ObjectAnimator anim = ObjectAnimator.ofFloat(textViewMinusTen, "translationX", -40f);
        anim.setDuration(300);

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                textViewMinusTen.setVisibility(GONE);
                textViewMinusTen.animate().translationX(0).translationY(0).setDuration(2000);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();

    }

    private void Rewind() {

        if (videoView.getCurrentPosition() > 10000) {

            int skipToPrevious = videoView.getCurrentPosition();
            skipToPrevious = skipToPrevious - 10000;
            seekBar.setProgress(skipToPrevious);
            videoView.seekTo(skipToPrevious);
            videoDuration = getVideoDuration(videoView.getDuration());
            currentDuration = getCurrentDuration(skipToPrevious);
            textViewCurrentDuration.setText(currentDuration);
            textViewDuration.setText(videoDuration);
            //   textViewDuration.setText(currentDuration + " / " + videoDuration);
            HalfRotationLeftAnimation();
            AnimateNumberLeft();
        }

    }

    private void FastForward() {

        if(videoView.getCurrentPosition() < videoView.getDuration()-10000){

            int skipToNext = videoView.getCurrentPosition();
            skipToNext = skipToNext + 10000;
            seekBar.setProgress(skipToNext);
            videoView.seekTo(skipToNext);
            videoDuration = getVideoDuration(videoView.getDuration());
            currentDuration = getCurrentDuration(skipToNext);
            textViewCurrentDuration.setText(currentDuration);
            textViewDuration.setText(videoDuration);
            //   textViewDuration.setText(currentDuration + " / " + videoDuration);
            HalfRotationRightAnimation();
            AnimateNumberRight();
        }
        else {
            imageViewRotateRight.setEnabled(false);
            ImageViewCompat.setImageTintList(imageViewRotateRight, ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.md_grey_500)));
        }

    }
    /**
     * To watch video in Landscape mode pass true.
     */
    @SuppressLint("SourceLockedOrientationActivity")
    public void setOrientation(boolean value) {

        if (value) {
            ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private class LayoutGestureDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d("TAG", "onDown: ");

            // don't return false here or else none of the other
            // gestures will work
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.i("TAG", "onSingleTapConfirmed: ");

            if(constraintLayout.getVisibility() == VISIBLE){
                CCAnimation.FadeOutAnimation(constraintLayout);
                Log.d("TapView", "FadeOutAnimation: true" + " : isTransparentLayoutVisible = " + isOuterLayoutVisible);
            }
            else if(constraintLayout.getVisibility() !=VISIBLE){
               CCAnimation.FadeInAnimation(constraintLayout);
                Log.d("TapView", "FadeInAnimation:  true " + " : isTransparentLayoutVisible = " + isOuterLayoutVisible);
            }
            AutoHideViewTimer();
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.i("TAG", "onLongPress: ");
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.i("TAG", "onDoubleTap: ");
            final float x = e.getX();
            final float y = e.getY();

            float lastXAxis = x;
            float lastYAxis = y;

            int orientation = mContext.getResources().getConfiguration().orientation;

            switch (orientation) {
                case Configuration.ORIENTATION_PORTRAIT:

                    Log.d("ScreenOrientation", "Orientation: Portrait");
                    if ((lastXAxis >= 0 && lastXAxis <= 250) && (lastYAxis >= 350 && lastYAxis <= 850)) {
                        if (constraintLayout.getVisibility() != VISIBLE) {
                            CCAnimation.FadeInAnimation(constraintLayout);
                        }
                        Rewind();
                        AutoHideViewTimer();
                    } else if ((lastXAxis >= 550 && lastXAxis <= 900) && (lastYAxis >= 450 && lastYAxis <= 850)) {
                        if (constraintLayout.getVisibility() != VISIBLE) {
                            CCAnimation.FadeInAnimation(constraintLayout);
                        }
                        FastForward();
                        AutoHideViewTimer();
                    }
                    break;
                case Configuration.ORIENTATION_LANDSCAPE:
                    Log.d("ScreenOrientation", "Orientation: Landscape");

                    if ((lastXAxis >= 0 && lastXAxis <= 250) && (lastYAxis >= 0 && lastYAxis <= 800)) {
                        if (constraintLayout.getVisibility() != VISIBLE) {
                            CCAnimation.FadeInAnimation(constraintLayout);
                        }
                        Rewind();
                        AutoHideViewTimer();
                    } else if ((lastXAxis >= 1000 && lastXAxis <= 1500) && (lastYAxis >= 00 && lastYAxis <= 800)) {
                        if (constraintLayout.getVisibility() != VISIBLE) {
                            CCAnimation.FadeInAnimation(constraintLayout);
                        }
                        FastForward();
                        AutoHideViewTimer();
                    }

                    break;
            }

            Log.d("Coordinates", "onDoubleTap: " + lastXAxis + " : " + lastYAxis);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            Log.i("OnScroll", "onScroll: " + distanceX + " : " + distanceY);
            return false;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.d("TAG", "onFling: ");
            return false;
        }
    }
}
