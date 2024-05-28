package com.xiaohe.gateway.center.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationSystemRichInfo {
    /**
     * 网关id
     */
    private String gatewayId;

    /**
     * 系统列表
     */
    private List<ApplicationSystemVO> applicationSystemVOList;
}
