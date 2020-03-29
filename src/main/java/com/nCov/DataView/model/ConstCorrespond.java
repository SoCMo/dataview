package com.nCov.DataView.model;

import lombok.Data;

/**
 * program: ConstCorrespond
 * description: 常量设置
 * author: SoCMo
 * create: 2019/12/14 12:13
 */
@Data
public class ConstCorrespond {
    //交通种类，0代表公交车，1代表地铁，2未知，3代表火车，4代表飞机。
    public final static String[] TRAN_TYPE = {
            "公交车",
            "地铁",
            "",
            "火车",
            "飞机"
    };

    public final static double[] CLEAN_SCORE = {
            80,
            100,
            0,
            100,
            70
    };

    public final static double[] SPEED = {
            30,
            32.9,
            0,
            250,
            900,
    };

    //人数(1/200人次),时间,清洁,当地的权重
    public final static double[] ROUTE_WEIGHT = {
            0.1,
            0.35,
            0.2,
            0.35
    };

    //人数(1/200人次)
    public final static double[] CROWD = {
            20,
            20,
            0,
            30,
            100
    };
}