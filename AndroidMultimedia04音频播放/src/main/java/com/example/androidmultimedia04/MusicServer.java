package com.example.androidmultimedia04;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xwf on 16/5/9.
 * TODO:用于播放音乐的服务。
 */
public class MusicServer extends Service {

    private static final String TAG = "Hsia";
    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBind();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化MediaPlay，根据官网copy一份，改下path就行了
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(Environment.getExternalStorageDirectory().getPath()+"/Download/"+"zxxpg.mp3");
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void startMusicService() {
        mediaPlayer.start();
        mediaPlayer.setLooping(false);
        //更新进度条
        updateSeekBar();
    }

    private void updateSeekBar() {
        //获取音频的总文件长度
        final int duration = mediaPlayer.getDuration();
        //添加一个定时器，不断的更新SeekBar
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //获取当前播放进度
                int currentPosition = mediaPlayer.getCurrentPosition();
                //把数据发送出去
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putInt("duration",duration);
                bundle.putInt("currentPosition",currentPosition);
                message.setData(bundle);
                MainActivity.handler.sendMessage(message);
            }
        };
        timer.schedule(timerTask,10,1000);
    }

    private void stopMusicService() {
        mediaPlayer.pause();
    }

    private void restartMusicService() {
        mediaPlayer.start();
    }

    private void setMusicProgress(int progress){
        mediaPlayer.seekTo(progress);
    }

    //定义一个中间人对象，把想要暴露出去的方法暴露出去
    class MyBind extends Binder implements Iservices {

        @Override
        public void callStartMusic() {
            startMusicService();
        }

        @Override
        public void callStopMusic() {
            stopMusicService();
        }

        @Override
        public void callRestartMusic() {
            restartMusicService();
        }

        @Override
        public void callsetMusicProgress(int progress) {
            setMusicProgress(progress);
        }

    }

}
