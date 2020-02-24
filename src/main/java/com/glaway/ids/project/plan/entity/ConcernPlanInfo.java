package com.glaway.ids.project.plan.entity;

import com.glaway.foundation.common.entity.GLObject;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 关注计划信息表
 */
@Entity(name = "ConcernPlanInfo")
@Table(name = "PM_CONCERN_PLAN_INFO")
public class ConcernPlanInfo extends GLObject {

    /**
     * <!-- begin-user-doc --> 计划ID <!-- end-user-doc -->
     *
     * @generated
     */
    @Basic()
    private String planId;

    /**
     * <!-- begin-user-doc --> 用户ID <!-- end-user-doc -->
     *
     * @generated
     */
    @Basic()
    private String userId;

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
