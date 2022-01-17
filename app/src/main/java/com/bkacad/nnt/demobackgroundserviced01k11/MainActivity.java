package com.bkacad.nnt.demobackgroundserviced01k11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView tvNumber;
    private EditText edtNumber;
    private Button btnService, btnSend;
//    private MyService myService;
    private boolean isRunning = false;
    private BroadcastReceiver receiver;
    private IntentFilter intentFilter;


    private void initUI(){
        tvNumber = findViewById(R.id.tv_main_number);
        edtNumber = findViewById(R.id.edt_main_number);
        btnService = findViewById(R.id.btn_main_service);
        btnSend = findViewById(R.id.btn_main_send);
    }

    private void handleOnClick(){
        btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRunning) {
                    startService(new Intent(MainActivity.this, MyService.class));
                    btnService.setText("Stop");
                    isRunning = true;
                }
                else{
                    stopService(new Intent(MainActivity.this, MyService.class));
                    btnService.setText("Start");
                    isRunning = false;
                }
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("FROM_MAIN_ACTIVITY");
                intent.putExtra("number", Integer.parseInt(edtNumber.getText().toString()));
                sendBroadcast(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        handleOnClick();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()){
                    case "FROM_SERVICE":
                        Toast.makeText(MainActivity.this, "number" + intent.getIntExtra("number_service", -1), Toast.LENGTH_SHORT).show();
//                        tvNumber.setText(String.valueOf(intent.getIntExtra("number", -1)));
                        break;
                }
            }
        };
        intentFilter = new IntentFilter("FROM_SERVICE");

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}