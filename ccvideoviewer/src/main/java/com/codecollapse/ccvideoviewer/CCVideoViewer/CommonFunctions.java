package com.codecollapse.ccvideoviewer.CCVideoViewer;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;

import java.util.concurrent.TimeUnit;

public class CommonFunctions {

    private static String hours;
    private static String minutes;
    private static String seconds;


    public static String getVideoDuration(int videoDuration) {
        String hms = null;

        hours = String.format("%02d", TimeUnit.MILLISECONDS.toHours(videoDuration));
        minutes = String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(videoDuration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(videoDuration)));
        seconds = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(videoDuration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(videoDuration)));

        if (hours.equals("00")) {
            hms = minutes + ":" + seconds;
        } else {
            hms = hours + ":" + minutes + ":" + seconds;
        }

        return hms;
    }

    public static String getCurrentDuration(int currentDuration) {
        String hms = null;
        hours = String.format("%02d", TimeUnit.MILLISECONDS.toHours(currentDuration));
        minutes = String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(currentDuration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(currentDuration)));
        seconds = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(currentDuration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentDuration)));

        if (hours.equals("00")) {
            hms = minutes + ":" + seconds;
        } else {
            hms = hours + ":" + minutes + ":" + seconds;
        }

        return hms;
    }

    public static void HideNavAndStatusBar(Context mContext) {
        View decorView = ((Activity) mContext).getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public static int getScreenBrightness(Context mContext) {

        int brightnessValue = Settings.System.getInt(
                mContext.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS,
                0
        );
        return brightnessValue;
    }
    public static void setScreenBrightness(int brightnessValue,Context mContext) {

        // Make sure brightness value between 0 to 255
        if (brightnessValue >= 0 && brightnessValue <= 255) {

            WindowManager.LayoutParams params = ((Activity)mContext).getWindow().getAttributes();
            //  range from 0 to 1, specify -1 for default brightness
            params.screenBrightness = brightnessValue/255f;
            ((Activity)mContext).getWindow().setAttributes(params);

        }
    }

}
