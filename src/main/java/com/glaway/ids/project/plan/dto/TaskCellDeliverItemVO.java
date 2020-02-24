package com.glaway.ids.project.plan.dto;


import javax.persistence.Basic;
import javax.persistence.Transient;


/**
 * 流程模板VO对象
 * @author wqb
 *
 */
public class TaskCellDeliverItemVO {
    
    /** 所属的对象的id*/
    private String userObjectId;
    
    /** 文件路径*/
    private String fileName;
    
    /** 文件名称*/
    private String filePath;
    
    /** 类型*/
    private String originType;
    
    /** 任务流程编号*/
    private String id;
    
    /** 生命周期状态*/
    private String bizCurrent;

    /** 版本编号*/
    private String bizId;

    /** 版本号*/
    private String bizVersion;

    /**
     * <!-- begin-user-doc --> 关联baseinfo的id <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String cellId = null;

    /**
     * <!-- begin-user-doc --> 交付项的ID <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String deliverId = null;

    /**
     * <!-- begin-user-doc --> 交付项的名称 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String deliverName = null;

    /**
     * <!-- begin-user-doc --> 交付项类型 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String deliverType = null;

    /**
     * <!-- begin-user-doc --> 是否必须指定输出交付项 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String inputOrOutputName = null;

    /**
     * <!-- begin-user-doc --> 供删除输入交付项的时候使用 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String fromDeliverId = null;

    /**
     * <!-- begin-user-doc -->是否是名称库默认带出的交付项 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String isNameStandardDefault = null;

    /**
     * <!-- begin-user-doc --> 是否是必要的输出交付项 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String isOutputDeliverRequired = null;

    /**
     * <!-- begin-user-doc -->* 列表标示是否选中 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Transient()
    @Basic()
    private String checked = null;
    
    /**
     * <!-- begin-user-doc -->* 来源节点ID <!-- end-user-doc -->
     */
    @Transient()
    @Basic()
    private String originObjectId = null;

    /**
     * Returns the value of '<em><b>cellId</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>cellId</b></em>' feature
     * @generated
     */
    public String getCellId() {
        return cellId;
    }

    /**
     * Sets the '{@link TaskCellDeliverItem#getCellId() <em>cellId</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newCellId
     *            the new value of the '{@link TaskCellDeliverItem#getCellId() cellId}' feature.
     * @generated
     */
    public void setCellId(String newCellId) {
        cellId = newCellId;
    }

    /**
     * Returns the value of '<em><b>deliverId</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>deliverId</b></em>' feature
     * @generated
     */
    public String getDeliverId() {
        return deliverId;
    }

    /**
     * Sets the '{@link TaskCellDeliverItem#getDeliverId() <em>deliverId</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newDeliverId
     *            the new value of the '{@link TaskCellDeliverItem#getDeliverId() deliverId}'
     *            feature.
     * @generated
     */
    public void setDeliverId(String newDeliverId) {
        deliverId = newDeliverId;
    }

    /**
     * Returns the value of '<em><b>deliverName</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>deliverName</b></em>' feature
     * @generated
     */
    public String getDeliverName() {
        return deliverName;
    }

    /**
     * Sets the '{@link TaskCellDeliverItem#getDeliverName() <em>deliverName</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newDeliverName
     *            the new value of the '{@link TaskCellDeliverItem#getDeliverName() deliverName}'
     *            feature.
     * @generated
     */
    public void setDeliverName(String newDeliverName) {
        deliverName = newDeliverName;
    }

    /**
     * Returns the value of '<em><b>deliverType</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>deliverType</b></em>' feature
     * @generated
     */
    public String getDeliverType() {
        return deliverType;
    }

    /**
     * Sets the '{@link TaskCellDeliverItem#getDeliverType() <em>deliverType</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newDeliverType
     *            the new value of the '{@link TaskCellDeliverItem#getDeliverType() deliverType}'
     *            feature.
     * @generated
     */
    public void setDeliverType(String newDeliverType) {
        deliverType = newDeliverType;
    }

