package com.ingeniworks.mykomunitimardi.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ingeniworks.mykomunitimardi.FeedFragment;
import com.ingeniworks.mykomunitimardi.ListApplicationFragment;
import com.ingeniworks.mykomunitimardi.MessageFragment;
import com.ingeniworks.mykomunitimardi.ProfileFragment;
import com.ingeniworks.mykomunitimardi.ProjectFragment;
import com.ingeniworks.mykomunitimardi.R;

public class MainTabAdapter extends FragmentPagerAdapter {

    private String title[];
    private SharedPreferences mSharedPreferences;

    public MainTabAdapter(Context context, FragmentManager fm, String[] title) {
        super(fm);
        this.title = title;
        mSharedPreferences = context.getSharedPreferences(context.getString(R.string.sessionString),
                Context.MODE_PRIVATE);


    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        String userId = mSharedPreferences.getString("user_id", "");

        System.out.println("Position clicked: " + position);
        if (userId.length() <= 0) {
            switch (position) {
                case 0:
                    fragment = new FeedFragment().newInstance();
                    break;
                case 1:
                    fragment = new ListApplicationFragment().newInstance();
                    break;
            }
        } else {
            switch (position) {
                case 0:
                    fragment = new FeedFragment().newInstance();
                    break;
                case 1:
                    fragment = new ProjectFragment().newInstance();
                    break;
                case 2:
                    fragment = new MessageFragment().newInstance();
                    break;
//                case 3:
//                    fragment = new NotificationFragment().newInstance();
//                    break;
                case 3:
                    fragment = new ProfileFragment().newInstance();
                    break;
                default:
                    fragment = new FeedFragment().newInstance();
                    break;
            }
        }


        return fragment;
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}