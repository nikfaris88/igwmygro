package com.ingeniworks.mykomunitimardi;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import static com.ingeniworks.mykomunitimardi.MainActivity.mEmail;
import static com.ingeniworks.mykomunitimardi.MainActivity.mFullName;
import static com.ingeniworks.mykomunitimardi.MainActivity.mPhoneNo;
import static com.ingeniworks.mykomunitimardi.MainActivity.mUserId;

public class ProfileActivity extends AppCompatActivity {
    //    private static String LOG_TAG = "ProfileActivity";
    int user_role = 0;
    String actionBarTitle = "";
    ImageView imgBackground;
    TextView txtName, txtEmail, txtPhone, txtUserId, txtState;
    //    TextView txtmsgDetails;
    String msgDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();

        txtUserId.setText(mUserId);
        actionBarTitle = mFullName;
        txtName.setText(mFullName);
        txtEmail.setText(mEmail);
        txtPhone.setText(mPhoneNo);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(actionBarTitle);
        }

        setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        String firstLetter = String.valueOf(actionBarTitle.charAt(0));

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color = generator.getColor(actionBarTitle);
        //int color = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .buildRect(firstLetter, color); // radius in px

        imgBackground.setImageDrawable(drawable);

    }

    private void init() {
        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.sessionString), Context.MODE_PRIVATE);
        user_role = mSharedPreferences.getInt("user_role", 0);
        imgBackground = (ImageView) findViewById(R.id.imgBackground);
        txtName = (TextView) findViewById(R.id.txtName);
        txtPhone = (TextView) findViewById(R.id.txtPhone);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtUserId = (TextView) findViewById(R.id.txtUserId);
        txtState = (TextView) findViewById(R.id.txtState);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
