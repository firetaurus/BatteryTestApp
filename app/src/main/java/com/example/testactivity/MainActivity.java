package com.example.testactivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//        Intent batteryStatus = context.registerReceiver(null, ifilter);
//
//        // Are we charging / charged?
//        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
//        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
//                status == BatteryManager.BATTERY_STATUS_FULL;
//
//        // How are we charging?
//        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
//        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
//        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
//
//        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
//        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
//
//        float batteryPct = level / (float)scale;

        BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
            int scale = -1;
            int level = -1;
            int voltage = -1;
            int temp = -1;

            @Override
            public void onReceive(Context context, Intent intent) {
                level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
                voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
                Log.e("BatteryManager", "level is " + level + "/" + scale + ", temp is " + temp + ", voltage is " + voltage);

                IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                Intent batteryStatus = context.registerReceiver(null, ifilter);

                // How are we charging?
                int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
                boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
                if (level > 85) {
                    Log.e("BatteryManager", "Livello della batteria buono");
//                    Toast.makeText(this, "Ciao a tutti!", Toast.LENGTH_SHORT).show();


//                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
//                    builder.setTitle("Attenzione!");
//                    builder.setMessage("Operazione non valida!");
//                    builder.show();
                    alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                    intent = new Intent(context, MainActivity.class);
                    alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

                    alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            SystemClock.elapsedRealtime() +
                                    60 * 1000, alarmIntent);
                } else if(level < 45){
                    Log.e("BatteryManager", "Livello della batteria non ottimale!");
                } else {
                    Log.e("BatteryManager", "Non saprei dirti!");
                    alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                    intent = new Intent(context, MainActivity.class);
                    alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

                    alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            SystemClock.elapsedRealtime() +
                                    60 * 1000, alarmIntent);
                }

                if (usbCharge) {
                    Log.e("BatteryManager", "USB Charge");
                } else if (acCharge) {
                    Log.e("BatteryManager", "AC Charge");
                } else {
                    Log.e("BatteryManager", "Non so come stai caricando");
                }
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);

    }

}
