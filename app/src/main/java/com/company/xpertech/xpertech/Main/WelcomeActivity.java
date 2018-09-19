package com.company.xpertech.xpertech.Main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.company.xpertech.xpertech.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        int secondsDelayed = 1;
        //Time delay to display the splash screen
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(WelcomeActivity.this, SignUpActivity.class));
                finish();
            }
        }, secondsDelayed * 1000);

    }
}
