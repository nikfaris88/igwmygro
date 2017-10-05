package com.ingeniworks.mykomunitimardi.model;


import java.util.ArrayList;

public class Project {

    private int project_id = 0;
    private int project_categoryId = 0;
    private String project_categoryName = "";
    private String project_title = "";
    private String project_position = "";
    private String project_start = "";
    private String project_end = "";
    private String project_EO;
    private String agency = "";

    private int section_id = 0;

    private ArrayList<Enrollment> enrollmentArrayList = new ArrayList<>();

    public Project(){

    }


    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }


    public String getProject_title() {
        return project_title;
    }

    public void setProject_title(String project_title) {
        this.project_title = project_title;
    }

    public String getProject_position() {
        return project_position;
    }

    public void setProject_position(String project_position) {
        this.project_position = project_position;
    }


    public int getProject_categoryId() {
        return project_categoryId;
    }

    public void setProject_categoryId(int project_categoryId) {
        this.project_categoryId = project_categoryId;
    }

    public String getProject_categoryName() {
        return project_categoryName;
    }

    public void setProject_categoryName(String project_categoryName) {
        this.project_categoryName = project_categoryName;
    }

    public String getProject_start() {
        return project_start;
    }

    public void setProject_start(String project_start) {
        this.project_start = project_start;
    }

    public String getProject_end() {
        return project_end;
    }

    public void setProject_end(String project_end) {
        this.project_end = project_end;
    }

    public String getProject_EO() {
        return project_EO;
    }

    public void setProject_EO(String project_EO) {
        this.project_EO = project_EO;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public ArrayList<Enrollment> getEnrollmentArrayList() {
        return enrollmentArrayList;
    }

    public void setEnrollmentArrayList(ArrayList<Enrollment> enrollmentArrayList) {
        this.enrollmentArrayList = enrollmentArrayList;
    }

    public int getSection_id() {
        return section_id;
    }

    public void setSection_id(int section_id) {
        this.section_id = section_id;
    }
}
