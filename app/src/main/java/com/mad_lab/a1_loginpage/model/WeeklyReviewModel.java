package com.mad_lab.a1_loginpage.model;

public class WeeklyReviewModel {

    public String cardHeading, weeklyReviewId, status, week, remarks, stamp;

    public WeeklyReviewModel(String cardHeading, String id, String status, String week, String remarks, String stamp){
        this.cardHeading = cardHeading;
        this.weeklyReviewId = id;
        this.status = status;
        this.week = week;
        this.remarks = remarks;
        this.stamp = stamp;
    }
}
