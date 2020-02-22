package com.nCov.DataView.model.entity;

import java.util.Date;

public class CovData {
    private Integer id;

    private String provincename;

    private String cityname;

    private Date date;

    private Integer totalconfirm;

    private Integer totalsuspect;

    private Integer totaldead;

    private Integer totalheal;

    private Integer todayconfirm;

    private Integer todaysuspect;

    private Integer todaydead;

    private Integer todayheal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProvincename() {
        return provincename;
    }

    public void setProvincename(String provincename) {
        this.provincename = provincename == null ? null : provincename.trim();
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname == null ? null : cityname.trim();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getTotalconfirm() {
        return totalconfirm;
    }

    public void setTotalconfirm(Integer totalconfirm) {
        this.totalconfirm = totalconfirm;
    }

    public Integer getTotalsuspect() {
        return totalsuspect;
    }

    public void setTotalsuspect(Integer totalsuspect) {
        this.totalsuspect = totalsuspect;
    }

    public Integer getTotaldead() {
        return totaldead;
    }

    public void setTotaldead(Integer totaldead) {
        this.totaldead = totaldead;
    }

    public Integer getTotalheal() {
        return totalheal;
    }

    public void setTotalheal(Integer totalheal) {
        this.totalheal = totalheal;
    }

    public Integer getTodayconfirm() {
        return todayconfirm;
    }

    public void setTodayconfirm(Integer todayconfirm) {
        this.todayconfirm = todayconfirm;
    }

    public Integer getTodaysuspect() {
        return todaysuspect;
    }

    public void setTodaysuspect(Integer todaysuspect) {
        this.todaysuspect = todaysuspect;
    }

    public Integer getTodaydead() {
        return todaydead;
    }

    public void setTodaydead(Integer todaydead) {
        this.todaydead = todaydead;
    }

    public Integer getTodayheal() {
        return todayheal;
    }

    public void setTodayheal(Integer todayheal) {
        this.todayheal = todayheal;
    }
}