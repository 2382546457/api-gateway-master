package com.xiaohe.gateway.center.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 应用接口方法
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("application_interface_method")
public class ApplicationInterfaceMethod {
    @TableId
    private Integer id;

    /**
     * 系统标识
     */
    private String systemId;
    /**
     * 接口标识
     */
    private String interfaceId;

    /**
     * 方法标识
     */
    private String methodId;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 参数类型
     */
    private String parameterType;

    /**
     * 网关接口
     */
    private String uri;

    /**
     * 接口类型，GET、POST、PUT..
     */
    private String httpCommandType;

    /**
     * 是否需要进行权限验证
     * 0 : 否
     * 1 : 是
     */
    private Integer auth;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}
