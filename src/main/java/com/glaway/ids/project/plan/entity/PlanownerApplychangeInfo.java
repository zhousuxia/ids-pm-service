package com.glaway.ids.project.plan.entity;


import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.glaway.foundation.businessobject.entity.BusinessObject;


/**
 * A representation of the model object '<em><b>PlanownerApplychangeInfo</b></em>'.
 * <!-- begin-user-doc
 * -->负责人申请变更信息<!--end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "PlanownerApplychangeInfo")
@Table(name = "PM_PLANOWNER_APPLYCHANGE_INFO")
public class PlanownerApplychangeInfo extends BusinessObject {

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String changeType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Column(length = 2048)
    private String changeRemark = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String planId = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String launcher = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Temporal(TemporalType.DATE)
    private Date launchTime = null;

    /**
     * Returns the value of '<em><b>changeType</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>changeType</b></em>' feature
     * @generated
     */
    public String getChangeType() {
        return changeType;
    }

    /**
     * Sets the '{@link PlanownerApplychangeInfo#getChangeType() <em>changeType</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newChangeType
     *            the new value of the '{@link PlanownerApplychangeInfo#getChangeType() changeType}
     *            ' feature.
     * @generated
     */
    public void setChangeType(String newChangeType) {
        changeType = newChangeType;
    }

    /**
     * Returns the value of '<em><b>changeRemark</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>changeRemark</b></em>' feature
     * @generated
     */
    public String getChangeRemark() {
        return changeRemark;
    }

    /**
     * Sets the '{@link PlanownerApplychangeInfo#getChangeRemark() <em>changeRemark</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newChangeRemark
     *            the new value of the '{@link PlanownerApplychangeInfo#getChangeRemark()
     *            changeRemark}' feature.
     * @generated
     */
    public void setChangeRemark(String newChangeRemark) {
        changeRemark = newChangeRemark;
    }

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
     * Sets the '{@link PlanownerApplychangeInfo#getPlanId() <em>planId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanId
     *            the new value of the '{@link PlanownerApplychangeInfo#getPlanId() planId}'
     *            feature.
     * @generated
     */
    public void setPlanId(String newPlanId) {
        planId = newPlanId;
    }

    /**
     * Returns the value of '<em><b>launcher</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>launcher</b></em>' feature
     * @generated
     */
    public String getLauncher() {
        return launcher;
    }

    /**
     * Sets the '{@link PlanownerApplychangeInfo#getLauncher() <em>launcher</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newLauncher
     *            the new value of the '{@link PlanownerApplychangeInfo#getLauncher() launcher}'
     *            feature.
     * @generated
     */
    public void setLauncher(String newLauncher) {
        launcher = newLauncher;
    }

    /**
     * Returns the value of '<em><b>launchTime</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>launchTime</b></em>' feature
     * @generated
     */
    public Date getLaunchTime() {
        return launchTime;
    }

    /**
     * Sets the '{@link PlanownerApplychangeInfo#getLaunchTime() <em>launchTime</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newLaunchTime
     *            the new value of the '{@link PlanownerApplychangeInfo#getLaunchTime() launchTime}
     *            ' feature.
     * @generated
     */
    public void setLaunchTime(Date newLaunchTime) {
        launchTime = newLaunchTime;
    }

    /**
     * A toString method which prints the values of all EAttributes of this instance.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String toString() {
        return "PlanownerApplychangeInfo " + " [changeType: " + getChangeType() + "]"
               + " [changeRemark: " + getChangeRemark() + "]" + " [planId: " + getPlanId() + "]"
               + " [launcher: " + getLauncher() + "]" + " [launchTime: " + getLaunchTime() + "]";
    }
}
