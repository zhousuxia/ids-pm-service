package com.glaway.ids.project.plan.entity;


import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import com.glaway.foundation.common.entity.GLObject;


/**
 * A representation of the model object '<em><b>PreposePlan</b></em>'.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "PreposePlan")
@Table(name = "PM_PREPOSE_PLAN")
public class PreposePlan extends GLObject {

    /**
     * <!-- begin-user-doc -->计划id <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String planId = null;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "planId", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private Plan planInfo = null;

    /**
     * <!-- begin-user-doc --> 前置计划id <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String preposePlanId = null;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "preposePlanId", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private Plan preposePlanInfo = null;

    /**
     * <!-- begin-user-doc --> 是否可用 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String avaliable = "1";

    /**
     * Returns the value of '<em><b>planId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>planId</b></em>' feature
     * @generated
     */
    public String getPlanId() {
        return planId;
    }

    /**
     * Sets the '{@link PreposePlan#getPlanId() <em>planId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanId
     *            the new value of the '{@link PreposePlan#getPlanId() planId}' feature.
     * @generated
     */
    public void setPlanId(String newPlanId) {
        planId = newPlanId;
    }

    /**
     * Returns the value of '<em><b>preposePlanId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>preposePlanId</b></em>' feature
     * @generated
     */
    public String getPreposePlanId() {
        return preposePlanId;
    }

    /**
     * Sets the '{@link PreposePlan#getPreposePlanId() <em>preposePlanId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPreposePlanId
     *            the new value of the '{@link PreposePlan#getPreposePlanId() preposePlanId}'
     *            feature.
     * @generated
     */
    public void setPreposePlanId(String newPreposePlanId) {
        preposePlanId = newPreposePlanId;
    }

    /**
     * Returns the value of '<em><b>avaliable</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>avaliable</b></em>' feature
     * @generated
     */
    public String getAvaliable() {
        return avaliable;
    }

    /**
     * Sets the '{@link PreposePlan#getAvaliable() <em>avaliable</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newAvaliable
     *            the new value of the '{@link PreposePlan#getAvaliable() avaliable}' feature.
     * @generated
     */
    public void setAvaliable(String newAvaliable) {
        avaliable = newAvaliable;
    }

    public Plan getPlanInfo() {
        return planInfo;
    }

    public void setPlanInfo(Plan planInfo) {
        this.planInfo = planInfo;
    }

    public Plan getPreposePlanInfo() {
        return preposePlanInfo;
    }

    public void setPreposePlanInfo(Plan preposePlanInfo) {
        this.preposePlanInfo = preposePlanInfo;
    }

    /**
     * A toString method which prints the values of all EAttributes of this instance.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String toString() {
        return "PreposePlan " + " [planId: " + getPlanId() + "]" + " [preposePlanId: "
               + getPreposePlanId() + "]" + " [avaliable: " + getAvaliable() + "]";
    }
}
