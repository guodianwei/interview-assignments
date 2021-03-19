package com.example.shorturl.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class ConvertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private String shortCode ;
    /**
     * 过期日期
     */
    private String expirationDate ;
    /**
     * 访问次数
     */
    private Long totalClickCount ;

    private String shortUrl;

}
