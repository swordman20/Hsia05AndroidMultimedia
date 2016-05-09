#05Android学习从零单排之AndroidMultimedia（多媒体）
**读了那么多年的书让我明白一个道理。人要稳重，不要想到啥就做啥。做一行越久即使你不会，几年之后慢慢的你也会了，加上一点努力你或许你能成为别人眼中的专家。**
> 注：本章blog主要学习Android下图片、音频、视频的处理。

##Android下大图片的处理
在平时开发中，如果我们加载一个图片分辨率或者加载批量图片，超过了手机的屏幕分辨率或者内存空间时，就会导致加载图片不成功，并会报OOM异常，所以为了解决这个问题，我们就需要通过一定比例的缩放，方便图片加载。

![image](/Users/xwf/Desktop/1.png)

> 大图片缩放的步骤

1. 1、获取当前手机屏幕的宽和高（分辨率）。
2. 2、获取图片的宽和高。 
3. 3、用图片的分辨率除以当前屏幕的分辨率。
4. 4、加载缩放后的图片资源。

![image](/Users/xwf/Desktop/2.gif)

```
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
```

##Android对图片的修改操作
Android是不允许对图片的源文件进行直接修改的，必须要先复制改图片，才能对图片进行修改或涂鸦。

> 创建一个原图的副本（即复制图片）

![image](/Users/xwf/Desktop/3.gif)

```

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
```
> 对创建的图片进行“旋转”、“移动”等操作。

- 1、旋转  
	matrix.setRotate(30);
- 2、平移
    matrix.setTranslate(20, 0);
- 3、缩放
	matrix.setScale(0.5f, 0.5f);
- 4、倒影
	//对图片进行倒影效果
		matrix.setScale(1.0f, -1.0f);
		matrix.postTranslate(0, copyBitmap.getHeight());
- 5、翻转
	//对图片进行镜面
		matrix.setScale(-1.0f, 1.0f);
		matrix.postTranslate(copyBitmap.getWidth(), 0);

![image](/Users/xwf/Desktop/4.gif)

```
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
```

##Android版涂鸦板
通过Bitmap复制一张图片作为背景，然后获取到触摸事件，不断的在这张图上作画。
最后发送一条广播，通知图库更新。

![image](/Users/xwf/Desktop/5.gif)

```

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
```

##Android音频播放
> 相对来说Android下的音视频播放还是比较简单的，都被封装好在MediaPlay里面了，简单的Demo，几行代码就可以了。

```
public void startOpen(View view){
        //初始化MediaPlay，根据官网copy一份，改下path就行了
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource("http://10.0.2.2/xpg.mp3");
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
```

> 复杂点的，带服务后台播放加显示播放进度的Mp3播放器

![image](/Users/xwf/Desktop/6.gif)

- 混合方式开启音乐服务(就是为了调用服务里面的方法)

```
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

```
- 在服务里面初始化MediaPlay（Iservices接口就不贴出来了）

```
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
```
##Android视频播放
> VideoView实现（封装好的简单几行代码）

![image](/Users/xwf/Desktop/7.gif)

```
VideoView mVideo = (VideoView) findViewById(R.id.vv);
        mVideo.setVideoPath("http://10.0.2.2/vd.3gp");
        //添加一个进度条
        mVideo.setMediaController(new MediaController(this));
        mVideo.start();
```
>SurfaceView实现（VideoView是基于SurfaceView的封装）

```
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
```
####Android万能播放器
> 是通过使用vitamio第三方工程来实现的。官网
[www.vitamio.org](https://www.vitamio.org/docs/Tutorial/)

- 1、导入vitamio库工程到AndroidStudio中。
![image](/Users/xwf/Desktop/8.gif)
- 2、vitamio基本用法和VideoView差不多。
##Android拍照和摄像
>打开照相机

```
 //打开照相机
    public void openC(View view) {
        //创建意图对象
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath()+"/Download", "paizhao.png"))); // set the image file name
        //开启意图   获取结果
        startActivityForResult(intent, 0);
    }
```
>打开摄像机

```
//打开摄像机
    public void openS(View view) {
//创建意图对象
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath()+"/Download", "luxiang.3gp"))); // set the image file name
        //开启意图   获取结果
        startActivityForResult(intent, 0);
    }

```

**关于作者**
	- 个人网站：[北京互联科技](http://shop.zbj.com/14622657/)
	- Email：[xiaweifeng@live.cn](https://login.live.com)
	- 项目地址:[https://github.com/swordman20/Hsia05AndroidMultimedia.git](https://github.com/swordman20/Hsia05AndroidMultimedia.git)