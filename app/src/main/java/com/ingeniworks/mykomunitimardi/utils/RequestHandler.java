package com.ingeniworks.mykomunitimardi.utils;


import android.os.NetworkOnMainThreadException;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

class RequestHandler {
    private OutputStream mOutStream = null;
    private BufferedWriter mBufferedWriter = null;

    RequestHandler() {
    }

    String sendGetRequest(String uri) {
        try {
            System.out.println("sendGetRequest, Uri: " + uri);
            URL mUrl = new URL(uri);
            HttpURLConnection mConnection = (HttpURLConnection) mUrl.openConnection();
            mConnection.setReadTimeout(10000);
            mConnection.setConnectTimeout(15000);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(mConnection.getInputStream()));

            String result;

            StringBuilder sb = new StringBuilder();

            while ((result = bufferedReader.readLine()) != null) {
                sb.append(result);
            }

            return sb.toString();
        } catch (SocketTimeoutException e) {
            sendGetRequest(uri);
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    String sendPostRequest(String requestURL,
                           HashMap<String, String> postDataParams) throws Exception {
        URL url;
        HttpURLConnection conn = null;
        String response = "";
        try {
            System.out.println("requestURL: " + requestURL);
            url = new URL(requestURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Converting input to one string data params
            String mPostData = getPostDataString(postDataParams);
//            System.out.println("getContentType: " + conn.getContentType());
            // Post parameters
            mOutStream = conn.getOutputStream();
            mBufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(mOutStream, "UTF-8"));
            mBufferedWriter.write(mPostData);
            mBufferedWriter.flush();
            mBufferedWriter.close();
            // Get response code
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                // Get response result
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            }
        } catch (NetworkOnMainThreadException | IOException e) {
            e.printStackTrace();
        } finally {
            if (mOutStream != null) {
                try {
                    // this will close the bReader as well
                    mBufferedWriter.flush();
                    mOutStream.close();
                } catch (IOException ignored) {
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        Log.d("RequestHandler", "response: " + response);
        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
//        System.out.println("params: " + params);
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}