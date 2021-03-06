package com.wisdom.auth.provider.service;

import com.wisdom.auth.autoconfigure.service.BaseService;
import com.wisdom.auth.provider.mapper.model.RoleInfo;
import com.wisdom.auth.provider.mapper.mapper.RoleInfoMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yxs on 2019/1/9.
 */
@Service
public class RoleInfoService extends BaseService<RoleInfo> {

    /**
     * 根据用户查询角色
     * @param userId
     * @return
     */
    public List<RoleInfo> getRoleByUserId(Integer userId) {
        return ((RoleInfoMapper)mapper).getRoleByUserId(userId);
    }

    public List<RoleInfo> userRoleTree(Integer userId) {
        return ((RoleInfoMapper)mapper).userRoleTree(userId);
    }


}
