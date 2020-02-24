package com.glaway.ids.project.plan.entity;


import com.glaway.foundation.common.entity.GLObject;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;


/**
 * A representation of the model object '<em><b>ResourceLinkInfoHistory</b></em>'.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "ResourceLinkInfoHistory")
@Table(name = "PM_RESOURCE_LINK_INFO_HISTORY", schema = "")
public class ResourceLinkInfoHistory extends GLObject {

    /**
     * ResourceLinkInfo对象ID
     * 
     * @generated
     */
    @Basic()
    private String infoId = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String bizId = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String bizVersion = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String bizCurrent = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private Short securityLevel = new Short((short)1);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String avaliable = "1";

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String planCreateBy = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Temporal(TemporalType.DATE)
    private Date planCreateTime = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String planUpdateBy = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Temporal(TemporalType.DATE)
    private Date planUpdateTime = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String resourceId = null;

    /**
     * 
     */
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "resourceId", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private Resource resourceInfo;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String useObjectType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String useObjectId = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String useRate = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String resourceName = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String resourceType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String formId = null;

    /**
     * Returns the value of '<em><b>infoId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>infoId</b></em>' feature
     * @generated
     */
    public String getInfoId() {
        return infoId;
    }

    /**
     * Sets the '{@link ResourceLinkInfoHistory#getInfoId() <em>infoId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newInfoId
     *            the new value of the '{@link ResourceLinkInfoHistory#getInfoId() infoId}'
     *            feature.
     * @generated
     */
    public void setInfoId(String newInfoId) {
        infoId = newInfoId;
    }

    /**
     * Returns the value of '<em><b>bizId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>bizId</b></em>' feature
     * @generated
     */
    public String getBizId() {
        return bizId;
    }

    /**
     * Sets the '{@link ResourceLinkInfoHistory#getBizId() <em>bizId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newBizId
     *            the new value of the '{@link ResourceLinkInfoHistory#getBizId() bizId}' feature.
     * @generated
     */
    public void setBizId(String newBizId) {
        bizId = newBizId;
    }

    /**
     * Returns the value of '<em><b>bizVersion</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>bizVersion</b></em>' feature
     * @generated
     */
    public String getBizVersion() {
        return bizVersion;
    }

    /**
     * Sets the '{@link ResourceLinkInfoHistory#getBizVersion() <em>bizVersion</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newBizVersion
     *            the new value of the '{@link ResourceLinkInfoHistory#getBizVersion() bizVersion}'
     *            feature.
     * @generated
     */
    public void setBizVersion(String newBizVersion) {
        bizVersion = newBizVersion;
    }

    /**
     * Returns the value of '<em><b>bizCurrent</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>bizCurrent</b></em>' feature
     * @generated
     */
    public String getBizCurrent() {
        return bizCurrent;
    }

    /**
     * Sets the '{@link ResourceLinkInfoHistory#getBizCurrent() <em>bizCurrent</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newBizCurrent
     *            the new value of the '{@link ResourceLinkInfoHistory#getBizCurrent() bizCurrent}'
     *            feature.
     * @generated
     */
    public void setBizCurrent(String newBizCurrent) {
        bizCurrent = newBizCurrent;
    }

    public Short getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(Short securityLevel) {
        this.securityLevel = securityLevel;
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
     * Sets the '{@link ResourceLinkInfoHistory#getAvaliable() <em>avaliable</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newAvaliable
     *            the new value of the '{@link ResourceLinkInfoHistory#getAvaliable() avaliable}'
     *            feature.
     * @generated
     */
    public void setAvaliable(String newAvaliable) {
        avaliable = newAvaliable;
    }

    /**
     * Returns the value of '<em><b>planCreateBy</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>planCreateBy</b></em>' feature
     * @generated
     */
    public String getPlanCreateBy() {
        return planCreateBy;
    }

    /**
     * Sets the '{@link ResourceLinkInfoHistory#getPlanCreateBy() <em>planCreateBy</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanCreateBy
     *            the new value of the '{@link ResourceLinkInfoHistory#getPlanCreateBy()
     *            planCreateBy}' feature.
     * @generated
     */
    public void setPlanCreateBy(String newPlanCreateBy) {
        planCreateBy = newPlanCreateBy;
    }

    /**
     * Returns the value of '<em><b>planCreateTime</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>planCreateTime</b></em>' feature
     * @generated
     */
    public Date getPlanCreateTime() {
        return planCreateTime;
    }

    /**
     * Sets the '{@link ResourceLinkInfoHistory#getPlanCreateTime() <em>planCreateTime</em>}'
     * feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanCreateTime
     *            the new value of the '{@link ResourceLinkInfoHistory#getPlanCreateTime()
     *            planCreateTime}' feature.
     * @generated
     */
    public void setPlanCreateTime(Date newPlanCreateTime) {
        planCreateTime = newPlanCreateTime;
    }

    /**
     * Returns the value of '<em><b>planUpdateBy</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>planUpdateBy</b></em>' feature
     * @generated
     */
    public String getPlanUpdateBy() {
        return planUpdateBy;
    }

    /**
     * Sets the '{@link ResourceLinkInfoHistory#getPlanUpdateBy() <em>planUpdateBy</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanUpdateBy
     *            the new value of the '{@link ResourceLinkInfoHistory#getPlanUpdateBy()
     *            planUpdateBy}' feature.
     * @generated
     */
    public void setPlanUpdateBy(String newPlanUpdateBy) {
        planUpdateBy = newPlanUpdateBy;
    }

    /**
     * Returns the value of '<em><b>planUpdateTime</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>planUpdateTime</b></em>' feature
     * @generated
     */
    public Date getPlanUpdateTime() {
        return planUpdateTime;
    }

    /**
     * Sets the '{@link ResourceLinkInfoHistory#getPlanUpdateTime() <em>planUpdateTime</em>}'
     * feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanUpdateTime
     *            the new value of the '{@link ResourceLinkInfoHistory#getPlanUpdateTime()
     *            planUpdateTime}' feature.
     * @generated
     */
    public void setPlanUpdateTime(Date newPlanUpdateTime) {
        planUpdateTime = newPlanUpdateTime;
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
     * Sets the '{@link ResourceLinkInfoHistory#getResourceId() <em>resourceId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newResourceId
     *            the new value of the '{@link ResourceLinkInfoHistory#getResourceId() resourceId}'
     *            feature.
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
     * Sets the '{@link ResourceLinkInfoHistory#getUseObjectType() <em>useObjectType</em>}'
     * feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newUseObjectType
     *            the new value of the '{@link ResourceLinkInfoHistory#getUseObjectType()
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
     * Sets the '{@link ResourceLinkInfoHistory#getUseObjectId() <em>useObjectId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newUseObjectId
     *            the new value of the '{@link ResourceLinkInfoHistory#getUseObjectId()
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
     * Sets the '{@link ResourceLinkInfoHistory#getUseRate() <em>useRate</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newUseRate
     *            the new value of the '{@link ResourceLinkInfoHistory#getUseRate() useRate}'
     *            feature.
     * @generated
     */
    public void setUseRate(String newUseRate) {
        useRate = newUseRate;
    }

    /**
     * Returns the value of '<em><b>resourceName</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>resourceName</b></em>' feature
     * @generated
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * Sets the '{@link ResourceLinkInfoHistory#getResourceName() <em>resourceName</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newResourceName
     *            the new value of the '{@link ResourceLinkInfoHistory#getResourceName()
     *            resourceName}' feature.
     * @generated
     */
    public void setResourceName(String newResourceName) {
        resourceName = newResourceName;
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
     * Sets the '{@link ResourceLinkInfoHistory#getResourceType() <em>resourceType</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newResourceType
     *            the new value of the '{@link ResourceLinkInfoHistory#getResourceType()
     *            resourceType}' feature.
     * @generated
     */
    public void setResourceType(String newResourceType) {
        resourceType = newResourceType;
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
     * Sets the '{@link ResourceLinkInfoHistory#getFormId() <em>formId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newFormId
     *            the new value of the '{@link ResourceLinkInfoHistory#getFormId() formId}'
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

    /**
     * A toString method which prints the values of all EAttributes of this instance.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String toString() {
        return "ResourceLinkInfoHistory " + " [infoId: " + getInfoId() + "]" + " [bizId: "
               + getBizId() + "]" + " [bizVersion: " + getBizVersion() + "]" + " [bizCurrent: "
               + getBizCurrent() + "]" + " [securityLevel: " + getSecurityLevel() + "]"
               + " [avaliable: " + getAvaliable() + "]" + " [planCreateBy: " + getPlanCreateBy()
               + "]" + " [planCreateTime: " + getPlanCreateTime() + "]" + " [planUpdateBy: "
               + getPlanUpdateBy() + "]" + " [planUpdateTime: " + getPlanUpdateTime() + "]"
               + " [resourceId: " + getResourceId() + "]" + " [useObjectType: "
               + getUseObjectType() + "]" + " [useObjectId: " + getUseObjectId() + "]"
               + " [useRate: " + getUseRate() + "]" + " [resourceName: " + getResourceName() + "]"
               + " [resourceType: " + getResourceType() + "]" + " [formId: " + getFormId() + "]";
    }
}
