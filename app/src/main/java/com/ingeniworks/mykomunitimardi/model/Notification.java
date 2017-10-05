package com.ingeniworks.mykomunitimardi.model;

/**
 * Created by Nik on 17/4/2017.
 * Games Activity
 * Open Dashboard Games and view highscores
 */

public class Notification {

    private int notification_id = 0;
    private String notification_msg = "";
    private String created_at = "";
    private String updated_at = "";
    private boolean isReadFlag = false;
    private User msg_user_from = new User();
    private User msg_user_to = new User();

    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public String getNotification_msg() {
        return notification_msg;
    }

    public void setNotification_msg(String notification_msg) {
        this.notification_msg = notification_msg;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public boolean isReadFlag() {
        return isReadFlag;
    }

    public void setReadFlag(boolean readFlag) {
        isReadFlag = readFlag;
    }

    public User getMsg_user_from() {
        return msg_user_from;
    }

    public void setMsg_user_from(User msg_user_from) {
        this.msg_user_from = msg_user_from;
    }

    public User getMsg_user_to() {
        return msg_user_to;
    }

    public void setMsg_user_to(User msg_user_to) {
        this.msg_user_to = msg_user_to;
    }
}
