package com.ingeniworks.mykomunitimardi;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.Toast;

import com.ingeniworks.mykomunitimardi.model.Attachment;
import com.ingeniworks.mykomunitimardi.utils.SquareImageView;
import com.ingeniworks.mykomunitimardi.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Nik on 03/11/2016.
 * Gallery Images
 */

public class CustomImageGallery extends AppCompatActivity {

    private ArrayList<Attachment> imagesEncodedList = new ArrayList<>();
    private ArrayList<String> imageUrls;
    private ImageAdapter imageAdapter;
    private String feed_desc = "";

    protected void onCreate(Bundle onSavedInstanceState) {
        super.onCreate(onSavedInstanceState);
        setContentView(R.layout.image_gallery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(getString(R.string.strPicture));
        }


        Bundle b = getIntent().getExtras();
        if (b != null) {
            feed_desc = b.getString("feed_desc");
        }

        if (Utility.checkPermission(CustomImageGallery.this)) {
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
            Cursor imagecursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                    null, orderBy + " DESC");

            this.imageUrls = new ArrayList<>();

            assert imagecursor != null;
            for (int i = 0; i < imagecursor.getCount(); i++) {
                imagecursor.moveToPosition(i);
                int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
                imageUrls.add(imagecursor.getString(dataColumnIndex));
            }
            imagecursor.close();

            imageAdapter = new ImageAdapter(this, imageUrls);
            GridView gridView = (GridView) findViewById(R.id.gvImages);
            gridView.setAdapter(imageAdapter);
            imageAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gallery_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_done) {
            //TODO calling api post message
            ArrayList<String> selectedItems = imageAdapter.getCheckedItems();

            if (selectedItems.size() > 10) {
                Toast.makeText(CustomImageGallery.this, getString(R.string.cannot_exceed_10), Toast.LENGTH_SHORT).show();
            } else {
                if (selectedItems.size() <= 0) {
                    onBackPressed();
                } else {
                    Attachment attach;
                    imagesEncodedList = new ArrayList<>();
                    if (selectedItems.size() == 1) {
                        // single pick
                        attach = new Attachment();
                        attach.setPath(selectedItems.get(0)); //data.getData().toString();
                        imagesEncodedList.add(attach);
                    } else {
                        // multiple pick
                        for (int i = 0; i < selectedItems.size(); i++) {
                            attach = new Attachment();
                            attach.setPath(selectedItems.get(i));
                            imagesEncodedList.add(attach);
//                            imagesEncodedList.add(uri.toString());
//                            In case you need image 's absolute path
                        }
                    }

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("imageList", imagesEncodedList);
                    Intent intent = new Intent(CustomImageGallery.this, CreateAnnouncement.class);
                    intent.putExtra("feed_desc", feed_desc);
                    intent.putExtra("action_by", "gallery");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }
//        } else if (id == R.id.action_open_camera) {
//            Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
//            startActivityForResult(intentPicture, REQUEST_CAMERA);
        } else if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

//    public Uri setImageUri() {
//        // Store image in dcim
//        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
//        Uri imgUri = Uri.fromFile(file);
//        this.imgCapturePath = file.getAbsolutePath();
//        return imgUri;
//    }

//    public String getImagePath() {
//        return imgCapturePath;
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == REQUEST_CAMERA) {
//                Attachment attach;
//                try {
//                    if (getImagePath().length() > 0) {
//                        imagesEncodedList = new ArrayList<>();
//                        attach = new Attachment();
//                        attach.setPath(getImagePath()); //data.getData().toString();
//                        imagesEncodedList.add(attach);
//
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("imageList", imagesEncodedList);
//                        Intent intent = new Intent(CustomImageGallery.this, CreateAnnouncement.class);
//                        intent.putExtra("action_by", "camera");
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                        finish();
//                    } else {
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }
//    }

    private class ImageAdapter extends BaseAdapter {

        ArrayList<String> mList;
        LayoutInflater mInflater;
        Context mContext;
        SparseBooleanArray mSparseBooleanArray;

        ImageAdapter(Context context, ArrayList<String> imageList) {
            // TODO Auto-generated constructor stub
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
            mSparseBooleanArray = new SparseBooleanArray();
            mList = new ArrayList<>();
            this.mList = imageList;

        }

        ArrayList<String> getCheckedItems() {
            ArrayList<String> mTempArry = new ArrayList<String>();

            for (int i = 0; i < mList.size(); i++) {
                if (mSparseBooleanArray.get(i)) {
                    mTempArry.add(mList.get(i));
                }
            }

            return mTempArry;
        }

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.gallery_item, null);
            }

            final CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.chkImage);
            final SquareImageView imageView = (SquareImageView) convertView.findViewById(R.id.imgThumbnail);

            try {
                Picasso.with(CustomImageGallery.this)
                        .load("file://" + imageUrls.get(position))
                        .placeholder(R.drawable.error)
                        .error(R.drawable.error)
                        .into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }


            mCheckBox.setTag(position);
            mCheckBox.setChecked(mSparseBooleanArray.get(position));
            mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);

            return convertView;
        }

        CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("imageList", imagesEncodedList);
        Intent intent = new Intent(CustomImageGallery.this, CreateAnnouncement.class);
        intent.putExtra("feed_desc", feed_desc);
        intent.putExtra("action_by", "gallery");
        intent.putExtras(bundle);
        startActivity(intent);
        finish();

    }
}
