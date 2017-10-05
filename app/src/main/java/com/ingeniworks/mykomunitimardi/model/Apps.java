package com.ingeniworks.mykomunitimardi.model;

/**
 * Created by Nik on 27/7/2017.
 * List of MARDI Application
 */

public class Apps {
    private int id = 0;
    private String app_name = "";
    private String google_play_link = "";
    private String app_package_name = "";
    private int icon = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getGoogle_play_link() {
        return google_play_link;
    }

    public void setGoogle_play_link(String google_play_link) {
        this.google_play_link = google_play_link;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getApp_package_name() {
        return app_package_name;
    }

    public void setApp_package_name(String app_package_name) {
        this.app_package_name = app_package_name;
    }
}
