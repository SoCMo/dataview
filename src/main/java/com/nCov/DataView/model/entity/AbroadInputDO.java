package com.nCov.DataView.model.entity;

import java.util.Date;

public class AbroadInputDO {
    private Integer id;

    private String provincename;

    private Integer thenumber;

    private Date date;

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

    public Integer getThenumber() {
        return thenumber;
    }

    public void setThenumber(Integer thenumber) {
        this.thenumber = thenumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}