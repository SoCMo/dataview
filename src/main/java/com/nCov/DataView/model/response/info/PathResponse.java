package com.nCov.DataView.model.response.info;

import lombok.Data;

import java.util.List;

/**
 * program: PathResponse
 * description: 路径信息
 * author: SoCMo
 * create: 2020/3/29
 */
@Data
public class PathResponse {
    List<SumCalResponse> sumCalResponseList;
}
