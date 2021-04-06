package com.wisdom.auth.provider.mapper.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Table(name="COMPANY_INFO")
public class CompanyInfo implements Serializable,Cloneable {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "PARENT_ID")
    private Integer parentId;

    @Column(name = "COMPANY_ID")
    private String companyId;

    @Column(name = "COMPANY_NAME")
    private String companyName;

    @Column(name = "COMPANY_LEVEL")
    private Integer companyLevel;

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


    /**
     * 下级部门
     */
    private List<CompanyInfo> children;

    private List<String> companyIdList;
    @Transient
    private Long companyIdLike;


    @Transient
    private boolean  checked;

    @Transient
    private boolean  expand;

    @Transient
    private String title;

    private static final long serialVersionUID = 1L;

    public Long getCompanyIdLike() {
        return companyIdLike;
    }

    public void setCompanyIdLike(Long companyIdLike) {
        this.companyIdLike = companyIdLike;
    }

    /**
     * @return the value of COMPANY_INFO.ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the value for COMPANY_INFO.ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the value of DCOMPANY_INFO.PARENT_ID
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * @param parentId the value for COMPANY_INFO.PARENT_ID
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * @return the value of COMPANY_INFO.DEPT_ID
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the value for COMPANY_INFO.COMPANY_ID
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }


    /**
     * @return the value of COMPANY_INFO.COMPANY_LEVEL
     */
    public Integer getCompanyLevel() {
        return companyLevel;
    }

    /**
     * @param companyLevel the value for COMPANY_INFO.COMPANY_TYPE
     */
    public void setCompanyLevel(Integer companyLevel) {
        this.companyLevel = companyLevel;
    }

    /**
     * @return the value of COMPANY_INFO.CREATE_DATE
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the value for COMPANY_INFO.CREATE_DATE
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the value of COMPANY_INFO.CLOSE_DATE
     */
    public Date getCloseDate() {
        return closeDate;
    }

    /**
     * @param closeDate the value for COMPANY_INFO.CLOSE_DATE
     */
    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    /**
     * @return the value of COMPANY_INFO.REMARK
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the value for COMPANY_INFO.REMARK
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * @return the value of COMPANY_INFO.STATUS
     */
    public Short getStatus() {
        return status;
    }

    /**
     * @param status the value for COMPANY_INFO.STATUS
     */
    public void setStatus(Short status) {
        this.status = status;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public List<CompanyInfo> getChildren() {
        return children;
    }

    public void setChildren(List<CompanyInfo> children) {
        this.children = children;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getCompanyIdList() {
        return companyIdList;
    }

    public void setCompanyIdList(List<String> companyIdList) {
        this.companyIdList = companyIdList;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", parentId=").append(parentId);
        sb.append(", companyId=").append(companyId);
        sb.append(", companyName=").append(companyName);
        sb.append(", title=").append(title);
        sb.append(", companyLevel=").append(companyLevel);
        sb.append(", createDate=").append(createDate);
        sb.append(", closeDate=").append(closeDate);
        sb.append(", remark=").append(remark);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    public Object clone(){
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e){
            return null;
        }
    }
}
