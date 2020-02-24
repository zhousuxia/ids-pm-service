package com.glaway.ids.project.planview.entity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.glaway.foundation.common.entity.GLObject;

/**
 * 视图关联用户
 * @author likaiyong
 * @version 2018年5月30日
 */
@Entity(name = "PlanViewApplyUser")
@Table(name = "PLANVIEW_APPLY_USER")
public class PlanViewApplyUser extends GLObject{

    /**
     * 视图信息ID
     */
    @Basic()
    private String planViewInfoId = null;
    
    /**
     * 用户ID 如果关联了所有用户,则值为空
     */
    @Basic()
    private String userId = null;

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

    @Override
    public String toString() {
        return "PlanViewApplyUser [planViewInfoId=" + planViewInfoId
                + ", userId=" + userId + "]";
    }
    
}
