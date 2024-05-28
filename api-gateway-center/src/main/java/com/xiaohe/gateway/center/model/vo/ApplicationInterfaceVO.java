package com.xiaohe.gateway.center.model.vo;

import com.xiaohe.gateway.center.model.entity.ApplicationInterfaceMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationInterfaceVO {
    /**
     * 系统标识
     */
    private String systemId;

    /**
     * 接口标识
     */
    private String interfaceId;

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 接口版本
     */
    private String interfaceVersion;

    /**
     * 接口中注册的方法
     */
    private List<ApplicationInterfaceMethod> methodList;


}
