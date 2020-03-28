package com.nCov.DataView.model.entity;

import java.util.Date;

public class ImpAreaDO {
    private Integer id;

    private String provinceName;

    private String name;

    private Integer remainConfirmRank;

    private Integer remainCountRank;

    private Integer growthRank;

    private Double remainConfirm;

    private Integer remainCount;

    private Integer growth;

    private Integer remainScore;

    private Integer remainCountScore;

    private Integer growthScore;

    private Double sumScore;

    private Integer allrank;

    private Date date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName == null ? null : provinceName.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getRemainConfirmRank() {
        return remainConfirmRank;
    }

    public void setRemainConfirmRank(Integer remainConfirmRank) {
        this.remainConfirmRank = remainConfirmRank;
    }

    public Integer getRemainCountRank() {
        return remainCountRank;
    }

    public void setRemainCountRank(Integer remainCountRank) {
        this.remainCountRank = remainCountRank;
    }

    public Integer getGrowthRank() {
        return growthRank;
    }

    public void setGrowthRank(Integer growthRank) {
        this.growthRank = growthRank;
    }

    public Double getRemainConfirm() {
        return remainConfirm;
    }

    public void setRemainConfirm(Double remainConfirm) {
        this.remainConfirm = remainConfirm;
    }

    public Integer getRemainCount() {
        return remainCount;
    }

    public void setRemainCount(Integer remainCount) {
        this.remainCount = remainCount;
    }

    public Integer getGrowth() {
        return growth;
    }

    public void setGrowth(Integer growth) {
        this.growth = growth;
    }

    public Integer getRemainScore() {
        return remainScore;
    }

    public void setRemainScore(Integer remainScore) {
        this.remainScore = remainScore;
    }

    public Integer getRemainCountScore() {
        return remainCountScore;
    }

    public void setRemainCountScore(Integer remainCountScore) {
        this.remainCountScore = remainCountScore;
    }

    public Integer getGrowthScore() {
        return growthScore;
    }

    public void setGrowthScore(Integer growthScore) {
        this.growthScore = growthScore;
    }

    public Double getSumScore() {
        return sumScore;
    }

    public void setSumScore(Double sumScore) {
        this.sumScore = sumScore;
    }

    public Integer getAllrank() {
        return allrank;
    }

    public void setAllrank(Integer allrank) {
        this.allrank = allrank;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}