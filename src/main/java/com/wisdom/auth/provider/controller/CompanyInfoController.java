package com.wisdom.auth.provider.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.auth.autoconfigure.controller.CrudController;
import com.wisdom.auth.provider.common.pojo.TableData;
import com.wisdom.auth.provider.config.redis.AccessTokenUtils;
import com.wisdom.auth.provider.mapper.model.CompanyInfo;
import com.wisdom.auth.provider.pojo.ResponseCode;
import com.wisdom.auth.provider.pojo.ResponseData;
import com.wisdom.auth.provider.pojo.request.CompanyInfoRequest;
import com.wisdom.auth.provider.service.CompanyInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class CompanyInfoController extends CrudController<CompanyInfo, CompanyInfoRequest> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private CompanyInfoService companyInfoService;



    @PostMapping(value = "/company/findByWhere")
    private ResponseData<List<CompanyInfo>> findByWhere(@RequestBody CompanyInfo moduleResources) {

        List<CompanyInfo> list;
        try {
            list = companyInfoService.findByWhere(moduleResources);
        } catch (Exception e) {
            logger.error("查询findByWhere异常" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
    }


    @PostMapping("/company/table")
    @Override
    protected ResponseData<TableData<CompanyInfo>> queryRecord(@RequestBody CompanyInfoRequest query) {
        logger.debug("查询组织机构表格");
        Example example = new Example(CompanyInfo.class);
        Example.Criteria criteria = example.createCriteria();

        if (query != null && query.getParentId() != null && query.getParentId() > 0) {
            criteria.andEqualTo("parentId", query.getParentId());
        }
        example.orderBy("sortNo");
        PageInfo<CompanyInfo> pageInfo = companyInfoService.selectByExampleList(example, query.getPageNum(), query.getPageSize());

        return getTableData(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), pageInfo);
    }

    private List<CompanyInfo> listHierarchy(List<CompanyInfo> parent) {
        List<CompanyInfo> result = new ArrayList<CompanyInfo>();
        for (CompanyInfo companyInfo : parent) {
            CompanyInfo temp = (CompanyInfo) companyInfo.clone();
            temp.setChildren(null);
            result.add(temp);
            if (companyInfo.getChildren() != null) {
                result.addAll(listHierarchy(companyInfo.getChildren()));
            }
        }
        return result;
    }

    @PostMapping(value = "/company/tree")
    private ResponseData<List<CompanyInfo>> getCompanyTree(@RequestBody CompanyInfo moduleResources) {
        logger.debug("查询组织机构树");
        List<CompanyInfo> list;
        try {
            list = companyInfoService.selectCompanyTree(moduleResources.getId());
        } catch (Exception e) {
            logger.error("查询组织机构树异常" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
    }



    @PostMapping("/company/add")
    @Override
    protected ResponseData<CompanyInfo> addRecord(@RequestBody CompanyInfo record) {
        logger.debug("添加组织机构");
        try {
//            record.setId(UUID.uuid32());
            record.setCreateDate(new Date());
            companyInfoService.insertSelective(record);
        } catch (Exception e) {
            logger.error("添加组织机构失败：" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @PostMapping("/company/delete")
    @Override
    protected ResponseData<CompanyInfo> deleteRecord(@RequestBody List<CompanyInfo> record) {
        logger.debug("删除组织机构");
        try {
            companyInfoService.deleteBatchByPrimaryKey(record);
        } catch (Exception e) {
            logger.error("删除组织机构失败：" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @PostMapping("/company/update")
    @Override
    protected ResponseData<CompanyInfo> updateRecord(@RequestBody CompanyInfo record) {
        logger.debug("更新组织机构");
        try {
//            record.setUpdateDate(new Date());
            companyInfoService.updateByPrimaryKeySelective(record);
        } catch (Exception e) {
            logger.error("更新组织机构失败：" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @GetMapping("/company/validate/{companyId}")
    public ResponseData<CompanyInfo> validateCompanyCode(@PathVariable("companyId") String companyId) {
        logger.debug("校验组织机构编码是否存在");
        CompanyInfo companyInfo = new CompanyInfo();
        companyInfo.setCompanyId(companyId);
        companyInfo = companyInfoService.selectOne(companyInfo);
        if (companyInfo == null) {
            return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
        }
        return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
    }

    @GetMapping("/company/getCompanyById/{id}")
    public ResponseData<CompanyInfo> getCompanyById(@PathVariable("id") String id) {
        CompanyInfo companyInfo = new CompanyInfo();
        companyInfo.setId(new Integer(id));
        companyInfo = companyInfoService.selectOne(companyInfo);
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), companyInfo);
    }



    //桂东网站下拉
    @RequestMapping(value = "/company/getCompanyDrop")
    public ResponseData<List<CompanyInfo>> getCompanyDrop(@RequestBody CompanyInfo moduleResources) {
        logger.debug("查询桂东网站下拉");
        List<CompanyInfo> list;
        try {
            list = companyInfoService.selectCompanyDrop(moduleResources);
        } catch (Exception e) {
            logger.error("查询桂东网站下拉异常" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
    }

    @PostMapping("/company/findIdMapByDomain")
    @ResponseBody
    protected ResponseData<List<Map<Long,Object>>> findIdMapByDomain(@RequestBody CompanyInfo record) {
        List<Map<Long,Object>> companyList;
        try {
            companyList=companyInfoService.findIdMapByDomain(record);
        } catch (Exception e) {
            logger.error("查询用户map失败：" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(),companyList);
    }

    @PostMapping("/company/findCompanyIdMapByDomain")
    @ResponseBody
    protected ResponseData<List<Map<Long,Object>>> findCompanyIdMapByDomain(@RequestBody CompanyInfo record) {
        List<Map<Long,Object>> companyList;
        try {
            companyList=companyInfoService.findCompanyIdMapByDomain(record);
        } catch (Exception e) {
            logger.error("查询用户map失败：" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(),companyList);
    }
}
