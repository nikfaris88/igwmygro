package com.ingeniworks.mykomunitimardi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apps_info);

        init();

    }

    public void init() {

        String versionCode = BuildConfig.VERSION_NAME;

        TextView txtVersion = (TextView) findViewById(R.id.appsVersion);
        txtVersion.setText(getString(R.string.app_name) + " v" + versionCode);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
