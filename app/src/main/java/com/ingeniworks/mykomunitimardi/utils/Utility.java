package com.ingeniworks.mykomunitimardi.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class Utility {
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    public static boolean checkPermission(final Context context) {
//        int currentAPIVersion = Build.VERSION.SDK_INT;
//
//        if (currentAPIVersion >= Build.VERSION_CODES.M) {
//
//            int write_permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            int read_permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
//
//            if (write_permission != PackageManager.PERMISSION_GRANTED && read_permission != PackageManager.PERMISSION_GRANTED) {
//                // We don't have permission so prompt the user
//                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
//                        Manifest.permission.READ_EXTERNAL_STORAGE) &&
//                        ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
//                    alertBuilder.setCancelable(true);
//                    alertBuilder.setTitle("Permission necessary");
//                    alertBuilder.setMessage("External storage permission is necessary");
//                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//                        public void onClick(DialogInterface dialog, int which) {
////                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//                            ActivityCompat.requestPermissions(
//                                    (Activity) context,
//                                    PERMISSIONS_STORAGE,
//                                    REQUEST_EXTERNAL_STORAGE
//                            );
//
//
//                        }
//                    });
//                    AlertDialog alert = alertBuilder.create();
//                    alert.show();
//                    return false;
//                } else {
//                    ActivityCompat.requestPermissions(
//                            (Activity) context,
//                            PERMISSIONS_STORAGE,
//                            REQUEST_EXTERNAL_STORAGE
//                    );
////                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//                    return true;
//                }
//            } else {
//                return true;
//            }
//
////            if (!verifyStoragePermissions((Activity) context)) {
////                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
////                        Manifest.permission.READ_EXTERNAL_STORAGE) &&
////                        ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
////                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
////                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
////                    alertBuilder.setCancelable(true);
////                    alertBuilder.setTitle("Permission necessary");
////                    alertBuilder.setMessage("External storage permission is necessary");
////                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
////                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
////                        public void onClick(DialogInterface dialog, int which) {
//////                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
////                            ActivityCompat.requestPermissions(
////                                    (Activity) context,
////                                    PERMISSIONS_STORAGE,
////                                    REQUEST_EXTERNAL_STORAGE
////                            );
////                        }
////                    });
////                    AlertDialog alert = alertBuilder.create();
////                    alert.show();
////                } else {
////                    ActivityCompat.requestPermissions(
////                            (Activity) context,
////                            PERMISSIONS_STORAGE,
////                            REQUEST_EXTERNAL_STORAGE
////                    );
//////                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
////                }
////                return false;
////            } else {
////                return true;
////            }
//        } else {
//            return true;
//        }
//    }
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        final List<String> permissionNeeded = new ArrayList<>();

        if (currentAPIVersion >= Build.VERSION_CODES.M) {

            int write_permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int read_permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);

            if (write_permission != PackageManager.PERMISSION_GRANTED) {
                permissionNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (read_permission != PackageManager.PERMISSION_GRANTED) {
                permissionNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            System.out.println("permissionNeeded: " + permissionNeeded);

            if (!permissionNeeded.isEmpty()) {
//                ActivityCompat.requestPermissions((Activity) context, permissionNeeded.toArray(new String[permissionNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
//                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
//                        Manifest.permission.READ_EXTERNAL_STORAGE) &&
//                        ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//
//                }
                android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(context);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Permission necessary");
                alertBuilder.setMessage("External storage permission is necessary");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                        ActivityCompat.requestPermissions((Activity) context,
                                permissionNeeded.toArray(new String[permissionNeeded.size()]),
                                REQUEST_ID_MULTIPLE_PERMISSIONS);
//                        ActivityCompat.requestPermissions(
//                                (Activity) context,
//                                PERMISSIONS_STORAGE,
//                                REQUEST_EXTERNAL_STORAGE
//                        );


                    }
                });
                android.support.v7.app.AlertDialog alert = alertBuilder.create();
                alert.show();

                return false;
            }

            return true;
//            if (write_permission != PackageManager.PERMISSION_GRANTED && read_permission != PackageManager.PERMISSION_GRANTED) {
//                // We don't have permission so prompt the user
//                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
//                        Manifest.permission.READ_EXTERNAL_STORAGE) &&
//                        ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
//                    alertBuilder.setCancelable(true);
//                    alertBuilder.setTitle("Permission necessary");
//                    alertBuilder.setMessage("External storage permission is necessary");
//                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//                        public void onClick(DialogInterface dialog, int which) {
////                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//                            ActivityCompat.requestPermissions(
//                                    (Activity) context,
//                                    PERMISSIONS_STORAGE,
//                                    REQUEST_EXTERNAL_STORAGE
//                            );
//
//
//                        }
//                    });
//                    AlertDialog alert = alertBuilder.create();
//                    alert.show();
//                    return false;
//                } else {
//                    ActivityCompat.requestPermissions(
//                            (Activity) context,
//                            PERMISSIONS_STORAGE,
//                            REQUEST_EXTERNAL_STORAGE
//                    );
////                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//                    return true;
//                }
//            } else {
//                return true;
//            }

//            if (!verifyStoragePermissions((Activity) context)) {
//                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
//                        Manifest.permission.READ_EXTERNAL_STORAGE) &&
//                        ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
//                    alertBuilder.setCancelable(true);
//                    alertBuilder.setTitle("Permission necessary");
//                    alertBuilder.setMessage("External storage permission is necessary");
//                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//                        public void onClick(DialogInterface dialog, int which) {
////                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//                            ActivityCompat.requestPermissions(
//                                    (Activity) context,
//                                    PERMISSIONS_STORAGE,
//                                    REQUEST_EXTERNAL_STORAGE
//                            );
//                        }
//                    });
//                    AlertDialog alert = alertBuilder.create();
//                    alert.show();
//                } else {
//                    ActivityCompat.requestPermissions(
//                            (Activity) context,
//                            PERMISSIONS_STORAGE,
//                            REQUEST_EXTERNAL_STORAGE
//                    );
////                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//                }
//                return false;
//            } else {
//                return true;
//            }

        } else {
            return true;
        }
    }

}