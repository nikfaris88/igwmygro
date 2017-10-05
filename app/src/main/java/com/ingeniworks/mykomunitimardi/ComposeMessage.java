package com.ingeniworks.mykomunitimardi;

import android.accounts.NetworkErrorException;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.ingeniworks.mykomunitimardi.utils.NetworkCheck;
import com.ingeniworks.mykomunitimardi.utils.OnTaskCompleted;
import com.ingeniworks.mykomunitimardi.utils.Webservice;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public class ComposeMessage extends AppCompatActivity {

    private static String LOG_TAG = "ComposeMessage";
    private EditText etTopic, etContent;
    private String[] enrollment;
    private SharedPreferences mSharedPreferences;
    private String mToken = "";
    private NetworkCheck mNetworkCheck;
    private Webservice mWebService;
    private int project_id = 0;
    private String title = "";
    private String content = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compose_msg);
        setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        mNetworkCheck = new NetworkCheck(getApplicationContext());
        mWebService = new Webservice(getApplicationContext());


        init();

    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.strComposeMsg));
        }

        mSharedPreferences = getSharedPreferences(getString(R.string.sessionString), Context.MODE_PRIVATE);
        mToken = mSharedPreferences.getString("token", "");
        etTopic = (EditText) findViewById(R.id.etTopic);
        etContent = (EditText) findViewById(R.id.etContent);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            enrollment = bundle.getStringArray("enrollment");
            project_id = bundle.getInt("project_id");
            assert enrollment != null;
            if (enrollment.length > 0) {
                peopleInConversation();
            }
        }
    }

    private void peopleInConversation() {
        LinearLayout layoutConversation = (LinearLayout) findViewById(R.id.llConversation);

        for (int i = 0; i < enrollment.length; i++) {
            ImageView imgConversation = new ImageView(ComposeMessage.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100, 100);
            lp.setMargins(8, 8, 8, 8);
            imgConversation.setLayoutParams(lp);
            String firstLetter = String.valueOf(enrollment[i].charAt(0));
            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            int color = generator.getColor(enrollment[i]);
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(firstLetter, color); // radius in px
            imgConversation.setImageDrawable(drawable);
            layoutConversation.addView(imgConversation);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.3
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send) {

            if (etTopic.getText().toString().length() == 0) {
                etTopic.requestFocus();
                etTopic.setError("Sila masukkan topik mesej");
            } else if (etContent.getText().toString().length() == 0) {
                etContent.requestFocus();
                etContent.setError("Sila isi kandungan mesej");
            } else {

                title = etTopic.getText().toString();
                content = etContent.getText().toString();

                try {
                    if (mNetworkCheck.isNetworkConnected()) {
                        new PostNewMessage(mToken, new OnTaskCompleted() {
                            @Override
                            public void onCompleted(String result) {

                                if (result != null) {

                                    try {
                                        JSONObject objResult = new JSONObject(result);

                                        if (objResult.getInt("status") > 0) {

                                            Toast.makeText(ComposeMessage.this, "Message dihantar....", Toast.LENGTH_SHORT).show();
                                            finish();


                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }


                            }
                        }).execute();
                    } else {
                        Toast.makeText(this, getString(R.string.strErrConnection), Toast.LENGTH_SHORT).show();
                    }
                } catch (SocketTimeoutException | SocketException | NetworkErrorException e) {
                    e.printStackTrace();
                }

//                Intent i = new Intent(ComposeMessage.this, MainActivity.class);
//                i.putExtra("tabId", 2);
//                startActivity(i);

                return true;
            }
        } else if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private class PostNewMessage extends AsyncTask<Void, Void, String> {


        private OnTaskCompleted onTaskCompleted;
        private ProgressDialog progressDialog;
        private String mToken;

        PostNewMessage(String token, OnTaskCompleted onTaskCompleted) {
            mToken = token;
            this.onTaskCompleted = onTaskCompleted;
        }

        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ComposeMessage.this);
            progressDialog.setMax(100);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String results = null;
            try {
                results = mWebService.createMessage(mToken, String.valueOf(project_id),
                        title, content);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return results;
        }

        @Override
        protected void onPostExecute(final String success) {
            progressDialog.dismiss();
            onTaskCompleted.onCompleted(success);
        }

        @Override
        protected void onCancelled() {
            progressDialog.dismiss();
        }
    }

}
