package cn.com.farsight.serviceplayphoto;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootComepletedReceiver extends BroadcastReceiver {
    public BootComepletedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Intent newIntent= new Intent(context,MyService.class);
            context.startService(newIntent);
        }
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