    /**
     * Returns the value of '<em><b>inputOrOutputName</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>inputOrOutputName</b></em>' feature
     * @generated
     */
    public String getInputOrOutputName() {
        return inputOrOutputName;
    }

    /**
     * Sets the '{@link TaskCellDeliverItem#getInputOrOutputName() <em>inputOrOutputName</em>}'
     * feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newInputOrOutputName
     *            the new value of the '{@link TaskCellDeliverItem#getInputOrOutputName()
     *            inputOrOutputName}' feature.
     * @generated
     */
    public void setInputOrOutputName(String newInputOrOutputName) {
        inputOrOutputName = newInputOrOutputName;
    }

    /**
     * Returns the value of '<em><b>fromDeliverId</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>fromDeliverId</b></em>' feature
     * @generated
     */
    public String getFromDeliverId() {
        return fromDeliverId;
    }

    /**
     * Sets the '{@link TaskCellDeliverItem#getFromDeliverId() <em>fromDeliverId</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newFromDeliverId
     *            the new value of the '{@link TaskCellDeliverItem#getFromDeliverId()
     *            fromDeliverId}' feature.
     * @generated
     */
    public void setFromDeliverId(String newFromDeliverId) {
        fromDeliverId = newFromDeliverId;
    }

    /**
     * Returns the value of '<em><b>isNameStandardDefault</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>isNameStandardDefault</b></em>' feature
     * @generated
     */
    public String getIsNameStandardDefault() {
        return isNameStandardDefault;
    }

    /**
     * Sets the '{@link TaskCellDeliverItem#getIsNameStandardDefault()
     * <em>isNameStandardDefault</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newIsNameStandardDefault
     *            the new value of the '{@link TaskCellDeliverItem#getIsNameStandardDefault()
     *            isNameStandardDefault}' feature.
     * @generated
     */
    public void setIsNameStandardDefault(String newIsNameStandardDefault) {
        isNameStandardDefault = newIsNameStandardDefault;
    }

    /**
     * Returns the value of '<em><b>isOutputDeliverRequired</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>isOutputDeliverRequired</b></em>' feature
     * @generated
     */
    public String getIsOutputDeliverRequired() {
        return isOutputDeliverRequired;
    }

    /**
     * Sets the '{@link TaskCellDeliverItem#getIsOutputDeliverRequired()
     * <em>isOutputDeliverRequired</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newIsOutputDeliverRequired
     *            the new value of the '{@link TaskCellDeliverItem#getIsOutputDeliverRequired()
     *            isOutputDeliverRequired}' feature.
     * @generated
     */
    public void setIsOutputDeliverRequired(String newIsOutputDeliverRequired) {
        isOutputDeliverRequired = newIsOutputDeliverRequired;
    }

    /**
     * Returns the value of '<em><b>checked</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>checked</b></em>' feature
     * @generated
     */
    public String getChecked() {
        return checked;
    }

    /**
     * Sets the '{@link TaskCellDeliverItem#getChecked() <em>checked</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newChecked
     *            the new value of the '{@link TaskCellDeliverItem#getChecked() checked}' feature.
     * @generated
     */
    public void setChecked(String newChecked) {
        checked = newChecked;
    }

    public String getOriginObjectId() {
        return originObjectId;
    }

    public void setOriginObjectId(String originObjectId) {
        this.originObjectId = originObjectId;
    }

    public String getBizCurrent() {
        return bizCurrent;
    }

    public void setBizCurrent(String bizCurrent) {
        this.bizCurrent = bizCurrent;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getBizVersion() {
        return bizVersion;
    }

    public void setBizVersion(String bizVersion) {
        this.bizVersion = bizVersion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserObjectId() {
        return userObjectId;
    }

    public void setUserObjectId(String userObjectId) {
        this.userObjectId = userObjectId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getOriginType() {
        return originType;
    }

    public void setOriginType(String originType) {
        this.originType = originType;
    }

}
