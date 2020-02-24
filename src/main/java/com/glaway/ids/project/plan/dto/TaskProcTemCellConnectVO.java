package com.glaway.ids.project.plan.dto;


import javax.persistence.Basic;






/**
 * 流程模板VO对象
 * @author wqb
 *
 */
public class TaskProcTemCellConnectVO {
    
    /** 任务流程编号*/
    private String id;

    /**
     * <!-- begin-user-doc --> 模板的ID <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String taskId = null;

    /**
     * <!-- begin-user-doc --> 预留前置节点的ID <!-- end-user-doc -->
     * 
     * @generated
     */

    @Basic()
    private String cellId = null;

    /**
     * <!-- begin-user-doc --> 后置节点的ID <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String targetId = null;

    /**
     * <!-- begin-user-doc --> 前置节点的信息 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String infoId = null;

    /**
     * <!-- begin-user-doc --> 前置节点的信息 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String targetInfoId = null;

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
     * Sets the '{@link TaskProcTemCellConnect#getTaskId() <em>taskId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newTaskId
     *            the new value of the '{@link TaskProcTemCellConnect#getTaskId() taskId}' feature.
     * @generated
     */
    public void setTaskId(String newTaskId) {
        taskId = newTaskId;
    }

    /**
     * Returns the value of '<em><b>cellId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>cellId</b></em>' feature
     * @generated
     */
    public String getCellId() {
        return cellId;
    }

    /**
     * Sets the '{@link TaskProcTemCellConnect#getCellId() <em>cellId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newCellId
     *            the new value of the '{@link TaskProcTemCellConnect#getCellId() cellId}' feature.
     * @generated
     */
    public void setCellId(String newCellId) {
        cellId = newCellId;
    }

    /**
     * Returns the value of '<em><b>targetId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>targetId</b></em>' feature
     * @generated
     */
    public String getTargetId() {
        return targetId;
    }

    /**
     * Sets the '{@link TaskProcTemCellConnect#getTargetId() <em>targetId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newTargetId
     *            the new value of the '{@link TaskProcTemCellConnect#getTargetId() targetId}'
     *            feature.
     * @generated
     */
    public void setTargetId(String newTargetId) {
        targetId = newTargetId;
    }

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
     * Sets the '{@link TaskProcTemCellConnect#getInfoId() <em>infoId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newInfoId
     *            the new value of the '{@link TaskProcTemCellConnect#getInfoId() infoId}' feature.
     * @generated
     */
    public void setInfoId(String newInfoId) {
        infoId = newInfoId;
    }

    /**
     * Returns the value of '<em><b>targetInfoId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>targetInfoId</b></em>' feature
     * @generated
     */
    public String getTargetInfoId() {
        return targetInfoId;
    }

    /**
     * Sets the '{@link TaskProcTemCellConnect#getTargetInfoId() <em>targetInfoId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newTargetInfoId
     *            the new value of the '{@link TaskProcTemCellConnect#getTargetInfoId()
     *            targetInfoId}' feature.
     * @generated
     */
    public void setTargetInfoId(String newTargetInfoId) {
        targetInfoId = newTargetInfoId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}
