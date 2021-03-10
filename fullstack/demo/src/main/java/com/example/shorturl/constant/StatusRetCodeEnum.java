/**
 * Copyright 2019 China Renaissance Inc.
 */
package com.example.shorturl.constant;

public enum StatusRetCodeEnum {
    OK("200", "操作成功"),
    PARAM_ERROR("400", "参数错误"),
    UNAUTHORIZED("401", "未登录"),
    METHOD_NOT_SUPPORTED("405", "请求方式不支持"),
    FORBIDDEN("403", "权限不足"),
    SERVICE_ERROR("500", "服务器异常");

    private String code;
    private String msg;

    StatusRetCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
