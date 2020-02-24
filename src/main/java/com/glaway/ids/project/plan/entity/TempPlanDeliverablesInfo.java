package com.glaway.ids.project.plan.entity;


import com.glaway.foundation.businessobject.entity.BusinessObject;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * A representation of the model object '<em><b>TempPlanDeliverablesInfo</b></em>'.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "TempPlanDeliverablesInfo")
@Table(name = "PM_TEMP_PLAN_DELIVERABLES_INFO")
public class TempPlanDeliverablesInfo extends BusinessObject {

    /**
     * <!-- begin-user-doc --> 交付物id <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String deliverablesId = null;

    /**
     * <!-- begin-user-doc -->名称 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String name = null;

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
     * <!-- begin-user-doc --> 是否来源与活动名称库 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String origin = null;

    /**
     * <!-- begin-user-doc --> 关联的资源id <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String deliverLinkId = null;

    /**
     * 文件
     */
    @Transient()
    private Document document = null;
    
    
    /**
     * 文件
     */
    @Transient()
    private String docId = null;
    
    /**
     * 文件
     */
    @Transient()
    private String docName = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String formId = null;

    /**
     * Returns the value of '<em><b>deliverablesId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>deliverablesId</b></em>' feature
     * @generated
     */
    public String getDeliverablesId() {
        return deliverablesId;
    }

    /**
     * Sets the '{@link TempPlanDeliverablesInfo#getDeliverablesId() <em>deliverablesId</em>}'
     * feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newDeliverablesId
     *            the new value of the '{@link TempPlanDeliverablesInfo#getDeliverablesId()
     *            deliverablesId}' feature.
     * @generated
     */
    public void setDeliverablesId(String newDeliverablesId) {
        deliverablesId = newDeliverablesId;
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
     * Sets the '{@link TempPlanDeliverablesInfo#getName() <em>name</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newName
     *            the new value of the '{@link TempPlanDeliverablesInfo#getName() name}' feature.
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
     * Sets the '{@link TempPlanDeliverablesInfo#getUseObjectType() <em>useObjectType</em>}'
     * feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newUseObjectType
     *            the new value of the '{@link TempPlanDeliverablesInfo#getUseObjectType()
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
     * Sets the '{@link TempPlanDeliverablesInfo#getUseObjectId() <em>useObjectId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newUseObjectId
     *            the new value of the '{@link TempPlanDeliverablesInfo#getUseObjectId()
     *            useObjectId}' feature.
     * @generated
     */
    public void setUseObjectId(String newUseObjectId) {
        useObjectId = newUseObjectId;
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
     * Sets the '{@link TempPlanDeliverablesInfo#getOrigin() <em>origin</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newOrigin
     *            the new value of the '{@link TempPlanDeliverablesInfo#getOrigin() origin}'
     *            feature.
     * @generated
     */
    public void setOrigin(String newOrigin) {
        origin = newOrigin;
    }

    /**
     * Returns the value of '<em><b>deliverLinkId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>deliverLinkId</b></em>' feature
     * @generated
     */
    public String getDeliverLinkId() {
        return deliverLinkId;
    }

    /**
     * Sets the '{@link TempPlanDeliverablesInfo#getDeliverLinkId() <em>deliverLinkId</em>}'
     * feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newDeliverLinkId
     *            the new value of the '{@link TempPlanDeliverablesInfo#getDeliverLinkId()
     *            deliverLinkId}' feature.
     * @generated
     */
    public void setDeliverLinkId(String newDeliverLinkId) {
        deliverLinkId = newDeliverLinkId;
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
     * Sets the '{@link TempPlanDeliverablesInfo#getFormId() <em>formId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newFormId
     *            the new value of the '{@link TempPlanDeliverablesInfo#getFormId() formId}'
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

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
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
        return "TempPlanDeliverablesInfo " + " [deliverablesId: " + getDeliverablesId() + "]"
               + " [name: " + getName() + "]" + " [useObjectType: " + getUseObjectType() + "]"
               + " [useObjectId: " + getUseObjectId() + "]" + " [origin: " + getOrigin() + "]"
               + " [deliverLinkId: " + getDeliverLinkId() + "]" + " [formId: " + getFormId() + "]";
    }
}
