package com.glaway.ids.project.plan.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.glaway.foundation.common.vdata.GLVData;

import javax.persistence.Basic;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 计划视图信息
 * @author likaiyong
 * @version 2018年5月30日
 */
public class PlanViewInfoDto extends GLVData {

    /**
     * 视图名称
     */
    @Basic()
    private String name = null;
    
    /**
     * 状态(已发布：PUBLIC,未发布PRIVATE)
     */
    @Basic()
    private String status = null;
    
    /**
     * 发布人
     */
    @Basic()
    private String publishPerson = null;
    
    /**
     * 发布人
     */
    @Transient
    private String publishPersonName = null;
    
    /**
     * 发布部门
     */
    @Basic()
    private String publishDept = null;
    
    /**
     * 发布部门
     */
    @Transient
    private String publishDeptName = null;
    
    
    /**
     * 发布时间
     */
    @Basic()
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishTime = null;

    /**
     * 是否是默认的公共视图,true代表为自带的公共视图，例如“全部计划”,false代表不是
     */
    @Basic()
    private String isDefault = null;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPublishPerson() {
        return publishPerson;
    }

    public void setPublishPerson(String publishPerson) {
        this.publishPerson = publishPerson;
    }

    public String getPublishDept() {
        return publishDept;
    }

    public void setPublishDept(String publishDept) {
        this.publishDept = publishDept;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getPublishPersonName() {
        return publishPersonName;
    }

    public void setPublishPersonName(String publishPersonName) {
        this.publishPersonName = publishPersonName;
    }

    public String getPublishDeptName() {
        return publishDeptName;
    }

    public void setPublishDeptName(String publishDeptName) {
        this.publishDeptName = publishDeptName;
    }
    
    @Override
    public String toString() {
        return "PlanViewInfo [name=" + name + ", status=" + status
                + ", publishPerson=" + publishPerson + ", publishDept="
                + publishDept + ", publishTime=" + publishTime + ", isDefault="
                + isDefault + "]";
    }
}
