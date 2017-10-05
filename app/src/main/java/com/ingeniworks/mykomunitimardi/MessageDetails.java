package com.ingeniworks.mykomunitimardi;

import android.accounts.NetworkErrorException;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v13.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.ingeniworks.mykomunitimardi.adapter.RespondNormalRVAdapter;
import com.ingeniworks.mykomunitimardi.model.Conversation;
import com.ingeniworks.mykomunitimardi.model.User;
import com.ingeniworks.mykomunitimardi.utils.DateTime;
import com.ingeniworks.mykomunitimardi.utils.NetworkCheck;
import com.ingeniworks.mykomunitimardi.utils.OnTaskCompleted;
import com.ingeniworks.mykomunitimardi.utils.Webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;

import static com.ingeniworks.mykomunitimardi.LoginActivity.mName;

public class MessageDetails extends AppCompatActivity {
    private RespondNormalRVAdapter mAdapter;
    private Button btnReply;

    private int msg_id = 0;
    private NetworkCheck mNetworkCheck;
    private Webservice mWebService;
    private String mToken;

    private ImageView imgUserEngage;
    private TextView txtUserEngageName;
    private TextView txtEngageTime;
    private TextView txtTitle;
    private TextView txtInquiry;
    private LinearLayout llNoRespond;
    private ArrayList<Conversation> conversationArrayList = new ArrayList<>();
    private ArrayList<String> listContact = new ArrayList<>();
    private EditText etMsg;
    private Button btnSubmit;
    private String conversation_id = "";
    private String respondMsg = "";
    private String engageFrom = "", engageFromRole = "";
    private DateTime dateTime = new DateTime();
    private Date date = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_details);
        setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            msg_id = bundle.getInt("msg_id");
        }

        System.out.println("msg_id: " + msg_id);
        if (msg_id > 0) {
            //TODO get message details
            try {
                if (mNetworkCheck.isNetworkConnected()) {
                    new GetMessageTask(mToken, new OnTaskCompleted() {
                        @Override
                        public void onCompleted(String result) {
                            try {
                                System.out.println("result msgDetails: " + result);
                                JSONObject objResult = new JSONObject(result);
                                if (objResult.getInt("status") > 0) {

                                    JSONArray arrData = objResult.getJSONArray("data");
                                    JSONObject objData;
                                    JSONArray arrEngage;
                                    JSONObject objEngage;
                                    JSONObject objUserEngage;

                                    JSONArray arrRespond;
                                    JSONObject objRespond;
                                    JSONObject objUserRespond;
                                    JSONObject objUserRespondEngage;
                                    JSONObject objUserRespondRole;

                                    Conversation conversation;
                                    User user;

                                    for (int i = 0; i < arrData.length(); i++) {
                                        objData = arrData.getJSONObject(i);
                                        txtTitle.setText(objData.getString("title"));
                                        txtInquiry.setText(objData.getString("content"));

                                        conversation_id = objData.getString("id");

                                        arrEngage = objData.getJSONArray("engages");
                                        objEngage = arrEngage.getJSONObject(0);
                                        objUserEngage = objEngage.getJSONObject("user");
                                        txtUserEngageName.setText(objUserEngage.getString("name"));
                                        if (!objUserEngage.getString("created_at").equalsIgnoreCase("null")) {
                                            date = dateTime.getDate(objUserEngage.getString("created_at"));
                                            txtEngageTime.setText(dateTime.getFormattedDate(date.getTime()));
                                        }
                                        engageFrom = objUserEngage.getString("name");
                                        listContact.add(objUserEngage.getString("name"));
                                        String firstLetter = String.valueOf(objUserEngage.getString("name").charAt(0));

                                        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
                                        // generate random color
                                        int color = generator.getColor(objUserEngage.getString("name"));
                                        //int color = generator.getRandomColor();

                                        TextDrawable drawable = TextDrawable.builder()
                                                .buildRound(firstLetter, color); // radius in px
                                        imgUserEngage.setImageDrawable(drawable);
                                        arrRespond = objData.getJSONArray("responds");
                                        if (arrRespond.length() < 1) {
                                            llNoRespond.setVisibility(View.VISIBLE);
                                        } else {
                                            llNoRespond.setVisibility(View.GONE);

                                            for (int x = 0; x < arrRespond.length(); x++) {
                                                objRespond = arrRespond.getJSONObject(x);
                                                conversation = new Conversation();
                                                user = new User();
                                                conversation.setConversation_id(objRespond.getInt("conversation_id"));
                                                conversation.setConversation_respond(objRespond.getString("message"));
                                                conversation.setRespond_created_at(objRespond.getString("created_at"));
                                                objUserRespondEngage = objRespond.getJSONObject("engage");
                                                objUserRespond = objUserRespondEngage.getJSONObject("user");
                                                objUserRespondRole = objUserRespondEngage.getJSONObject("role");

                                                user.setName(objUserRespond.getString("name"));
                                                user.setAlt_username(objUserRespondRole.getString("name"));
                                                conversation.setUser(user);

                                                if (!listContact.contains(objUserRespond.getString("name"))) {
                                                    listContact.add(objUserRespond.getString("name"));
                                                }
                                                conversationArrayList.add(conversation);
                                            }
                                        }
                                        populateList();
                                        mAdapter.notifyDataSetChanged();
                                    }

                                }
                            } catch (JSONException e) {
                                Toast.makeText(MessageDetails.this, "error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).execute();
                } else {
                    Toast.makeText(this, getString(R.string.strErrConnection), Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (SocketTimeoutException | SocketException | NetworkErrorException e) {
                e.printStackTrace();
            }
        }

        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etMsg.post(new Runnable() {
                    public void run() {
                        etMsg.requestFocusFromTouch();
                        InputMethodManager lManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        lManager.showSoftInput(etMsg, InputMethodManager.SHOW_FORCED);
                    }
                });
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                respondMsg = etMsg.getText().toString();
                if (respondMsg.length() == 0) {
                    etMsg.setError("Sila isi maklum balas anda.");
                } else {

                    try {
                        if (mNetworkCheck.isNetworkConnected()) {
                            new PostReply(mToken, new OnTaskCompleted() {
                                @Override
                                public void onCompleted(String result) {
                                    try {
                                        JSONObject objResult = new JSONObject(result);
                                        User user;
                                        Conversation conversation;
                                        if (objResult.getInt("status") > 0) {
                                            JSONObject objData = objResult.getJSONObject("data");
                                            user = new User();
                                            conversation = new Conversation();
                                            conversation.setConversation_id(objData.getInt("conversation_id"));
                                            conversation.setConversation_respond(objData.getString("message"));
                                            user.setName(mName);
                                            user.setAlt_username(engageFromRole);
                                            conversation.setUser(user);

                                            if (!listContact.contains(engageFrom)) {
                                                listContact.add(engageFrom);
                                            }
                                            conversationArrayList.add(conversation);
                                            mAdapter.swapItem(conversationArrayList);

                                            mAdapter.notifyDataSetChanged();


                                            etMsg.setText("");




                                        } else {
                                            Toast.makeText(MessageDetails.this, "Maaf, masalah teknikal.", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(MessageDetails.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).execute();
                        } else {
                            Toast.makeText(MessageDetails.this, getString(R.string.strErrConnection), Toast.LENGTH_SHORT).show();
                        }
                    } catch (SocketTimeoutException | SocketException | NetworkErrorException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }

    private void populateList() {

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mesej dari " + engageFrom);
        }

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        ViewCompat.setNestedScrollingEnabled(mRecyclerView, false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RespondNormalRVAdapter(conversationArrayList);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        peopleInConversation();
    }

    private void peopleInConversation() {
        LinearLayout layoutConversation = (LinearLayout) findViewById(R.id.llConversation);
        LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < listContact.size(); i++) {
            View view = inflate.inflate(R.layout.mardi_imgname, null, false);
            ImageView imgConversation = (ImageView) view.findViewById(R.id.imgUser);
            TextView txtEnrollName = (TextView) view.findViewById(R.id.txtName);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100, 100);
            lp.setMargins(8, 8, 8, 8);
            imgConversation.setLayoutParams(lp);
            txtEnrollName.setLayoutParams(lp);
            txtEnrollName.setText(listContact.get(i));
            String firstLetter = String.valueOf(listContact.get(i).charAt(0));
            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            int color = generator.getColor(listContact.get(i));
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(firstLetter, color); // radius in px
            imgConversation.setImageDrawable(drawable);

            layoutConversation.addView(view);
        }

    }


    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Mesej dari " + engageFrom);
        }
        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.sessionString), Context.MODE_PRIVATE);
        mNetworkCheck = new NetworkCheck(MessageDetails.this);
        mWebService = new Webservice(MessageDetails.this);
        mToken = mSharedPreferences.getString("token", "");
        mName = mSharedPreferences.getString("name", "");
        engageFromRole = mSharedPreferences.getString("role_name", "");

        imgUserEngage = (ImageView) findViewById(R.id.imgUserEngage);
        txtUserEngageName = (TextView) findViewById(R.id.txtUserEngageName);
        txtEngageTime = (TextView) findViewById(R.id.txtEngageTime);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtInquiry = (TextView) findViewById(R.id.txtInquiry);
        btnReply = (Button) findViewById(R.id.btnReply);
        llNoRespond = (LinearLayout) findViewById(R.id.llNoResponds);

        etMsg = (EditText) findViewById(R.id.etMesej);
        btnSubmit = (Button) findViewById(R.id.btnHantar);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {

//        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//        if (isViewMsgNewActive) {
//            isViewMsgNewActive = false;
//        } else {
//            this.finish();
//        }
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return false;
    }


    private class GetMessageTask extends AsyncTask<Void, Void, String> {


        private OnTaskCompleted onTaskCompleted;
        private ProgressDialog progressDialog;
        private String mToken;

        GetMessageTask(String token, OnTaskCompleted onTaskCompleted) {
            mToken = token;
            this.onTaskCompleted = onTaskCompleted;
        }

        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MessageDetails.this);
            progressDialog.setMax(100);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String results = null;
            try {
                results = mWebService.getMessageDetails(mToken, msg_id);
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

    private class PostReply extends AsyncTask<Void, Void, String> {


        private OnTaskCompleted onTaskCompleted;
        private ProgressDialog progressDialog;
        private String mToken;

        PostReply(String token, OnTaskCompleted onTaskCompleted) {
            mToken = token;
            this.onTaskCompleted = onTaskCompleted;
        }

        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MessageDetails.this);
            progressDialog.setMax(100);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String results = null;
            try {
                results = mWebService.postReply(mToken, conversation_id, respondMsg, "1");
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
