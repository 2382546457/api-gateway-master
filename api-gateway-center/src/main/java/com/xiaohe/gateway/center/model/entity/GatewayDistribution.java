package com.xiaohe.gateway.center.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 网关分配
 */
@Data
@TableName("gateway_distribution")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class GatewayDistribution {
    @TableId
    private Integer id;

    /**
     * 分组标识
     */
    private String groupId;

    /**
     * 网关标识
     */
    private String gatewayId;

    /**
     * 系统标识
     */
    private String systemId;

    /**
     * 系统名称
     */
    private String systemName;

    private Date createTime;

    private Date updateTime;
}
