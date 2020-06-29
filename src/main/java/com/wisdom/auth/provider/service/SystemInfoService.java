package com.wisdom.auth.provider.service;

import com.wisdom.auth.autoconfigure.service.BaseService;
import com.wisdom.auth.provider.mapper.model.SystemInfo;
import com.wisdom.auth.provider.pojo.response.ModuleAndSystemResponse;
import com.wisdom.auth.provider.mapper.mapper.SystemInfoMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yxs on 2019/1/9.
 */
@Service
public class SystemInfoService extends BaseService<SystemInfo> {
    public List<ModuleAndSystemResponse> selectModuleAndSystem() {
        return ((SystemInfoMapper)mapper).selectModuleAndSystem();
    }
}
