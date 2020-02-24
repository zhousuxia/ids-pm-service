package com.glaway.ids.project.planview.entity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.glaway.foundation.common.entity.GLObject;

/**
 * 视图关联的项目
 * @author likaiyong
 * @version 2018年5月30日
 */
@Entity(name = "PlanViewApplyProject")
@Table(name = "PLANVIEW_APPLY_PROJECT")
public class PlanViewApplyProject extends GLObject{

    /**
     * 视图信息ID
     */
    @Basic()
    private String planViewInfoId = null;
    
    /**
     * 项目ID 如果关联所有项目,则值为空
     */
    @Basic()
    private String projectId = null;

    public String getPlanViewInfoId() {
        return planViewInfoId;
    }

    public void setPlanViewInfoId(String planViewInfoId) {
        this.planViewInfoId = planViewInfoId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "PlanViewApplyProject [planViewInfoId=" + planViewInfoId
                + ", projectId=" + projectId + "]";
    }
    
}
