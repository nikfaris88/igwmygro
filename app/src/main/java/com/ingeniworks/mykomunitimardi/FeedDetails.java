package com.ingeniworks.mykomunitimardi;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.ingeniworks.mykomunitimardi.utils.WrapContentHeightViewPager;
import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Nik 18/04/2016
 */

public class FeedDetails extends AppCompatActivity {

    private WrapContentHeightViewPager mViewPager;
    //    private ArrayList<String> imgList = new ArrayList<>();
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_details);

//        NetworkCheck mNetworkCheck = new NetworkCheck(FeedDetails.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.strFeedDetails));
        }

        String regex;
        Pattern p;
        Matcher m;

        regex = "(?<=<img src=\")[^\"]*";
        p = Pattern.compile(regex);

        TextView txtFeedTitle = (TextView) findViewById(R.id.txtFeedTitle);
        TextView txtFeed = (TextView) findViewById(R.id.txtFeed);
        TextView txtMsgType = (TextView) findViewById(R.id.txtFeedMsgType);
        TextView txtMsgDateTime = (TextView) findViewById(R.id.txtMsgDateTime);
        TextView txtFeedOleh = (TextView) findViewById(R.id.txtFeedOleh);
        ImageView imgAttachImage = (ImageView) findViewById(R.id.imgAttachImage);


//        mViewPager = (WrapContentHeightViewPager) findViewById(R.id.mViewPager);
        ImageView imgSmall = (ImageView) findViewById(R.id.img_small);

//        ImageButton left_nav = (ImageButton) findViewById(R.id.left_nav);
//        ImageButton right_nav = (ImageButton) findViewById(R.id.right_nav);

        Bundle b = getIntent().getExtras();

        if (b != null) {
            String title = b.getString("title");
            String content = b.getString("content");
            String datetime = b.getString("dateTime");
            String type = b.getString("msg_type");
            String createdby = b.getString("createdBy");
            int imgDrawable = b.getInt("attachment");
            System.out.println("imgDrawable: " + imgDrawable);
            if (imgDrawable > 0) {
                Picasso.with(FeedDetails.this)
                        .load(imgDrawable)
                        .into(imgAttachImage);

//                int imgWidth = imgAttachImage.getDrawable().getIntrinsicHeight();
//                System.out.println("height: " + imgWidth);
//
//                imgAttachImage.getLayoutParams().height = (int) (imgWidth * 0.5);

            } else
                imgAttachImage.getLayoutParams().height = 0;


//            int imgWidth = (int) (imgAttachImage.getDrawable().getIntrinsicHeight() * 0.5);
//            System.out.println("height: " + imgWidth);
//
//            imgAttachImage.getLayoutParams().height = imgWidth;

//            assert content != null;
//            m = p.matcher(content);
//            while (m.find()) {
//                imgList.add(m.group());
//            }
//
//            if (imgList.size() < 1) {
//                left_nav.setVisibility(View.GONE);
//                right_nav.setVisibility(View.GONE);
//                mViewPager.setVisibility(View.GONE);
//            } else if (imgList.size() == 1) {
//                left_nav.setVisibility(View.GONE);
//                right_nav.setVisibility(View.GONE);
//                mViewPager.setVisibility(View.VISIBLE);
//            } else {
//                left_nav.setVisibility(View.VISIBLE);
//                right_nav.setVisibility(View.VISIBLE);
//                mViewPager.setVisibility(View.VISIBLE);
//            }

//            try {
//                if (mNetworkCheck.isNetworkConnected()) {
//                    Log.d("FeedDetails", "imgList1: " + imgList);
//                    mViewPager.setAdapter(new ImageVPAdapter(FeedDetails.this, imgList));
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            {
                txtFeedTitle.setText(title);
//                txtFeed.setText(content);

            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (Build.VERSION.SDK_INT >= 24) {
                txtFeed.setText(Html.fromHtml(content.replaceAll("<img.+?>", ""), Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE));
            } else
                txtFeed.setText(content);

            txtMsgType.setText(type);
            txtMsgDateTime.setText(datetime);
            txtFeedOleh.setText(createdby);

            //get first letter of each String item
            String firstLetter = String.valueOf(createdby != null ? createdby.charAt(0) : 0);

            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            // generate random color
            int color = 0;
            if (createdby != null) {
                color = generator.getColor(createdby);
            }
//            int color = generator.getRandomColor();

            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(firstLetter, color); // radius in px

            imgSmall.setImageDrawable(drawable);

//            left_nav.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    currentPosition = mViewPager.getCurrentItem();
//                    Log.d("FeedDetails", "ggwp left" + currentPosition);
//                    if (currentPosition > 0) {
//                        mViewPager.setCurrentItem(currentPosition - 1);
//                    }
//                }
//            });
//            right_nav.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    currentPosition = mViewPager.getCurrentItem();
//                    if (currentPosition >= 0) {
//                        mViewPager.setCurrentItem(currentPosition + 1);
//                    }
//
//                }
//            });

        }

    }


    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return false;
    }
}
