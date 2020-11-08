package com.pastel.dalpook.data;

import java.util.Calendar;

public class TodayModels {
    // 기록 내용
    private Calendar time;
    private String cont;
    private int color;
    private String flag;

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
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
