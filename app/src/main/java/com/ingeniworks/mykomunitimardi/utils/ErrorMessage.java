package com.ingeniworks.mykomunitimardi.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Nik on 19/04/2016.
 */
public class ErrorMessage {

    private Context mContext;

    public ErrorMessage(Context context){
        mContext = context;
    }

    public void showToast(String errMsg, String duration) throws Exception{
        if(duration.equalsIgnoreCase("short")){
            Toast.makeText(mContext, "" + errMsg, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mContext, "" + errMsg, Toast.LENGTH_LONG).show();
        }

    }
}
