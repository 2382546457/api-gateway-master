package com.xiaohe.gateway.center.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 应用系统
 */
@Data
@Accessors(chain = true)
@TableName("application_system")
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationSystem {
    private Integer id;

    /**
     * 系统标识
     */
    private String systemId;

    /**
     * 系统名称
     */
    private String systemName;

    /**
     * 系统类型 RPC、HTTP
     */
    private String systemType;

    /**
     * 注册中心  zookeeper://127.0.0.1:2181
     */
    private String systemRegistry;

    private Date createTime;

    private Date updateTime;


}
