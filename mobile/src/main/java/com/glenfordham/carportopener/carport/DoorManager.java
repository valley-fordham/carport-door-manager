package com.glenfordham.carportopener.carport;

import android.util.Log;

import com.glenfordham.carportopener.ErrorTag;
import com.glenfordham.carportopener.gui.MainScreen;
import com.glenfordham.carportopener.gui.SettingsScreen;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DoorManager {
    private Door door = Door.getInstance();
    private static DoorManager doorManager = null;
    private MainScreen mainScreen;
    private boolean statusCheckerIsRunning = false;

    public static DoorManager getInstance() {
        if (doorManager == null) {
            doorManager = new DoorManager();
        }
        return doorManager;
    }

    public void setHomeScreen(MainScreen input) {
        mainScreen = input;
    }

    /**
     * Status checker thread which will continually update the HomeScreen activity with the response
     */
    public void startStatusChecker() {
        Thread statusCheckerThread = new Thread(new Runnable() {
            public synchronized void run() {
                try {
                    while (mainScreen.statusCheckIsRunning()) {
                        Boolean doorOpen = door.isDoorOpen();
                        if (doorOpen == null) {
                            mainScreen.setDoorStatusToUnknown();
                        } else if (doorOpen) {
                            mainScreen.setDoorStatusToOpen();
                        } else {
                            mainScreen.setDoorStatusToClosed();
                        }
                        // Sleep for a time-period before checking status again
                        int sleepInterval;
                        try {
                            sleepInterval = Integer.parseInt(Objects.requireNonNull(MainScreen.preferences.getString(SettingsScreen.KEY_PREF_STATUS_INTERVAL, "5")));
                        } catch (Exception e) {
                            sleepInterval = 5;
                        }
                        try {
                            TimeUnit.MILLISECONDS.sleep(sleepInterval * 1000);
                        } catch (Exception e) {
                            Log.e(ErrorTag.HOMESCREEN.get(),"Error occurred when waiting to start next status check", e);
                        }
                    }
                } finally {
                    statusCheckerIsRunning = false;
                }
            }
        });
        if (!statusCheckerIsRunning) {
            statusCheckerIsRunning = true;
            statusCheckerThread.start();
        }
    }

    /**
     * Triggers a door open/close request and writes the response to the HomeScreen activity
     */
    public void toggleDoor() {
        if (door == null) {
            System.err.println("door not initialised");
            return;
        }
        Thread toggleDoorThread = new Thread(new Runnable() {
            public void run() {
                boolean triggerSucceeded = door.sendDoorTrigger();
                if (mainScreen != null) {
                    if (triggerSucceeded) {
                        mainScreen.setTriggerStatusToSuccess();
                    } else {
                        mainScreen.setTriggerStatusToFail();
                    }
                }
            }
        });
        toggleDoorThread.start();
        // Ensure door trigger thread has completed prior to returning control
        if (mainScreen == null) {
            try {
                toggleDoorThread.join();
            } catch (Exception e) {
                Log.e(ErrorTag.THREAD_PROTECT.get(),"Error waiting for thread to join", e);
            }
        }
    }

    /**
     * Makes a door status request and returns the response
     * @return : true/false, or null if an HTTP error occurs
     */
    synchronized Boolean isDoorOpen() {
        return door.isDoorOpen();
    }

    // Singleton
    private DoorManager() {
    }
}
