package com.ingeniworks.mykomunitimardi.model;

/**
 * Created by Nik on 17/4/2017.
 * Games Activity
 * Open Dashboard Games and view highscores
 */

public class Category {

    private int category_id = 0;
    private String category_desc = "";
    private String headerCategory = "";

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_desc() {
        return category_desc;
    }

    public void setCategory_desc(String category_desc) {
        this.category_desc = category_desc;
    }

    public String getHeaderCategory() {
        return headerCategory;
    }

    public void setHeaderCategory(String headerCategory) {
        this.headerCategory = headerCategory;
    }
}
