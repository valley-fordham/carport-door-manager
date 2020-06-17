package com.glenfordham.carportopener.carport;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.glenfordham.carportopener.ErrorTag;
import com.glenfordham.carportopener.gui.SettingsScreen;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

class Door {
    private static Door door = null;

    static Door getInstance() {
        if (door == null) {
            door = new Door();
        }
        return door;
    }

    /**
     * Sends a door status request on a separate thread, and returns the response once received
     * @return : returns response of true, false, or null if an HTTP error occurred
     */
    Boolean isDoorOpen(final Context context) {
        class DoorOpenChecker implements Runnable {
            private Boolean result = true;

            @Override
            public void run() {
                try {
                    result = makeHttpRequest(context, DoorRequestType.STATUS);
                } catch (Throwable t) {
                    Log.e(ErrorTag.THREAD_PROTECT.get(),"Unexpected error occurred in thread", t);
                }
            }

            private Boolean getResult() {
                return result;
            }
        }
        DoorOpenChecker doorOpenChecker = new DoorOpenChecker();
        Thread doorOpenThread = new Thread(doorOpenChecker);
        doorOpenThread.start();
        try {
            doorOpenThread.join();
        } catch (InterruptedException e) {
            Log.e(ErrorTag.THREAD_PROTECT.get(),"Error waiting for thread to join", e);
        }
        return doorOpenChecker.getResult();
    }

    /**
     * Sends a door trigger request on a separate thread, and returns the response once received
     * @return : returns true if successful request
     */
    boolean sendDoorTrigger(final Context context) {
        class DoorTrigger implements Runnable {
            private Boolean result = null;

            @Override
            public void run() {
                try {
                    result = makeHttpRequest(context, DoorRequestType.TRIGGER);
                } catch (Throwable t) {
                    Log.e(ErrorTag.THREAD_PROTECT.get(),"Unexpected error occurred in thread", t);
                }
            }
            private Boolean getResult() {
                return result;
            }
        }
        DoorTrigger doorTrigger = new DoorTrigger();
        Thread doorTriggerThread = new Thread(doorTrigger);
        doorTriggerThread.start();
        try {
            doorTriggerThread.join();
        } catch (InterruptedException e) {
            Log.e(ErrorTag.THREAD_PROTECT.get(),"Error waiting for thread to join", e);
        }
        return doorTrigger.getResult() != null;
    }

    /**
     * Makes the appropriate HTTP request based on the passed request type
     * @param doorRequestType : the type of HTTP request to make
     * @return : true/false response, null if HTTP request error
     */
    private Boolean makeHttpRequest(Context context, DoorRequestType doorRequestType) {
        HttpClient httpClient = new DefaultHttpClient();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String statusParam = preferences.getString(SettingsScreen.KEY_PREF_STATUS_PARAM, null);
        String triggerParam = preferences.getString(SettingsScreen.KEY_PREF_TRIGGER_PARAM, null);
        try {
            return httpClient.execute(new HttpGet(preferences.getString(SettingsScreen.KEY_PREF_HOST_URL, null) + (doorRequestType == DoorRequestType.STATUS ? statusParam : triggerParam)),
                    new BasicResponseHandler()).equals(preferences.getString(SettingsScreen.KEY_PREF_DOOR_RESPONSE, null));
        } catch (Exception e) {
            Log.e(ErrorTag.BACKGROUND.get(),"Error occurred when attempting HTTP request", e);
            return null;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    // Singleton
    private Door() {
    }
}