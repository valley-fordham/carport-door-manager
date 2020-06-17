package com.glenfordham.carportopener.carport;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.glenfordham.carportopener.ErrorTag;
import com.glenfordham.carportopener.R;
import com.glenfordham.carportopener.gui.SettingsScreen;
import com.glenfordham.carportopener.gui.notifications.Toaster;
import com.glenfordham.carportopener.gui.notifications.channels.Channel;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Door Status service, created when app is opened. Is triggered regularly to check door status
 */
public class DoorStatusService extends Service {

    private Handler mHandler = new Handler();
    private Timer mTimer = null;

    /**
     * Starts the scheduled task
     */
    private void startTimer() {
        stopTimer();
        mTimer = new Timer();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // Schedule task to run every few minutes
        int serviceInterval;
        try {
            serviceInterval = Integer.parseInt(Objects.requireNonNull(preferences.getString(SettingsScreen.KEY_PREF_SERVICE_INTERVAL, "15")));
        } catch (Exception e) {
            serviceInterval = 15;
        }
        mTimer.scheduleAtFixedRate(new StatusChecker(), 0, serviceInterval * 1000 * 60);
    }

    /**
     * Stops the current scheduled task
     */
    public void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    /**
     * Invoked on a schedule, retrieves the current door status and displays a notification if
     * an error occurs or the door is open
     */
    class StatusChecker extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Toaster toaster = new Toaster(getApplicationContext());
                        // Retrieve the door status, and display a notification if error response or door is open
                        Boolean doorOpen = DoorManager.getInstance().isDoorOpen(getApplicationContext());
                        if (doorOpen == null) {
                            toaster.makeToast(
                                    getResources().getString(R.string.toast_title),
                                    getResources().getString(R.string.toast_fail),
                                    Channel.DOOR_ERROR).sendNotification();
                        } else if (doorOpen) {
                            toaster.makeToast(
                                    getResources().getString(R.string.toast_title),
                                    getResources().getString(R.string.toast_open),
                                    Channel.DOOR_STATUS).sendNotification();
                        }
                    } catch (Throwable t) {
                        Log.e(ErrorTag.BACKGROUND.get(), "Error occurred in background service trigger", t);
                    }
                }
            });
        }
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(ErrorTag.BACKGROUND.get(), "onBind not implemented");
        return null;
    }

    /**
     * Keep service running in background after process is closed
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startForeground(Channel.FOREGROUND_SERVICE.getId(),
                new Toaster(getApplicationContext())
                        .makeToast(getResources().getString(R.string.toast_service),
                                "",
                                Channel.FOREGROUND_SERVICE,
                                true));
        if (mTimer == null) {
            startTimer();
        }
        return START_STICKY;
    }
}