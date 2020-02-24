package com.glaway.ids.config.entity;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.glaway.foundation.businessobject.entity.BusinessObject;


/**
 * A representation of the model object '<em><b>ParamSwitch</b></em>'. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "ParamSwitch")
@Table(name = "CM_PARAM_SWITCH")
public class ParamSwitch extends BusinessObject {

    /**
     * <!-- begin-user-doc -->计划参数开关名称 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String name = null;

    /**
     * <!-- begin-user-doc -->状态 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Column(length=4000)
    private String status = null;

    /**
     * <!-- begin-user-doc -->开关类型 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String switchType = null;

    /**
     * <!-- begin-user-doc -->状态列表 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Column(length=4000)
    private String statusList = null;

    /**
     * Returns the value of '<em><b>name</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>name</b></em>' feature
     * @generated
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the '{@link ParamSwitch#getName() <em>name</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newName
     *            the new value of the '{@link ParamSwitch#getName() name}' feature.
     * @generated
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Returns the value of '<em><b>status</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>status</b></em>' feature
     * @generated
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the '{@link ParamSwitch#getStatus() <em>status</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newStatus
     *            the new value of the '{@link ParamSwitch#getStatus() status}' feature.
     * @generated
     */
    public void setStatus(String newStatus) {
        status = newStatus;
    }

    /**
     * Returns the value of '<em><b>switchType</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>switchType</b></em>' feature
     * @generated
     */
    public String getSwitchType() {
        return switchType;
    }

    /**
     * Sets the '{@link ParamSwitch#getSwitchType() <em>switchType</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newSwitchType
     *            the new value of the '{@link ParamSwitch#getSwitchType() switchType}' feature.
     * @generated
     */
    public void setSwitchType(String newSwitchType) {
        switchType = newSwitchType;
    }

    public String getStatusList() {
        return statusList;
    }

    public void setStatusList(String statusList) {
        this.statusList = statusList;
    }

    /**
     * A toString method which prints the values of all EAttributes of this instance.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String toString() {
        return "ParamSwitch " + " [name: " + getName() + "]" + " [status: " + getStatus() + "]"
               + " [switchType: " + getSwitchType() + "]" + " [statusList: " + getStatusList()
               + "]";
    }
}
