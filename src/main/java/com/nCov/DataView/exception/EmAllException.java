package com.nCov.DataView.exception;

/**
 * @description: 报错枚举
 * @author: 0GGmr0
 * @create: 2019-12-01 21:27
 */
public enum EmAllException implements CommonError {
    BAD_REQUEST(400, "请求参数格式有误"),

    QUERY_TIME_OUT(500, "请求超时"),

    INSERT_ERROR(500, "插入数据失败"),

    DATABASE_ERROR(500, "数据库异常或数据有误"),

    UNIFY_ERROR(400,"请保证Excel表格正确无误"),

    BAD_FILE_TYPE(400, "文件格式错误");


    // 错误码
    private Integer code;

    // 错误信息
    private String msg;

    EmAllException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getErrCode() {
        return this.code;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.msg = errMsg;
        return this;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
