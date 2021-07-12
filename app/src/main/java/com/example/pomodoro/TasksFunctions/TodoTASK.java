package com.example.pomodoro.TasksFunctions;

public class TodoTASK {

    private String Content, Id, month;
    private int Status;

    public TodoTASK() {
        // mặc định khi nhận data từ firebase
    }

    public TodoTASK(String content, int status, String Month) {
        Content = content;
        Status = status;
        month = Month;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getId() { return Id; }

    public void setId(String id) { Id = id; }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}