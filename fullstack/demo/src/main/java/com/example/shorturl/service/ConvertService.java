package com.example.shorturl.service;

import com.example.shorturl.vo.ConvertVO;

public interface ConvertService {

    /**
     * 根据完整URL转换 shortCode
     * @param fullUrl  完整rul
     * @return
     */
    ConvertVO getShortCodeByFullUrl (String fullUrl);


    /**
     * 根据短码获取真实
     * @param shortCode
     * @return
     */
    ConvertVO  getFullUrlByShort  (String shortCode);
}
