package com.mad_lab.a1_loginpage.model;

public class NotesModel {

    public String taskName, id, note, givenOn, givenBy, timeTaken;

    public NotesModel(String taskName, String id, String note, String givenOn, String givenBy, String timeTaken){
        this.taskName = taskName;
        this.id = id;
        this.note = note;
        this.givenOn = givenOn;
        this.givenBy = givenBy;
        this.timeTaken = timeTaken;
    }
}
