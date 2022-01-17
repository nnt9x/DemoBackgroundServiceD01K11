package com.bkacad.nnt.demobackgroundserviced01k11;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyService extends Service {
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;
    private int number;

    public MyService() {
    }

    private void sendData(int number){
        Intent i = new Intent("FROM_SERVICE");
        i.putExtra("number_service", number );
        sendBroadcast(i);
    }

    private void countDown(int max){
        // Đếm ngược về 0
        for(int i = max ; i >= 0 ; i--){
            try {
                sendData(i);
                Log.d("Count", "count "+i);
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Đăng kí broadcast receiver
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()){
                    case "FROM_MAIN_ACTIVITY":
                        // Xử lý
                        number = intent.getIntExtra("number", 0);
                        Log.d("Number", ""+number);
                        countDown(number);
                        break;
                }

            }
        };
        // intent filter
        intentFilter = new IntentFilter("FROM_MAIN_ACTIVITY");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
        // Huỷ đăng kí

    }
}