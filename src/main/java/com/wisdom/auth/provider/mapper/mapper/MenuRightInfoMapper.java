package com.wisdom.auth.provider.mapper.mapper;


import com.wisdom.auth.provider.mapper.model.MenuRightInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface MenuRightInfoMapper extends Mapper<MenuRightInfo> {
    List<MenuRightInfo> getRightsByMenuId(@Param("menuId") Integer menuId);
    List<MenuRightInfo> checkRight(MenuRightInfo menuRightInfo);

}