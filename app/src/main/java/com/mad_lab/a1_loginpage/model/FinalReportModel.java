package com.mad_lab.a1_loginpage.model;

public class FinalReportModel {

    public String cardHeading, reportId, status, fileName, uploadedOn;

    public FinalReportModel(String taskName, String id, String status, String fileName, String uploadedOn){
        this.cardHeading = taskName;
        this.reportId = id;
        this.status = status;
        this.fileName = fileName;
        this.uploadedOn = uploadedOn;
    }
}
