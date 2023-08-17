package com.mad_lab.a1_loginpage.model;

public class NoteLinksModel {
    public String taskName, id, note, givenOn, givenBy,  linkType, noteLink;

    public NoteLinksModel(String taskName,String id, String givenOn, String givenBy,  String taskLink, String linkType) {
        this.taskName = taskName;
        this.id = id;
        this.givenOn = givenOn;
        this.givenBy = givenBy;
        this.noteLink = taskLink;
        this.linkType = linkType;
    }
}
