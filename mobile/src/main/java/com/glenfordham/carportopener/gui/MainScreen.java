package com.glenfordham.carportopener.gui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.preference.PreferenceManager;

import com.glenfordham.carportopener.R;
import com.glenfordham.carportopener.carport.DoorManager;
import com.glenfordham.carportopener.carport.DoorStatusService;

public class MainScreen extends Activity {

    private TextView txtDoorStatus;
    private TextView txtTriggerStatus;

    private DoorManager doorManager = DoorManager.getInstance();

    private boolean statusCheckRunning = true;

    // allow access of application settings to classes without a context
    public static SharedPreferences preferences;

    /*
     * Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up configuration
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        setContentView(R.layout.main_activity);
        txtTriggerStatus = findViewById(R.id.txt_trigger_status);
        txtDoorStatus = findViewById(R.id.txt_door_status);
        doorManager.setHomeScreen(this);

        Button btnStartStop = findViewById(R.id.btn_opendoor);
        btnStartStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setTriggerStatusToSending();
                doorManager.toggleDoor();
            }
        });

        ImageView btnSettings = findViewById(R.id.btn_settings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainScreen.this, SettingsScreen.class);
                startActivity(intent);
            }
        });

        startForegroundService(new Intent(this, DoorStatusService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        setTriggerStatusToClear();
        setStatusCheckRunning(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setStatusCheckRunning(true);
        doorManager.startStatusChecker();
    }

    public boolean statusCheckIsRunning() {
        return statusCheckRunning;
    }

    private void setStatusCheckRunning(boolean status) {
        statusCheckRunning = status;
    }


    /*
     * UI
     */
    public void setTriggerStatusToSending() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtTriggerStatus.setTextColor(Color.WHITE);
                txtTriggerStatus.setText(R.string.gui_sending);
            }
        });
    }

    public void setTriggerStatusToFail() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtTriggerStatus.setTextColor(Color.RED);
                txtTriggerStatus.setText(R.string.gui_fail);
            }
        });
    }

    public void setTriggerStatusToSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtTriggerStatus.setTextColor(Color.GREEN);
                txtTriggerStatus.setText(R.string.gui_sent);
            }
        });
    }

    public void setTriggerStatusToClear() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtTriggerStatus.setTextColor(Color.WHITE);
                txtTriggerStatus.setText("");
            }
        });
    }

    public void setDoorStatusToClosed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtDoorStatus.setTextColor(Color.WHITE);
                txtDoorStatus.setText(R.string.gui_closed);
            }
        });
    }

    public void setDoorStatusToOpen() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtDoorStatus.setTextColor(Color.WHITE);
                txtDoorStatus.setText(R.string.gui_open);
            }
        });
    }

    public void setDoorStatusToUnknown() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtDoorStatus.setTextColor(Color.RED);
                txtDoorStatus.setText(R.string.gui_unknown);
            }
        });
    }
}
