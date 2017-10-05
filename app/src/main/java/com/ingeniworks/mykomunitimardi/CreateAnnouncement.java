package com.ingeniworks.mykomunitimardi;

import android.accounts.NetworkErrorException;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.ingeniworks.mykomunitimardi.model.Attachment;
import com.ingeniworks.mykomunitimardi.utils.NetworkCheck;
import com.ingeniworks.mykomunitimardi.utils.OnTaskCompleted;
import com.ingeniworks.mykomunitimardi.utils.SquareImageView;
import com.ingeniworks.mykomunitimardi.utils.Utility;
import com.ingeniworks.mykomunitimardi.utils.Webservice;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;

import static android.os.Build.VERSION_CODES.M;
import static com.ingeniworks.mykomunitimardi.SelectProject.projectId;
import static com.ingeniworks.mykomunitimardi.SelectProject.projectTitle;

public class CreateAnnouncement extends AppCompatActivity {
    private EditText etSubject, etTitle;
    private String mToken = "";
    private String mName = "";
    private ImageView imgUser;
    private Button btnSelectTo;
    private Webservice mWebservice;
    private NetworkCheck mNetworkCheck;
    private String title = "";
    private String content = "";
    private boolean isSelected = false;
    private ImageView imgOpenCamera, imgAttachFile;
    private String imagePath = "";
    private SquareImageView imgSingleImage;
    private GridView gvImages;

    private final static int CAMERA_REQ = 1;

