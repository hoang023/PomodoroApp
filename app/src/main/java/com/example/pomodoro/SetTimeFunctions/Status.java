package com.example.pomodoro.SetTimeFunctions;

public class Status {

    public static String Focus;
    public static String  Break;
    public static String  Stage;

    public Status(){}

    public Status(String Focus, String Break, String Stage){
        this.Focus = Focus;
        this.Break = Break;
        this.Stage = Stage;
    }

    public String getFocus() {
        return Focus;
    }

    public void setFocus(String focus) {
        Focus = focus;
    }

    public String getBreak() {
        return Break;
    }

    public void setBreak(String aBreak) {
        Break = aBreak ;
    }

    public String getStage() {
        return Stage;
    }

    public void setStage(String stage) {
        Stage = stage;
    }


}