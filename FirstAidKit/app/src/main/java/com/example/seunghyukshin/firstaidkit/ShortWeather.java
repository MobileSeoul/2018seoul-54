package com.example.seunghyukshin.firstaidkit;

public class ShortWeather {
    private String hour;  // 시간
    private String day;
    private String temp;  // 온도
    private String wfKor; // 상태
    private String pop; // 강수확률
    private String reh; // 습도
    private String pty; // 강수 상태
    private String tmx;
    private String tmn;

    public String getTmx() {
        return tmx;
    }

    public void setTmx(String tmx) {
        this.tmx = tmx;
    }

    public String getTmn() {
        return tmn;
    }

    public void setTmn(String tmn) {
        this.tmn = tmn;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getReh() {
        return reh;
    }

    public void setReh(String reh) {
        this.reh = reh;
    }

    public String getPop() {
        return pop;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setWfKor(String wfKor) {
        this.wfKor = wfKor;
    }

    public void setPty(String pty){
        this.pty = pty;
    }
    public String getHour() {
        return hour;
    }

    public String getTemp() {
        return temp;
    }

    public String getWfKor() {
        return wfKor;
    }

    public String getPty(){
        return pty;
    }

}
