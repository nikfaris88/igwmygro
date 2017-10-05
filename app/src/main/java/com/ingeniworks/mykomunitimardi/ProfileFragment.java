package com.ingeniworks.mykomunitimardi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import static com.ingeniworks.mykomunitimardi.MainActivity.mFullName;
import static com.ingeniworks.mykomunitimardi.MainActivity.mUserId;


/**
 * Created by Nik on 17/4/2017.
 * Profile Fragment
 * Display user briefly user profile, settings_main menu and logout
 */

public class ProfileFragment extends Fragment {
    private View rootView;
    private SharedPreferences mSharedPreferences;
    private LinearLayout llIfLoggedIn;
    private LinearLayout llIfNotLoggedIn;
    private TextView txtUserName;
    private ImageView imgUser;

    public ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    public ProfileFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.profile_fragment_main, container, false);
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();

        String username;
        txtUserName.setText(mFullName);
        username = mFullName;

        if (mUserId.length() > 0) {
            // shown all post
            llIfLoggedIn.setVisibility(View.VISIBLE);
            llIfNotLoggedIn.setVisibility(View.GONE);
        } else {
            // you are guest or shown only public post
            llIfLoggedIn.setVisibility(View.GONE);
            llIfNotLoggedIn.setVisibility(View.VISIBLE);
        }


        String firstLetter = String.valueOf(username.charAt(0));

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getColor(username);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter, color); // radius in px
        imgUser.setImageDrawable(drawable);
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
        llIfLoggedIn = (LinearLayout) rootView.findViewById(R.id.llIfLoggedIn);
        llIfNotLoggedIn = (LinearLayout) rootView.findViewById(R.id.llIfNotLoggedIn);
        txtUserName = (TextView) rootView.findViewById(R.id.txtUserName);
        imgUser = (ImageView) rootView.findViewById(R.id.imgUser);
        rootView.findViewById(R.id.txtViewProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProfileActivity.class));
            }
        });

        rootView.findViewById(R.id.llSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
            }
        });

        rootView.findViewById(R.id.llInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
            }
        });


        rootView.findViewById(R.id.llLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharedPreferences.edit().clear().apply();
                getActivity().finish();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
    }
}
