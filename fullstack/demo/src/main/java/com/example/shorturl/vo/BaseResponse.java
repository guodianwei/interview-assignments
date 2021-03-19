/**
 * Copyright 2019 China Renaissance Inc.
 */
package com.example.shorturl.vo;

import com.example.shorturl.constant.ServiceErrorEnum;
import com.example.shorturl.constant.StatusRetCodeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class BaseResponse<T> implements Serializable {

    private static final BaseResponse<Void> OK_RESPONSE = new BaseResponse<>();

    private static final BaseResponse<Void> UNAUTHORIZED_RESPONSE = new BaseResponse<>(
            StatusRetCodeEnum.UNAUTHORIZED, StatusRetCodeEnum.UNAUTHORIZED.getMsg());

    private static final BaseResponse<Void> FORBIDDEN_RESPONSE = new BaseResponse<>(
            StatusRetCodeEnum.FORBIDDEN, StatusRetCodeEnum.FORBIDDEN.getMsg());

    /**记录数据*/
    private T result;
    /**返回信息*/
    private String retMsg;
    /**返回结果码*/
    private String retCode;
    /**请求状态code*/
    private String statusCode;
    /**字段错误提示信息*/
    private List<FieldErrorVO> fieldErrors = new ArrayList<>();

    public BaseResponse() {
        this.retMsg = StatusRetCodeEnum.OK.getMsg();
        this.statusCode = StatusRetCodeEnum.OK.getCode();
    }

    public BaseResponse(T result) {
        this.retMsg = StatusRetCodeEnum.OK.getMsg();
        this.statusCode = StatusRetCodeEnum.OK.getCode();
        this.result = result;
    }


    public BaseResponse(StatusRetCodeEnum statusCode, String msg) {
        this.retMsg = msg;
        this.statusCode = statusCode.getCode();
    }

    public BaseResponse(StatusRetCodeEnum statusCode, ServiceErrorEnum retCode, String msg) {
        this.retMsg = msg;
        this.retCode = retCode != null ? retCode.getCode() : null;
        this.statusCode = statusCode.getCode();
    }

    public BaseResponse(StatusRetCodeEnum statusCode, List<FieldErrorVO> fieldErrors) {
        this.retMsg = statusCode.getMsg();
        this.statusCode = statusCode.getCode();
        this.fieldErrors = fieldErrors;
    }

    public void add(String field, String message) {
        if (fieldErrors == null) {
            fieldErrors = new ArrayList<>();
        }
        fieldErrors.add(new FieldErrorVO(field, message));
    }

    public static BaseResponse<Void> buildByCode(StatusRetCodeEnum statusCode) {
        BaseResponse<Void> responseDTO =  new BaseResponse<>();
        responseDTO.setStatusCode(statusCode.getCode());
        responseDTO.setRetMsg(statusCode.getMsg());
        return responseDTO;
    }

    public static BaseResponse<Void> buildOk() {
        return OK_RESPONSE;
    }

    public static BaseResponse<Void> buildUnauthorized() {
        return UNAUTHORIZED_RESPONSE;
    }

    public static BaseResponse<Void> buildForbidden() {
        return FORBIDDEN_RESPONSE;
    }
}
