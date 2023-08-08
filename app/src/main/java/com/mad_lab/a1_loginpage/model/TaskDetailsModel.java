package com.mad_lab.a1_loginpage.model;

public class TaskDetailsModel {

    public String id, status, assignedDate, name, deadline, assignedBy, priority, type, description;

    public TaskDetailsModel(String id, String status, String name, String deadline, String assignedDate, String priority, String type, String assignedBy, String description){
        this.id = id;
        this.status = status;
        this.assignedDate = assignedDate;
        this.priority = priority;
        this.name = name;
        this.type = type;
        this.assignedBy = assignedBy;
        this.deadline = deadline;
        this.description = description;
    }


}
