package cn.com.farsight.serviceplayphoto;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import cn.com.farsight.serviceplayphoto.SerialService;
/**
 * Created by zhaoliutao on 2016/10/11.
 */
public class MyService extends Service {
    private String tag = "MyService";
    private int startCount = 0;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final int  CLOSE_DIALOG=2000;
    private WindowManager wm;
    private LayoutInflater appInf;
    private View mView;
    private ImageView mImageView;
    private WindowManager.LayoutParams wmParams;
    private boolean StopThread = false;
    private boolean Running = false;
    //SerialService serialService = new SerialService();
    private Context mContext;

    private MyLocalReceiver myLocalReceiver; // 广播接收器
    private LocalBroadcastManager localBroadcastManager; // 当地广播

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class MyThread implements Runnable {
        public void run() {
            Running = true;
            try {
                Thread.sleep(100);
                while(!StopThread)
                {
                    Thread.sleep(4000);
                   //String data = serialService.read();
                    Log.e(tag, "serialService.read()");
                   // if(data != null)
                    {
                        //Log.e(tag, "serialService.read()="+data);

                        Intent intent = new Intent("cn.com.farsight.serviceplayphoto.intent");
                        intent.putExtra("serial","1");
                        localBroadcastManager.sendBroadcast(intent);
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onCreate() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter("cn.com.farsight.serviceplayphoto.intent"); // 过滤广播
        myLocalReceiver = new MyLocalReceiver();
        localBroadcastManager.registerReceiver(myLocalReceiver, intentFilter); // 注册广播
        if(!StopThread && !Running ) {
            Log.d(tag,"Create MyThread");
            mContext = this;
            wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            MyThread m = new MyThread();
            new Thread(m).start();
        }
        super.onCreate();
        Log.d(tag,"onCreate executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(tag, "onStartCommand executed");

            wmParams = new WindowManager.LayoutParams();
            wmParams.format = PixelFormat.RGBA_8888;
            wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            wmParams.gravity = Gravity.BOTTOM;
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

            appInf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = appInf.from(mContext).inflate(R.layout.loading_dialog, null);
            mImageView = (ImageView) mView.findViewById(R.id.img);

            mImageView.setImageResource(R.mipmap.ic_launcher);
            wm.addView(mView, wmParams);
            mHandler.postDelayed(mCloseDialog, CLOSE_DIALOG);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*还要增加销毁广播*/
        wm.removeView(mView);
        StopThread= true;
        Running = false;
        Log.d(tag,"onDestroy executed");
    }

    private Runnable mCloseDialog = new Runnable() {
        @Override
        public void run() {
            wm.removeView(mView);
        }
    };
}
