package com.wisdom.auth.provider.service;

import com.wisdom.auth.autoconfigure.service.BaseService;
import com.wisdom.auth.provider.mapper.mapper.CompanyInfoMapper;
import com.wisdom.auth.provider.mapper.model.CompanyInfo;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

@Service
public class CompanyInfoService extends BaseService<CompanyInfo> {
    /**
     * 根据父节点查询组织机构
     * @param id
     * @return
     */
    public List<CompanyInfo> selectCompanyTree(Integer id) {
        return ((CompanyInfoMapper)mapper).selectCompanyTree(id);
    }

    public List<CompanyInfo> findByWhere(CompanyInfo companyInfo) {
        return ((CompanyInfoMapper)mapper).findByWhere(companyInfo);
    }


    public List<CompanyInfo> selectCompanyDrop(CompanyInfo companyInfo){
        return ((CompanyInfoMapper)mapper).selectCompanyDrop(companyInfo);
    }
    public List<Map<Long,Object>> findIdMapByDomain(CompanyInfo t) {
        // TODO Auto-generated method stub
        return ((CompanyInfoMapper)mapper).findIdMapByDomain(t);
    }
    public List<Map<Long,Object>> findCompanyIdMapByDomain(CompanyInfo t) {
        // TODO Auto-generated method stub
        return ((CompanyInfoMapper)mapper).findCompanyIdMapByDomain(t);
    }
}
