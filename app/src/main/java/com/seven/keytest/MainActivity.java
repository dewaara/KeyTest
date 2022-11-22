package com.seven.keytest;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ToggleButton toggleButton;
    Button edtNumber;
    TextView statuss;

    private StringBuilder log;


    //   @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        KeyHashUtility.getKeys(this);

        try {
            logViewShow();
        } catch (IOException e) {
            e.printStackTrace();
        }



        ///////// for hideen apps//////////
       /*
       PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(this, com.seven.keytest.MainActivity.class);
        p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        */


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // restricted screenshot

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE); // screen short lock


        //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE); // isse activity screen par koi tach click nahi hoga



        toggleButton = (ToggleButton) findViewById(R.id.fatch);
        statuss = (TextView) findViewById(R.id.statuss);
        edtNumber = (Button) findViewById(R.id.edtNumber);
        edtNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UseNetworkData();
                VerifyApks();




//
//                PackageManager pm = getPackageManager();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                    pm.setAutoRevokeWhitelisted(getPackageName(), false);
//                }
//                boolean result;
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
//                    result = pm.isAutoRevokeWhitelisted();
//                    Log.d("remove", String.valueOf(result));
//                }

//                if (!applicationContext.packageManager.isAutoRevokeWhitelisted) {
//                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                    intent.data = Uri.fromParts("package", packageName, null)
//                    startActivity(intent)
//

            }


        });


        toggleButton.setOnClickListener(new View.OnClickListener() {
            //  @SuppressLint("HardwareIds")
            @SuppressLint("HardwareIds")
            @Override
            public void onClick(View view) {
                getDeviceSuperInfo();

                //   screenrecod(view);

                openActivity();

                checkUsb();
            }
        });


    }



    private void VerifyApks() {
      /*  SafetyNet.getClient(this)
                .listHarmfulApps()
                .addOnCompleteListener(new OnCompleteListener<SafetyNetApi.HarmfulAppsResponse>() {
                    @Override
                    public void onComplete(Task<SafetyNetApi.HarmfulAppsResponse> task) {
                        Log.d("ListOfHarmfulApps", "Received listHarmfulApps() result");

                        if (task.isSuccessful()) {
                            SafetyNetApi.HarmfulAppsResponse result = task.getResult();
                            long scanTimeMs = result.getLastScanTimeMs();

                            List<HarmfulAppsData> appList = result.getHarmfulAppsList();
                            if (appList.isEmpty()) {
                                Log.d("MY_APP_TAG", "There are no known " +
                                        "potentially harmful apps installed.");
                            } else {
                                Log.e("MY_APP_TAG",
                                        "Potentially harmful apps are installed!");

                                for (HarmfulAppsData harmfulApp : appList) {
                                    Log.e("MY_APP_TAG", "Information about a harmful app:");
                                    Log.e("MY_APP_TAG",
                                            "  APK: " + harmfulApp.apkPackageName);
                                    Log.e("MY_APP_TAG",
                                            "  SHA-256: " + harmfulApp.apkSha256);

                                    // Categories are defined in VerifyAppsConstants.
                                    Log.e("MY_APP_TAG",
                                            "  Category: " + harmfulApp.apkCategory);
                                }
                            }
                        } else {
                            Log.d("MY_APP_TAG", "An error occurred. " +
                                    "Call isVerifyAppsEnabled() to ensure " +
                                    "that the user has consented.");
                        }
                    }
                });*/

        SafetyNet.getClient(this)
                .isVerifyAppsEnabled()
                .addOnCompleteListener(new OnCompleteListener<SafetyNetApi.VerifyAppsUserResponse>() {
                    @Override
                    public void onComplete(Task<SafetyNetApi.VerifyAppsUserResponse> task) {
                        if (task.isSuccessful()) {
                            SafetyNetApi.VerifyAppsUserResponse result = task.getResult();
                            if (!result.isVerifyAppsEnabled()) {

                                int verifierInt = -1;
                                if (Build.VERSION.SDK_INT >= 23) {
                                    verifierInt = Settings.Global.getInt(getApplicationContext().getContentResolver(), "package_verifier_enable", 1);
                                } else if (Build.VERSION.SDK_INT >= 23) {
                                    verifierInt = Settings.Secure.getInt(getApplicationContext().getContentResolver(), "verifier_enable", 1);
                                } else {
                                    // No package verification option before API Level 14
                                }
                                boolean isVerifyAppEnabled = verifierInt == 1;

                                Log.d("MY_APP_TAG", "The Verify Apps feature is enabled.");
                            } else {
                                Log.d("MY_APP_TAG", "The Verify Apps feature is disabled.");
                            }
                        } else {
                            Log.e("MY_APP_TAG", "A general error occurred.");
                        }
                    }
                });
    }

    private void UseNetworkData() {
        printAllDataUsage();
    }


    private void checkUsb() {

        if (Settings.Secure.getInt(getApplicationContext().getContentResolver(), Settings.Secure.DEVELOPMENT_SETTINGS_ENABLED, 0) == 1) {


            Toast.makeText(this, "developer mode on", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(this, "developer mode off", Toast.LENGTH_SHORT).show();
//            int adb = 1;
//            Settings.Secure.putInt(getApplicationContext().getContentResolver(),
//                    Settings.Secure.DEVELOPMENT_SETTINGS_ENABLED, adb);
        }

//        int adb = Settings.Secure.getInt(getApplicationContext().getContentResolver(),
//                Settings.Secure.DEVELOPMENT_SETTINGS_ENABLED, 0);
//        // toggle the USB debugging setting
////        adb = 1;
////        Settings.Secure.putInt(getApplicationContext().getContentResolver(),
////                Settings.Secure.ADB_ENABLED, adb);

    }


    private void openActivity() {
//        Intent send = new Intent(MainActivity.this, ContactActivity.class);
//        startActivity(send);
        Intent send = new Intent(MainActivity.this, AppTimeUseActivity.class);
        startActivity(send);
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


////  // clearing app data but not work of proper well
//    private void clearAppData() {
//        try {
//            // clearing app data
//            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
//                ((ActivityManager)getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
//            } else {
//                String packageName = getApplicationContext().getPackageName();
//                Runtime runtime = Runtime.getRuntime();
//                runtime.exec("pm clear "+packageName);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


//////////////////

    //when using NetworkStatsManager you need the subscriber id
    @SuppressLint("HardwareIds")
    private String getSubscriberId(Context context, int networkType) {
        if (ConnectivityManager.TYPE_MOBILE == networkType) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSubscriberId();
        }
        return "";
    }


    // to get mobile data recived
    public long getPackageRxBytesMobile(Context context, NetworkStatsManager networkStatsManager, int packageUid) {
        NetworkStats networkStats = null;
        long totalRev = 0;
        networkStats = networkStatsManager.queryDetailsForUid(
                ConnectivityManager.TYPE_MOBILE,
                "",
                0,
//                getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),

                System.currentTimeMillis(),
                packageUid);
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        networkStats.getNextBucket(bucket);
        networkStats.getNextBucket(bucket);
        totalRev = totalRev + bucket.getRxBytes();
        //retrieve data
        double totalKBRx = totalRev / 1024;
        if (totalKBRx < 1024)
            Log.d("Mobile_Retrieve", totalKBRx + "KB");

        double totalMBRx = totalKBRx / 1024;
        if (totalMBRx < 1024)
            Log.d("Mobile_Retrieve", totalMBRx + "MB");
        double totalGBRx = totalMBRx / 1024;
        if (totalGBRx < 1024)
            Log.d("Mobile_Retrieve", totalGBRx + "GB");

        return totalRev;
    }


    // to get mobile data transmitted
    public long getPackageTxBytesMobile(Context context, NetworkStatsManager networkStatsManager, int packageUid) {
        NetworkStats networkStats = null;
        long totalTef = 0;
        networkStats = networkStatsManager.queryDetailsForUid(
                ConnectivityManager.TYPE_MOBILE,
                "",
                0,
//                getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),

                System.currentTimeMillis(),
                packageUid);
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        networkStats.getNextBucket(bucket);

        //transmitted bytes
        double totalKBTx = totalTef / 1024;
        if (totalKBTx < 1024)
            Log.d("Mobile_Transfer", totalKBTx + "KB");
        double totalMBTx = totalKBTx / 1024;
        if (totalMBTx < 1024)
            Log.d("Mobile_Transfer", totalMBTx + "MB");
        double totalGBTx = totalMBTx / 1024;
        if (totalGBTx < 1024)
            Log.d("Mobile_Transfer", totalGBTx + "GB");

        return totalTef;
    }


    // to get wifi data received
    public long getPackageRxBytesWifi(NetworkStatsManager networkStatsManager, int packageUid) {
        NetworkStats networkStats = null;
        long totalRev = 0;
        networkStats = networkStatsManager.queryDetailsForUid(
                ConnectivityManager.TYPE_WIFI,
                "",
                0,
                System.currentTimeMillis(),
                packageUid);
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        networkStats.getNextBucket(bucket);

        totalRev = totalRev + bucket.getRxBytes();
        //retrieve data
        double totalKBRx = totalRev / 1024;
        if (totalKBRx < 1024)
            Log.d("Wifi_Retrieve", totalKBRx + "KB");

        double totalMBRx = totalKBRx / 1024;
        if (totalMBRx < 1024)
            Log.d("Wifi_Retrieve", totalMBRx + "MB");
        double totalGBRx = totalMBRx / 1024;
        if (totalGBRx < 1024)
            Log.d("Wifi_Retrieve", totalGBRx + "GB");

        return totalRev;
    }


    // to get wifi data transmitted
    public long getPackageTxBytesWifi(NetworkStatsManager networkStatsManager, int packageUid) {
        NetworkStats networkStats = null;
        long totalTef = 0;
        networkStats = networkStatsManager.queryDetailsForUid(
                ConnectivityManager.TYPE_WIFI,
                "",
                0,
                System.currentTimeMillis(),
                packageUid);
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        networkStats.getNextBucket(bucket);


        totalTef = totalTef + bucket.getTxBytes();

        //transmitted bytes
        double totalKBTx = totalTef / 1024;
        if (totalKBTx < 1024)
            Log.d("Wifi_Transfer", totalKBTx + "KB");
        double totalMBTx = totalKBTx / 1024;
        if (totalMBTx < 1024)
            Log.d("Wifi_Transfer", totalMBTx + "MB");
        double totalGBTx = totalMBTx / 1024;
        if (totalGBTx < 1024)
            Log.d("Wifi_Transfer", totalGBTx + "GB");

        return totalTef;

    }


    // print to log all the data usage value per application
    public void printAllDataUsage() {
        PackageManager pm = getPackageManager();
        // get all the applications in the phone
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        NetworkStatsManager networkStatsManager = (NetworkStatsManager) getApplicationContext().getSystemService(Context.NETWORK_STATS_SERVICE);


        for (ApplicationInfo packageInfo : packages) {
            // Log.d("MYLOG_uid", String.valueOf(packageInfo.uid));
            //   Log.d("MYLOG_name", String.valueOf(packageInfo.name));
            Log.d("MYLOG_package", String.valueOf(packageInfo.packageName));

            // get data usage from trafficStats
//            Log.d("MYLOG_rxUid", String.valueOf(TrafficStats.getUidRxBytes(packageInfo.uid)));
//            Log.d("MYLOG_txUid", String.valueOf(TrafficStats.getUidTxBytes(packageInfo.uid)));

            // get data usage from networkStatsManager using mobile
            Log.d("TOTAL_rxMobile", String.valueOf(getPackageRxBytesMobile(this, networkStatsManager, packageInfo.uid)));
            Log.d("TOTAL_txMobile", String.valueOf(getPackageTxBytesMobile(this, networkStatsManager, packageInfo.uid)));

            // get data usage from networkStatsManager using wifi

            Log.d("TOTAL_rxWifi", String.valueOf(getPackageRxBytesWifi(networkStatsManager, packageInfo.uid)));
            Log.d("TOTAL_txWifi", String.valueOf(getPackageTxBytesWifi(networkStatsManager, packageInfo.uid)));

        }
    }




    private void logViewShow() throws IOException {
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            log=new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
            }
//            TextView tv = (TextView)findViewById(R.id.statuss);
//            tv.setText(log.toString());
            Log.d("ShowMe", String.valueOf(log));
        } catch (IOException e) {
        }

        final String logString = new String(log.toString());

        //create text file in SDCard
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/Download/MY_MyLocat/");
        dir.createNewFile(); //
        dir.mkdirs();
        File file = new File(dir, "logcat.txt");

        try {
            //to write logcat in text file
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);

            // Write the string to the file
            osw.write(logString);
            osw.flush();
            osw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}