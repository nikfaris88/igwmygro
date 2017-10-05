package com.ingeniworks.mykomunitimardi.model;

import com.ingeniworks.mykomunitimardi.utils.DateTime;

import java.util.Date;

/**
 * Created by Nik on 17/4/2017.
 * Games Activity
 * Open Dashboard Games and view highscores
 */

public class Message {

    private int msg_id = 0;
    private int msg_type = 0;
    private String msg_title = "";
    private String msg_desc = "";
    private User msg_user_from = new User();
    private User msg_user_to = new User();
    private String created_at = "";
    private String updated_at = "";
    private DateTime dateTime = new DateTime();

//    public Message( int msg_id, int msg_type, String msg_title, String msg_desc, User msg_user_from,
//                    User msg_user_to, String created_at, String updated_at){
//
//        this.msg_id = msg_id;
//        this.msg_type = msg_type;
//        this.msg_title = msg_title;
//        this.msg_desc = msg_desc;
//        this.msg_user_from = msg_user_from;
//        this.msg_user_to = msg_user_to;
//        this.created_at = created_at;
//        this.updated_at = updated_at;
//
//    }

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public String getMsg_title() {
        return msg_title;
    }

    public void setMsg_title(String msg_title) {
        this.msg_title = msg_title;
    }

    public String getMsg_desc() {
        return msg_desc;
    }

    public void setMsg_desc(String msg_desc) {
        this.msg_desc = msg_desc;
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

    public String getCreated_at() {
        Date date = dateTime.getDate(created_at);
        return dateTime.getFormattedDate(date.getTime());
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


}
