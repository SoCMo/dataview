package com.nCov.DataView.model.response.info;

import lombok.Data;

/**
 * program: provinceInfo
 * description: 省疫情信息
 * author: SoCMo
 * create: 2020/2/28
 */
@Data
public class DayInfo {
    private String date;

    private int confirm;

    public DayInfo(String time, int confirm) {
        this.date = time;

        this.confirm = confirm;
    }

    public DayInfo() {
        this.date = null;

        this.confirm = 0;
    }
}
