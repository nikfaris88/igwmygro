package com.ingeniworks.mykomunitimardi.model;

import com.ingeniworks.mykomunitimardi.utils.DateTime;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Nik on 17/4/2017.
 * Games Activity
 * Open Dashboard Games and view highscores
 */

public class Feed {

    private int feed_id = 0;
    private User user = new User();
    private int state_id = 0;
    private int district_id = 0;
    private Project project = new Project();
    private String title = "";
    private String content = "";
    private int national_level = 0;
    private int publish_status = 0;
    private String created_at = "";
    private String updated_at = "";
    private ArrayList<Attachment> attachments = new ArrayList<>();
    private DateTime dateTime = new DateTime();

    public int getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(int feed_id) {
        this.feed_id = feed_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getState_id() {
        return state_id;
    }

    public void setState_id(int state_id) {
        this.state_id = state_id;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNational_level() {
        return national_level;
    }

    public void setNational_level(int national_level) {
        this.national_level = national_level;
    }

    public int getPublish_status() {
        return publish_status;
    }

    public void setPublish_status(int publish_status) {
        this.publish_status = publish_status;
    }

    public String getCreated_at() {
        Date date = dateTime.getDate(created_at);
        return dateTime.getFormattedDate(date.getTime());
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        Date date = dateTime.getDate(updated_at);
        return dateTime.getFormattedDate(date.getTime());
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public ArrayList<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<Attachment> attachments) {
        this.attachments = attachments;
    }
}
