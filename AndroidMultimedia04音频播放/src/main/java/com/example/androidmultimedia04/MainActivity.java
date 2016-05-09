package com.example.androidmultimedia04;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Hsia";
    private Iservices iservices;
    private static SeekBar mSB;
    private MyConn myConn;
    public static Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            //总大小
            duration = data.getInt("duration");
            //当前位置
            currentPosition = data.getInt("currentPosition");

            mSB.setMax(duration);
            mSB.setProgress(currentPosition);
        }
    };
    private static int currentPosition;
    private static int duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        mSB = ((SeekBar) findViewById(R.id.sb));
//        mSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            //当SeekBar进度改变时
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                int position = seekBar.getProgress();  //获取当前播放的进度
//                //调用服务里面的方法更新更新到制定位置
//                iservices.callsetMusicProgress(position);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });

        mSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //停止拖动回调
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int position = seekBar.getProgress();  //获取当前播放的进度
                iservices.callsetMusicProgress(position);

            }
            //开始拖动
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            //进度发生了改变
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

            }
        });

        Intent intent = new Intent(this,MusicServer.class);
        startService(intent);
        myConn = new MyConn();
        bindService(intent, myConn,BIND_AUTO_CREATE);
    }
    class MyConn implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: ");
            iservices = (Iservices)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    //开始
    public void startMusic(View view) {
        iservices.callStartMusic();
    }

    //暂停
    public void stopMusic(View view) {
        iservices.callStopMusic();
    }

    //重新播放
    public void restartMusic(View view) {
        iservices.callRestartMusic();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(myConn);
    }
}
