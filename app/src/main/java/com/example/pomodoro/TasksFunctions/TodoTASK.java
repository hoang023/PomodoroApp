package com.example.pomodoro.TasksFunctions;

public class TodoTASK {

    private String Content;
    private String Id;
    private int Status;

    public TodoTASK() {
        // mặc định khi nhận data từ firebase
    }

    public TodoTASK(String content, int status) {
        Content = content;
        Status = status;
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