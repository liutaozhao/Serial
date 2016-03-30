package cn.com.farsight.SerialService;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

public class SerialService {
	private static final String TAG = "SerialService";
	private boolean isInitOk = false;

	static {
		Log.d(TAG, "System.loadLibrary()");
		System.loadLibrary("serial_runtime");
	}   
	
	public SerialService(){	  
		Log.d(TAG, "SerialService()");
		isInitOk =  _init();
		Log.d(TAG, "_init()");
	}

	public String read() {
		Log.d(TAG, "read()");
		if(!isInitOk)
			return "Error:can't find serialdevice";
		byte[] data = new byte[128];
		int count = _read(data, 128);
		 
		String ret;
		try{
			ret = new String(data,0,count, "GBK");
		}catch(UnsupportedEncodingException e1) {
			return "Error:can't EncodingException";//null;
		}
		return ret;
	}

	public int  write(String s) {
		Log.d(TAG, "write()");
		int len;
		try{
			len = _write(s.getBytes("GBK"));
		}catch(UnsupportedEncodingException e1) {
			return -1;
		}
		return len;
	}

	private static native boolean _init();
	private static native int _read(byte[] data, int len);
	private static native int _write(byte[] data);

}
