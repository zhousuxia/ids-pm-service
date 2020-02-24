package com.glaway.ids.project.plan.entity;


import com.glaway.foundation.common.entity.GLObject;

import javax.persistence.*;
import java.util.Date;


/**
 * A representation of the model object '<em><b>DeliverablesInfoHistory</b></em>'.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "DeliverablesInfoHistory")
@Table(name = "PM_DELIVERABLES_INFO_HISTORY")
public class DeliverablesInfoHistory extends GLObject {

    /**
     * DeliverablesInfo对象ID
     * 
     * @generated
     */
    @Basic()
    private String infoId = null;

    /**
     * 外键id
     * 
     * @generated
     */
    @Basic()
    private String bizId = null;

    /**
     * 状态
     * 
     * @generated
     */
    @Basic()
    private String bizVersion = null;

    /**
     * 状态
     * 
     * @generated
     */
    @Basic()
    private String bizCurrent = null;

    /**
     * 密级
     * 
     * @generated
     */
    @Basic()
    private Short securityLevel = new Short((short)0);

    /**
     * 是否可用
     * 
     * @generated
     */
    @Basic()
    private String avaliable = "1";

    /**
     * 创建者
     * 
     * @generated
     */
    @Basic()
    private String planCreateBy = null;

    /**
     * 创建时间
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
    private Date planUpdateName = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String name = null;

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
     * 文件
     */
    @Transient()
    private Document document = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String fileId = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String origin = null;

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
     * Sets the '{@link DeliverablesInfoHistory#getInfoId() <em>infoId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newInfoId
     *            the new value of the '{@link DeliverablesInfoHistory#getInfoId() infoId}'
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
     * Sets the '{@link DeliverablesInfoHistory#getBizId() <em>bizId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newBizId
     *            the new value of the '{@link DeliverablesInfoHistory#getBizId() bizId}' feature.
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
     * Sets the '{@link DeliverablesInfoHistory#getBizVersion() <em>bizVersion</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newBizVersion
     *            the new value of the '{@link DeliverablesInfoHistory#getBizVersion() bizVersion}'
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
     * Sets the '{@link DeliverablesInfoHistory#getBizCurrent() <em>bizCurrent</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newBizCurrent
     *            the new value of the '{@link DeliverablesInfoHistory#getBizCurrent() bizCurrent}'
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
     * Sets the '{@link DeliverablesInfoHistory#getAvaliable() <em>avaliable</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newAvaliable
     *            the new value of the '{@link DeliverablesInfoHistory#getAvaliable() avaliable}'
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
     * Sets the '{@link DeliverablesInfoHistory#getPlanCreateBy() <em>planCreateBy</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanCreateBy
     *            the new value of the '{@link DeliverablesInfoHistory#getPlanCreateBy()
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
     * Sets the '{@link DeliverablesInfoHistory#getPlanCreateTime() <em>planCreateTime</em>}'
     * feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanCreateTime
     *            the new value of the '{@link DeliverablesInfoHistory#getPlanCreateTime()
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
     * Sets the '{@link DeliverablesInfoHistory#getPlanUpdateBy() <em>planUpdateBy</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanUpdateBy
     *            the new value of the '{@link DeliverablesInfoHistory#getPlanUpdateBy()
     *            planUpdateBy}' feature.
     * @generated
     */
    public void setPlanUpdateBy(String newPlanUpdateBy) {
        planUpdateBy = newPlanUpdateBy;
    }

    /**
     * Returns the value of '<em><b>planUpdateName</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>planUpdateName</b></em>' feature
     * @generated
     */
    public Date getPlanUpdateName() {
        return planUpdateName;
    }

    /**
     * Sets the '{@link DeliverablesInfoHistory#getPlanUpdateName() <em>planUpdateName</em>}'
     * feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanUpdateName
     *            the new value of the '{@link DeliverablesInfoHistory#getPlanUpdateName()
     *            planUpdateName}' feature.
     * @generated
     */
    public void setPlanUpdateName(Date newPlanUpdateName) {
        planUpdateName = newPlanUpdateName;
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
     * Sets the '{@link DeliverablesInfoHistory#getName() <em>name</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newName
     *            the new value of the '{@link DeliverablesInfoHistory#getName() name}' feature.
     * @generated
     */
    public void setName(String newName) {
        name = newName;
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
     * Sets the '{@link DeliverablesInfoHistory#getUseObjectType() <em>useObjectType</em>}'
     * feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newUseObjectType
     *            the new value of the '{@link DeliverablesInfoHistory#getUseObjectType()
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
     * Sets the '{@link DeliverablesInfoHistory#getUseObjectId() <em>useObjectId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newUseObjectId
     *            the new value of the '{@link DeliverablesInfoHistory#getUseObjectId()
     *            useObjectId}' feature.
     * @generated
     */
    public void setUseObjectId(String newUseObjectId) {
        useObjectId = newUseObjectId;
    }

    /**
     * Returns the value of '<em><b>fileId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>fileId</b></em>' feature
     * @generated
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * Sets the '{@link DeliverablesInfoHistory#getFileId() <em>fileId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newFileId
     *            the new value of the '{@link DeliverablesInfoHistory#getFileId() fileId}'
     *            feature.
     * @generated
     */
    public void setFileId(String newFileId) {
        fileId = newFileId;
    }

    /**
     * Returns the value of '<em><b>origin</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>origin</b></em>' feature
     * @generated
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * Sets the '{@link DeliverablesInfoHistory#getOrigin() <em>origin</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newOrigin
     *            the new value of the '{@link DeliverablesInfoHistory#getOrigin() origin}'
     *            feature.
     * @generated
     */
    public void setOrigin(String newOrigin) {
        origin = newOrigin;
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
     * Sets the '{@link DeliverablesInfoHistory#getFormId() <em>formId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newFormId
     *            the new value of the '{@link DeliverablesInfoHistory#getFormId() formId}'
     *            feature.
     * @generated
     */
    public void setFormId(String newFormId) {
        formId = newFormId;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
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
        return "DeliverablesInfoHistory " + " [infoId: " + getInfoId() + "]" + " [bizId: "
               + getBizId() + "]" + " [bizVersion: " + getBizVersion() + "]" + " [bizCurrent: "
               + getBizCurrent() + "]" + " [securityLevel: " + getSecurityLevel() + "]"
               + " [avaliable: " + getAvaliable() + "]" + " [planCreateBy: " + getPlanCreateBy()
               + "]" + " [planCreateTime: " + getPlanCreateTime() + "]" + " [planUpdateBy: "
               + getPlanUpdateBy() + "]" + " [planUpdateName: " + getPlanUpdateName() + "]"
               + " [name: " + getName() + "]" + " [useObjectType: " + getUseObjectType() + "]"
               + " [useObjectId: " + getUseObjectId() + "]" + " [fileId: " + getFileId() + "]"
               + " [origin: " + getOrigin() + "]" + " [formId: " + getFormId() + "]";
    }
}
