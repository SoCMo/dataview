package com.nCov.DataView.model.entity;

import com.nCov.DataView.model.ConstCorrespond;

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

    private Double sumScore;

    private Date updateTime;

    public double finalScoreCal() {
        if (timeScore == null || cleanlinessScore == null || localScore == null || crowdScore == null) {
            throw new NullPointerException("Assess计算时有分数为空");
        }

        return this.finalScore = ConstCorrespond.ROUTE_WEIGHT[0] * this.crowdScore
                + ConstCorrespond.ROUTE_WEIGHT[1] * (this.time >= 8 ? 100 : (time / 8 * 100))
                + ConstCorrespond.ROUTE_WEIGHT[2] * this.cleanlinessScore
                + ConstCorrespond.ROUTE_WEIGHT[3] * this.localScore;
    }

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