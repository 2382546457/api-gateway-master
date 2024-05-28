package com.xiaohe.gateway.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohe.gateway.center.model.entity.ApplicationInterface;
import com.xiaohe.gateway.center.model.vo.ApplicationInterfaceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApplicationInterfaceMapper extends BaseMapper<ApplicationInterface> {
    public List<ApplicationInterfaceVO> queryApplicationInterfaceVO(@Param("systemId") String systemId);
}
