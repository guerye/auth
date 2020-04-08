package com.wisdom.auth.provider.mapper.mapper;


import com.wisdom.auth.provider.mapper.model.UserInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface UserInfoMapper extends Mapper<UserInfo> {
    List<UserInfo> getUserTableByRoleId(UserInfo info);
    List<UserInfo> getUnUserTableByRoleId(UserInfo info);
    UserInfo selectHegangUser(UserInfo info);
}