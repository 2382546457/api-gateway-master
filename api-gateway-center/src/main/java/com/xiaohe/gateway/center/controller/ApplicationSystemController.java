package com.xiaohe.gateway.center.controller;

import com.xiaohe.gateway.center.common.ResponseCode;
import com.xiaohe.gateway.center.common.Result;
import com.xiaohe.gateway.center.model.vo.ApplicationSystemRichInfo;
import com.xiaohe.gateway.center.service.ApplicationSystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/wg/admin/config")
public class ApplicationSystemController {

    private final Logger logger = LoggerFactory.getLogger(ApplicationSystemController.class);

    @Resource
    private ApplicationSystemService applicationSystemService;

    @PostMapping(value = "/queryApplicationSystemRichInfo", produces = "application/json;charset=utf-8")
    public Result<ApplicationSystemRichInfo> queryApplicationSystemRichInfo(@RequestParam String gatewayId,
                                                                            @RequestParam String systemId) {
        if (!StringUtils.hasText(gatewayId)) {
            return new Result<>(ResponseCode.ILLEGAL_PARAMETER.getCode(), "网关id为空.");
        }
        logger.info("查询分配到网关下的待注册系统信息(系统、接口、方法) gatewayId : {}, systemId : {}.", gatewayId, systemId);
        ApplicationSystemRichInfo applicationSystemRichInfo = applicationSystemService.queryAapplicationSystemRichInfo(gatewayId, systemId);
        return Result.buildSuccess(applicationSystemRichInfo);
    }
}
