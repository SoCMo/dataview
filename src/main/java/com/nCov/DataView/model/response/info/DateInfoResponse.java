package com.nCov.DataView.model.response.info;

import lombok.Data;

/**
 * program: DateInfoResponse
 * description: 某地区全部日期数据
 * author: SoCMo
 * create: 2020/2/24
 */
@Data
public class DateInfoResponse extends AreaInfoResponse {
    private String date;
}
