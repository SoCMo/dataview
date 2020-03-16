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
    private int id;

    private String provincename;

    private String name;

    private int remainConfirmRank;

    private int deadRank;

    private int growthRank;

    private double remainConfirm;

    private String dead;

    private String growth;
}
