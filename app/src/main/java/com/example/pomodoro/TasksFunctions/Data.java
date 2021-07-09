package com.example.pomodoro.TasksFunctions;

public class Data{

    private String Content;
    private Long Status;

    public Data() {
        // mặc định khi nhận data từ firebase
    }

    public Data(String content, Long status) {
        Content = content;
        Status = status;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Long getStatus() {
        return Status;
    }

    public void setStatus(Long status) {
        Status = status;
    }
}