package com.hogoworld.biryanihouse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String user = preferences.getString("registered", "");

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                if (user.equalsIgnoreCase("Authorized")) {

                    Intent loginPage = new Intent(SplashScreen.this, HomeActivity.class);
                    startActivity(loginPage);
                    finish();

                } else {

                    Intent loginPage = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(loginPage);
                    finish();

                }

            }
        }, 2500);


    }
}
