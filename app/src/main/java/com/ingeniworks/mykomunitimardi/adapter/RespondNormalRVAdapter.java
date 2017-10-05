package com.ingeniworks.mykomunitimardi.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
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
import com.ingeniworks.mykomunitimardi.utils.DateTime;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Nik on 17/4/2017.
 * ProjectRVAdapter
 * Populating rvlist data ,eg:- List Respond
 */

public class RespondNormalRVAdapter extends Adapter<RespondNormalRVAdapter.DataObjectHolder> {
    private ArrayList<Conversation> mDataset;
    private DateTime dateTime = new DateTime();
    private Date date = null;

    public RespondNormalRVAdapter(ArrayList<Conversation> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_reply, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        try {
            String firstLetter = String.valueOf(mDataset.get(position).getUser().getName().charAt(0));

            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            // generate random color
            int color = generator.getColor(mDataset.get(position).getUser().getName());
            //int color = generator.getRandomColor();

            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(firstLetter, color); // radius in px

            holder.imgUserReply.setImageDrawable(drawable);
            holder.txtUserRespondPos.setText(mDataset.get(position).getUser().getAlt_username());

            if (!mDataset.get(position).getRespond_created_at().equalsIgnoreCase("null")) {
                date = dateTime.getDate(mDataset.get(position).getRespond_created_at());
                holder.txtUserRespondDate.setText(dateTime.getFormattedDate(date.getTime()));
            }

            holder.txtUserRespond.setText(mDataset.get(position).getUser().getName());
            holder.txtMessageRespond.setText(mDataset.get(position).getConversation_respond());
        } catch (Exception e) {
            e.printStackTrace();
            String LOG_TAG = "RespondRVAdapter";
            Log.e(LOG_TAG, e.getLocalizedMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
//    private static MyClickListener myClickListener;

    static class DataObjectHolder extends RecyclerView.ViewHolder {
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


    public Conversation getItem(int position) {
        return mDataset.get(position);
    }

    public void swapItem(ArrayList<Conversation> listConversation) {
        this.mDataset = listConversation;
        notifyDataSetChanged();
    }
}
