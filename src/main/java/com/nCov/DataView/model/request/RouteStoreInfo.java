package com.nCov.DataView.model.request;

import lombok.Data;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * program: routeStoreInfo
 * description: 多路径存储兼查询请求
 * author: SoCMo
 * create: 2020/3/29
 */
@Data
public class RouteStoreInfo {
    @Min(value = 1, message = "至少应有一个路径")
    List<RouteListRequest> pathList;
}
