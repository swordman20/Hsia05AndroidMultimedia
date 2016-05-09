package com.example.androidmultimedia01;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = (ImageView) findViewById(R.id.iv);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.dog);//执行到这里就是OOM了
//        imageView.setImageBitmap(bitmap);
        //获取当前屏幕的宽高
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        Point point = new Point();
        //TODO 注意测量屏幕的宽高和下面测量图片的宽高，这种写法，以前没遇到过。
        /**
         * 基本顺序是 wm.getDefaultDisplay().getSize发现需要Point对象，就去new了
         * 但是new完之后，Point不能直接使用，例如，不能直接point.x。
         * 需要被wm装载到容器之后，才能执行point.x。不然获取不到屏幕的宽高。
         */
        wm.getDefaultDisplay().getSize(point);
        int x = point.x;
        int y = point.y;
        Toast.makeText(this, "当前屏幕分辨率"+x+"*"+y, Toast.LENGTH_SHORT).show();
        BitmapFactory.Options options = new BitmapFactory.Options();
        //通过bitmap获取的资源图片的信息，而不是加载图片资源。
        options.inJustDecodeBounds = true;
        //Options
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dog,options);
        //获取图片的宽高，方法同上
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        Toast.makeText(this, "获取到图片的分辨率："+outWidth+"*"+outHeight, Toast.LENGTH_SHORT).show();

        //计算缩放比例
        int scale = 1;
        int scaleX = outWidth / x;
        int scaleY = outHeight / y;
        /**
         * scaleX>=scleY    是判断宽高比例
         * scaleX>scale     是判断图片像素除以屏幕像素是否大于1，也就是不能比屏幕小才做缩放。
         */
        if (scaleX>=scaleY&&scaleX>scale) {
            scale = scaleX;
        }else if (scaleY >= scaleX && scaleY>scale) {
            scale = scaleY;
        }
        //inSampleSize 可以把它看成setSampleSize,就是设置缩放后的图片大小。
        options.inSampleSize = scale;
        //开始真正加载图片资源了，不是获取图片信息了。
        options.inJustDecodeBounds = false;
        Bitmap bit = BitmapFactory.decodeResource(getResources(), R.drawable.dog,options);
        int scaleWidth = options.outWidth;
        int scaleHeight = options.outHeight;
        Toast.makeText(this, "缩放后的图片分辨率："+scaleWidth+"*"+scaleHeight, Toast.LENGTH_SHORT).show();
        imageView.setImageBitmap(bit);
    }
}
