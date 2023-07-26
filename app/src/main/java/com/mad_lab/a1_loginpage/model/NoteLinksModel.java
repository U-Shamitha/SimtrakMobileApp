package com.mad_lab.a1_loginpage.model;

public class NoteLinksModel {
    public String id, note, givenOn, givenBy,  linkType;

    public NoteLinksModel(String id, String note, String givenOn, String givenBy, String linkType) {
        this.id = id;
        this.note = note;
        this.givenOn = givenOn;
        this.givenBy = givenBy;
        this.linkType = linkType;
    }
}
