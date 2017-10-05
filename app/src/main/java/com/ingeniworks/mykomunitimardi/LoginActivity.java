package com.ingeniworks.mykomunitimardi;

import android.Manifest;
import android.accounts.NetworkErrorException;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.heinrichreimersoftware.singleinputform.SingleInputFormActivity;
import com.heinrichreimersoftware.singleinputform.steps.Step;
import com.heinrichreimersoftware.singleinputform.steps.TextStep;
import com.ingeniworks.mykomunitimardi.utils.NetworkCheck;
import com.ingeniworks.mykomunitimardi.utils.OnTaskCompleted;
import com.ingeniworks.mykomunitimardi.utils.Webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends SingleInputFormActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATES = 0;
    private static String DATA_KEY_EMAIL = "email";
    private static final String DATA_KEY_PASSWORD = "password";

    private SharedPreferences mSharedPreferences;
    private Webservice mWebservice;
    private NetworkCheck mNetworkCheck;
    private String mDevice_id = "";

    private String mUserID;
    public static String mName;
    private String mEmail;
    private String mAndroidOS;
    private String mPhoneNo;
    private String mToken;
    private String mRoleName;
    private int mRoleID;
    private String globalPassword = "";


    @Override
    protected List<Step> onCreateSteps() {
        mSharedPreferences = getSharedPreferences(getString(R.string.sessionString), Context.MODE_PRIVATE);
        mWebservice = new Webservice(LoginActivity.this);
        mNetworkCheck = new NetworkCheck(LoginActivity.this);

        getDeviceId();

        getAndroidVersion();

        // Obtain the shared Tracker instance.
        String userId = mSharedPreferences.getString("user_id", "");
        if (userId.length() > 0) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        List<Step> steps = new ArrayList<>();

        steps.add(new TextStep.Builder(this, DATA_KEY_EMAIL)
                .titleResId(R.string.email)
                .errorResId(R.string.email_error)
                .detailsResId(R.string.email_details)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .validator(new TextStep.Validator() {
                    @Override
                    public boolean validate(String input) {
                        return input.length() > 0;
                    }
                })
                .textWatcher(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_NEXT ||
                                actionId == EditorInfo.IME_ACTION_DONE) {
                            nextStep();
                            return true;
                        }
                        return false;
                    }
                })
                .build());


        steps.add(new TextStep.Builder(this, DATA_KEY_PASSWORD)
                .titleResId(R.string.password)
                .errorResId(R.string.password_error)
                .detailsResId(R.string.password_details)
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .validator(new TextStep.Validator() {
                    @Override
                    public boolean validate(String input) {
                        return input.length() >= 5;
                    }
                })
                .textWatcher(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_NEXT ||
                                actionId == EditorInfo.IME_ACTION_DONE) {
                            nextStep();
                            return true;
                        }
                        return false;
                    }
                })
                .build());


        return steps;
    }

    protected View onCreateFinishedView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.finished_layout, parent, false);
    }

    @Override
    protected void onFormFinished(Bundle data) {

        try {
            if (mNetworkCheck.isNetworkConnected()) {
                globalPassword = TextStep.text(data, DATA_KEY_PASSWORD);
                System.out.println("globalPassword: " + globalPassword);
                new UserLoginTask(TextStep.text(data, DATA_KEY_EMAIL),
                        TextStep.text(data, DATA_KEY_PASSWORD), mDevice_id, mAndroidOS, new OnTaskCompleted() {

                    @Override
                    public void onCompleted(String result) {
                        try {
                            JSONObject objResult = new JSONObject(result);
                            System.out.println("result: " + objResult);
                            if (objResult.getInt("status") > 0) {
                                //TODO if login success
                                JSONObject objData = objResult.getJSONObject("data");
                                mUserID = objData.getString("ic_no");
                                mName = objData.getString("name");
                                mEmail = objData.getString("email");
                                mPhoneNo = objData.getString("hp_no");
                                mToken = objData.getString("token");
                                JSONArray arrDataRoles = objData.getJSONArray("roles");
                                JSONObject objDataRoles = arrDataRoles.getJSONObject(0);
                                mRoleID = objDataRoles.getInt("id");
                                mRoleName = objDataRoles.getString("name");
                                SharedPreferences.Editor edit = mSharedPreferences.edit();
                                edit.putString("user_id", mUserID);
                                edit.putString("name", mName);
                                edit.putString("email", mEmail);
                                edit.putInt("role_id", mRoleID);
                                edit.putString("role_name", mRoleName);
                                edit.putString("hp_no", mPhoneNo);
                                edit.putString("token", mToken);
                                edit.putString("globalPassword", globalPassword);
                                edit.apply();

                                finish();

                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);

                            } else {
                                //TODO if login failed

                                String errorMsg = objResult.getString("message");

                                Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();


                            }
                        } catch (JSONException e) {
//                            previousStep();
                            nextStep();
                            Toast.makeText(LoginActivity.this, "error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).execute();

            } else {
                Toast.makeText(this, getString(R.string.strErrConnection), Toast.LENGTH_SHORT).show();
            }
        } catch (SocketTimeoutException | SocketException | NetworkErrorException e) {
            e.printStackTrace();
        }
    }

    private class UserLoginTask extends AsyncTask<Void, Void, String> {


        private OnTaskCompleted onTaskCompleted;
        private ProgressDialog progressDialog;
        private String mUsername;
        private String mPassword;

        UserLoginTask(String username, String password, String imei, String androidOS,
                      OnTaskCompleted onTaskCompleted) {

            mUsername = username;
            mPassword = password;
            mDevice_id = imei;
            mAndroidOS = androidOS;
            this.onTaskCompleted = onTaskCompleted;
        }

        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMax(100);
            progressDialog.setMessage(getString(R.string.pls_wait));
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String results = "";
            try {
                results = mWebservice.userLogin(mUsername, mPassword);
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

    private void getDeviceId() {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                // only for gingerbread and newer versions
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                mDevice_id = telephonyManager.getDeviceId();

//                if (mDevice_id != null) {
//                    registerMobile();
//                }
            } else {
                if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    mDevice_id = telephonyManager.getDeviceId();
//                    if (mDevice_id != null) {
//                        registerMobile();
//                    }

                } else {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_PHONE_STATE)) {
                        Toast.makeText(LoginActivity.this, getString(R.string.strPhonePermissionCloudMsg),
                                Toast.LENGTH_SHORT).show();

                    }

                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                            MY_PERMISSIONS_REQUEST_READ_PHONE_STATES);
                }
            }
        } catch (Exception e) {
            Log.e("LoginActivity", e.getMessage());
        }
    }

    public void getAndroidVersion() {

        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        mAndroidOS = "Android SDK: " + sdkVersion + " (" + release + ")";

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATES: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    mDevice_id = telephonyManager.getDeviceId();
                    System.out.println("mDeviceID: " + mDevice_id);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(LoginActivity.this, getString(R.string.strPhonePermissionCloudMsg),
                            Toast.LENGTH_SHORT).show();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


//    @Override
//    public void onBackPressed() {
//
//        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//    }
}
