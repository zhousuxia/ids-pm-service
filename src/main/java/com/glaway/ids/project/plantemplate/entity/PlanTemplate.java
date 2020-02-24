package com.glaway.ids.project.plantemplate.entity;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.glaway.foundation.common.dto.TSUserDto;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import com.glaway.foundation.businessobject.entity.BusinessObject;


/**
 * A representation of the model object '<em><b>planTemplate</b></em>'.
 * <!-- begin-user-doc -->计划模版表 <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "PlanTemplate")
@Table(name = "PM_PLAN_TEMPLATE")
public class PlanTemplate extends BusinessObject {

    /**
     * <!-- begin-user-doc -->计划序号 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String planTmplNumber = null;

    /**
     * <!-- begin-user-doc -->计划名称 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String name = null;

    /**
     * <!-- begin-user-doc -->备注 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Column(length = 1000)
    private String remark = "";

    /**
     * 计划开始时间
     */
    @Transient()
    private String createTimeStr = "";

    /**
     * <!-- begin-user-doc -->计划状态 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String status = null;


    /**
     * <!-- begin-user-doc -->流程实例Id <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String processInstanceId = null;
    
    /**
     * 显示创建人
     */
    @Transient()
    private String creator = "";

    /**
     * Returns the value of '<em><b>planTmplNumber</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>planTmplNumber</b></em>' feature
     * @generated
     */
    public String getPlanTmplNumber() {
        return planTmplNumber;
    }

    /**
     * Sets the '{@link PlanTemplate#getPlanTmplNumber() <em>planTmplNumber</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanTmplNumber
     *            the new value of the '{@link PlanTemplate#getPlanTmplNumber() planTmplNumber}'
     *            feature.
     * @generated
     */
    public void setPlanTmplNumber(String newPlanTmplNumber) {
        planTmplNumber = newPlanTmplNumber;
    }

    /**
     * Returns the value of '<em><b>name</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>name</b></em>' feature
     * @generated
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the '{@link PlanTemplate#getName() <em>name</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newName
     *            the new value of the '{@link PlanTemplate#getName() name}' feature.
     * @generated
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Returns the value of '<em><b>remark</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>remark</b></em>' feature
     * @generated
     */
    public String getRemark() {
        return remark;
    }

    /**
     * Sets the '{@link PlanTemplate#getRemark() <em>remark</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newRemark
     *            the new value of the '{@link PlanTemplate#getRemark() remark}' feature.
     * @generated
     */
    public void setRemark(String newRemark) {
        remark = newRemark;
    }

    /**
     * Returns the value of '<em><b>status</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>status</b></em>' feature
     * @generated
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the '{@link PlanTemplate#getStatus() <em>status</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newStatus
     *            the new value of the '{@link PlanTemplate#getStatus() status}' feature.
     * @generated
     */
    public void setStatus(String newStatus) {
        status = newStatus;
    }

    /**
     * Returns the value of '<em><b>processInstanceId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>processInstanceId</b></em>' feature
     * @generated
     */
    public String getProcessInstanceId() {
        return processInstanceId;
    }

    /**
     * Sets the '{@link PlanTemplate#getProcessInstanceId() <em>processInstanceId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newProcessInstanceId
     *            the new value of the '{@link PlanTemplate#getProcessInstanceId()
     *            processInstanceId}' feature.
     * @generated
     */
    public void setProcessInstanceId(String newProcessInstanceId) {
        processInstanceId = newProcessInstanceId;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }


    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }


}
