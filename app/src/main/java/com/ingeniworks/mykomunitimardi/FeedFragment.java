package com.ingeniworks.mykomunitimardi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingeniworks.mykomunitimardi.adapter.FeedRVAdapter;
import com.ingeniworks.mykomunitimardi.model.Attachment;
import com.ingeniworks.mykomunitimardi.model.Feed;
import com.ingeniworks.mykomunitimardi.model.User;
import com.ingeniworks.mykomunitimardi.utils.NetworkCheck;
import com.ingeniworks.mykomunitimardi.utils.OnTaskCompleted;
import com.ingeniworks.mykomunitimardi.utils.Webservice;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class FeedFragment extends Fragment {
    private View rootView;
    private FeedRVAdapter mAdapter;
    private NetworkCheck mNetworkCheck;
    private Webservice mWebService;
    private String mToken = "";
    private String user_id = "";
    private String mURL = "";
    private RelativeLayout rlNoAnnounceChild;
    private UltimateRecyclerView mRecyclerView;
    private ArrayList<Feed> feedArrayList;
    private ImageView imgNoData;
    private TextView txtNoSignal;

    public FeedFragment newInstance() {
        return new FeedFragment();
    }

    public FeedFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.feed_main, container, false);
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mNetworkCheck = new NetworkCheck(getActivity());
        mWebService = new Webservice(getActivity());

        init();

        getFeedDataSet();

        mRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (user_id.length() > 0) {
                    mURL = getString(R.string.apiGetFeedRegUser, getString(R.string.apiRootMardi));
                } else {
                    mURL = getString(R.string.apiGetFeedGuest, getString(R.string.apiRootMardi));
                }
                feedArrayList.clear();
                getFeedDataSet();
            }
        });

        mRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                getFeedDataSet();
            }
        });

        return rootView;
    }

    private void init() {

        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences(getString(R.string.sessionString), Context.MODE_PRIVATE);
        mToken = mSharedPreferences.getString("token", "");
        user_id = mSharedPreferences.getString("user_id", "");

        mAdapter = new FeedRVAdapter(getActivity());


        rlNoAnnounceChild = (RelativeLayout) rootView.findViewById(R.id.rlNoAnnounceChild);
        imgNoData = (ImageView) rootView.findViewById(R.id.imgNoData);
        txtNoSignal = (TextView) rootView.findViewById(R.id.txtNoSignal);
        mRecyclerView = (UltimateRecyclerView) rootView.findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (user_id.length() > 0) {
            mURL = getString(R.string.apiGetFeedRegUser, getString(R.string.apiRootMardi));
        } else {
            mURL = getString(R.string.apiGetFeedGuest, getString(R.string.apiRootMardi));
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.setOnItemClickListener(new FeedRVAdapter.MyClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    if (mAdapter != null) {
                        Feed feed = mAdapter.getItem(position);
                        Intent intent = new Intent(getActivity(), FeedDetails.class);
                        intent.putExtra("title", feed.getTitle());
                        intent.putExtra("content", feed.getContent());
                        intent.putExtra("dateTime", feed.getCreated_at());
                        intent.putExtra("createdBy", feed.getUser().getName());
                        if (feed.getAttachments().size() > 0) {
                            intent.putExtra("attachment", feed.getAttachments().get(position).getDrawables());
                        }

                        startActivity(intent);
                    }
                }
            });
        }
    }

    public ArrayList<Feed> getFeedDataSet() {

        try {

            if (mNetworkCheck.isNetworkConnected()) {
                mRecyclerView.setVisibility(View.VISIBLE);
                rlNoAnnounceChild.setVisibility(View.GONE);

                new GetFeeds(mToken, new OnTaskCompleted() {
                    @Override
                    public void onCompleted(String result) {
                        if (result != null) {
                            if (result.length() > 0) {
                                try {
                                    JSONObject objResult = new JSONObject(result);
                                    if (objResult.getInt("status") > 0) {
                                        JSONObject objData = objResult.getJSONObject("data");
                                        if ((objData.getString("next_page_url").equalsIgnoreCase("null"))) {
                                            mRecyclerView.disableLoadmore();
                                        } else {
                                            mRecyclerView.reenableLoadmore();
                                            mURL = objData.getString("next_page_url");
                                        }

                                        JSONArray arrData = objData.getJSONArray("data");
                                        JSONObject objDataData;
                                        feedArrayList = new ArrayList<>();
                                        ArrayList<Attachment> attachmentArrayList = new ArrayList<>();
                                        Feed feed;
                                        Attachment attachment;
                                        User user;

                                        JSONObject objUser;

                                        if(arrData.length() > 0){
                                            mRecyclerView.setVisibility(View.VISIBLE);
                                            rlNoAnnounceChild.setVisibility(View.GONE);

                                            for (int i = 0; i < arrData.length(); i++) {
                                                objDataData = arrData.getJSONObject(i);
                                                feed = new Feed();
                                                user = new User();
                                                attachment = new Attachment();
                                                attachment.setDrawables(R.drawable.persidangan);
                                                attachmentArrayList.add(attachment);
                                                feed.setFeed_id(i);
                                                feed.setTitle(objDataData.getString("title"));
                                                feed.setContent(objDataData.getString("content"));
                                                feed.setCreated_at(objDataData.getString("created_at"));
                                                feed.setAttachments(attachmentArrayList);
                                                feed.setNational_level(0);

                                                objUser = objDataData.getJSONObject("user");
                                                user.setName(objUser.getString("name"));
                                                feed.setUser(user);
                                                feedArrayList.add(feed);
                                            }

                                            populateList();
                                        }else{
                                            mRecyclerView.setVisibility(View.GONE);
                                            rlNoAnnounceChild.setVisibility(View.VISIBLE);
                                            imgNoData.setImageResource(R.drawable.error);
                                            txtNoSignal.setText(getString(R.string.strNoAnnouncement));
                                        }

                                    } else {
                                        mRecyclerView.setVisibility(View.GONE);
                                        rlNoAnnounceChild.setVisibility(View.VISIBLE);
                                        imgNoData.setImageResource(R.drawable.error);
                                        txtNoSignal.setText(getString(R.string.strNoAnnouncement));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
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

        return feedArrayList;

    }


//    public String loadJSONFromAsset() {
//        String json;
//        try {
//            InputStream is = getActivity().getAssets().open("feed.json");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            json = new String(buffer, "UTF-8");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//        return json;
//    }

    private class GetFeeds extends AsyncTask<Void, Void, String> {


        private OnTaskCompleted onTaskCompleted;
        private ProgressDialog progressDialog;
        private String mToken;

        GetFeeds(String token, OnTaskCompleted onTaskCompleted) {
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
                if (user_id.length() > 0) {
                    //TODO get feed as registered user
                    results = mWebService.getFeedRegUser(mURL, mToken);
                } else {
                    //TODO get feed as guest
                    results = mWebService.getFeedGuest(mURL);
                }

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


    private void populateList() {
        mRecyclerView.setHasFixedSize(true);
        Collections.sort(feedArrayList, new Comparator<Feed>() {
            public int compare(Feed s1, Feed s2) {
                return s2.getCreated_at().compareTo(s1.getCreated_at());
            }
        });
        mAdapter.setFeedData(feedArrayList);
        mRecyclerView.setAdapter(mAdapter);
    }
}
