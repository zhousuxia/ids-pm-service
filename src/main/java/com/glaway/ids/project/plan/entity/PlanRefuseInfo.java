package com.glaway.ids.project.plan.entity;


import com.glaway.foundation.common.entity.GLObject;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * A representation of the model object '<em><b>PlanLog</b></em>'.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * 计划拒绝信息
 * @generated
 */
@Entity(name = "PlanRefuseInfo")
@Table(name = "PM_REFUSE_INFO")
public class PlanRefuseInfo extends GLObject {

    /**
     * 拒绝原因
     */
    @Basic()
    private String refuseReason = null;

    /**
     * 备注
     */
    @Basic()
    private String remark = null;

    /**
     * 计划id
     */
    @Basic()
    private String planId = null;

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }
}
