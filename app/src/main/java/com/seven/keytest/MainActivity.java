package com.seven.keytest;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button fatch;
    private static final int REQUEST_CODE = 1000;


    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        KeyHashUtility.getKeys(this);

        fatch = (Button) findViewById(R.id.fatch);

        fatch.setOnClickListener(new View.OnClickListener() {
            //  @SuppressLint("HardwareIds")
            @SuppressLint("HardwareIds")
            @Override
            public void onClick(View view) {
                getDeviceSuperInfo();


            }
        });


    }

    @SuppressLint("HardwareIds")
    private void getDeviceSuperInfo() {
        Log.i("TAG", "getDeviceSuperInfo");

        try {

            String s = "Debug-infos:";
            s += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
            s += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
            s += "\n Device: " + android.os.Build.DEVICE;
            s += "\n Model (and Product): " + android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")";

            s += "\n RELEASE: " + android.os.Build.VERSION.RELEASE;
            s += "\n BRAND: " + android.os.Build.BRAND;
            s += "\n DISPLAY: " + android.os.Build.DISPLAY;
            s += "\n CPU_ABI: " + android.os.Build.CPU_ABI;
            s += "\n CPU_ABI2: " + android.os.Build.CPU_ABI2;
            s += "\n UNKNOWN: " + android.os.Build.UNKNOWN;
            s += "\n HARDWARE: " + android.os.Build.HARDWARE;
            s += "\n Build ID: " + android.os.Build.ID;
            s += "\n MANUFACTURER: " + android.os.Build.MANUFACTURER;
            s += "\n SERIAL: " + android.os.Build.SERIAL;
            s += "\n USER: " + android.os.Build.USER;
            s += "\n HOST: " + android.os.Build.HOST;

            Log.i("TAG" + " | Device Info > ", s);

            // device id and display info show ho raha hai

            @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            Log.d("adroidId: ", androidId);
            DisplayManager displayManager = (DisplayManager) getApplicationContext().getSystemService(Context.DISPLAY_SERVICE);
            Display[] var1 = displayManager.getDisplays();
            Log.d("var1: ", Arrays.toString(var1));
            Display[] var2 = displayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
            Log.d("var2: ", Arrays.toString(var2));


        } catch (Exception e) {
            Log.e("TAG", "Error getting Device INFO");
        }

    }//end getDeviceSuperInfo



}