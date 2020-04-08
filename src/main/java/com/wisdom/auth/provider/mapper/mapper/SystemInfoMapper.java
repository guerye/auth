package com.wisdom.auth.provider.mapper.mapper;


import com.wisdom.auth.provider.mapper.model.SystemInfo;
import com.wisdom.auth.provider.pojo.response.ModuleAndSystemResponse;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface SystemInfoMapper extends Mapper<SystemInfo> {
    List<ModuleAndSystemResponse> selectModuleAndSystem();
}