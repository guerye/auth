package com.wisdom.auth.provider.mapper.mapper.master;


import com.wisdom.auth.provider.mapper.model.master.MenuInfo;
import com.wisdom.auth.provider.mapper.model.master.MenuRightInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface MenuInfoMapper extends Mapper<MenuInfo> {
    List<MenuInfo> getMenusByUserId(@Param("userId") Integer userId);

    List<MenuInfo> selectModuleTree(@Param("id") Integer id, @Param("systemId") Integer systemId, @Param("status") Integer status);

    List<MenuInfo> roleMenuTree(@Param("ROLE_ID") Integer id);

    List<MenuRightInfo> menuButton(@Param("MENU_ID") Integer id);
}