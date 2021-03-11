package com.wisdom.auth.provider.mapper.mapper;

import com.wisdom.auth.provider.mapper.model.CompanyInfo;
import com.wisdom.auth.provider.mapper.model.DeptInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;
@org.apache.ibatis.annotations.Mapper
public interface CompanyInfoMapper extends Mapper<CompanyInfo> {
    List<CompanyInfo> selectCompanyTree(@Param("id") Integer id);
    List<CompanyInfo> selectCompanyDrop(CompanyInfo companyInfo);
    List<CompanyInfo> findByWhere(CompanyInfo companyInfo);
    List<Map<Long,Object>> findIdMapByDomain(CompanyInfo t) ;
    List<Map<Long,Object>> findCompanyIdMapByDomain(CompanyInfo t) ;
}
