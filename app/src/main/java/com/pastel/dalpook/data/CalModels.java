package com.pastel.dalpook.data;

public class CalModels {
    private String time;
    private String flag;

    public CalModels() {
    }

    public CalModels(String time, String flag) {
        this.time = time;
        this.flag = flag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
