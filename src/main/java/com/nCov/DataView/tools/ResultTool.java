package com.nCov.DataView.tools;

import com.nCov.DataView.model.response.Result;

import java.util.List;

/**
 * @description: 返回工具
 * @author: 0GGmr0
 * @create: 2019-12-01 21:44
 */
public class ResultTool {

    @SuppressWarnings("unchecked")
    public static Result success(List<Object> object) {
        Result result = new Result();
        result.setCode(200);
        result.setData(object);
        return result;
    }


    @SuppressWarnings("unchecked")
    public static Result success(Object object) {
        Result result = new Result();
        result.setCode(200);
        result.setData(object);
        return result;
    }


    public static Result success() {
        Result result = new Result();
        result.setCode(200);
        return result;
    }


    public static Result error(Integer code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }


    public static Result error(Integer code) {
        Result result = new Result();
        result.setCode(code);
        return result;
    }
}

