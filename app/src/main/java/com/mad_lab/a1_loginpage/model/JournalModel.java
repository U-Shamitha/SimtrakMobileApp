package com.mad_lab.a1_loginpage.model;

import java.util.ArrayList;

public class JournalModel {

    public String date, id, report;
    public String learnings, pendings, adminRemarks, leaderRemarks;
    public float rating;

    public JournalModel(String date, String id, String report, String learnings, String pendings, String adminRemarks, String leaderRemarks){
        this.date = date;
        this.id = id;
        this.report = report;
        this.learnings = learnings;
        this.pendings = pendings;
        this.adminRemarks = adminRemarks;
        this.leaderRemarks = leaderRemarks;
    }
}
