package com.nCov.DataView.model.response.info;

import lombok.Data;

/**
 * program: CovRankResponse
 * description: 排名信息返回
 * author: SoCMo
 * create: 2020/3/16
 */
@Data
public class CovRankResponse {
    private String provincename;

    private String name;

    private int remainConfirmRank;

    private int remainCountRank;

    private int growthRank;

    private double remainConfirm;

    private int remainCount;

    private int growth;

    private int remainScore;

    private int remainCountScore;

    private int growthScore;

    private double sumScore;

    private int allRank;
}
