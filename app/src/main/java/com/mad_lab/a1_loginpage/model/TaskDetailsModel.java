package com.mad_lab.a1_loginpage.model;

public class TaskDetailsModel {

    public String id, assignedDate, name, deadline, assignedBy;

    public TaskDetailsModel(String id, String assignedDate, String name, String deadline, String assignedBy){
        this.id = id;
        this.assignedDate = assignedDate;
        this.name = name;
        this.deadline = deadline;
        this.assignedBy = assignedBy;
    }
}
