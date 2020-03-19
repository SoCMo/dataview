package com.nCov.DataView.model.request;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * program: AreaListRequest
 * description: 地区列表
 * author: SoCMo
 * create: 2020/3/19
 */
@Data
public class AreaListRequest {
    @Size(min = 1, message = "地点列表不能为空")
    List<String> areaList;
}
