package com.glaway.ids.project.projectmanager.entity;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.glaway.foundation.common.entity.GLObject;


/**
 * A representation of the model object '<em><b>ProjWarn</b></em>'.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "ProjWarn")
@Table(name = "PM_PROJECT_WARN")
public class ProjWarn extends GLObject {

    /**
     * <!-- begin-user-doc -->用户名
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String userName = null;

    /**
     * <!-- begin-user-doc -->项目编号
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Column(length = 4000)
    private String projectNum = null;

    /**
     * <!-- begin-user-doc -->标记
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String flag = null;

    /**
     * Returns the value of '<em><b>userName</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>userName</b></em>' feature
     * @generated
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the '{@link ProjWarn#getUserName() <em>userName</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newUserName
     *            the new value of the '{@link ProjWarn#getUserName() userName}' feature.
     * @generated
     */
    public void setUserName(String newUserName) {
        userName = newUserName;
    }

    /**
     * Returns the value of '<em><b>projectNum</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>projectNum</b></em>' feature
     * @generated
     */
    public String getProjectNum() {
        return projectNum;
    }

    /**
     * Sets the '{@link ProjWarn#getProjectNum() <em>projectNum</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newProjectNum
     *            the new value of the '{@link ProjWarn#getProjectNum() projectNum}' feature.
     * @generated
     */
    public void setProjectNum(String newProjectNum) {
        projectNum = newProjectNum;
    }

    /**
     * Returns the value of '<em><b>flag</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>flag</b></em>' feature
     * @generated
     */
    public String getFlag() {
        return flag;
    }

    /**
     * Sets the '{@link ProjWarn#getFlag() <em>flag</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newFlag
     *            the new value of the '{@link ProjWarn#getFlag() flag}' feature.
     * @generated
     */
    public void setFlag(String newFlag) {
        flag = newFlag;
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
        return "ProjWarn " + " [userName: " + getUserName() + "]" + " [projectNum: "
               + getProjectNum() + "]" + " [flag: " + getFlag() + "]";
    }
}
