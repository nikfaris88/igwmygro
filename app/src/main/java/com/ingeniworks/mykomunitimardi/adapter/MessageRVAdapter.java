package com.ingeniworks.mykomunitimardi.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.ingeniworks.mykomunitimardi.R;
import com.ingeniworks.mykomunitimardi.model.Message;
import com.ingeniworks.mykomunitimardi.utils.DateTime;
import com.marshalchen.ultimaterecyclerview.URLogs;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Nik on 17/4/2017.
 * ProjectRVAdapter
 * Populating rvlist data ,eg:- ListPeople
 */

public class MessageRVAdapter extends UltimateViewAdapter {
    private static String LOG_TAG = "MessageRVAdapter";
    private ArrayList<Message> mDataset;
    private static MyClickListener myClickListener;
    private DateTime dateTime = new DateTime();
    private Date date = null;

    private static class DataObjectHolder extends UltimateRecyclerviewViewHolder
            implements View.OnClickListener {
        TextView txtUserName;
        TextView txtUserPosition;
        TextView txtDateTime;
        ImageView imgUser;
        ImageView imgChat;

        private DataObjectHolder(View itemView) {
            super(itemView);
            txtUserName = (TextView) itemView.findViewById(R.id.txtTitle);
            txtUserPosition = (TextView) itemView.findViewById(R.id.txtDescription1);
            txtDateTime = (TextView) itemView.findViewById(R.id.txtDateTime);
            imgUser = (ImageView) itemView.findViewById(R.id.imgUser);
            imgChat = (ImageView) itemView.findViewById(R.id.imgChat);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        MessageRVAdapter.myClickListener = myClickListener;
    }

    public MessageRVAdapter(ArrayList<Message> myDataset) {
        mDataset = myDataset;
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
                .inflate(R.layout.rvlist_item, parent, false);
        return new DataObjectHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder vh, int position) {
        try {
            final DataObjectHolder holder = (DataObjectHolder) vh;
            if (position < getItemCount() && (customHeaderView != null
                    ? position <= mDataset.size() : position < mDataset.size())
                    && (customHeaderView == null || position > 0)) {

                String firstLetter = String.valueOf(mDataset.get(position).getMsg_title().charAt(0));

                ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
                // generate random color
                int color = generator.getColor(mDataset.get(position).getMsg_title());
                //int color = generator.getRandomColor();

                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(firstLetter, color); // radius in px

                holder.imgUser.setImageDrawable(drawable);
                holder.txtUserPosition.setText(mDataset.get(position).getMsg_desc());

                holder.imgChat.setVisibility(View.GONE);
                holder.txtDateTime.setVisibility(View.VISIBLE);
                holder.txtDateTime.setText(mDataset.get(position).getCreated_at());
                holder.txtUserName.setText(mDataset.get(position).getMsg_title());

            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getLocalizedMessage());
        }
    }

    public Message getItem(int position) {
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
        if (mDataset.get(position).getMsg_title().length() > 0)
            return mDataset.get(position).getMsg_title().charAt(0);
        else return -1;
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }
}
