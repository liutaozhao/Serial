package cn.com.farsight.serviceplayphoto;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

/**
 * Created by zhaoliutao on 2016/11/28.
 */

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

    public static String bytesToHexString(byte[] src,int length){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < length; i++) {

            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);

            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
            stringBuilder.append("  ");
        }
        return stringBuilder.toString();
    }

    public String read() {
        Log.d(TAG, "read()");
        if(!isInitOk)
            return "Error:can't find serialdevice";
        byte[] data = new byte[12];
        byte[] head = new byte[1];
        byte[] result = new byte[13];
        int count = _read(head, 1);
        if(count == 1)
        {
			/*try{
				Thread.currentThread().sleep(1000);
			}catch(UnsupportedEncodingException e1){

			}*/
            try {
                //Thread.sleep(1000);
                Thread.currentThread().sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            count = _read(data, 11);
            count++;
        }
        for(int i = 0; i < count; i++)
        {
            if(i == 0)
                result[i] = head[0];
            else
                result[i] = data[i-1];
        }
        String ret;
        ret = bytesToHexString(result,count);
		/*try{
			ret = new String(data, 0, count, "GBK");
		}catch(UnsupportedEncodingException e1) {
			return "Error:can't EncodingException";//null;
		}*/
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

