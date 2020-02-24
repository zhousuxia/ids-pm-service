package com.glaway.ids.project.plan.entity;


import com.glaway.foundation.businessobject.entity.BusinessObject;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;


/**
 * A representation of the model object '<em><b>TempPlanResourceLinkInfo</b></em>'.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "TempPlanResourceLinkInfo")
@Table(name = "PM_TEMP_PLAN_RESOURCELINK_INFO")
public class TempPlanResourceLinkInfo extends BusinessObject {

    /**
     * <!-- begin-user-doc --> 关联的资源id <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String resourceLinkId = null;

    /**
     * <!-- begin-user-doc --> 资源id <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String resourceId = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     */
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "resourceId", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private Resource resourceInfo;

    /**
     * <!-- begin-user-doc --> 关联的外键类型 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String useObjectType = null;

    /**
     * <!-- begin-user-doc --> 关联的外键id <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String useObjectId = null;

    /**
     * <!-- begin-user-doc --> 进度 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String useRate = null;

    /**
     * <!-- begin-user-doc --> 资源类型 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String resourceType = null;

    /**
     * 资源名称
     */
    @Transient()
    private String resourceName = null;

    /**
     * <!-- begin-user-doc --> 开始时间 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Temporal(TemporalType.DATE)
    private Date startTime = null;

    /**
     * 开始时间
     */
    @Transient()
    private boolean startTimeOverflow = false;

    /**
     * <!-- begin-user-doc --> 结束时间 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Temporal(TemporalType.DATE)
    private Date endTime = null;

    /**
     * 结束时间
     */
    @Transient()
    private boolean endTimeOverflow = false;

    /**
     * 计划开始时间
     */
    @Transient()
    private String planStartTime = null;

    /**
     * 计划结束时间
     */
    @Transient()
    private String planEndTime = null;

    /**
     * 计划名称
     */
    @Transient()
    private String planName = null;

    /**
     * 外键id
     */
    @Transient()
    private String linkId = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String formId = null;

    /**
     * Returns the value of '<em><b>resourceLinkId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>resourceLinkId</b></em>' feature
     * @generated
     */
    public String getResourceLinkId() {
        return resourceLinkId;
    }

    /**
     * Sets the '{@link TempPlanResourceLinkInfo#getResourceLinkId() <em>resourceLinkId</em>}'
     * feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newResourceLinkId
     *            the new value of the '{@link TempPlanResourceLinkInfo#getResourceLinkId()
     *            resourceLinkId}' feature.
     * @generated
     */
    public void setResourceLinkId(String newResourceLinkId) {
        resourceLinkId = newResourceLinkId;
    }

    /**
     * Returns the value of '<em><b>resourceId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>resourceId</b></em>' feature
     * @generated
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * Sets the '{@link TempPlanResourceLinkInfo#getResourceId() <em>resourceId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newResourceId
     *            the new value of the '{@link TempPlanResourceLinkInfo#getResourceId() resourceId}
     *            ' feature.
     * @generated
     */
    public void setResourceId(String newResourceId) {
        resourceId = newResourceId;
    }

    /**
     * Returns the value of '<em><b>useObjectType</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>useObjectType</b></em>' feature
     * @generated
     */
    public String getUseObjectType() {
        return useObjectType;
    }

    /**
     * Sets the '{@link TempPlanResourceLinkInfo#getUseObjectType() <em>useObjectType</em>}'
     * feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newUseObjectType
     *            the new value of the '{@link TempPlanResourceLinkInfo#getUseObjectType()
     *            useObjectType}' feature.
     * @generated
     */
    public void setUseObjectType(String newUseObjectType) {
        useObjectType = newUseObjectType;
    }

    /**
     * Returns the value of '<em><b>useObjectId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>useObjectId</b></em>' feature
     * @generated
     */
    public String getUseObjectId() {
        return useObjectId;
    }

    /**
     * Sets the '{@link TempPlanResourceLinkInfo#getUseObjectId() <em>useObjectId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newUseObjectId
     *            the new value of the '{@link TempPlanResourceLinkInfo#getUseObjectId()
     *            useObjectId}' feature.
     * @generated
     */
    public void setUseObjectId(String newUseObjectId) {
        useObjectId = newUseObjectId;
    }

    /**
     * Returns the value of '<em><b>useRate</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>useRate</b></em>' feature
     * @generated
     */
    public String getUseRate() {
        return useRate;
    }

    /**
     * Sets the '{@link TempPlanResourceLinkInfo#getUseRate() <em>useRate</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newUseRate
     *            the new value of the '{@link TempPlanResourceLinkInfo#getUseRate() useRate}'
     *            feature.
     * @generated
     */
    public void setUseRate(String newUseRate) {
        useRate = newUseRate;
    }

    /**
     * Returns the value of '<em><b>resourceType</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>resourceType</b></em>' feature
     * @generated
     */
    public String getResourceType() {
        return resourceType;
    }

    /**
     * Sets the '{@link TempPlanResourceLinkInfo#getResourceType() <em>resourceType</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newResourceType
     *            the new value of the '{@link TempPlanResourceLinkInfo#getResourceType()
     *            resourceType}' feature.
     * @generated
     */
    public void setResourceType(String newResourceType) {
        resourceType = newResourceType;
    }

    /**
     * Returns the value of '<em><b>startTime</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>startTime</b></em>' feature
     * @generated
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Sets the '{@link TempPlanResourceLinkInfo#getStartTime() <em>startTime</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newStartTime
     *            the new value of the '{@link TempPlanResourceLinkInfo#getStartTime() startTime}'
     *            feature.
     * @generated
     */
    public void setStartTime(Date newStartTime) {
        startTime = newStartTime;
    }

    /**
     * Returns the value of '<em><b>endTime</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>endTime</b></em>' feature
     * @generated
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * Sets the '{@link TempPlanResourceLinkInfo#getEndTime() <em>endTime</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newEndTime
     *            the new value of the '{@link TempPlanResourceLinkInfo#getEndTime() endTime}'
     *            feature.
     * @generated
     */
    public void setEndTime(Date newEndTime) {
        endTime = newEndTime;
    }

    /**
     * Returns the value of '<em><b>formId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>formId</b></em>' feature
     * @generated
     */
    public String getFormId() {
        return formId;
    }

    /**
     * Sets the '{@link TempPlanResourceLinkInfo#getFormId() <em>formId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newFormId
     *            the new value of the '{@link TempPlanResourceLinkInfo#getFormId() formId}'
     *            feature.
     * @generated
     */
    public void setFormId(String newFormId) {
        formId = newFormId;
    }

    public Resource getResourceInfo() {
        return resourceInfo;
    }

    public void setResourceInfo(Resource resourceInfo) {
        this.resourceInfo = resourceInfo;
    }

    public boolean isStartTimeOverflow() {
        return startTimeOverflow;
    }

    public void setStartTimeOverflow(boolean startTimeOverflow) {
        this.startTimeOverflow = startTimeOverflow;
    }

    public boolean isEndTimeOverflow() {
        return endTimeOverflow;
    }

    public void setEndTimeOverflow(boolean endTimeOverflow) {
        this.endTimeOverflow = endTimeOverflow;
    }

    public String getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(String planStartTime) {
        this.planStartTime = planStartTime;
    }

    public String getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(String planEndTime) {
        this.planEndTime = planEndTime;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
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
        return "TempPlanResourceLinkInfo " + " [resourceLinkId: " + getResourceLinkId() + "]"
               + " [resourceId: " + getResourceId() + "]" + " [useObjectType: "
               + getUseObjectType() + "]" + " [useObjectId: " + getUseObjectId() + "]"
               + " [useRate: " + getUseRate() + "]" + " [resourceType: " + getResourceType() + "]"
               + " [startTime: " + getStartTime() + "]" + " [endTime: " + getEndTime() + "]"
               + " [formId: " + getFormId() + "]";
    }
}
