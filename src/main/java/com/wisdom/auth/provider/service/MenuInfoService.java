package com.wisdom.auth.provider.service;

import com.wisdom.auth.autoconfigure.service.BaseService;
import com.wisdom.auth.provider.mapper.model.MenuInfo;
import com.wisdom.auth.provider.mapper.model.MenuRightInfo;
import com.wisdom.auth.provider.mapper.mapper.MenuInfoMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yxs on 2019/1/9.
 */
@Service
public class MenuInfoService extends BaseService<MenuInfo> {


    /**
     * 根据用户查询菜单
     * @param userId
     * @return
     */
    public List<MenuInfo> getMenusByUserId(Integer userId) {
        return ((MenuInfoMapper)mapper).getMenusByUserId(userId);
    }

    public List<MenuInfo> getModuleTree(Integer id, Integer systemId, Integer status) {
        return ((MenuInfoMapper)mapper).selectModuleTree(id, systemId, status);
    }

    public List<MenuInfo> roleMenuTree(Integer id) {
        return ((MenuInfoMapper)mapper).roleMenuTree(id);
    }
    public List<MenuRightInfo> menuButton(Integer id) {
        return ((MenuInfoMapper)mapper).menuButton(id);
    }

    public List<Integer> getMenuIdsByUserId(Integer userId) {
        return ((MenuInfoMapper)mapper).getMenuIdsByUserId(userId);
    }

    public List<MenuInfo> findByWhere(MenuInfo menuInfo){
        return ((MenuInfoMapper)mapper).findByWhere(menuInfo);
    }
}
