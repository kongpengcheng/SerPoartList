package com.haier.ranghood.serpoartdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PowerSerialOpt powerSerialOpt = null;
        try {
            powerSerialOpt = PowerSerialOpt.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final PowerSerialOpt finalPowerSerialOpt = powerSerialOpt;
        final byte[] cmd = new byte[]{(byte) 0xAA, (byte) 0x55, (byte) 0x04, (byte) 0x01, (byte) 0x4d, (byte) 0x01, (byte) 0x53};
        button = (Button) findViewById(R.id.btn_test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalPowerSerialOpt.sendCmd(cmd);
                Log.d("ddd", "点击");
            }
        });
    }

}
