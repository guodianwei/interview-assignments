package com.example.shorturl;

import com.example.shorturl.constant.CommonConstant;
import com.example.shorturl.controller.ConvertController;
import com.example.shorturl.service.ConvertService;
import com.example.shorturl.utils.ScaleConvertUtil;
import com.example.shorturl.vo.ConvertVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private ConvertService convertService;

    @Autowired
    private ConvertController convertController;

    @Test
    public void shortToFullTest1() throws Exception {
        String url = "https://www.baidu.com";
        ConvertVO shortUrl = convertService.getShortCodeByFullUrl(url);
        Thread.sleep(3000);
        ConvertVO fullUrl = convertService.getFullUrlByShort(shortUrl.getShortCode().replace(CommonConstant.PRIFIX_SHORT_URL, ""));
        Assertions.assertEquals(fullUrl.getFullUrl(), url);
    }

    @Test
    public void shortToFullTest2() throws Exception {
        String url = "https://www.baidu.com";
        ConvertVO first = convertService.getShortCodeByFullUrl(url);
        Thread.sleep(3000);
        ConvertVO second = convertService.getShortCodeByFullUrl(url);
        Thread.sleep(3000);
        Assertions.assertEquals(first.getFullUrl(), second.getFullUrl());
    }

    @Test
    public void shortToFullTest3() {
        String url = "https://www.baidu.com";
//        ConvertVO shortUrl = convertService.getShortCodeByFullUrl(url);
        ConvertVO first = convertService.getFullUrlByShort(null);
        Assertions.assertNull(first);
    }

    @Test
    public void shortToFullTest4() throws Exception {
        String url = "";
        ConvertVO shortUrl = convertService.getShortCodeByFullUrl(url);
        Thread.sleep(3000);
        ConvertVO first = convertService.getFullUrlByShort(null);
        Assertions.assertNull(first);
    }

    @Test
    public void shortToFullTest5() throws Exception {
        String url = "https://www.baidu.com";
        String url1 = "https://www.qq.com";
        ConvertVO shortUrl = convertService.getShortCodeByFullUrl(url);
        ConvertVO shortUrl1 = convertService.getShortCodeByFullUrl(url1);
        Assertions.assertNotEquals(shortUrl.getShortCode(), shortUrl1.getShortCode());
    }

    @Test
    public void shortToFullTest6() {
        String url = "https://www.baidu.com";
        convertController.getShortUrlByUrl(url);
    }

    @Test
    public void shortToFullTest7() {
        String convert = ScaleConvertUtil.convert(1, 62);
        Assertions.assertEquals("1", convert);
    }

    @Test
    public void shortToFullTest8() {
        String convert = ScaleConvertUtil.convert(-1, 62);
        Assertions.assertEquals("4GFfc3", convert);
    }

    @Test
    public void shortToFullTest9() {
        String convert = ScaleConvertUtil.convert(63, 62);
        Assertions.assertEquals("11", convert);
    }
}
