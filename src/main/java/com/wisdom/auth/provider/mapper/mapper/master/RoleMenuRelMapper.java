package com.wisdom.auth.provider.mapper.mapper.master;


import com.wisdom.auth.provider.mapper.model.master.RoleMenuRel;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface RoleMenuRelMapper extends Mapper<RoleMenuRel> {
    List<RoleMenuRel> selectLeafRoleModule(String roleId);
    List<Integer> roleMenuButton(RoleMenuRel roleMenuRel);
    int saveButtonAccess(RoleMenuRel roleMenuRel);

}