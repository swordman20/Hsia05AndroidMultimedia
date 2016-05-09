package com.example.androidmultimedia05;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SurfaceView mSV = (SurfaceView) findViewById(R.id.sv);
        final SurfaceHolder holder = mSV.getHolder();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    final MediaPlayer mediaPlayer = new MediaPlayer();
                    //设置播放数据源
                    mediaPlayer.setDataSource("http://10.0.2.2/vd.mp4");
                    //准备播放
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setDisplay(holder);
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
