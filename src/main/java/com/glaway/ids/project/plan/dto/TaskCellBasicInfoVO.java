package com.glaway.ids.project.plan.dto;


import javax.persistence.Basic;

import javax.persistence.Column;



/**
 * 流程模板基本信息VO对象
 * @author wqb
 *
 */
public class TaskCellBasicInfoVO {
    
    /** 任务流程编号*/
    private String id;
    
    /** 生命周期状态*/
    private String bizCurrent;

    /** 版本编号*/
    private String bizId;

    /** 版本号*/
    private String bizVersion;
    
    /** */
    private String refDurationStr = null;

    /**
     * <!-- begin-user-doc -->
     * 模板ID
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String taskId = null;

    /**
     * <!-- begin-user-doc -->
     * 前置节点ID
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String frontCellIds = null;

    /**
     * <!-- begin-user-doc -->
     * 节点名称
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String cellName = null;

    /**
     * <!-- begin-user-doc -->
     * 参考工期
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private Integer refDuration = null;

    /**
     * <!-- begin-user-doc -->
     * 是否必要活动节点
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String isCellRequired = "false";

    /**
     * <!-- begin-user-doc -->
     * 节点备注
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Column(columnDefinition = "", length = 1000)
    private String cellRemark = null;

    /**
     * <!-- begin-user-doc -->
     * 名称库ID
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String nameStandardId = null;

    /**
     * <!-- begin-user-doc -->
     * 节点序号
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String cellIndex = null;
    
    /**
     * <!-- begin-user-doc --> 计划类型：研发类、评审类、风险类等 <!-- end-user-doc -->
     * 
     * @generated
     */
    private String taskNameType = null;

    /**
     * 绑定的页签组合模板id
     */
    private String tabCbTemplateId = null;

    /**
     * Returns the value of '<em><b>taskId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>taskId</b></em>' feature
     * @generated
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * Sets the '{@link TaskCellBasicInfo#getTaskId() <em>taskId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newTaskId
     *            the new value of the '{@link TaskCellBasicInfo#getTaskId() taskId}' feature.
     * @generated
     */
    public void setTaskId(String newTaskId) {
        taskId = newTaskId;
    }

    /**
     * Returns the value of '<em><b>frontCellIds</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>frontCellIds</b></em>' feature
     * @generated
     */
    public String getFrontCellIds() {
        return frontCellIds;
    }

    /**
     * Sets the '{@link TaskCellBasicInfo#getFrontCellIds() <em>frontCellIds</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newFrontCellIds
     *            the new value of the '{@link TaskCellBasicInfo#getFrontCellIds() frontCellIds}'
     *            feature.
     * @generated
     */
    public void setFrontCellIds(String newFrontCellIds) {
        frontCellIds = newFrontCellIds;
    }

    /**
     * Returns the value of '<em><b>cellName</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>cellName</b></em>' feature
     * @generated
     */
    public String getCellName() {
        return cellName;
    }

    /**
     * Sets the '{@link TaskCellBasicInfo#getCellName() <em>cellName</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newCellName
     *            the new value of the '{@link TaskCellBasicInfo#getCellName() cellName}' feature.
     * @generated
     */
    public void setCellName(String newCellName) {
        cellName = newCellName;
    }

    /**
     * Returns the value of '<em><b>refDuration</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>refDuration</b></em>' feature
     * @generated
     */
    public Integer getRefDuration() {
        return refDuration;
    }

    /**
     * Sets the '{@link TaskCellBasicInfo#getRefDuration() <em>refDuration</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newRefDuration
     *            the new value of the '{@link TaskCellBasicInfo#getRefDuration() refDuration}'
     *            feature.
     * @generated
     */
    public void setRefDuration(Integer newRefDuration) {
        refDuration = newRefDuration;
    }

    /**
     * Returns the value of '<em><b>isCellRequired</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>isCellRequired</b></em>' feature
     * @generated
     */
    public String getIsCellRequired() {
        return isCellRequired;
    }

    /**
     * Sets the '{@link TaskCellBasicInfo#getIsCellRequired() <em>isCellRequired</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newIsCellRequired
     *            the new value of the '{@link TaskCellBasicInfo#getIsCellRequired()
     *            isCellRequired}' feature.
     * @generated
     */
    public void setIsCellRequired(String newIsCellRequired) {
        isCellRequired = newIsCellRequired;
    }

    /**
     * Returns the value of '<em><b>cellRemark</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>cellRemark</b></em>' feature
     * @generated
     */
    public String getCellRemark() {
        return cellRemark;
    }

    /**
     * Sets the '{@link TaskCellBasicInfo#getCellRemark() <em>cellRemark</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newCellRemark
     *            the new value of the '{@link TaskCellBasicInfo#getCellRemark() cellRemark}'
     *            feature.
     * @generated
     */
    public void setCellRemark(String newCellRemark) {
        cellRemark = newCellRemark;
    }

    /**
     * Returns the value of '<em><b>nameStandardId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>nameStandardId</b></em>' feature
     * @generated
     */
    public String getNameStandardId() {
        return nameStandardId;
    }

    /**
     * Sets the '{@link TaskCellBasicInfo#getNameStandardId() <em>nameStandardId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newNameStandardId
     *            the new value of the '{@link TaskCellBasicInfo#getNameStandardId()
     *            nameStandardId}' feature.
     * @generated
     */
    public void setNameStandardId(String newNameStandardId) {
        nameStandardId = newNameStandardId;
    }

    /**
     * Returns the value of '<em><b>cellIndex</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>cellIndex</b></em>' feature
     * @generated
     */
    public String getCellIndex() {
        return cellIndex;
    }

    /**
     * Sets the '{@link TaskCellBasicInfo#getCellIndex() <em>cellIndex</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newCellIndex
     *            the new value of the '{@link TaskCellBasicInfo#getCellIndex() cellIndex}'
     *            feature.
     * @generated
     */
    public void setCellIndex(String newCellIndex) {
        cellIndex = newCellIndex;
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

    public String getRefDurationStr() {
        return refDurationStr;
    }

    public void setRefDurationStr(String refDurationStr) {
        this.refDurationStr = refDurationStr;
    }

    public String getTaskNameType() {
        return taskNameType;
    }

    public void setTaskNameType(String taskNameType) {
        this.taskNameType = taskNameType;
    }

    public String getTabCbTemplateId() {
        return tabCbTemplateId;
    }

    public void setTabCbTemplateId(String tabCbTemplateId) {
        this.tabCbTemplateId = tabCbTemplateId;
    }
}
