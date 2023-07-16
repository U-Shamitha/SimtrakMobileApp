package com.mad_lab.a1_loginpage.model;

public class TaskDetailsModel {

    public String id, assignedDate, name, deadline, assignedBy, priority, type;

    public TaskDetailsModel(String id, String name, String assignedDate, String priority, String type, String assignedBy){
        this.id = id;
        this.assignedDate = assignedDate;
        this.priority = priority;
        this.name = name;
        this.type = type;
        this.assignedBy = assignedBy;
    }


}
