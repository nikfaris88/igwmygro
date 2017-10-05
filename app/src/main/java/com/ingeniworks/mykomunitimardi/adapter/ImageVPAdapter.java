package com.ingeniworks.mykomunitimardi.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ingeniworks.mykomunitimardi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by hafiz on 24/02/2016.
 */

public class ImageVPAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    private ArrayList<String> imageList;

    public ImageVPAdapter(Context context, ArrayList<String> imageList) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageList = imageList;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.image_list_pager, container, false);

        final ImageView imageView = (ImageView) itemView.findViewById(R.id.imgList);

        try {
            Picasso.with(mContext)
                    .load(imageList.get(position))
                    .into(imageView);
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(mContext, Message_detail_largeview.class);
//                    i.putExtra("arrImage", imageList);
//                    i.putExtra("position", position);
//                    mContext.startActivity(i);
//
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        (container).removeView((ImageView) object);
    }
}
