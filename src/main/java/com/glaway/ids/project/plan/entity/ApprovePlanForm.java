package com.glaway.ids.project.plan.entity;


import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.glaway.foundation.common.entity.GLObject;


/**
 * A representation of the model object '<em><b>ApprovePlanForm</b></em>'.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "ApprovePlanForm")
@Table(name = "PM_APPROVE_PLAN_FORM")
public class ApprovePlanForm extends GLObject {

    /**
     * <!-- begin-user-doc --> 类型 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String approveType = null;

    /**
     * <!-- begin-user-doc --> 流程id <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String procInstId = null;

    /**
     * Returns the value of '<em><b>approveType</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>approveType</b></em>' feature
     * @generated
     */
    public String getApproveType() {
        return approveType;
    }

    /**
     * Sets the '{@link ApprovePlanForm#getApproveType() <em>approveType</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newApproveType
     *            the new value of the '{@link ApprovePlanForm#getApproveType() approveType}'
     *            feature.
     * @generated
     */
    public void setApproveType(String newApproveType) {
        approveType = newApproveType;
    }

    /**
     * Returns the value of '<em><b>procInstId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>procInstId</b></em>' feature
     * @generated
     */
    public String getProcInstId() {
        return procInstId;
    }

    /**
     * Sets the '{@link ApprovePlanForm#getProcInstId() <em>procInstId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newProcInstId
     *            the new value of the '{@link ApprovePlanForm#getProcInstId() procInstId}'
     *            feature.
     * @generated
     */
    public void setProcInstId(String newProcInstId) {
        procInstId = newProcInstId;
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
        return "ApprovePlanForm " + " [approveType: " + getApproveType() + "]" + " [procInstId: "
               + getProcInstId() + "]";
    }
}
