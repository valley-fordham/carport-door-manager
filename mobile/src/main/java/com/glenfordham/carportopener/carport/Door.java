package com.glenfordham.carportopener.carport;

import android.util.Log;

import com.glenfordham.carportopener.ErrorTag;
import com.glenfordham.carportopener.gui.MainScreen;
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
    Boolean isDoorOpen() {
        class DoorOpenChecker implements Runnable {
            private Boolean result = true;

            @Override
            public void run() {
                try {
                    result = makeHttpRequest(MainScreen.preferences.getString(SettingsScreen.KEY_PREF_STATUS_PARAM, null));
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
    boolean sendDoorTrigger() {
        class DoorTrigger implements Runnable {
            private Boolean result = null;

            @Override
            public void run() {
                try {
                    result = makeHttpRequest(MainScreen.preferences.getString(SettingsScreen.KEY_PREF_TRIGGER_PARAM, ""));
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
    private Boolean makeHttpRequest(String doorRequestType) {
        HttpClient httpClient = new DefaultHttpClient();
        try {
            return httpClient.execute(new HttpGet(MainScreen.preferences.getString(SettingsScreen.KEY_PREF_HOST_URL, "") + doorRequestType),
                    new BasicResponseHandler()).equals(MainScreen.preferences.getString(SettingsScreen.KEY_PREF_DOOR_RESPONSE, "-99"));
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