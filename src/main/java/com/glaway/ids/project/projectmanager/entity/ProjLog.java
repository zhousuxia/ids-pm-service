package com.glaway.ids.project.projectmanager.entity;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.glaway.foundation.businessobject.entity.BusinessObject;


/**
 * A representation of the model object '<em><b>ProjLog</b></em>'.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "ProjLog")
@Table(name = "PM_PROJ_LOG")
public class ProjLog extends BusinessObject {
    
    /**
     * <!-- begin-user-doc -->项目id
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String projectId = null;

    /**
     * <!-- begin-user-doc -->项目编号
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String projectNumber = null;

    /**
     * <!-- begin-user-doc -->操作信息
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String logInfo = null;

    /**
     * <!-- begin-user-doc -->操作人
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String createUserName = null;

    /**
     * <!-- begin-user-doc -->备注
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Column(length = 500)
    private String remark = null;
    
    /**
     * <!-- begin-user-doc -->用于显示日志操作人
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Transient
    private String showName;
    
    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }
    
    public String getProjectId()
    {
        return projectId;
    }

    public void setProjectId(String projectId)
    {
        this.projectId = projectId;
    }

    /**
     * Returns the value of '<em><b>projectNumber</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>projectNumber</b></em>' feature
     * @generated
     */
    public String getProjectNumber() {
        return projectNumber;
    }

    /**
     * Sets the '{@link ProjLog#getProjectNumber() <em>projectNumber</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newProjectNumber
     *            the new value of the '{@link ProjLog#getProjectNumber() projectNumber}' feature.
     * @generated
     */
    public void setProjectNumber(String newProjectNumber) {
        projectNumber = newProjectNumber;
    }

    /**
     * Returns the value of '<em><b>logInfo</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>logInfo</b></em>' feature
     * @generated
     */
    public String getLogInfo() {
        return logInfo;
    }

    /**
     * Sets the '{@link ProjLog#getLogInfo() <em>logInfo</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newLogInfo
     *            the new value of the '{@link ProjLog#getLogInfo() logInfo}' feature.
     * @generated
     */
    public void setLogInfo(String newLogInfo) {
        logInfo = newLogInfo;
    }

    /**
     * Returns the value of '<em><b>createUserName</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>createUserName</b></em>' feature
     * @generated
     */
    public String getCreateUserName() {
        return createUserName;
    }

    /**
     * Sets the '{@link ProjLog#getCreateUserName() <em>createUserName</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newCreateUserName
     *            the new value of the '{@link ProjLog#getCreateUserName() createUserName}'
     *            feature.
     * @generated
     */
    public void setCreateUserName(String newCreateUserName) {
        createUserName = newCreateUserName;
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
     * Sets the '{@link ProjLog#getRemark() <em>remark</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newRemark
     *            the new value of the '{@link ProjLog#getRemark() remark}' feature.
     * @generated
     */
    public void setRemark(String newRemark) {
        remark = newRemark;
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
        return "ProjLog " + " [projectNumber: " + getProjectNumber() + "]" + " [logInfo: "
               + getLogInfo() + "]" + " [createUserName: " + getCreateUserName() + "]"
               + " [remark: " + getRemark() + "]";
    }
}
