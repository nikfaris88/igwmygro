package com.ingeniworks.mykomunitimardi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;

import com.ingeniworks.mykomunitimardi.adapter.AppsGVAdapter;
import com.ingeniworks.mykomunitimardi.model.Apps;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    //    private static String LOG_TAG = "ListApplicationActivity";
//    private SharedPreferences mSharedPreferences;
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_main);
        setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

//        init();

//        String userId = mSharedPreferences.getString("user_id", "");
//        if (userId.length() > 0) {
//            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }

//    private void init() {
//        mSharedPreferences = getSharedPreferences(getString(R.string.sessionString), Context.MODE_PRIVATE);
//        // Obtain the shared Tracker instance.
//        findViewById(R.id.txtNonRegister).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(SplashActivity.this, MainActivity.class);
//                startActivity(i);
//                finish();
//
//            }
//        });
//        findViewById(R.id.txtLogIn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
//                startActivity(i);
//                finish();
//            }
//        });
//    }
//
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//    }

}
