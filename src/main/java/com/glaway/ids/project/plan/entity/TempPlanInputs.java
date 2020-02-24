package com.glaway.ids.project.plan.entity;


import com.glaway.foundation.common.entity.GLObject;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 变更计划 输入
 * 
 * @generated
 */
@Entity(name = "TempPlanInputs")
@Table(name = "PM_TEMP_PLAN_INPUTS")
public class TempPlanInputs extends GLObject {

    /**
     * <!-- begin-user-doc -->输入id
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String inputId = null;

    /**
     * <!-- begin-user-doc -->输入名称 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String name = null;

    /**
     * <!-- begin-user-doc -->外键类型 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String useObjectType = null;

    /**
     * <!-- begin-user-doc -->外键id<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String useObjectId = null;

    /**
     * <!-- begin-user-doc -->所属<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String fileId = null;

    /**
     * <!-- begin-user-doc -->来源<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String origin = null;

    /**
     * <!-- begin-user-doc -->是否必要 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String required = null;

    /**
     * <!-- begin-user-doc -->项目库文档ID <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String docId = null;

    /**
     * <!-- begin-user-doc -->文档名称 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
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
     * <!-- begin-user-doc -->是否选中 <!-- end-user-doc -->
     */
    @Transient()
    private String checked = null;

    /**
     * <!-- begin-user-doc -->文件 <!-- end-user-doc -->
     */
    @Transient
    private Document document = null;

    /**
     * <!-- begin-user-doc -->来源计划对象ID <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String originObjectId = null;

    /**
     * <!-- begin-user-doc -->来源输出对象ID <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String originDeliverablesInfoId = null;
    
    /**
     * 输入类型
     */
    @Basic()
    private String originType = null;
    
    /**
     * 文档名称
     */
    @Transient()
    private String docNameShow = null;
    
    /**
     * 来源路径显示
     */
    @Transient()
    private String originPath = null;
    
    @Transient()
    private String docIdShow = null;

    /**
     * Returns the value of '<em><b>inputId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>inputId</b></em>' feature
     * @generated
     */
    public String getInputId() {
        return inputId;
    }

    /**
     * Sets the '{@link TempPlanInputs#getInputId() <em>inputId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newInputId
     *            the new value of the '{@link TempPlanInputs#getInputId() inputId}' feature.
     * @generated
     */
    public void setInputId(String newInputId) {
        inputId = newInputId;
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
     * Sets the '{@link TempPlanInputs#getName() <em>name</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newName
     *            the new value of the '{@link TempPlanInputs#getName() name}' feature.
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
     * Sets the '{@link TempPlanInputs#getUseObjectType() <em>useObjectType</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newUseObjectType
     *            the new value of the '{@link TempPlanInputs#getUseObjectType() useObjectType}'
     *            feature.
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
     * Sets the '{@link TempPlanInputs#getUseObjectId() <em>useObjectId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newUseObjectId
     *            the new value of the '{@link TempPlanInputs#getUseObjectId() useObjectId}'
     *            feature.
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
     * Sets the '{@link TempPlanInputs#getFileId() <em>fileId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newFileId
     *            the new value of the '{@link TempPlanInputs#getFileId() fileId}' feature.
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
     * Sets the '{@link TempPlanInputs#getOrigin() <em>origin</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newOrigin
     *            the new value of the '{@link TempPlanInputs#getOrigin() origin}' feature.
     * @generated
     */
    public void setOrigin(String newOrigin) {
        origin = newOrigin;
    }

    /**
     * Returns the value of '<em><b>required</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>required</b></em>' feature
     * @generated
     */
    public String getRequired() {
        return required;
    }

    /**
     * Sets the '{@link TempPlanInputs#getRequired() <em>required</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newRequired
     *            the new value of the '{@link TempPlanInputs#getRequired() required}' feature.
     * @generated
     */
    public void setRequired(String newRequired) {
        required = newRequired;
    }

    /**
     * Returns the value of '<em><b>docId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>docId</b></em>' feature
     * @generated
     */
    public String getDocId() {
        return docId;
    }

    /**
     * Sets the '{@link TempPlanInputs#getDocId() <em>docId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newDocId
     *            the new value of the '{@link TempPlanInputs#getDocId() docId}' feature.
     * @generated
     */
    public void setDocId(String newDocId) {
        docId = newDocId;
    }

    /**
     * Returns the value of '<em><b>docName</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>docName</b></em>' feature
     * @generated
     */
    public String getDocName() {
        return docName;
    }

    /**
     * Sets the '{@link TempPlanInputs#getDocName() <em>docName</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newDocName
     *            the new value of the '{@link TempPlanInputs#getDocName() docName}' feature.
     * @generated
     */
    public void setDocName(String newDocName) {
        docName = newDocName;
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
     * Sets the '{@link TempPlanInputs#getFormId() <em>formId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newFormId
     *            the new value of the '{@link TempPlanInputs#getFormId() formId}' feature.
     * @generated
     */
    public void setFormId(String newFormId) {
        formId = newFormId;
    }

    /**
     * Returns the value of '<em><b>originObjectId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>originObjectId</b></em>' feature
     * @generated
     */
    public String getOriginObjectId() {
        return originObjectId;
    }

    /**
     * Sets the '{@link TempPlanInputs#getOriginObjectId() <em>originObjectId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newOriginObjectId
     *            the new value of the '{@link TempPlanInputs#getOriginObjectId() originObjectId}'
     *            feature.
     * @generated
     */
    public void setOriginObjectId(String newOriginObjectId) {
        originObjectId = newOriginObjectId;
    }

    /**
     * Returns the value of '<em><b>originDeliverablesInfoId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>originDeliverablesInfoId</b></em>' feature
     * @generated
     */
    public String getOriginDeliverablesInfoId() {
        return originDeliverablesInfoId;
    }

    /**
     * Sets the '{@link TempPlanInputs#getOriginDeliverablesInfoId()
     * <em>originDeliverablesInfoId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newOriginDeliverablesInfoId
     *            the new value of the '{@link TempPlanInputs#getOriginDeliverablesInfoId()
     *            originDeliverablesInfoId}' feature.
     * @generated
     */
    public void setOriginDeliverablesInfoId(String newOriginDeliverablesInfoId) {
        originDeliverablesInfoId = newOriginDeliverablesInfoId;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
    

    public String getOriginType() {
        return originType;
    }

    public void setOriginType(String originType) {
        this.originType = originType;
    }


    public String getDocNameShow() {
        return docNameShow;
    }

    public void setDocNameShow(String docNameShow) {
        this.docNameShow = docNameShow;
    }
    

    public String getOriginPath() {
        return originPath;
    }

    public void setOriginPath(String originPath) {
        this.originPath = originPath;
    }
    

    public String getDocIdShow() {
        return docIdShow;
    }

    public void setDocIdShow(String docIdShow) {
        this.docIdShow = docIdShow;
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
        return "TempPlanInputs [inputId=" + inputId + ", name=" + name + ", useObjectType="
               + useObjectType + ", useObjectId=" + useObjectId + ", fileId=" + fileId
               + ", origin=" + origin + ", required=" + required + ", docId=" + docId
               + ", docName=" + docName + ", formId=" + formId + ", checked=" + checked
               + ", document=" + document + ", originObjectId=" + originObjectId
               + ", originDeliverablesInfoId=" + originDeliverablesInfoId + ", originType="
               + originType + ", docNameShow=" + docNameShow + ", originPath=" + originPath
               + ", docIdShow=" + docIdShow + "]";
    }
}
