package com.xiaohe.gateway.center.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 网关服务明细
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("gateway_server_detail")
public class GatewayServerDetail {

    /**
     * 自增ID
     */
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
     * 网关名称
     */
    private String gatewayName;
    /**
     * 网关地址
     */
    private String gatewayAddress;
    /**
     * 服务状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

}
