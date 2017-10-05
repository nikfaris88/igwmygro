package com.ingeniworks.mykomunitimardi.utils;

import android.content.Context;
import android.os.StrictMode;

import com.ingeniworks.mykomunitimardi.R;
import com.ingeniworks.mykomunitimardi.model.Attachment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Webservice {

    private static final String LOG_TAG = "Webservice";
    private HashMap<String, String> mPostDataParams = new HashMap<>();
    private String mResponse = "";
    private Context mContext;
    private RequestHandler requestHandler;

    public Webservice(Context contex) {
        mContext = contex;
        requestHandler = new RequestHandler();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public String userLogin(String username, String password) throws Exception {
        mPostDataParams.clear();
        mPostDataParams.put("username", username);
        mPostDataParams.put("password", password);

        String urlLogin = mContext.getString(R.string.apiRootMardi) + mContext.getString(R.string.apiLogin);

        mResponse = requestHandler.sendPostRequest(urlLogin, mPostDataParams);

        return mResponse;
    }

    public String getFeedGuest(String url) throws Exception {
        mResponse = "";
        mResponse = requestHandler.sendGetRequest(url);

        return mResponse;
    }

    public String getFeedRegUser(String url, String token) throws Exception {
        mResponse = "";
        mPostDataParams.clear();
        mPostDataParams.put("token", token);
        mResponse = requestHandler.sendPostRequest(url, mPostDataParams);

        return mResponse;
    }

    public String getProjectData(String token, String sector_id) throws Exception {
        mResponse = "";
        mPostDataParams.clear();

        mPostDataParams.put("sector_id", sector_id);
        mPostDataParams.put("token", token);

        mResponse = requestHandler.sendPostRequest(mContext.getString(R.string.apiGetProjectData, mContext.getString(R.string.apiRootMardi)), mPostDataParams);

        return mResponse;

    }

    public String getMessages(String token) throws Exception {
        mResponse = "";
        mPostDataParams.clear();
        mPostDataParams.put("token", token);
        mResponse = requestHandler.sendPostRequest(mContext.getString(R.string.apiGetMessages, mContext.getString(R.string.apiRootMardi)), mPostDataParams);

        return mResponse;

    }

    public String getMessageDetails(String token, int msg_id) throws Exception {
        mResponse = "";
        mPostDataParams.clear();
        mPostDataParams.put("token", token);
        mResponse = requestHandler.sendPostRequest(mContext.getString(R.string.apiGetMessageDetails,
                mContext.getString(R.string.apiRootMardi), msg_id), mPostDataParams);

        return mResponse;

    }

    public String postReply(String token, String msg_id, String message, String status) throws Exception {
        mResponse = "";
        mPostDataParams.clear();
        mPostDataParams.put("token", token);
        mPostDataParams.put("message", message);
        mPostDataParams.put("conversation_id", msg_id);
        mPostDataParams.put("status", status);

        System.out.println("token: " + token);
        System.out.println("message: " + message);
        System.out.println("conversation_id: " + msg_id);
        System.out.println("status: " + status);
        mResponse = requestHandler.sendPostRequest(mContext.getString(R.string.apiPostRespond,
                mContext.getString(R.string.apiRootMardi)), mPostDataParams);

        return mResponse;

    }


    public String createMessage(String token, String projectId, String title, String content) throws Exception {
        mResponse = "";
        mPostDataParams.clear();
        mPostDataParams.put("token", token);
        mPostDataParams.put("project_id", projectId);
        mPostDataParams.put("title", title);
        mPostDataParams.put("content", content);

        mResponse = requestHandler.sendPostRequest(mContext.getString(R.string.apiCreateMessage,
                mContext.getString(R.string.apiRootMardi)), mPostDataParams);

        return mResponse;
    }

//    public String postAnnouncement(String token, String projectId, String title, String content) throws Exception {
//        mResponse = "";
//        mPostDataParams.clear();
//        mPostDataParams.put("token", token);
//        mPostDataParams.put("project_id", projectId);
//        mPostDataParams.put("title", title);
//        mPostDataParams.put("content", content);
//
//        System.out.println("token: " + token);
//        System.out.println("project_id: " + projectId);
//        System.out.println("title: " + title);
//        System.out.println("content: " + content);
//
//        mResponse = requestHandler.sendPostRequest(mContext.getString(R.string.apiCreateAnnouncement,
//                mContext.getString(R.string.apiRootMardi)), mPostDataParams);
//
//        return mResponse;
//    }

    public String postAnnouncement(String token, String projectId, String title, String content, ArrayList<Attachment> attachments) {

        String charset = "UTF-8";

        try {
            FileUploader multipart = new FileUploader(mContext.getString(R.string.apiCreateAnnouncement,
                    mContext.getString(R.string.apiRootMardi)), charset);
//            multipart.addHeaderField("User-Agent", "CodeJava");
//            multipart.addHeaderField("Test-Header", "Header-Value");
            if (attachments.size() > 0) {
                File sourceFile[] = new File[attachments.size()];
//            File oneFile = new File(imgPaths.get(0).image);
                for (int i = 0; i < attachments.size(); i++) {
                    sourceFile[i] = new File(attachments.get(i).getPath());
                    // Toast.makeText(getApplicationContext(),imgPaths.get(i),Toast.LENGTH_SHORT).show();
                }

                for (int i = 0; i < attachments.size(); i++) {
                    System.out.println("attachments: " + sourceFile[i]);
                    multipart.addFilePart("image[]", sourceFile[i]);
                }
            }

            System.out.println("token: " + token);
            multipart.addFormField("token", token);
            multipart.addFormField("project_id", projectId);
            multipart.addFormField("title", title);
            multipart.addFormField("content", content);

            List<String> response = multipart.finish();

            System.out.println("SERVER REPLIED:");

            for (String line : response) {
                mResponse = line;
            }


        } catch (IOException ex) {
            System.err.println("IOException: " + ex);
        }
        System.out.println("SERVER REPLIED:" + mResponse);
        return mResponse;
    }

    public String resetPassword(String password) throws Exception {
        mPostDataParams.clear();
        mPostDataParams.put("password", password);

        String urlLogin = mContext.getString(R.string.apiRootMardi) + mContext.getString(R.string.apiLogin);
        mResponse = requestHandler.sendPostRequest(urlLogin, mPostDataParams);

        return mResponse;
    }

}
