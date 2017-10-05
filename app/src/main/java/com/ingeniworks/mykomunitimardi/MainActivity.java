package com.ingeniworks.mykomunitimardi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ingeniworks.mykomunitimardi.adapter.MainTabAdapter;

public class MainActivity extends AppCompatActivity {

    private int pointToTab = 0;
    private FloatingActionButton mFab;

    public static String mUserId;
    public static String mFullName;
    public static int mRoleId;
    public static String mEmail;
    public static String mPhoneNo;
    public static String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        savedInstanceState = getIntent().getExtras();
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("tabId")) {
                pointToTab = savedInstanceState.getInt("tabId", 0);
            }
        }

        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.sessionString), Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.app_name));
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        //            int idName = prefs.getInt("idName", 0); //0 is the default value.

        mFullName = mSharedPreferences.getString("name", "No name defined");
//        }

        String mTitle[] = {"Utama", getString(R.string.action_otherapps)};
//        int mIcon[] = {R.drawable.ic_home_black_24px, R.drawable.ic_account_box_black_24dp};

        //IF USER LOGGED IN, Menu will change
        mUserId = mSharedPreferences.getString("user_id", "");
        mRoleId = mSharedPreferences.getInt("role_id", 0);
        mEmail = mSharedPreferences.getString("email", "");
        mPhoneNo = mSharedPreferences.getString("hp_no", "");
        mToken = mSharedPreferences.getString("token", "");

        System.out.println("User id: " + mUserId);

        if (mUserId.length() > 0) {
            mTitle = new String[]{"Utama", "Projek", "Mesej", "Profil"};
//            mIcon = new int[]{R.drawable.ic_home_black_24px, R.drawable.ic_people_black_24px,
//                    R.drawable.ic_message_black_24dp, R.drawable.ic_person_black_24px};
        }

        MainTabAdapter mMainTabAdapter = new MainTabAdapter(MainActivity.this, getSupportFragmentManager(), mTitle);

//        Display display = getWindowManager().getDefaultDisplay();
//        int height = display.getHeight();
//        AppBarLayout appsBar = (AppBarLayout) findViewById(R.id.appbar);

//        mViewPager.setMinimumHeight(height - appsBar.getWidth());
        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mMainTabAdapter);
        mViewPager.setCurrentItem(pointToTab);

//        mViewPager.getLayoutParams().height = 650;

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            tabLayout.getTabAt(i).setIcon(mIcon[i]);
            tabLayout.getTabAt(i).setText(mTitle[i]);


        }

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        if (mFab.getVisibility() == View.VISIBLE) {
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, CreateAnnouncement.class));
                }
            });
        }
        //on tab change
        ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    if (mRoleId == 3) {
                        mFab.setVisibility(View.VISIBLE);
                    } else {
                        mFab.setVisibility(View.GONE);
                    }

                } else
                    mFab.setVisibility(View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        mViewPager.addOnPageChangeListener(listener);
        listener.onPageSelected(0);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem action_app = menu.findItem(R.id.action_apps);
        MenuItem action_login = menu.findItem(R.id.action_login);
        if (mUserId.length() > 0) {
            action_app.setVisible(true);
            action_login.setVisible(false);
        } else {
            action_app.setVisible(false);
            action_login.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_apps) {
            startActivity(new Intent(MainActivity.this, ListApplicationActivity.class));
            return true;
        } else if (id == R.id.action_login) {
//            finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Adakah anda pasti anda mahu keluar dari aplikasi ini?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}
