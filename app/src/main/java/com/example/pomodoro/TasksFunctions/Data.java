package com.example.pomodoro.TasksFunctions;

public class Data{

    private String Content;
    private int Status;

    public Data() {
        // mặc định khi nhận data từ firebase
    }

    public Data(String content, int status) {
        Content = content;
        Status = status;
    }

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