
package com.example.shorturl.constant;


import org.apache.commons.lang3.StringUtils;

public enum ServiceErrorEnum {

    TEST("000001", "测试");
	/**
	 * enum code.
	 * */
    private final String code;

    /**
     * enum memo.
     * */
    private final String memo;

    /**
     * resultEnum.
     * */
    private ServiceErrorEnum(String code, String memo) {
        this.code = code;
        this.memo = memo;
    }

    /**
     * get code.
     * */
    public String getCode() {
        return code;
    }

    /**
     * get memo.
     * */
    public String getMemo() {
        return memo;
    }
    
    /**
     * 遍历取值
     * */
    public static ServiceErrorEnum getEnum(String code) {
        for (ServiceErrorEnum item : values()) {
            if (StringUtils.endsWithIgnoreCase(code, item.getCode())) {
                return item;
            }
        }
        return null;
    }

}
