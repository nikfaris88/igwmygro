package com.ingeniworks.mykomunitimardi.utils;

/**
 * Created by Nik on 17/4/2017.
 * Games Activity
 * Open Dashboard Games and view highscores
 */

public class Settings {

    private String[] imageQuizRes = {"150x98_", "600x391_"};

    public String getImageQuizPath() {
        String imageQuizPath = "http://admin.myapp.my/image_upload/promo/";
        return imageQuizPath;
    }

    public String getImageQuizRes(String res) {
        if (res.equalsIgnoreCase("small")) {
            return imageQuizRes[0];
        } else {
            return imageQuizRes[1];
        }
    }
}