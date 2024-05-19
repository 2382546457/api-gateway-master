package com.xiaohe.gateway.center.model.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API 数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiData {
    private String application;

    private String methodName;

    private String parameterType;

    private String uri;

    private String httpCommandType;

    private Integer auth;
}
