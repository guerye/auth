package com.wisdom.auth.provider.mapper.mapper;

import com.wisdom.auth.provider.mapper.model.DeptInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface DeptInfoMapper extends Mapper<DeptInfo> {
    List<DeptInfo> getDeptsByUserId(@Param("userId") Integer userId);
    List<DeptInfo> selectDeptTree(@Param("id") Integer id);
    List<DeptInfo> roleDeptTree(@Param("ROLE_ID") Integer id);
    List<DeptInfo> selectDeptDrop(DeptInfo deptInfo);
}