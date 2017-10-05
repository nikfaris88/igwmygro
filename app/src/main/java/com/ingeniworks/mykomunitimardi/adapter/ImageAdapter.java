package com.ingeniworks.mykomunitimardi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ingeniworks.mykomunitimardi.R;
import com.ingeniworks.mykomunitimardi.model.Attachment;
import com.ingeniworks.mykomunitimardi.utils.SquareImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Attachment> arrAttachment;

    public ImageAdapter(Context context, ArrayList<Attachment> arrAttachment) {
        this.context = context;
        this.arrAttachment = arrAttachment;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view;

        if (convertView == null) {

//            view = new View(context);
            // get layout from mobile.xml
            view = inflater.inflate(R.layout.feed_images_row, null);
            // set image based on selected text
            SquareImageView imageView = (SquareImageView) view.findViewById(R.id.imgThumbnail);
            try {
                Picasso.with(context)
                        .load("file://" + arrAttachment.get(position).getPath())
                        .fit()
                        .placeholder(R.drawable.defaultpic)
                        .error(R.drawable.no_image)
                        .into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            view = convertView;
        }

        return view;
    }

    @Override
    public int getCount() {
        return arrAttachment.size();
    }

    @Override
    public Attachment getItem(int position) {
        return arrAttachment.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}