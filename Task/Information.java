package com.example.pomodoro.Task;

public class Information {
    private String id;
    private String subject;
    // private int IconImage;
    public Information(String id, String subject) {
        this.id = id;
        this.subject = subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public String getId() {
        return id;
    }
}
