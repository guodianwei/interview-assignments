package com.example.shorturl.vo;

import lombok.Data;

@Data
public class ConvertVO {

    private Long id;

    /**
     * 域名
     */
    private String baseUrl ;
    /**
     * 后缀
     */
    private String suffixUrl;
    /**
     * 完整链接
     */
    private String fullUrl ;
    /**
     * 短码
     */
    private String shotCode ;
    /**
     * 过期日期
     */
    private String expirationDate ;
    /**
     * 访问次数
     */
    private Long totalClickCount ;
}
