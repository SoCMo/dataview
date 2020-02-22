package com.nCov.DataView.exception;

/**
 * @description: 报错接口
 * @author: 0GGmr0
 * @create: 2019-12-01 21:25
 */
public interface CommonError {
    Integer getErrCode();

    String getMsg();

    CommonError setErrMsg(String errMsg);
}
