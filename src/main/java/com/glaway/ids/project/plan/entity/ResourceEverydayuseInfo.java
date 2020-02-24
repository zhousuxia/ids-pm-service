package com.glaway.ids.project.plan.entity;


import com.glaway.foundation.common.entity.GLObject;

import javax.persistence.*;
import java.util.Date;


/**
 * A representation of the model object '<em><b>ResourceEverydayuseInfo</b></em>
 * '. <!-- begin-user-doc --> 资源每日使用信息表<!-- end-user-doc -->
 * @author wangyangzan
 */
@Entity(name = "ResourceEverydayuseInfo")
@Table(name = "CM_RESOURCE_EVERYDAYUSE_INFO")
public class ResourceEverydayuseInfo extends GLObject {

    /**
     * <!-- begin-user-doc -->项目ID<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String projectId = null;

    /**
     * <!-- begin-user-doc -->资源ID<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String resourceId = null;

    /**
     * <!-- begin-user-doc -->使用对象类型<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String useObjectType = null;

    /**
     * <!-- begin-user-doc -->使用对象ID<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String useObjectId = null;

    /**
     * <!-- begin-user-doc -->使用率<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String useRate = null;

    /**
     * <!-- begin-user-doc -->使用时间<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Temporal(TemporalType.DATE)
    private Date useDate = null;

    /**
     * Returns the value of '<em><b>projectId</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>projectId</b></em>' feature
     * @generated
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * Sets the '{@link ResourceEverydayuseInfo#getProjectId() <em>projectId</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newProjectId
     *            the new value of the '{@link ResourceEverydayuseInfo#getProjectId() projectId}'
     *            feature.
     * @generated
     */
    public void setProjectId(String newProjectId) {
        projectId = newProjectId;
    }

    /**
     * Returns the value of '<em><b>resourceId</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>resourceId</b></em>' feature
     * @generated
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * Sets the '{@link ResourceEverydayuseInfo#getResourceId() <em>resourceId</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newResourceId
     *            the new value of the '{@link ResourceEverydayuseInfo#getResourceId() resourceId}'
     *            feature.
     * @generated
     */
    public void setResourceId(String newResourceId) {
        resourceId = newResourceId;
    }

    /**
     * Returns the value of '<em><b>useObjectType</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>useObjectType</b></em>' feature
     * @generated
     */
    public String getUseObjectType() {
        return useObjectType;
    }

    /**
     * Sets the '{@link ResourceEverydayuseInfo#getUseObjectType() <em>useObjectType</em>}'
     * feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newUseObjectType
     *            the new value of the '{@link ResourceEverydayuseInfo#getUseObjectType()
     *            useObjectType}' feature.
     * @generated
     */
    public void setUseObjectType(String newUseObjectType) {
        useObjectType = newUseObjectType;
    }

    /**
     * Returns the value of '<em><b>useObjectId</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>useObjectId</b></em>' feature
     * @generated
     */
    public String getUseObjectId() {
        return useObjectId;
    }

    /**
     * Sets the '{@link ResourceEverydayuseInfo#getUseObjectId() <em>useObjectId</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newUseObjectId
     *            the new value of the '{@link ResourceEverydayuseInfo#getUseObjectId()
     *            useObjectId}' feature.
     * @generated
     */
    public void setUseObjectId(String newUseObjectId) {
        useObjectId = newUseObjectId;
    }

    /**
     * Returns the value of '<em><b>useRate</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>useRate</b></em>' feature
     * @generated
     */
    public String getUseRate() {
        return useRate;
    }

    /**
     * Sets the '{@link ResourceEverydayuseInfo#getUseRate() <em>useRate</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newUseRate
     *            the new value of the '{@link ResourceEverydayuseInfo#getUseRate() useRate}'
     *            feature.
     * @generated
     */
    public void setUseRate(String newUseRate) {
        useRate = newUseRate;
    }

    /**
     * Returns the value of '<em><b>useDate</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>useDate</b></em>' feature
     * @generated
     */
    public Date getUseDate() {
        return useDate;
    }

    /**
     * Sets the '{@link ResourceEverydayuseInfo#getUseDate() <em>useDate</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newUseDate
     *            the new value of the '{@link ResourceEverydayuseInfo#getUseDate() useDate}'
     *            feature.
     * @generated
     */
    public void setUseDate(Date newUseDate) {
        useDate = newUseDate;
    }

    /**
     * A toString method which prints the values of all EAttributes of this instance.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String toString() {
        return "ResourceEverydayuseInfo " + " [projectId: " + getProjectId() + "]"
               + " [resourceId: " + getResourceId() + "]" + " [useObjectType: "
               + getUseObjectType() + "]" + " [useObjectId: " + getUseObjectId() + "]"
               + " [useRate: " + getUseRate() + "]" + " [useDate: " + getUseDate() + "]";
    }
}
