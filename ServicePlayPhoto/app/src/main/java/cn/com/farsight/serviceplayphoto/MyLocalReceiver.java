package cn.com.farsight.serviceplayphoto;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyLocalReceiver extends BroadcastReceiver {
    public MyLocalReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
            if(intent.getStringExtra("serial").equals("1")){
                Intent newIntent= new Intent(context,MyService.class);
                context.startService(newIntent);
            }

        // Toast.makeText(context, "广播接收的内容:" + intent.getStringExtra("content"), Toast.LENGTH_LONG).show();
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
