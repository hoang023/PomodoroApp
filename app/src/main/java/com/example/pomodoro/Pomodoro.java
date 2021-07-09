package com.example.pomodoro;

public class Pomodoro {
    String Completed;
    String Total;
    String TotalTime;

    public Pomodoro(){}

    public Pomodoro(String Completed, String Total, String TotalTime){
        this.Completed = Completed;
        this.Total = Total;
        this.TotalTime = TotalTime;
    }

    public String getCompleted() {
        return Completed;
    }

    public void setCompleted(String completed) {
        Completed = completed;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getTotalTime() {
        return TotalTime;
    }

    public void setTotalTime(String totalTime) {
        TotalTime = totalTime;
    }


}
