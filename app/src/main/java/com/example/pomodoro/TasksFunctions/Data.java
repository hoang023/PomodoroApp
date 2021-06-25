package com.example.pomodoro.TasksFunctions;

public class Data extends  TaskId{

    public String task;
    private String month;
    private String year;
    public int status;

    public Data() {
        // mặc định khi nhận data từ firebase
    }

    public Data(String Task, int Status) {
        task = Task;
        status = Status;
    }

    public String getTask() {
        return task;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public int getStatus() {
        return status;
    }
}