package com.ingeniworks.mykomunitimardi.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingeniworks.mykomunitimardi.R;
import com.ingeniworks.mykomunitimardi.Settings;
import com.ingeniworks.mykomunitimardi.model.Apps;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AppsGVAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Apps> apps = new ArrayList<>();
    private Settings settings;
    private static LayoutInflater inflater = null;

    public AppsGVAdapter(Context context, ArrayList<Apps> apps) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.apps = apps;
        settings = new Settings();
        inflater = (LayoutInflater) mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return apps.size();
    }

    @Override
    public Apps getItem(int position) {
        // TODO Auto-generated method stub
        return apps.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    private class Holder {
        TextView txtAppName;
        ImageView imgAppIcon;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;

        if (convertView == null) {
            rowView = inflater.inflate(R.layout.list_apps_rows, parent, false);
        } else {
            rowView = convertView;
        }
        holder.txtAppName = (TextView) rowView.findViewById(R.id.txtAppsName);
        holder.imgAppIcon = (ImageView) rowView.findViewById(R.id.imgAppsIcon);
        holder.txtAppName.setText(getItem(position).getApp_name());

        try {
            Picasso.with(mContext)
                    .load(getItem(position).getIcon())
                    .fit()
                    .error(R.drawable.no_signal_internet)
                    .placeholder(R.drawable.no_image)
                    .into(holder.imgAppIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                System.out.println("rowView clicked!!!");
                Intent i = mContext.getPackageManager().getLaunchIntentForPackage(getItem(position).getApp_package_name());
                final String appPackageName = getItem(position).getApp_package_name(); // getPackageName() from Context or Activity object
                if (i != null) {
                    mContext.startActivity(i);
                } else {
                    try {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(settings.getGoogleplayStorePath() + appPackageName)));
                    }
                }

                WebView webView = new WebView(v.getContext());
                webView.loadUrl(getItem(position).getGoogle_play_link());
            }
        });

        return rowView;
    }

} 