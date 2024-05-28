package com.xiaohe.gateway.center.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationSystemVO {
    /**
     * 系统标识
     */
    private String systemId;

    /**
     * 系统名称
     */
    private String systemName;

    /**
     * 系统类型，http、rpc
     */
    private String systemType;

    /**
     * 注册中心
     */
    private String systemRegistry;

    /**
     * 该系统中的所有接口
     */
    private List<ApplicationInterfaceVO> interfaceList;

}
