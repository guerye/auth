package com.wisdom.auth.provider.pojo.request;

import com.wisdom.auth.provider.common.pojo.BaseRequestPojo;
import com.wisdom.auth.provider.mapper.model.CompanyInfo;
import com.wisdom.auth.provider.mapper.model.DeptInfo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CompanyInfoRequest extends BaseRequestPojo implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "PARENT_ID")
    private Integer parentId;

    @Column(name = "COMPANY_ID")
    private String companyId;

    @Column(name = "COMPANY_NAME")
    private String title;

    @Column(name = "COMPANY_TYPE")
    private Integer companyType;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "CLOSE_DATE")
    private Date closeDate;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "STATUS")
    private Short status;

    @Column(name = "SORT_NO")
    private Integer sortNo;


    private static final long serialVersionUID = 1L;

    /**
     * 下级部门
     */
    @Transient
    private List<CompanyInfo> children;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCompanyType() {
        return companyType;
    }

    public void setCompanyType(Integer companyType) {
        this.companyType = companyType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<CompanyInfo> getChildren() {
        return children;
    }

    public void setChildren(List<CompanyInfo> children) {
        this.children = children;
    }
}
