package com.glenfordham.carportopener.carport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.glenfordham.carportopener.ErrorTag;

public class DoorServiceAutostarter extends BroadcastReceiver {
    /**
     * Called automatically on phone start-up
     * @param context : default application context
     * @param intent : the action to be executed
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // Check that the action is what we expect, in case other apps spoof the receiver
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            context.startForegroundService(new Intent(context, DoorStatusService.class));
        } else {
            Log.w(ErrorTag.BACKGROUND.get(), "Unexpected intent" + intent.getAction());
        }
    }
}
