package com.paradigmshift.fyp.FirebaseDatamodel;

public class AppointmentDataModel {

    private String sced_by;
    private String sced_date;
    private String sced_time;
    private String sced_with;
    private String sced_review;

    public AppointmentDataModel(){}

    public AppointmentDataModel(String sced_by, String sced_date, String sced_time, String sced_with, String sced_review) {
        this.sced_by = sced_by;
        this.sced_date = sced_date;
        this.sced_time = sced_time;
        this.sced_with = sced_with;
        this.sced_review = sced_review;
    }

    public String getSced_by() {
        return sced_by;
    }

    public String getSced_date() {
        return sced_date;
    }

    public String getSced_time() {
        return sced_time;
    }

    public String getSced_with() {
        return sced_with;
    }

    public String getSced_review() {
        return sced_review;
    }
}
