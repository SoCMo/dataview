package com.nCov.DataView.model.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * program: AddressRequestRequest
 * description: 全部地区信息请求
 * author: SoCMo
 * create: 2020/2/23
 */
@Data
public class AddressRequest {
    @NotEmpty(message = "地址数组不能为空")
    private List<String> addressList;
}
