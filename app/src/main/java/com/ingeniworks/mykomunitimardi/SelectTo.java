package com.ingeniworks.mykomunitimardi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.ingeniworks.mykomunitimardi.utils.NetworkCheck;
import com.ingeniworks.mykomunitimardi.utils.Webservice;

import java.util.ArrayList;

public class SelectTo extends AppCompatActivity {

    private RadioButton mRbPublic, mRbKomuniti;
    private int type_post = 0;
    private String feedTitle = "";
    private String feedDesc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_list);

        init();

        mRbPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type_post = 0;
                    mRbPublic.setChecked(true);
                    mRbKomuniti.setChecked(false);
                }
            }
        });

        mRbKomuniti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type_post = 1;
                    mRbPublic.setChecked(false);
                    mRbKomuniti.setChecked(true);
                }
            }
        });
    }

    private void init() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sessionString), Context.MODE_PRIVATE);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            feedDesc = b.getString("feed_desc");
            feedTitle = b.getString("feed_title");
//            imagesEncodedList = (ArrayList<FeedAttachment>) b.getSerializable("imageList");
        }

        mRbPublic = (RadioButton) findViewById(R.id.rbPublic);
        mRbKomuniti = (RadioButton) findViewById(R.id.rbKomuniti);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            //TODO back previous page
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("imageList", imagesEncodedList);
//        bundle.putSerializable("list_recipients", mEtRecipient.getText().toString());
//        bundle.putString("feed_desc", feed_desc);
//        bundle.putString("action_by", "gallery");

        Intent intent = new Intent();
        intent.putExtra("feed_title", feedTitle);
        intent.putExtra("feed_desc", feedDesc);
        intent.putExtra("type_post", type_post);
//        intent.putExtra("recipientIds", (ArrayList<String>) collections);
////        intent.putStringArrayListExtra("list_recipients", (ArrayList<String>) collections);
//        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
//        finishActivity(1);
    }
}