package com.glaway.ids.project.plan.entity;


import com.glaway.foundation.common.entity.GLObject;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 计划 输入
 * 
 * @generated
 */
@Entity(name = "InputsHistory")
@Table(name = "PM_INPUTS_HISTORY")
public class InputsHistory extends GLObject {

    /**
     * <!-- begin-user-doc -->id
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String inputsId = null;

    /**
     * <!-- begin-user-doc -->名称
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String name = null;

    /**
     * <!-- begin-user-doc -->外键类型
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String useObjectType = null;

    /**
     * <!-- begin-user-doc -->外键id
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String useObjectId = null;

    /**
     * <!-- begin-user-doc -->所属
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String fileId = null;

    /**
     * <!-- begin-user-doc -->是否活动名称来源
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String origin = null;
    
    /**
     * 来源类型(LOCAL:本地上传；PLAN:计划(实际为计划的输出)；PROJECTLIBDOC:项目库文档)
     */
    @Basic()
    private String originType = null;
    
    /**
     * 补充类型,记录流程分解时：研发流程模版带来的内部和外部输入的区别(INNERTASK,DELIEVER)
     */
    @Basic()
    private String originTypeExt = null;

    /**
     * <!-- begin-user-doc -->是否必要
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String required = null;

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
     * 
     */
    @Transient()
    private String checked = null;

    /**
     * 文档
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
    private String formId = null;

    /**
     * Returns the value of '<em><b>inputsId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>inputsId</b></em>' feature
     * @generated
     */
    public String getInputsId() {
        return inputsId;
    }

    /**
     * Sets the '{@link InputsHistory#getInputsId() <em>inputsId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newInputsId
     *            the new value of the '{@link InputsHistory#getInputsId() inputsId}' feature.
     * @generated
     */
    public void setInputsId(String newInputsId) {
        inputsId = newInputsId;
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
     * Sets the '{@link InputsHistory#getName() <em>name</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newName
     *            the new value of the '{@link InputsHistory#getName() name}' feature.
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
     * Sets the '{@link InputsHistory#getUseObjectType() <em>useObjectType</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newUseObjectType
     *            the new value of the '{@link InputsHistory#getUseObjectType() useObjectType}'
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
     * Sets the '{@link InputsHistory#getUseObjectId() <em>useObjectId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newUseObjectId
     *            the new value of the '{@link InputsHistory#getUseObjectId() useObjectId}'
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
     * Sets the '{@link InputsHistory#getFileId() <em>fileId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newFileId
     *            the new value of the '{@link InputsHistory#getFileId() fileId}' feature.
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
     * Sets the '{@link InputsHistory#getOrigin() <em>origin</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newOrigin
     *            the new value of the '{@link InputsHistory#getOrigin() origin}' feature.
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
     * Sets the '{@link InputsHistory#getRequired() <em>required</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newRequired
     *            the new value of the '{@link InputsHistory#getRequired() required}' feature.
     * @generated
     */
    public void setRequired(String newRequired) {
        required = newRequired;
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
     * Sets the '{@link InputsHistory#getOriginObjectId() <em>originObjectId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newOriginObjectId
     *            the new value of the '{@link InputsHistory#getOriginObjectId() originObjectId}'
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
     * Sets the '{@link InputsHistory#getOriginDeliverablesInfoId()
     * <em>originDeliverablesInfoId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newOriginDeliverablesInfoId
     *            the new value of the '{@link InputsHistory#getOriginDeliverablesInfoId()
     *            originDeliverablesInfoId}' feature.
     * @generated
     */
    public void setOriginDeliverablesInfoId(String newOriginDeliverablesInfoId) {
        originDeliverablesInfoId = newOriginDeliverablesInfoId;
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
     * Sets the '{@link InputsHistory#getFormId() <em>formId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newFormId
     *            the new value of the '{@link InputsHistory#getFormId() formId}' feature.
     * @generated
     */
    public void setFormId(String newFormId) {
        formId = newFormId;
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

    public String getOriginTypeExt() {
        return originTypeExt;
    }

    public void setOriginTypeExt(String originTypeExt) {
        this.originTypeExt = originTypeExt;
    }

}
