package com.wisdom.auth.provider.service;

import com.wisdom.auth.autoconfigure.service.BaseService;
import com.wisdom.auth.provider.mapper.model.UserInfo;
import com.wisdom.auth.provider.mapper.model.UserRoleRel;
import com.wisdom.auth.provider.mapper.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yxs on 2019/1/9.
 */
@Service
public class UserInfoService extends BaseService<UserInfo> {

    @Autowired
    private UserRoleRelService userRoleRelService;


    /**
     * 批量重置密码
     * @param record
     * @param newPassword
     */
    @Transactional
    public void resetPassword(List<UserInfo> record, String newPassword) {
        record.forEach(e -> {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(e.getId());
            userInfo.setPassword(new BCryptPasswordEncoder(6).encode(newPassword));
            userInfo.setUpdateDate(new Date());
            updateByPrimaryKeySelective(userInfo);
        });
    }

    /**
     * 删除用户
     * @param record
     */
    @Transactional
    public void deleteBatch(List<UserInfo> record) {
        record.forEach(e -> {
            Example example = new Example(UserRoleRel.class);
            example.createCriteria().andEqualTo("userId", e.getId());
            userRoleRelService.deleteByExample(example);
            deleteByPrimaryKey(e);
        });
    }

    public List<UserInfo> getUserTableByRoleId(UserInfo info) {
        return ((UserInfoMapper)mapper).getUserTableByRoleId(info);
    }
    public List<UserInfo> getUnUserTableByRoleId(UserInfo info) {
        return ((UserInfoMapper)mapper).getUnUserTableByRoleId(info);
    }
    public List<Map<Long,Object>> findMapByDomain(UserInfo t) {
        // TODO Auto-generated method stub
        return ((UserInfoMapper)mapper).findMapByDomain(t);
    }
    public List<UserInfo> findUSerByIds(List<Long> userIds) {
        // TODO Auto-generated method stub
        return ((UserInfoMapper)mapper).findUSerByIds(userIds);
    }

}
