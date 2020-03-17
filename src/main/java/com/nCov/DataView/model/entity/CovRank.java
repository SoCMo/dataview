package com.nCov.DataView.model.entity;

import lombok.Data;

/**
 * program: CovRank
 * description: 排名类
 * author: SoCMo
 * create: 2020/3/16
 */
@Data
public class CovRank {
    private int id;

    private String provincename;

    private String name;

    private double remainConfirm;

    private double dead;

    private double growth;

    private int remainConfirmRank;

    private int deadRank;

    private int growthRank;

    private int remainScore;

    private int deadScore;

    private int growthScore;

    private double sumScore;

    private int allRank;
}
