package com.ingeniworks.mykomunitimardi;

import android.accounts.NetworkErrorException;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.ingeniworks.mykomunitimardi.utils.NetworkCheck;
import com.ingeniworks.mykomunitimardi.utils.OnTaskCompleted;
import com.ingeniworks.mykomunitimardi.utils.Webservice;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import static com.ingeniworks.mykomunitimardi.MainActivity.mToken;

public class SettingsActivity extends AppCompatActivity {
    private EditText etCurrentPassword;
    private EditText etNewPassword;
    private EditText etConfirmPassword;
    private String globalPassword = "";

    private NetworkCheck mNetworkCheck;
    private Webservice mWebservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_main);
        init();

    }

    private void init() {
        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.sessionString), Context.MODE_PRIVATE);
        mToken = mSharedPreferences.getString("token", "");
        globalPassword = mSharedPreferences.getString("globalPassword", "");

        mNetworkCheck = new NetworkCheck(this);
        mWebservice = new Webservice(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.strSettings));
        }

        etCurrentPassword = (EditText) findViewById(R.id.etCurrentPass);
        etNewPassword = (EditText) findViewById(R.id.etNewPass);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPass);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.3
        getMenuInflater().inflate(R.menu.menu_reset_pass, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset:
                if (isValidate()) {
                    try {
                        if (mNetworkCheck.isNetworkConnected()) {
                            new ResetPassword(etNewPassword.getText().toString(), new OnTaskCompleted() {
                                @Override
                                public void onCompleted(String result) {
                                    Toast.makeText(SettingsActivity.this, getString(R.string.strSuccessfullyReset), Toast.LENGTH_SHORT).show();
                                }
                            }).execute();
                        } else {
                            Toast.makeText(this, getString(R.string.strErrConnection), Toast.LENGTH_SHORT).show();
                        }
                    } catch (SocketTimeoutException | SocketException | NetworkErrorException e) {
                        e.printStackTrace();
                    }


                }
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return false;
    }

    private boolean isValidate() {
        if (etCurrentPassword.getText().length() > 0) {
            if (etCurrentPassword.getText().toString().equals(globalPassword)) {
                if (etNewPassword.getText().length() > 0) {
                    if (etNewPassword.getText().length() < 14) {
                        etNewPassword.setError(getString(R.string.strPassMin));
                        return false;
                    } else if (etNewPassword.getText().length() > 20) {
                        etNewPassword.setError(getString(R.string.strPassMax));
                        return false;
                    } else {
                        if (etConfirmPassword.getText().length() > 0) {
                            if (etConfirmPassword.getText().length() < 14) {
                                etConfirmPassword.setError(getString(R.string.strPassMin));
                                return false;
                            } else if (etConfirmPassword.getText().length() > 20) {
                                etConfirmPassword.setError(getString(R.string.strPassMax));
                                return false;
                            } else {
                                if (!etConfirmPassword.getText().toString().equals(etNewPassword.getText().toString())) {
                                    etConfirmPassword.setError(getString(R.string.strPassNotSame));
                                    return false;
                                } else {
                                    return true;
                                }
                            }
                        } else {
                            etNewPassword.setError(getString(R.string.password_details));
                            return false;
                        }
                    }
                } else {
                    etNewPassword.setError(getString(R.string.password_details));
                    return false;
                }
            } else {
                etCurrentPassword.setError(getString(R.string.strPassNotSame));
                return false;
            }
        } else {
            etCurrentPassword.setError(getString(R.string.password_details));
            return false;
        }
    }

    private class ResetPassword extends AsyncTask<Void, Void, String> {


        private OnTaskCompleted onTaskCompleted;
        private ProgressDialog progressDialog;
        private String mPassword;

        ResetPassword(String password, OnTaskCompleted onTaskCompleted) {

            mPassword = password;
            this.onTaskCompleted = onTaskCompleted;
        }

        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SettingsActivity.this);
            progressDialog.setMax(100);
            progressDialog.setMessage(getString(R.string.pls_wait));
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String results = "";
            try {
                results = mWebservice.resetPassword(mPassword);
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

    @Override
    public void onBackPressed() {
        finish();
    }

}
