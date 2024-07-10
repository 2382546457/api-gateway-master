package com.xiaohe.gateway.center.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("gateway_server")
@NoArgsConstructor
@AllArgsConstructor
public class GatewayServer {
    @TableId
    private Long id;

    /**
     * 分组标识
     */
    private String groupId;

    /**
     * 分组名称
     */
    private String groupName;
}
