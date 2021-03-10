package com.example.shorturl.controller;

import com.example.shorturl.constant.CommonConstant;
import com.example.shorturl.service.ConvertService;
import com.example.shorturl.vo.BaseResponse;
import com.example.shorturl.vo.ConvertVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
public class ConvertController {

    @Autowired
    private ConvertService convertService;

    @PostMapping("/surl")
    public BaseResponse<ConvertVO> getShortUrlByUrl(String fullUrl) {

        ConvertVO convertVO  = convertService.getShortCodeByFullUrl(fullUrl);
        return new BaseResponse(convertVO);
    }

    @GetMapping("{shortUrl}")
    public void redirect(@PathVariable(required = false)  String shortCode,  HttpServletResponse response) throws IOException {

        ConvertVO convertVO = convertService.getFullUrlByShort(CommonConstant.PRIFIX_SHORT_URL + shortCode);
        String url = (convertVO == null) ? CommonConstant.DEFAULT_URL : convertVO.getFullUrl();
        response.sendRedirect(url);
    }
}
