package com.glaway.ids.project.projectmanager.entity;


import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.glaway.foundation.businessobject.entity.BusinessObject;


/**
 * A representation of the model object '<em><b>ProjLibFile</b></em>'.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "ProjLibFile")
@Table(name = "PM_PROJ_LIB_FILE")
public class ProjLibFile extends BusinessObject {

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String projectStatus = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String projectFileId = "";

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String feedbackProcInstId = null;

    /**
     * <!-- begin-model-doc -->
     * 老任务ID
     * <!-- end-model-doc -->
     */
    @Basic()
    private String oldTaskId = null;

    /**
     * Returns the value of '<em><b>projectStatus</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>projectStatus</b></em>' feature
     * @generated
     */
    public String getProjectStatus() {
        return projectStatus;
    }

    /**
     * Sets the '{@link ProjLibFile#getProjectStatus() <em>projectStatus</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newProjectStatus
     *            the new value of the '{@link ProjLibFile#getProjectStatus() projectStatus}'
     *            feature.
     * @generated
     */
    public void setProjectStatus(String newProjectStatus) {
        projectStatus = newProjectStatus;
    }

    /**
     * Returns the value of '<em><b>projectFileId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>projectFileId</b></em>' feature
     * @generated
     */
    public String getProjectFileId() {
        return projectFileId;
    }

    /**
     * Sets the '{@link ProjLibFile#getProjectFileId() <em>projectFileId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newProjectFileId
     *            the new value of the '{@link ProjLibFile#getProjectFileId() projectFileId}'
     *            feature.
     * @generated
     */
    public void setProjectFileId(String newProjectFileId) {
        projectFileId = newProjectFileId;
    }

    /**
     * Returns the value of '<em><b>feedbackProcInstId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>feedbackProcInstId</b></em>' feature
     * @generated
     */
    public String getFeedbackProcInstId() {
        return feedbackProcInstId;
    }

    /**
     * Sets the '{@link ProjLibFile#getFeedbackProcInstId() <em>feedbackProcInstId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newFeedbackProcInstId
     *            the new value of the '{@link ProjLibFile#getFeedbackProcInstId()
     *            feedbackProcInstId}' feature.
     * @generated
     */
    public void setFeedbackProcInstId(String newFeedbackProcInstId) {
        feedbackProcInstId = newFeedbackProcInstId;
    }

    /**
     * Returns the value of '<em><b>oldTaskId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>oldTaskId</b></em>' feature
     * @generated
     */
    public String getOldTaskId() {
        return oldTaskId;
    }

    /**
     * Sets the '{@link ProjLibFile#getOldTaskId() <em>oldTaskId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newOldTaskId
     *            the new value of the '{@link ProjLibFile#getOldTaskId() oldTaskId}' feature.
     * @generated
     */
    public void setOldTaskId(String newOldTaskId) {
        oldTaskId = newOldTaskId;
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
        return "ProjLibFile " + " [projectStatus: " + getProjectStatus() + "]"
               + " [projectFileId: " + getProjectFileId() + "]" + " [feedbackProcInstId: "
               + getFeedbackProcInstId() + "]" + " [oldTaskId: " + getOldTaskId() + "]";
    }
}
