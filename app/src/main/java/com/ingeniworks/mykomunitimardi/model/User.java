package com.ingeniworks.mykomunitimardi.model;


public class User {

    private int user_id = 0;
    private int address_id = 0;
    private String username = "";
    private String alt_username = "";
    private String name = "";
    private String ic_no = "";
    private String hp_no = "";
    private String phone_no = "";
    private String email = "";
    private String remember_token = "";
    private String created_at = "";
    private String updated_at = "";

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAlt_username() {
        return alt_username;
    }

    public void setAlt_username(String alt_username) {
        this.alt_username = alt_username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIc_no() {
        return ic_no;
    }

    public void setIc_no(String ic_no) {
        this.ic_no = ic_no;
    }

    public String getHp_no() {
        return hp_no;
    }

    public void setHp_no(String hp_no) {
        this.hp_no = hp_no;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemember_token() {
        return remember_token;
    }

    public void setRemember_token(String remember_token) {
        this.remember_token = remember_token;
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
}
