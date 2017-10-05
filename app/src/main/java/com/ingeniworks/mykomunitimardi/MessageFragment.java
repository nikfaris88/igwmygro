package com.ingeniworks.mykomunitimardi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ingeniworks.mykomunitimardi.adapter.MessageRVAdapter;
import com.ingeniworks.mykomunitimardi.model.Message;
import com.ingeniworks.mykomunitimardi.utils.NetworkCheck;
import com.ingeniworks.mykomunitimardi.utils.OnTaskCompleted;
import com.ingeniworks.mykomunitimardi.utils.Webservice;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Nik on 17/4/2017.
 * Message Fragment
 * Do the activity for Message
 */

public class MessageFragment extends Fragment {
    private MessageRVAdapter mAdapter;
    private View rootView;
    private TextView txtNoSignal;
    private ImageView imgNoData;
    private UltimateRecyclerView mRecyclerView;
    private RelativeLayout rlNoAnnounceChild;
    private Message message;
    private ArrayList<Message> listMessage = new ArrayList<>();
    private NetworkCheck mNetworkCheck;
    private Webservice mWebService;
    private String mToken;

    public MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.rv_main, container, false);
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();
        getDataSet();

        return rootView;
    }

    private void init() {
        mNetworkCheck = new NetworkCheck(getActivity());
        mWebService = new Webservice(getActivity());
        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences(getString(R.string.sessionString), Context.MODE_PRIVATE);
        mToken = mSharedPreferences.getString("token", "");
        txtNoSignal = (TextView) rootView.findViewById(R.id.txtNoSignal);
        imgNoData = (ImageView) rootView.findViewById(R.id.imgNoData);
        rlNoAnnounceChild = (RelativeLayout) rootView.findViewById(R.id.rlNoAnnounceChild);
        mRecyclerView = (UltimateRecyclerView) rootView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private ArrayList<Message> getDataSet() {
        listMessage = new ArrayList<>();

        try {

            if (mNetworkCheck.isNetworkConnected()) {
                new MessageTask(mToken, new OnTaskCompleted() {
                    @Override
                    public void onCompleted(String result) {
                        System.out.println("result messages: " + result);
                        if (result.length() > 0) {
                            mRecyclerView.setVisibility(View.VISIBLE);
                            rlNoAnnounceChild.setVisibility(View.GONE);
                            try {
                                JSONObject objResult = new JSONObject(result);
                                if (objResult.getInt("status") > 0) {
                                    JSONArray arrData = objResult.getJSONArray("data");
                                    JSONObject objData;
                                    JSONArray arrConversation;
                                    if (arrData.length() > 0) {
                                        mRecyclerView.setVisibility(View.VISIBLE);
                                        rlNoAnnounceChild.setVisibility(View.GONE);
                                        for (int i = 0; i < arrData.length(); i++) {
                                            objData = arrData.getJSONObject(i);

                                            JSONObject objConversation;
                                            arrConversation = objData.getJSONArray("conversations");
                                            for (int k = 0; k < arrConversation.length(); k++) {
                                                objConversation = arrConversation.getJSONObject(k);

                                                message = new Message();
                                                message.setMsg_id(objConversation.getInt("id"));
                                                message.setMsg_title(objConversation.getString("title"));
                                                message.setMsg_desc(objConversation.getString("content"));
                                                message.setCreated_at(objConversation.getString("created_at"));
                                                listMessage.add(message);
                                            }
                                        }

                                        populateList();
                                    } else {
                                        mRecyclerView.setVisibility(View.GONE);
                                        rlNoAnnounceChild.setVisibility(View.VISIBLE);
                                        imgNoData.setImageResource(R.drawable.error);
                                        txtNoSignal.setText(getString(R.string.strNoMessage));
                                    }

                                } else {
                                    mRecyclerView.setVisibility(View.GONE);
                                    rlNoAnnounceChild.setVisibility(View.VISIBLE);
                                    imgNoData.setImageResource(R.drawable.error);
                                    txtNoSignal.setText(getString(R.string.strErrTechnicalIssue));
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getActivity(), "error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            mRecyclerView.setVisibility(View.GONE);
                            rlNoAnnounceChild.setVisibility(View.VISIBLE);
                            imgNoData.setImageResource(R.drawable.error);
                            txtNoSignal.setText(getString(R.string.strNoMessage));
                        }
                    }
                }).execute();
            } else {
                mRecyclerView.setVisibility(View.GONE);
                rlNoAnnounceChild.setVisibility(View.VISIBLE);
                imgNoData.setImageResource(R.drawable.no_signal_internet);
                txtNoSignal.setText(getString(R.string.strErrConnection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listMessage;
    }

    private void populateList() {
        if (listMessage.size() > 0) {
            System.out.println("listMessage: " + listMessage.size());
            mRecyclerView.setVisibility(View.VISIBLE);
            rlNoAnnounceChild.setVisibility(View.GONE);
            mAdapter = new MessageRVAdapter(listMessage);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            RecyclerView.ItemDecoration itemDecoration =
                    new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
            mRecyclerView.addItemDecoration(itemDecoration);
            if (mAdapter != null) {
                mAdapter.setOnItemClickListener(new MessageRVAdapter.MyClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Message msg = mAdapter.getItem(position);
                        Intent i = new Intent(getActivity(), MessageDetails.class);
                        i.putExtra("msg_id", msg.getMsg_id());
                        startActivity(i);

                    }
                });
            }
        } else {
            mRecyclerView.setVisibility(View.GONE);
            rlNoAnnounceChild.setVisibility(View.VISIBLE);
            imgNoData.setImageResource(R.drawable.error);
            txtNoSignal.setText(getString(R.string.strNoMessage));
        }

    }

    private class MessageTask extends AsyncTask<Void, Void, String> {


        private OnTaskCompleted onTaskCompleted;
        private ProgressDialog progressDialog;
        private String mToken;

        MessageTask(String token, OnTaskCompleted onTaskCompleted) {
            mToken = token;
            this.onTaskCompleted = onTaskCompleted;
        }

        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMax(100);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String results = null;
            try {
                results = mWebService.getMessages(mToken);
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
