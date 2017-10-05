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
import com.ingeniworks.mykomunitimardi.model.Conversation;
import com.marshalchen.ultimaterecyclerview.URLogs;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;

/**
 * Created by Nik on 17/4/2017.
 * ProjectRVAdapter
 * Populating rvlist data ,eg:- List Respond
 */

public class RespondRVAdapter extends UltimateViewAdapter {
    private ArrayList<Conversation> mDataset;
//    private static MyClickListener myClickListener;

    private static class DataObjectHolder extends UltimateRecyclerviewViewHolder {
        TextView txtUserRespond;
        TextView txtUserRespondPos;
        TextView txtUserRespondDate;
        ImageView imgUserReply;
        TextView txtMessageRespond;

        private DataObjectHolder(View itemView) {
            super(itemView);
            txtUserRespond = (TextView) itemView.findViewById(R.id.txtUserRespond);
            txtUserRespondPos = (TextView) itemView.findViewById(R.id.txtUserRespondPos);
            txtUserRespondDate = (TextView) itemView.findViewById(R.id.txtUserRespondDate);
            imgUserReply = (ImageView) itemView.findViewById(R.id.imgUserReply);
            txtMessageRespond = (TextView) itemView.findViewById(R.id.txtMessageRespond);
//            itemView.setOnClickListener(this);
        }

//        @Override
//        public void onClick(View v) {
//            myClickListener.onItemClick(getAdapterPosition(), v);
//        }
    }

//    public void setOnItemClickListener(MyClickListener myClickListener) {
//        RespondRVAdapter.myClickListener = myClickListener;
//    }

    public RespondRVAdapter(ArrayList<Conversation> myDataset) {
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
                .inflate(R.layout.message_reply, parent, false);
        return new DataObjectHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder vh, int position) {
        try {
            final DataObjectHolder holder = (DataObjectHolder) vh;
            if (position < getItemCount() && (customHeaderView != null
                    ? position <= mDataset.size() : position < mDataset.size())
                    && (customHeaderView == null || position > 0)) {

                String firstLetter = String.valueOf(mDataset.get(position).getUser().getName().charAt(0));

                ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
                // generate random color
                int color = generator.getColor(mDataset.get(position).getUser().getName());
                //int color = generator.getRandomColor();

                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(firstLetter, color); // radius in px

                holder.imgUserReply.setImageDrawable(drawable);
                holder.txtUserRespondPos.setText(mDataset.get(position).getUser().getAlt_username());
                holder.txtUserRespondDate.setText(mDataset.get(position).getRespond_created_at());
                holder.txtUserRespond.setText(mDataset.get(position).getUser().getName());
                holder.txtMessageRespond.setText(mDataset.get(position).getConversation_respond());
            }
        } catch (Exception e) {
            e.printStackTrace();
            String LOG_TAG = "RespondRVAdapter";
            Log.e(LOG_TAG, e.getLocalizedMessage());
        }
    }

    public Conversation getItem(int position) {
        URLogs.d("position----" + position);
        if (customHeaderView != null)
            position--;
        if (position >= 0 && position < mDataset.size())
            return mDataset.get(position);
        else return null;
//        return mDataSet.get(position);
    }

    public void swapItem(ArrayList<Conversation> listConversation) {
        this.mDataset = listConversation;
        notifyDataSetChanged();
    }

//    public void addItem(Conversation dataObj) {
//        mDataset.add(dataObj);
//        notifyDataSetChanged();
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
        if (mDataset.get(position).getUser().getName().length() > 0)
            return mDataset.get(position).getUser().getName().charAt(0);
        else return -1;
    }

//    public interface MyClickListener {
//        void onItemClick(int position, View v);
//    }
}
