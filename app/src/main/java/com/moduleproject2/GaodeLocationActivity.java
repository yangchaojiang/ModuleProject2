package com.moduleproject2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.gaodemodule.GaoDeLocationManager;


public class GaodeLocationActivity extends AppCompatActivity {

    TextView textView18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_location);
        textView18 = (TextView) findViewById(R.id.textView18);
        GaoDeLocationManager.getSingleton().start(new com.gaodemodule.MyLocationsListener() {
            @Override
            public void onSucceed(String msg, String dateString) {
                textView18.setText(msg + ":\n定位时间：" + dateString);
            }

            @Override
            public void onFailed(int code, String msg) {
                Toast.makeText(GaodeLocationActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onStop() {
        GaoDeLocationManager.getSingleton().stop();
        super.onStop();
    }

}
