package com.codecollapse.ccvideoviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.codecollapse.ccvideoviewer.CCVideoViewer.CCVideoViewer;

public class MainActivity extends AppCompatActivity {

    private CCVideoViewer ccVideoViewer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         String path = "android.resource://" + getPackageName() + "/" + R.raw.rango;
      //  Uri uri = Uri.parse("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
        Uri uri = Uri.parse("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4");
        ccVideoViewer = (CCVideoViewer) findViewById(R.id.ccVideoViewer);
        ccVideoViewer.setVideoUrl(Uri.parse(path));
        ccVideoViewer.setVideoTitle("Rango");
        // ccVideoViewer.setOrientation(true);
        ccVideoViewer.startVideo();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("ScreenChange", "onConfigurationChanged: Landscape");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d("ScreenChange", "onConfigurationChanged: Portrait");
        }
    }
}
