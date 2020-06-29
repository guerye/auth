package com.wisdom.auth.provider.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.auth.autoconfigure.controller.CrudController;
import com.wisdom.auth.provider.pojo.ResponseData;
import com.wisdom.auth.provider.common.pojo.TableData;
import com.wisdom.auth.provider.mapper.model.DeptInfo;
import com.wisdom.auth.provider.mapper.model.UserInfo;
import com.wisdom.auth.provider.pojo.ResponseCode;
import com.wisdom.auth.provider.pojo.request.DeptInfoRequest;
import com.wisdom.auth.provider.config.redis.AccessTokenUtils;
import com.wisdom.auth.provider.service.DeptInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yxs on 2019/1/17.
 */
@RestController
public class DeptInfoController extends CrudController<DeptInfo, DeptInfoRequest> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DeptInfoService deptInfoService;

    @Autowired
    private AccessTokenUtils accessTokenUtils;

    @PostMapping(value = "/dept/findByWhere")
    private ResponseData<List<DeptInfo>> findByWhere(@RequestBody DeptInfo moduleResources) {

        List<DeptInfo> list;
        try {
            list = deptInfoService.findByWhere(moduleResources);
        } catch (Exception e) {
            logger.error("查询findByWhere异常" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
    }

    @RequestMapping(value = "/dept/user/{userId}", method = RequestMethod.GET)
    public ResponseData<List<DeptInfo>> getDeptsByUserId(@PathVariable("userId") Integer userId) {
        logger.debug("根据用户查询组织机构");
        List<DeptInfo> list;
        try {
            list = deptInfoService.getDeptsByUserId(userId);
        } catch (Exception e) {
            logger.error("根据用户查询组织机构错误");
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
    }

    /**
     * 从redis中查询当前用户拥有的组织机构，返回树结构
     *
     * @return
     */
    @GetMapping("/dept/auth/tree")
    public ResponseData<List<DeptInfo>> getCurrentDept() {
        System.out.println("--------------/menu----------provider1-------------------------------" + accessTokenUtils.getDeptInfo());
        logger.debug("查询当前用户组织机构");
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), accessTokenUtils.getDeptInfo());//
    }

    /**
     * 从redis中查询当前用户拥有的组织机构，返回列表结构
     *
     * @return
     */
    @GetMapping("/dept/auth/table")
    public ResponseData<List<DeptInfo>> getCurrentDeptList() {
        logger.debug("查询当前用户组织机构列表");
        List<DeptInfo> list = listHierarchy(accessTokenUtils.getDeptInfo());
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
    }

    private List<DeptInfo> listHierarchy(List<DeptInfo> parent) {
        List<DeptInfo> result = new ArrayList<DeptInfo>();
        for (DeptInfo deptInfo : parent) {
            DeptInfo temp = (DeptInfo) deptInfo.clone();
            temp.setChildren(null);
            result.add(temp);
            if (deptInfo.getChildren() != null) {
                result.addAll(listHierarchy(deptInfo.getChildren()));
            }
        }
        return result;
    }

    @PostMapping(value = "/dept/tree")
    private ResponseData<List<DeptInfo>> getDeptTree(@RequestBody DeptInfo moduleResources) {
        logger.debug("查询组织机构树");
        List<DeptInfo> list;
        try {
            list = deptInfoService.selectDeptTree(moduleResources.getId());
        } catch (Exception e) {
            logger.error("查询组织机构树异常" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
    }


    @PostMapping("/dept/table")
    @Override
    protected ResponseData<TableData<DeptInfo>> queryRecord(@RequestBody DeptInfoRequest query) {
        logger.debug("查询组织机构表格");
        Example example = new Example(DeptInfo.class);
        Example.Criteria criteria = example.createCriteria();

        if (query != null && query.getParentId() != null && query.getParentId() > 0) {
            criteria.andEqualTo("parentId", query.getParentId());
        } else {
            UserInfo userInfo = accessTokenUtils.getUserInfo();
            if ("sysadmin".equals(userInfo.getUserId())) {
                criteria.andEqualTo("parentId", 0);
            } else {
                List<DeptInfo> list = accessTokenUtils.getDeptInfo();
                List<DeptInfo> result = new ArrayList<DeptInfo>();
                for (int i = (query.getPageNum() - 1) * query.getPageSize(); i < Math.min(list.size(), query.getPageSize()); i++) {
                    result.add(list.get(i));
                }
                PageInfo<DeptInfo> pageInfo = new PageInfo<DeptInfo>();
                pageInfo.setPageNum(query.getPageNum());
                pageInfo.setPageSize(query.getPageSize());
                pageInfo.setList(result);
                return getTableData(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), pageInfo);
            }
        }
        example.orderBy("sortNo");
        PageInfo<DeptInfo> pageInfo = deptInfoService.selectByExampleList(example, query.getPageNum(), query.getPageSize());

        return getTableData(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), pageInfo);
    }

    @PostMapping("/dept/add")
    @Override
    protected ResponseData<DeptInfo> addRecord(@RequestBody DeptInfo record) {
        logger.debug("添加组织机构");
        try {
//            record.setId(UUID.uuid32());
            record.setCreateDate(new Date());
            deptInfoService.insertSelective(record);
        } catch (Exception e) {
            logger.error("添加组织机构失败：" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @PostMapping("/dept/delete")
    @Override
    protected ResponseData<DeptInfo> deleteRecord(@RequestBody List<DeptInfo> record) {
        logger.debug("删除组织机构");
        try {
            deptInfoService.deleteBatchByPrimaryKey(record);
        } catch (Exception e) {
            logger.error("删除组织机构失败：" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @PostMapping("/dept/update")
    @Override
    protected ResponseData<DeptInfo> updateRecord(@RequestBody DeptInfo record) {
        logger.debug("更新组织机构");
        try {
//            record.setUpdateDate(new Date());
            deptInfoService.updateByPrimaryKeySelective(record);
        } catch (Exception e) {
            logger.error("更新组织机构失败：" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @GetMapping("/dept/validate/{deptId}")
    public ResponseData<DeptInfo> validateDeptCode(@PathVariable("deptId") String deptId) {
        logger.debug("校验组织机构编码是否存在");
        DeptInfo deptInfo = new DeptInfo();
        deptInfo.setDeptId(deptId);
        deptInfo = deptInfoService.selectOne(deptInfo);
        if (deptInfo == null) {
            return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
        }
        return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
    }

    @GetMapping("/dept/getDeptById/{id}")
    public ResponseData<DeptInfo> getDeptById(@PathVariable("id") String id) {
        DeptInfo deptInfo = new DeptInfo();
        deptInfo.setId(new Integer(id));
        deptInfo = deptInfoService.selectOne(deptInfo);
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), deptInfo);
    }

    @PostMapping(value = "/menu/roleDeptTree")
    private ResponseData<List<DeptInfo>> roleDeptTree(@RequestBody DeptInfo moduleResources) {
        logger.debug("查询角色拥有的菜单树");
        List<DeptInfo> list;
        try {
            list = deptInfoService.roleDeptTree(moduleResources.getRoleId());
        } catch (Exception e) {
            logger.error("查询模块树异常" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
    }

    //桂东网站下拉
    @RequestMapping(value = "/dept/getDeptDrop")
    public ResponseData<List<DeptInfo>> getDeptDrop(@RequestBody DeptInfo moduleResources) {
        logger.debug("查询桂东网站下拉");
        List<DeptInfo> list;
        try {
            list = deptInfoService.selectDeptDrop(moduleResources);
        } catch (Exception e) {
            logger.error("查询桂东网站下拉异常" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
    }

    @PostMapping("/dept/findIdMapByDomain")
    @ResponseBody
    protected ResponseData<List<Map<Long,Object>>> findIdMapByDomain(@RequestBody DeptInfo record) {
        List<Map<Long,Object>> deptList;
        try {
            deptList=deptInfoService.findIdMapByDomain(record);
        } catch (Exception e) {
            logger.error("查询用户map失败：" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(),deptList);
    }

    @PostMapping("/dept/findDeptIdMapByDomain")
    @ResponseBody
    protected ResponseData<List<Map<Long,Object>>> findDeptIdMapByDomain(@RequestBody DeptInfo record) {
        List<Map<Long,Object>> deptList;
        try {
            deptList=deptInfoService.findDeptIdMapByDomain(record);
        } catch (Exception e) {
            logger.error("查询用户map失败：" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(),deptList);
    }
}
