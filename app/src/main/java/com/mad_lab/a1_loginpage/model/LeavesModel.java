package com.mad_lab.a1_loginpage.model;

public class LeavesModel {

    public String taskName, id, status, startDate, joiningDate, adminRemarks, leaderRemarks, reasonForLeave, responsibilitiesPassedTo;

    public LeavesModel(String taskName, String id, String status, String startDate, String joiningDate, String adminRemarks, String leaderRemarks, String reasonForLeave, String responsibilitiesPassedTo){
        this.taskName = taskName;
        this.id = id;
        this.status = status;
        this.startDate = startDate;
        this.joiningDate = joiningDate;
        this.adminRemarks = adminRemarks;
        this.leaderRemarks = leaderRemarks;
        this.reasonForLeave = reasonForLeave;
        this.responsibilitiesPassedTo = responsibilitiesPassedTo;
    }
}
