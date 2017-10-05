package com.ingeniworks.mykomunitimardi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.ingeniworks.mykomunitimardi.R;
import com.ingeniworks.mykomunitimardi.model.Feed;
import com.marshalchen.ultimaterecyclerview.URLogs;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Nik on 17/4/2017.
 * FeedAdapter
 * Populating feed data ,eg:- Announcements
 */

public class FeedRVAdapter extends UltimateViewAdapter {
    private static String LOG_TAG = "FeedRVAdapter";
    private ArrayList<Feed> mDataset;
    private static MyClickListener myClickListener;
    private Context mContext;
//    private ChipCloudConfig drawableConfig;


    private static class DataObjectHolder extends UltimateRecyclerviewViewHolder
            implements View.OnClickListener {
        TextView title;
        TextView details;
        ImageView imgPhotos;
        FlexboxLayout flexbox;

        private DataObjectHolder(View itemView) {
            super(itemView);
            flexbox = (FlexboxLayout) itemView.findViewById(R.id.flexbox);
            title = (TextView) itemView.findViewById(R.id.txtTitle);
            details = (TextView) itemView.findViewById(R.id.txtDetails);
            imgPhotos = (ImageView) itemView.findViewById(R.id.imgPhotos);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        FeedRVAdapter.myClickListener = myClickListener;
    }

    public FeedRVAdapter(Context context) {
        mContext = context;
//        mDataset = myDataset;
//        drawableConfig = new ChipCloudConfig()
//                .selectMode(ChipCloud.SelectMode.multi)
//                .checkedChipColor(Color.parseColor("#ddaa00"))
//                .checkedTextColor(Color.parseColor("#ffffff"))
//                .uncheckedChipColor(Color.parseColor("#e0e0e0"))
//                .uncheckedTextColor(Color.parseColor("#000000"));

    }

    public void setFeedData(ArrayList<Feed> myDataset) {
        this.mDataset = myDataset;
        notifyDataSetChanged();
    }

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
                .inflate(R.layout.feed_cardview_item, parent, false);
        DataObjectHolder holder = new DataObjectHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder vh, int position) {
        try {
            final DataObjectHolder holder = (DataObjectHolder) vh;
            if (position < getItemCount() && (customHeaderView != null
                    ? position <= mDataset.size() : position < mDataset.size())
                    && (customHeaderView == null || position > 0)) {
                holder.title.setText(mDataset.get(position).getTitle());

                String details = mDataset.get(position).getContent();

                if (details.length() > 100)
                    holder.details.setText(mDataset.get(position).getContent().substring(0, 100) + "....");
                else
                    holder.details.setText(mDataset.get(position).getContent());
                if (mDataset.get(position).getAttachments().get(position).getDrawables() != 0) {

                    holder.imgPhotos.setVisibility(View.VISIBLE);
                    Picasso.with(mContext)
                            .load(mDataset.get(position).getAttachments().get(position).getDrawables())
                            .into(holder.imgPhotos);
                } else {
                    holder.imgPhotos.setVisibility(View.GONE);
                }


            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getLocalizedMessage());
        }
    }

    public Feed getItem(int position) {
        URLogs.d("position----" + position);
        if (customHeaderView != null)
            position--;
        if (position >= 0 && position < mDataset.size())
            return mDataset.get(position);
        else return null;
//        return mDataSet.get(position);
    }

//    public void addItem(Feed dataObj, int index) {
//        mDataset.add(dataObj);
//        notifyItemInserted(index);
//    }
//
//    public void deleteItem(int index) {
//        mDataset.remove(index);
//        notifyItemRemoved(index);
//    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getAdapterItemCount() {
        return mDataset.size();
    }

    @Override
    public long generateHeaderId(int position) {
        if (getItem(position).getTitle().length() > 0)
            return getItem(position).getTitle().charAt(0);
        else return -1;
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }
}
