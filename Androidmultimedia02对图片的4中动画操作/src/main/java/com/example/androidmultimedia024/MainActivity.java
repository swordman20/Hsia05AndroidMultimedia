package com.example.androidmultimedia024;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.iv1)
    ImageView iv1;
    @Bind(R.id.iv2)
    ImageView iv2;
    @Bind(R.id.btn_rotate)
    Button btnRotate;
    @Bind(R.id.btn_move)
    Button btnMove;
    @Bind(R.id.btn_overturn)
    Button btnOverturn;
    @Bind(R.id.btn_reflection)
    Button btnReflection;
    private Matrix matrix;
    private Bitmap bitmap;
    private Canvas canvas;
    private Bitmap resource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        btnRotate.setOnClickListener(this);
        btnMove.setOnClickListener(this);
        btnOverturn.setOnClickListener(this);
        btnReflection.setOnClickListener(this);
        resource = BitmapFactory.decodeResource(getResources(), R.drawable.meinv);
        iv1.setImageBitmap(resource);
        //给第二个ImageView设置和第一个一样的图片
        bitmap = Bitmap.createBitmap(resource.getWidth()*2, resource.getHeight()*2, resource.getConfig());
        //准备画布
        canvas = new Canvas(bitmap);
        //开始作画，参照第一张图片 Paint可以理解为笔
        matrix = new Matrix();
//        matrix.setRotate(30, bitmap.getWidth()/2, bitmap.getHeight()/2);
        canvas.drawBitmap(resource, matrix, new Paint());
        iv2.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rotate:
                matrix.setRotate(30,bitmap.getWidth()/2,bitmap.getHeight()/2);
                canvas.drawBitmap(resource, matrix, new Paint());
                iv2.setImageBitmap(bitmap);
                break;
            case R.id.btn_move:
                matrix.setTranslate(20,20);
                canvas.drawBitmap(resource, matrix, new Paint());
                iv2.setImageBitmap(bitmap);
                break;
            case R.id.btn_overturn:
                //对图片进行镜面
                matrix.setScale(-1.0f, 1.0f);
                matrix.postTranslate(bitmap.getWidth(), 0);
                canvas.drawBitmap(resource, matrix, new Paint());
                iv2.setImageBitmap(bitmap);
                break;
            case R.id.btn_reflection:
                //对图片进行倒影效果
                matrix.setScale(1.0f, -1.0f);
                matrix.postTranslate(0, bitmap.getHeight());
                canvas.drawBitmap(resource, matrix, new Paint());
                iv2.setImageBitmap(bitmap);
                break;
        }
    }
}
