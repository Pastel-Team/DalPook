package com.pastel.dalpook.data;

public class CalModels {
    private String time;
    private int color;
    private String flag;

    public CalModels() {
    }

    public CalModels(String time, int color, String flag) {
        this.time = time;
        this.color = color;
        this.flag = flag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getColor() {return color;}

    public void setColor(int color) {this.color = color;}

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
