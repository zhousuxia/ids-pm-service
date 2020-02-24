package com.glaway.ids.project.plan.entity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.glaway.foundation.common.entity.GLObject;

/**
 * 视图设置条件
 * @author likaiyong
 * @version 2018年5月30日
 */
@Entity(name = "PlanViewSetCondition")
@Table(name = "PLANVIEW_SET_CONDITION")
public class PlanViewSetCondition extends GLObject{
    
    /**
     * 视图信息ID
     */
    @Basic()
    private String planViewInfoId = null;
    
    /**
     * 部门ID
     */
    @Basic()
    private String departmentId = null;
    
    
    /**
     * 计划Id
     */
    @Basic()
    private String planId = null;
    
    /**
     * 时间范围
     */
    @Basic()
    private String timeRange = null;
    
    /**
     * 展示范围
     */
    @Basic()
    private String showRange = null;

    public String getPlanViewInfoId() {
        return planViewInfoId;
    }

    public void setPlanViewInfoId(String planViewInfoId) {
        this.planViewInfoId = planViewInfoId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public String getShowRange() {
        return showRange;
    }

    public void setShowRange(String showRange) {
        this.showRange = showRange;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    @Override
    public String toString() {
        return "PlanViewSetCondition [planViewInfoId=" + planViewInfoId + ", departmentId="
               + departmentId + ", planId=" + planId + ", timeRange=" + timeRange + ", showRange="
               + showRange + "]";
    }

    
}
