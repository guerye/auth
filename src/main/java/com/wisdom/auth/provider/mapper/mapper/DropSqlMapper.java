package com.wisdom.auth.provider.mapper.mapper;


import com.wisdom.auth.provider.mapper.model.DropSql;
import tk.mybatis.mapper.common.Mapper;

import java.util.LinkedHashMap;
import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface DropSqlMapper extends Mapper<DropSql> {
    List<DropSql> selectDropSql();
    List<LinkedHashMap<String, Object>> findBySql(String sql);
}