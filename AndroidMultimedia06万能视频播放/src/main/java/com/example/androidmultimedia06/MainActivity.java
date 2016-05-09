package com.example.androidmultimedia06;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final VideoView ivv = (VideoView) findViewById(R.id.ivv);
        ivv.setVideoURI(Uri.parse("http://10.0.2.2/vd.mp4"));

        ivv.setMediaController(new MediaController(this));
        ivv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                ivv.start();
            }
        });
    }
}
