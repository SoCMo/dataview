package com.nCov.DataView.model.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * program: AddressRequest
 * description: 起始地址
 * author: pongshy
 * create: 2020/3/31
 */
@Data
public class AddressRequest {
    @NotEmpty(message = "地址列表不能为空")
    private List<String> addressList;
}
