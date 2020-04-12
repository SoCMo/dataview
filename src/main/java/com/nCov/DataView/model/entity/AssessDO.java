package com.nCov.DataView.model.entity;

import java.util.Date;

public class AssessDO {
    private Integer id;

    private Integer pathId;

    private String areaName;

    private Integer passOrder;

    private Integer cleanlinessScore;

    private Double time;

    private Double timeScore;

    private Integer crowdScore;

    private Integer localScore;

    private Double finalScore;

    private String startAddress;

    private Double sumTime;

    private Double sumScore;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPathId() {
        return pathId;
    }

    public void setPathId(Integer pathId) {
        this.pathId = pathId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName == null ? null : areaName.trim();
    }

    public Integer getPassOrder() {
        return passOrder;
    }

    public void setPassOrder(Integer passOrder) {
        this.passOrder = passOrder;
    }

    public Integer getCleanlinessScore() {
        return cleanlinessScore;
    }

    public void setCleanlinessScore(Integer cleanlinessScore) {
        this.cleanlinessScore = cleanlinessScore;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public Double getTimeScore() {
        return timeScore;
    }

    public void setTimeScore(Double timeScore) {
        this.timeScore = timeScore;
    }

    public Integer getCrowdScore() {
        return crowdScore;
    }

    public void setCrowdScore(Integer crowdScore) {
        this.crowdScore = crowdScore;
    }

    public Integer getLocalScore() {
        return localScore;
    }

    public void setLocalScore(Integer localScore) {
        this.localScore = localScore;
    }

    public Double getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(Double finalScore) {
        this.finalScore = finalScore;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress == null ? null : startAddress.trim();
    }

    public Double getSumTime() {
        return sumTime;
    }

    public void setSumTime(Double sumTime) {
        this.sumTime = sumTime;
    }

    public Double getSumScore() {
        return sumScore;
    }

    public void setSumScore(Double sumScore) {
        this.sumScore = sumScore;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}