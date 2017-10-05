package com.ingeniworks.mykomunitimardi.model;

import java.util.ArrayList;

/**
 * Created by Nik on 17/4/2017.
 * Games Activity
 * Open Dashboard Games and view highscores
 */

public class UserCategory {
    private String headerTitle;
    private ArrayList<User> usersList;


    public UserCategory() {

    }

    public UserCategory(String headerTitle, ArrayList<User> usersList) {
        this.headerTitle = headerTitle;
        this.usersList = usersList;
    }


    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<User> getUsersList() {
        return usersList;
    }

    public void setUsersList(ArrayList<User> usersList) {
        this.usersList = usersList;
    }
}
