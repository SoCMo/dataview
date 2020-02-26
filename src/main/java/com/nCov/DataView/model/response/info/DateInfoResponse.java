package com.nCov.DataView.model.response.info;

import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * program: DateInfoResponse
 * description: 某地区全部日期数据
 * author: SoCMo
 * create: 2020/2/24
 */
@Data
public class DateInfoResponse extends AreaInfoResponse {
    private String date;

    public static DateInfoResponse objectCopy(DateInfoResponse dateInfoResponse) {
        DateInfoResponse dateInfoResponseNew = new DateInfoResponse();
        BeanUtils.copyProperties(dateInfoResponse, dateInfoResponseNew);
        return dateInfoResponseNew;
    }
}
