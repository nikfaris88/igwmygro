package com.ingeniworks.mykomunitimardi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ingeniworks.mykomunitimardi.adapter.NotificationRVAdapter;
import com.ingeniworks.mykomunitimardi.model.Notification;
import com.ingeniworks.mykomunitimardi.model.User;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;


/**
 * Created by Nik on 17/4/2017.
 * Feed Fragment
 * Do the activity for feed
 */

public class NotificationFragment extends Fragment {
    private UltimateRecyclerView mRecyclerView;
    private NotificationRVAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "NotificationFragment";
    private View rootView;
//    private FloatingActionButton fab;
    private SharedPreferences mSharedPreferences;
    private RelativeLayout rlIfLoggedIn;
    private LinearLayout llIfNotLoggedIn;

    public NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.rv_main, container, false);
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();

        String userId = mSharedPreferences.getString("user_id", "");
        if (userId.length() > 0) {
            // shown all post
            rlIfLoggedIn.setVisibility(View.VISIBLE);
            llIfNotLoggedIn.setVisibility(View.GONE);
        } else {
            // you are guest or shown only public post
            rlIfLoggedIn.setVisibility(View.GONE);
            llIfNotLoggedIn.setVisibility(View.VISIBLE);
        }

        rootView.findViewById(R.id.btnGoLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        return rootView;
    }

    private void init() {
        mSharedPreferences = getActivity().getSharedPreferences(getString(R.string.sessionString), Context.MODE_PRIVATE);

        rlIfLoggedIn = (RelativeLayout) rootView.findViewById(R.id.rlIfLoggedIn);
        llIfNotLoggedIn = (LinearLayout) rootView.findViewById(R.id.llIfNotLoggedIn);

//        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
//        fab.setVisibility(View.GONE);
        mRecyclerView = (UltimateRecyclerView) rootView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NotificationRVAdapter(getActivity(), getDataSet());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.setOnItemClickListener(new NotificationRVAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        });
    }

    private ArrayList<Notification> getDataSet() {
        ArrayList results = new ArrayList<>();
        Notification notification;
        User user;
        int countNoti = 0;


//        for (int index = 0; index < 10; index++) {
        notification = new Notification();
        user = new User();
        notification.setNotification_id(countNoti + 1);
        user.setName("Cik Hidayah");
        notification.setNotification_msg("Cik Hidayah Ahmad has reply your message");
        notification.setUpdated_at("just now");
        notification.setMsg_user_from(user);
        results.add(notification);

        notification = new Notification();
        user = new User();
        user.setName("Cik Siti");
        notification.setNotification_id(countNoti + 1);
        notification.setNotification_msg("Cik Siti has reply your message");
        notification.setUpdated_at("13.15");
        notification.setMsg_user_from(user);
        results.add(notification);

        notification = new Notification();
        user = new User();
        user.setName("Faisal Mohd");
        notification.setNotification_id(countNoti + 1);
        notification.setNotification_msg("Faisal Mohd has reply your message");
        notification.setUpdated_at("15.55");
        notification.setMsg_user_from(user);
        results.add(notification);
//        }
        return results;
    }
}
