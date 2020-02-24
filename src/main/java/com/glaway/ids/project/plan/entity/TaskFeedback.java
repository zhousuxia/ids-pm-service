package com.glaway.ids.project.plan.entity;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.glaway.foundation.common.entity.GLObject;


/**
 * A representation of the model object '<em><b>taskFeedback</b></em>'. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "TaskFeedback")
@Table(name = "RD_TASK_FEEDBACK")
public class TaskFeedback extends GLObject {

    /**
     * <!-- begin-user-doc -->任务ID <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String taskId = null;

    /**
     * <!-- begin-user-doc -->是否有效 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String avaliable = "1";

    /**
     * <!-- begin-user-doc -->进度 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String progressRate = null;

    /**
     * <!-- begin-user-doc -->进度描述 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Column(length = 1000)
    private String progressRateRemark = null;

    /**
     * <!-- begin-user-doc --> 风险等级<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String risk = null;

    /**
     * <!-- begin-user-doc -->风险描述 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Column(length = 1000)
    private String riskRemark = null;

    /**
     * <!-- begin-user-doc -->风险附件路径 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String filePath = null;

    /**
     * <!-- begin-user-doc --> 进度附件路径<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String filePathP = null;

    /**
     * Returns the value of '<em><b>taskId</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>taskId</b></em>' feature
     * @generated
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * Sets the '{@link TaskFeedback#getTaskId() <em>taskId</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newTaskId
     *            the new value of the '{@link TaskFeedback#getTaskId() taskId}' feature.
     * @generated
     */
    public void setTaskId(String newTaskId) {
        taskId = newTaskId;
    }

    /**
     * Returns the value of '<em><b>avaliable</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>avaliable</b></em>' feature
     * @generated
     */
    public String getAvaliable() {
        return avaliable;
    }

    /**
     * Sets the '{@link TaskFeedback#getAvaliable() <em>avaliable</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newAvaliable
     *            the new value of the '{@link TaskFeedback#getAvaliable() avaliable}' feature.
     * @generated
     */
    public void setAvaliable(String newAvaliable) {
        avaliable = newAvaliable;
    }

    /**
     * Returns the value of '<em><b>progressRate</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>progressRate</b></em>' feature
     * @generated
     */
    public String getProgressRate() {
        return progressRate;
    }

    /**
     * Sets the '{@link TaskFeedback#getProgressRate() <em>progressRate</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newProgressRate
     *            the new value of the '{@link TaskFeedback#getProgressRate() progressRate}'
     *            feature.
     * @generated
     */
    public void setProgressRate(String newProgressRate) {
        progressRate = newProgressRate;
    }

    /**
     * Returns the value of '<em><b>progressRateRemark</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>progressRateRemark</b></em>' feature
     * @generated
     */
    public String getProgressRateRemark() {
        return progressRateRemark;
    }

    /**
     * Sets the '{@link TaskFeedback#getProgressRateRemark() <em>progressRateRemark</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newProgressRateRemark
     *            the new value of the '{@link TaskFeedback#getProgressRateRemark()
     *            progressRateRemark}' feature.
     * @generated
     */
    public void setProgressRateRemark(String newProgressRateRemark) {
        progressRateRemark = newProgressRateRemark;
    }

    /**
     * Returns the value of '<em><b>risk</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>risk</b></em>' feature
     * @generated
     */
    public String getRisk() {
        return risk;
    }

    /**
     * Sets the '{@link TaskFeedback#getRisk() <em>risk</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newRisk
     *            the new value of the '{@link TaskFeedback#getRisk() risk}' feature.
     * @generated
     */
    public void setRisk(String newRisk) {
        risk = newRisk;
    }

    /**
     * Returns the value of '<em><b>riskRemark</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>riskRemark</b></em>' feature
     * @generated
     */
    public String getRiskRemark() {
        return riskRemark;
    }

    /**
     * Sets the '{@link TaskFeedback#getRiskRemark() <em>riskRemark</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newRiskRemark
     *            the new value of the '{@link TaskFeedback#getRiskRemark() riskRemark}' feature.
     * @generated
     */
    public void setRiskRemark(String newRiskRemark) {
        riskRemark = newRiskRemark;
    }

    /**
     * Returns the value of '<em><b>filePath</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>filePath</b></em>' feature
     * @generated
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Sets the '{@link TaskFeedback#getFilePath() <em>filePath</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newFilePath
     *            the new value of the '{@link TaskFeedback#getFilePath() filePath}' feature.
     * @generated
     */
    public void setFilePath(String newFilePath) {
        filePath = newFilePath;
    }

    /**
     * Returns the value of '<em><b>filePathP</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>filePathP</b></em>' feature
     * @generated
     */
    public String getFilePathP() {
        return filePathP;
    }

    /**
     * Sets the '{@link TaskFeedback#getFilePathP() <em>filePathP</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newFilePathP
     *            the new value of the '{@link TaskFeedback#getFilePathP() filePathP}' feature.
     * @generated
     */
    public void setFilePathP(String newFilePathP) {
        filePathP = newFilePathP;
    }

    /**
     * A toString method which prints the values of all EAttributes of this instance.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String toString() {
        return "TaskFeedback " + " [taskId: " + getTaskId() + "]" + " [avaliable: "
               + getAvaliable() + "]" + " [progressRate: " + getProgressRate() + "]"
               + " [progressRateRemark: " + getProgressRateRemark() + "]" + " [risk: " + getRisk()
               + "]" + " [riskRemark: " + getRiskRemark() + "]" + " [filePath: " + getFilePath()
               + "]" + " [filePathP: " + getFilePathP() + "]";
    }
}
