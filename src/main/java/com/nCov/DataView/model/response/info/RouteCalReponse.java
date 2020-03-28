package com.nCov.DataView.model.response.info;

import lombok.Data;

import java.util.List;

/**
 * program: RouteCalReponse
 * description: 路径评估返回
 * author: SoCMo
 * create: 2020/3/26
 */
@Data
public class RouteCalReponse {
    int type;

    String time;

    String start;

    String end;

    String title;

    List<CityCal> city;

    double finalscore;
}
