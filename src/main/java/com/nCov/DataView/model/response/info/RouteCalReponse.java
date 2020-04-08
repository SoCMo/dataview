package com.nCov.DataView.model.response.info;

import lombok.Data;

import java.util.List;

/**
 * program: RouteCalReponse
 * description: 单条路段评估返回
 * author: SoCMo
 * create: 2020/3/26
 */
@Data
public class RouteCalReponse {
    private int type;

    private String time;

    private String start;

    private String end;

    private String title;

    private List<CityCal> city;

    private String timeScore;

    private String transportScore;

    private double finalscore;
}
