package com.nCov.DataView.model.response.info;

import lombok.Data;

/**
 * program: DayProvinceInfo
 * description: 每日省数据
 * author: SoCMo
 * create: 2020/2/28
 */
@Data
public class AreaInfo {
    private String name;

    private int confirm;

    private int heal;

    private int dead;

    private int remainConfirm;

    private double RemainInMillion;

    private double weekGrowth;

    private int growth;
}
