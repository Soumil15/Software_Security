package com.example.fileManager.controller;

import java.util.Date;

public class EmailDTO {
    private String subject;
    private String sender;
    private Date date;
    private String content;

    public EmailDTO(String subject, String sender, Date date, String content) {
        this.subject = subject;
        this.sender = sender;
        this.date = date;
        this.content = content;
    }

    public String getSubject() { return subject; }
    public String getSender() { return sender; }
    public Date getDate() { return date; }
    public String getContent() { return content; }
}
