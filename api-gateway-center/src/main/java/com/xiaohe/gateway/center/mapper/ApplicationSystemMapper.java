package com.xiaohe.gateway.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohe.gateway.center.common.OperationRequest;
import com.xiaohe.gateway.center.model.entity.ApplicationSystem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApplicationSystemMapper extends BaseMapper<ApplicationSystem> {
    List<ApplicationSystem> queryApplicationSystemListByPage(OperationRequest<ApplicationSystem> request);

}
