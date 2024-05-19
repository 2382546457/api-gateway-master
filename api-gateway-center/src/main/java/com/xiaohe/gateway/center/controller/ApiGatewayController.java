package com.xiaohe.gateway.center.controller;

import com.xiaohe.gateway.center.model.vo.ApiData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiGatewayController {
    private static final Logger logger = LoggerFactory.getLogger(ApiGatewayController.class);

    @GetMapping(value = "list", produces = "application/json;charset=utf-8")
    public List<ApiData> getAnswerMap() {
        return null;
    }

}
