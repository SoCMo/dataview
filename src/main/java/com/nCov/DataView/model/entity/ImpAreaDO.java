package com.nCov.DataView.model.entity;

import java.util.Date;

public class ImpAreaDO {
    private Integer id;

    private String provinceName;

    private String name;

    private Integer remainConfirmRank;

    private Integer remainCountRank;

    private Integer abroadInputRank;

    private Integer weekGrowthRank;

    private Integer growthRank;

    private Double remainConfirm;

    private Integer remainCount;

    private Double weekGrowth;

    private Integer abroadInput;

    private Integer growth;

    private Integer weekScore;

    private Integer abroadInputScore;

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

    public Integer getAbroadInputRank() {
        return abroadInputRank;
    }

    public void setAbroadInputRank(Integer abroadInputRank) {
        this.abroadInputRank = abroadInputRank;
    }

    public Integer getWeekGrowthRank() {
        return weekGrowthRank;
    }

    public void setWeekGrowthRank(Integer weekGrowthRank) {
        this.weekGrowthRank = weekGrowthRank;
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

    public Double getWeekGrowth() {
        return weekGrowth;
    }

    public void setWeekGrowth(Double weekGrowth) {
        this.weekGrowth = weekGrowth;
    }

    public Integer getAbroadInput() {
        return abroadInput;
    }

    public void setAbroadInput(Integer abroadInput) {
        this.abroadInput = abroadInput;
    }

    public Integer getGrowth() {
        return growth;
    }

    public void setGrowth(Integer growth) {
        this.growth = growth;
    }

    public Integer getWeekScore() {
        return weekScore;
    }

    public void setWeekScore(Integer weekScore) {
        this.weekScore = weekScore;
    }

    public Integer getAbroadInputScore() {
        return abroadInputScore;
    }

    public void setAbroadInputScore(Integer abroadInputScore) {
        this.abroadInputScore = abroadInputScore;
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