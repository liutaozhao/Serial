package cn.com.farsight.SerialDemo;
import cn.com.farsight.SerialService.SerialService;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.os.Message;
import android.os.Handler;
import android.os.Looper;


public class MainActivity extends Activity {
	private final static String TAG = "SerialService";
	/** Called when the activity is first created. */
	EditText mEditText;
	SerialService serialService = new SerialService();
	String data;
	MyHandler myHandler; 
	Bundle b = new Bundle();
	private boolean StopThread = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//Intent intent = new Intent(getApplicationContext(), PlayService.class);  
		//startService(intent);  
		// 当创建一个新的Handler实例时，它会绑定到当前线程和消息的队列中，开始分发数据
		myHandler = new MyHandler(); 
		 
		mEditText = (EditText)this.findViewById(R.id.et);
		Button button = (Button)this.findViewById(R.id.button1);

		button.setOnClickListener(new OnClickListener()//
		{

			public void onClick(View v) 
			{
				Log.d(TAG, "Button clickd!!");
				//Intent intent = new Intent(getApplicationContext(), PlayService.class);   
				//stopService(intent);
				
				//data = serialService.read();
				
				StopThread= true;
				
				if(data != null)
				{
				    System.out.println(data);
				    updateView(data);
				    //serialService.write(data);
				} 
				else 
				{
				    System.out.println("read return null");
				}
					
				
			};
		});
		
		MyThread m = new MyThread();
		new Thread(m).start(); 
	}
	
	    /** 
    * 接受消息，处理消息 ，此Handler会与当前主线程一块运行 
    * */ 
 
    class MyHandler extends Handler { 
        public MyHandler() { 
        } 
 
        public MyHandler(Looper L) { 
            super(L); 
        } 
 
        // 子类必须重写此方法，接受数据 
        @Override 
        public void handleMessage(Message msg) { 
            // TODO Auto-generated method stub 
           
            //super.handleMessage(msg); 
            //Bundle b = msg.getData(); 
			//int age = b.getInt("age"); 
			String data = b.getString("name");
			
			Log.d(TAG, "handleMessage..." + data); 
			
            if(data != null)
			{
				Log.e(TAG, "serialService.read()="+data); 
				updateView(data);
			}
 
        } 
    } 
	
	 class MyThread implements Runnable { 
        public void run() { 
 
            try {
					Thread.sleep(100);
					while(!StopThread)
					{
						//sleep(200);
						data = serialService.read();
						Log.e(TAG, "serialService.read()"); 
						if(data != null)
						{
							 Log.e(TAG, "serialService.read()="+data); 
							 Message msg = myHandler.obtainMessage();  
							 //b.putInt("age", 20);  
							 b.putString("name", data);
							 msg.setData(b);
							 //msg.sendToTarget();
							  MainActivity.this.myHandler.sendMessage(msg); 
						}
					}
				} catch (Exception e) 
						{
							e.printStackTrace();
						}
 
			} 
    } 
	
	public void updateView(String data){
		Log.d(TAG, "updateView()");
		mEditText.setText(data);	
	}
}
