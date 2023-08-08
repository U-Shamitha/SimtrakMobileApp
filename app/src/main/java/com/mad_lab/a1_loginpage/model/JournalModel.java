package com.mad_lab.a1_loginpage.model;

import java.util.ArrayList;

public class JournalModel {

    public String date, id, journalStatus, report;
    public String learnings, pendings, adminRemarks, leaderRemarks;
    public float rating;

    public JournalModel(float rating, String id, String journalStatus, String date,  String report, String learnings, String pendings, String adminRemarks, String leaderRemarks){
        this.date = date;
        this.rating = rating;
        this.id = id;
        this.journalStatus = journalStatus;
        this.report = report;
        this.learnings = learnings;
        this.pendings = pendings;
        this.adminRemarks = adminRemarks;
        this.leaderRemarks = leaderRemarks;
    }
}
