package cn.com.farsight.SerialDemo;
import cn.com.farsight.SerialService.SerialService;
import java.io.IOException;
import android.app.Service;
import android.content.Intent;

import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
public class PlayService extends Service {   
	String TAG = "SerialService";    
 	SerialService serialService = new SerialService();
	String data = null;
	private boolean StopThread;
	public PlayService()
	{
		StopThread = false;
	}
	public void stopThread()
	{
		StopThread = true;
	}
	public boolean getThreadState()
	{
		return StopThread;
	}
 @Override    
 public IBinder onBind(Intent intent) {       
 // TODO Auto-generated method stub       
 return null;    
 }    
 @Override    public void onCreate() 
 {        
 // TODO Auto-generated method stub        
 super.onCreate();        
 Toast.makeText(this, "Play Service Created", Toast.LENGTH_LONG).show();       
 Log.e(TAG, "ServiceonCreate");       
}

@Override
 public void onStart(Intent intent, int startId) {
 super.onStart(intent, startId);
 Toast.makeText(this, "Play Service onStart", Toast.LENGTH_LONG).show();
 Log.e(TAG, "ServiceonStart");
 final Thread thread = new Thread() {
 @Override
 public void run() {
 try {
		while(!StopThread)
		{
			//sleep(200);
			data = serialService.read();
			Log.e(TAG, "serialService.read()"); 
			if(data != null)
			{
				Log.e(TAG, "serialService.read()="+data); 
			}
		}
     } catch (Exception e) 
	 {
        e.printStackTrace();
     }
   }
   };
  thread.start();
  }
 
 @Override
  public void onDestroy() {

 // TODO Auto-generated method stub   
	super.onDestroy();
	StopThread = true;
	Toast.makeText(this, "Play Service Stopped", Toast.LENGTH_LONG).show();
	Log.v(TAG, "ServiconDestroy");
}

} 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
