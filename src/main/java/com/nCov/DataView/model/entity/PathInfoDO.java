package com.nCov.DataView.model.entity;

public class PathInfoDO {
    private Integer id;

    private String start;

    private String end;

    private Integer mainType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start == null ? null : start.trim();
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end == null ? null : end.trim();
    }

    public Integer getMainType() {
        return mainType;
    }

    public void setMainType(Integer mainType) {
        this.mainType = mainType;
    }
}