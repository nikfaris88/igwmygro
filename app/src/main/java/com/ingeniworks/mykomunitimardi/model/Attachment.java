package com.ingeniworks.mykomunitimardi.model;

import java.io.Serializable;

/**
 * Created by Nik on 17/4/2017.
 * Games Activity
 * Open Dashboard Games and view highscores
 */

public class Attachment implements Serializable {
    private String path = "";
    private String type = "";
    private int drawables = 0;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDrawables() {
        return drawables;
    }

    public void setDrawables(int drawables) {
        this.drawables = drawables;
    }
}
