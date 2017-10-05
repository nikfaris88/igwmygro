package com.ingeniworks.mykomunitimardi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingeniworks.mykomunitimardi.R;
import com.ingeniworks.mykomunitimardi.Settings;
import com.ingeniworks.mykomunitimardi.model.Apps;
import com.marshalchen.ultimaterecyclerview.URLogs;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by nikfaris on 28/08/2017.
 */

public class ListAppAdapter extends UltimateViewAdapter {
    private static String LOG_TAG = "ListAppsAdapter";
    private static MyClickListener myClickListener;

    private Context mContext;
    private ArrayList<Apps> apps = new ArrayList<>();
    private Settings settings;
    private static LayoutInflater inflater = null;


    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private UltimateRecyclerView.OnLoadMoreListener onLoadMoreListener;


    public ListAppAdapter(Context context, ArrayList<Apps> apps) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.apps = apps;
        settings = new Settings();
        inflater = (LayoutInflater) mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void insert(String string, int position) {
        insertInternal(apps, string, position);
    }

    private static class DataObjectHolder extends UltimateRecyclerviewViewHolder
            implements View.OnClickListener {

        TextView txtAppName;
        ImageView imgAppIcon;

        private DataObjectHolder(View itemView) {
            super(itemView);

            txtAppName = (TextView) itemView.findViewById(R.id.txtAppsName);
            imgAppIcon = (ImageView) itemView.findViewById(R.id.imgAppsIcon);
//            txtAppName.setText(getItem(position).getApp_name());

//            Log.i(LOG_TAG, "Adding Listener");

            itemView.setOnClickListener(this);


//            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                        @Override
//                        public void onScrolled(RecyclerView recyclerView,
//                                               int dx, int dy) {
//                            super.onScrolled(recyclerView, dx, dy);
//
//                            totalItemCount = linearLayoutManager.getItemCount();
//                            lastVisibleItem = linearLayoutManager
//                                    .findLastVisibleItemPosition();
//                            if (!loading
//                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
//                                // End has been reached
//                                // Do something
//                                if (onLoadMoreListener != null) {
//                                    onLoadMoreListener.onLoadMore();
//                                }
//                                loading = true;
//                            }
//                        }
//                    });
//
//


        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);

//            // TODO Auto-generated method stub
//                System.out.println("position: " + position);
//            Intent i = mContext.getPackageManager().getLaunchIntentForPackage(getItem(position).getApp_package_name());
//            final String appPackageName = getItem(position).getApp_package_name(); // getPackageName() from Context or Activity object
//            if (i != null) {
//                mContext.startActivity(i);
//            } else {
//                try {
//                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//                } catch (android.content.ActivityNotFoundException anfe) {
//                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(settings_main.getGoogleplayStorePath() + appPackageName)));
//                }
//            }
//
//            WebView webView = new WebView(v.getContext());
//            webView.loadUrl(getItem(position).getGoogle_play_link());
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        ListAppAdapter.myClickListener = myClickListener;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder vh, int position) {
        try {
            final DataObjectHolder holder = (DataObjectHolder) vh;

            if (position < getItemCount() && (customHeaderView != null
                    ? position <= apps.size() : position < apps.size())
                    && (customHeaderView == null || position > 0)) {

                holder.txtAppName.setText(apps.get(position).getApp_name());

//                String details = mDataset.get(position).getFeed_message();

//                if (details.length() > 100)
//                    holder.details.setText(mDataset.get(position).getFeed_message().substring(0, 100) + "....");
//                else
//                    holder.details.setText(mDataset.get(position).getFeed_message());


//                chipCloud.addChip("agro");
//                chipCloud.addChip("pertanian");
//                chipCloud.addChip("ketum");
//                chipCloud.addChip("tips");

//                if (mDataset.get(position).getAttachments().size() > 0) {
                if (apps.get(position).getIcon() != 0) {

                    holder.imgAppIcon.setVisibility(View.VISIBLE);
                    Picasso.with(mContext)
                            .load(apps.get(position).getIcon())
                            .resize(500, 400)
                            .into(holder.imgAppIcon);
                }
//                else {
//                    holder.imgPhotos.setVisibility(View.GONE);
//                }


            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getLocalizedMessage());
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getAdapterItemCount() {
        return apps.size();
    }

    @Override
    public long generateHeaderId(int position) {
        if (getItem(position).getApp_name().length() > 0)
            return getItem(position).getApp_name().charAt(0);
        else return -1;
    }

//    @Override
//    public long generateHeaderId(int position) {
//        if (getItem(position).getFeed_title().length() > 0)
//            return getItem(position).getFeed_title().charAt(0);
//        else return -1;
//    }


    @Override
    public UltimateRecyclerviewViewHolder newFooterHolder(View view) {
        return new UltimateRecyclerviewViewHolder<>(view);
    }

    @Override
    public UltimateRecyclerviewViewHolder newHeaderHolder(View view) {
        return new UltimateRecyclerviewViewHolder<>(view);
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_apps_rows, parent, false);
        DataObjectHolder holder = new DataObjectHolder(v);
//        chipCloud = new ChipCloud(mContext, holder.flexbox, drawableConfig);

        return holder;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return apps.size();
    }


    public Apps getItem(int position) {
        // TODO Auto-generated method stub
//        return apps.get(position);

        URLogs.d("position log: " + position);
        if (customHeaderView != null)
            position--;
        if (position >= 0 && position < apps.size())
            return apps.get(position);
        else return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }
}
