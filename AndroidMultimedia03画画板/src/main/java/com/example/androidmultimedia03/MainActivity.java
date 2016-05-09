package com.example.androidmultimedia03;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private ImageView iv;
    private Bitmap newBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.iv);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bb);
//        iv.setImageBitmap(bitmap);
        //复制了一直带有属性的画板
        newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        //准备画布
        final Canvas canvas = new Canvas(newBitmap);
        final Paint paint = new Paint();
        canvas.drawBitmap(bitmap,new Matrix(),paint);
        //在画布上画一条线
//        canvas.drawLine(10,10,200,100,paint);
        iv.setOnTouchListener(new View.OnTouchListener() {
            int downX = 0;
            int downY = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = (int) event.getX();
                        downY = (int) event.getY();
//                        Toast.makeText(MainActivity.this, "当前按下的坐标X:"+downX+"Y:"+downY, Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int moveX = (int) event.getX();
                        int moveY = (int) event.getY();
                        paint.setStrokeWidth(3);
//                        System.out.println("当前移动坐标X:" + moveX + "Y:" + moveY);
                        canvas.drawLine(downX,downY,moveX,moveY,paint);
                        iv.setImageBitmap(newBitmap);
                        //重新计算坐标
                        downX = (int) event.getX();
                        downY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_UP:

                        break;
                }
                return true;
            }
        });
        iv.setImageBitmap(newBitmap);

    }
    public void saveImage(View view){
        String path = Environment.getExternalStorageDirectory().getPath() + "/Download/" + "哈哈.png";
//        String path = getFilesDir().getPath() + "/哈哈.png";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            boolean compress = newBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            if (compress) {
                //发送一条广播，更新图库（已失效。）
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
//                intent.setData(Uri.parse("file://"+getFilesDir()));
//                sendBroadcast(intent);
                //发送广播，通知图库更新（扫描文件）。
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory().getPath() + "/Download/")));
                fos.close();
                Toast.makeText(this, "涂鸦保存成功", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
