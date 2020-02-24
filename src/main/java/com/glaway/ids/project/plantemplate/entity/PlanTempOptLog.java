package com.glaway.ids.project.plantemplate.entity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.glaway.foundation.common.dto.TSUserDto;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.glaway.foundation.common.entity.GLObject;

/**
 * A representation of the model object '<em><b>planTempOptLog</b></em>'.
 * <!-- begin-user-doc -->计划模版操作日志表 <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name="PlanTempOptLog")
@Table(name="PLAN_TEMPLATE_OPT_LOG")
public class PlanTempOptLog extends GLObject{
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String logInfo = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String tmplId = null;
    
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     */
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "tmplId", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private PlanTemplate planTmpl;


    /**
     * Returns the value of '<em><b>logInfo</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>logInfo</b></em>' feature
     * @generated
     */
    public String getLogInfo() {
        return logInfo;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newLogInfo
     * @generated
     */
    public void setLogInfo(String newLogInfo) {
        logInfo = newLogInfo;
    }

    /**
     * Returns the value of '<em><b>tmplId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>tmplId</b></em>' feature
     * @generated
     */
    public String getTmplId() {
        return tmplId;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newTmplId
     * @generated
     */
    public void setTmplId(String newTmplId) {
        tmplId = newTmplId;
    }
    
    public PlanTemplate getProcTmpl() {
        return planTmpl;
    }

    public void setProcTmpl(PlanTemplate planTmpl) {
        this.planTmpl = planTmpl;
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
        return "PlanTempOptLog " + " [logInfo: " + getLogInfo() + "]" + " [tmplId: " + getTmplId()
               + "]";
    }
}
