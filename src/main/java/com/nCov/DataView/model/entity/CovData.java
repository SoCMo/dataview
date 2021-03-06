package com.nCov.DataView.model.entity;

import java.util.Date;

public class CovData {
    private Integer id;

    private String provincename;

    private String areaname;

    private Date date;

    private Integer isprovince;

    private Integer totalconfirm;

    private Integer totalsuspect;

    private Integer totaldead;

    private Integer totalheal;

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

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname == null ? null : areaname.trim();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getIsprovince() {
        return isprovince;
    }

    public void setIsprovince(Integer isprovince) {
        this.isprovince = isprovince;
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
}