    private ArrayList<Attachment> attachments = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compose_announcement);
        init();
        peopleInConversation();
        btnSelectTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateAnnouncement.this, SelectProject.class);
                startActivity(i);
            }
        });

        imgAttachFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.checkPermission(CreateAnnouncement.this)) {
                    Intent i = new Intent(CreateAnnouncement.this, CustomImageGallery.class);
                    i.putExtra("feed_desc", etSubject.getText().toString());
                    startActivity(i);
                    finish();
                }

            }


        });

        imgOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                startActivityForResult(intent, CAMERA_REQ);
            }
        });
    }

    public Uri setImageUri() {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        Uri photoURI;
        if (Build.VERSION.SDK_INT > M) {
            photoURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName()
                    + ".my.package.name.provider", file);
        } else {
            photoURI = Uri.fromFile(file);
        }
        this.imagePath = file.getAbsolutePath();
        return photoURI;
    }

    public String getImagePath() {
        return imagePath;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQ) {
                attachments.clear();
//                Uri selectedImageUri = data.getData();
//                imagePath = getPath(selectedImageUri);
//                File imageFile = new File(imagePath);
                System.out.println("filePath: file://" + getImagePath());
                Attachment attachment = new Attachment();
                attachment.setPath(getImagePath());
                attachments.add(attachment);

                if (attachments.size() > 0) {
                    gvImages.setVisibility(View.GONE);
                    imgSingleImage.setVisibility(View.VISIBLE);
                    try {
                        Picasso.with(CreateAnnouncement.this)
                                .load("file://" + attachments.get(0).getPath())
                                .error(R.drawable.error)
                                .placeholder(R.drawable.error)
                                .into(imgSingleImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(this, getString(R.string.strErrTechnicalIssue), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, getString(R.string.strErrTechnicalIssue), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.strErrTechnicalIssue), Toast.LENGTH_SHORT).show();
        }
    }

//    public String getPath(Uri uri) {
//        String[] projection = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
//        assert cursor != null;
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        int columnIndex = cursor.getColumnIndex(projection[0]);
//        String filePath = cursor.getString(columnIndex);
//        Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
//        return cursor.getString(column_index);
//    }


    private void init() {
        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.sessionString), Context.MODE_PRIVATE);
        mToken = mSharedPreferences.getString("token", "");
        mName = mSharedPreferences.getString("name", "");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.strPostTitle));
        }


        mWebservice = new Webservice(CreateAnnouncement.this);
        mNetworkCheck = new NetworkCheck(CreateAnnouncement.this);
        gvImages = (GridView) findViewById(R.id.gvImages);
        imgSingleImage = (SquareImageView) findViewById(R.id.imgSingleImage);
        imgUser = (ImageView) findViewById(R.id.imgUser);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etSubject = (EditText) findViewById(R.id.etSubject);
        btnSelectTo = (Button) findViewById(R.id.btnSelectTo);
        btnSelectTo.setText("Pilih Projek");
        projectTitle = "";

        imgAttachFile = (ImageView) findViewById(R.id.imgAttachFile);
        imgOpenCamera = (ImageView) findViewById(R.id.imgOpenCamera);
    }

    private void peopleInConversation() {
        String firstLetter = String.valueOf(mName.charAt(0));
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getColor(mName);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter, color); // radius in px
        imgUser.setImageDrawable(drawable);

    }

    @Override
    public void onBackPressed() {

        if (etTitle.getText() != null || etSubject.getText() != null) {
            new AlertDialog.Builder(this)
                    .setMessage("Adakah anda pasti hendak menutup aktiviti ini?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_send) {
            if (isValidated()) {
                title = etTitle.getText().toString();
                content = etSubject.getText().toString();

                try {
                    if (mNetworkCheck.isNetworkConnected()) {
                        new PostNewAnnouncement(mToken, new OnTaskCompleted() {
                            @Override
                            public void onCompleted(String result) {
                                if (result != null) {
                                    try {
                                        JSONObject objResult = new JSONObject(result);
                                        if (objResult.getInt("status") > 0) {
                                            Toast.makeText(CreateAnnouncement.this, "Message dihantar....", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).execute();
                    } else {
                        Toast.makeText(this, getString(R.string.strErrConnection), Toast.LENGTH_SHORT).show();
                    }
                } catch (SocketTimeoutException | SocketException | NetworkErrorException e) {
                    e.printStackTrace();
                }
            }
        } else if (id == android.R.id.home) {
            finish();
        }

        return super.

                onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (projectTitle.length() > 0) {
            btnSelectTo.setText(projectTitle);
            isSelected = true;
            btnSelectTo.setError(null);
        } else {
            btnSelectTo.setText(getString(R.string.strChooseProject));
            isSelected = false;
            btnSelectTo.setError(null);
        }

    }

    private boolean isValidated() {
        if (!isSelected) {
            btnSelectTo.requestFocus();
            btnSelectTo.setError(getString(R.string.strChooseProject));
            return false;
        }
        if (etTitle.getText().toString().length() == 0) {
            etTitle.requestFocus();
            etTitle.setError("Sila masukkan tajuk yang ingin dikongsi");
            return false;
        }
        if (etSubject.getText().toString().length() == 0) {
            etSubject.requestFocus();
            etSubject.setError("Sila isi ruangan butiran");
            return false;
        }

        etTitle.setError(null);
        etSubject.setError(null);
        btnSelectTo.setError(null);

        return true;
    }

    private class PostNewAnnouncement extends AsyncTask<Void, Void, String> {


        private OnTaskCompleted onTaskCompleted;
        private ProgressDialog progressDialog;
        private String mToken;

        PostNewAnnouncement(String token, OnTaskCompleted onTaskCompleted) {
            mToken = token;
            this.onTaskCompleted = onTaskCompleted;
        }

        protected void onPreExecute() {
            progressDialog = new ProgressDialog(CreateAnnouncement.this);
            progressDialog.setMax(100);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String results = null;
            try {
                results = mWebservice.postAnnouncement(mToken, String.valueOf(projectId),
                        title, content, attachments);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return results;
        }

        @Override
        protected void onPostExecute(final String success) {
            progressDialog.dismiss();
            onTaskCompleted.onCompleted(success);
        }

        @Override
        protected void onCancelled() {
            progressDialog.dismiss();
        }
    }


}
