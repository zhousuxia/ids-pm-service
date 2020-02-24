package com.glaway.ids.project.plan.entity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.glaway.foundation.common.entity.GLObject;

/**
 * 用户项目视图
 * @author likaiyong
 * @version 2018年5月30日
 */
@Entity(name = "UserPlanViewProject")
@Table(name = "PLANVIEW_USER_PROJECT")
public class UserPlanViewProject extends GLObject{

    /**
     * 视图信息ID
     */
    @Basic()
    private String planViewInfoId = null;
    
    /**
     * 用户ID 
     */
    @Basic()
    private String userId = null;
    
    /**
     * 项目ID
     */
    @Basic()
    private String projectId = null;

    public String getPlanViewInfoId() {
        return planViewInfoId;
    }

    public void setPlanViewInfoId(String planViewInfoId) {
        this.planViewInfoId = planViewInfoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "UserPlanViewProject [planViewInfoId=" + planViewInfoId
                + ", userId=" + userId + ", projectId=" + projectId + "]";
    }
    
}
