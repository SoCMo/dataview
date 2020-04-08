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

    //用于查询路径后得到的交通种类与现存交通种类的转换
    //1:火车,2:飞机,3:公交,4:驾车,5:步行,6:大巴,7:地铁
    public final static Integer[] PATH_INFO_TYPE = {
            3,
            4,
            0,
            2,
            2,
            2,
            1
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

    //总剩余确诊，百万人剩余确诊, 14日增长, 周增长率, 境外输入数
    public final static double[] CITY_WEIGHT = {
            0.175,
            0.15,
            0.6,
            0.05,
            0.025
    };
}