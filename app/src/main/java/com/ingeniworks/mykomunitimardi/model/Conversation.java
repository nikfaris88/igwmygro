package com.ingeniworks.mykomunitimardi.model;

import com.ingeniworks.mykomunitimardi.utils.DateTime;

import java.util.Date;

/**
 * Created by Nik on 17/4/2017.
 * Games Activity
 * Open Dashboard Games and view highscores
 */

public class Conversation {

    private int conversation_id = 0;
    private String conversation_title = "";
    private String conversation_query = "";
    private String conversation_respond = "";
    private int status = 0;
    private String created_at = "";
    private String updated_at = "";
    private String respond_created_at = "";
    private String respond_updated_at = "";
    private User user = new User();
    private DateTime dateTime = new DateTime();

    public int getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(int conversation_id) {
        this.conversation_id = conversation_id;
    }

    public String getConversation_title() {
        return conversation_title;
    }

    public void setConversation_title(String conversation_title) {
        this.conversation_title = conversation_title;
    }

    public String getConversation_query() {
        return conversation_query;
    }

    public void setConversation_query(String conversation_query) {
        this.conversation_query = conversation_query;
    }

    public String getConversation_respond() {
        return conversation_respond;
    }

    public void setConversation_respond(String conversation_respond) {
        this.conversation_respond = conversation_respond;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public User getUser() {
        return user;
    }

    public String getRespond_created_at() {
        Date date = dateTime.getDate(respond_created_at);
        return dateTime.getFormattedDate(date.getTime());
    }

    public void setRespond_created_at(String respond_created_at) {
        this.respond_created_at = respond_created_at;
    }

    public String getRespond_updated_at() {
        return respond_updated_at;
    }

    public void setRespond_updated_at(String respond_updated_at) {
        this.respond_updated_at = respond_updated_at;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
