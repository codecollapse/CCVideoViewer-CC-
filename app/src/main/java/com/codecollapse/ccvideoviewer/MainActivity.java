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

        // String path = "android.resource://" + getPackageName() + "/" + R.raw.rango;
      //  Uri uri = Uri.parse("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
        Uri uri = Uri.parse("https://video.fisb5-1.fna.fbcdn.net/v/t39.24130-2/10000000_291673448931331_1986520465044616897_n.mp4?_nc_cat=100&_nc_sid=985c63&efg=eyJ2ZW5jb2RlX3RhZyI6Im9lcF9oZCJ9&_nc_ohc=ONeR1ccytnwAX-nTzjz&_nc_ht=video.fisb5-1.fna&oh=97ad81362d4405f7d3e20636c727540b&oe=5F3036DA");
        ccVideoViewer = (CCVideoViewer) findViewById(R.id.ccVideoViewer);
        ccVideoViewer.setVideoUrl(uri);
        ccVideoViewer.setVideoTitle("Naat");
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
