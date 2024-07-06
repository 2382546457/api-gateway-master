package com.xiaohe.gateway.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohe.gateway.center.common.OperationRequest;
import com.xiaohe.gateway.center.model.entity.ApplicationInterfaceMethod;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApplicationInterfaceMethodMapper extends BaseMapper<ApplicationInterfaceMethod> {
    List<ApplicationInterfaceMethod> queryApplicationInterfaceMethodListByPage(OperationRequest<ApplicationInterfaceMethod> request);

}
