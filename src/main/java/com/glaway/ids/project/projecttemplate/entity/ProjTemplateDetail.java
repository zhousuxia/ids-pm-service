package com.glaway.ids.project.projecttemplate.entity;


import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Convert;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import com.glaway.foundation.businessobject.entity.BusinessObject;
import com.glaway.ids.config.entity.BusinessConfig;


/**
 * A representation of the model object '<em><b>ProjTemplateDetail</b></em>'.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "ProjTemplateDetail")
@Table(name = "PM_PROJ_TEMPLATE_DETAIL")
public class ProjTemplateDetail extends BusinessObject {

    /**
     * <!-- begin-user-doc -->名称
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String name = null;

    /**
     * <!-- begin-user-doc -->父计划ID
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String parentPlanId = null;

    /**
     * <!-- begin-user-doc -->阶段
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String phase = null;

    /**
     * <!-- begin-user-doc -->计划等级
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String planLevel = null;

    /**
     * <!-- begin-user-doc -->数量
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private int num = 0;

    /**
     * <!-- begin-user-doc -->工作时间
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String workTime = null;

    /**
     * <!-- begin-user-doc -->备注
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String remark = null;

    /**
     * <!-- begin-user-doc -->项目模板ID
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String projTemplateId = null;

    /**
     * <!-- begin-user-doc -->文档
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String documents = "";

    /**
     * <!-- begin-user-doc -->里程碑
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String milestone = null;

    /**
     * <!-- begin-user-doc -->项目模板信息
     * <!-- end-user-doc -->
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "projTemplateId", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private ProjTemplate projTemplateInfo = null;

    /**
     * <!-- begin-user-doc -->计划等级信息
     * <!-- end-user-doc -->
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "planLevel", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private BusinessConfig planLevelInfo = null;

    /**
     * <!-- begin-user-doc -->阶段信息
     * <!-- end-user-doc -->
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "phase", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private BusinessConfig phaseInfo = null;

    /**
     * <!-- begin-user-doc -->计划数量
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Convert("")
    private int planNumber = 0;

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
     * Sets the '{@link ProjTemplateDetail#getName() <em>name</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newName
     *            the new value of the '{@link ProjTemplateDetail#getName() name}' feature.
     * @generated
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Returns the value of '<em><b>parentPlanId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>parentPlanId</b></em>' feature
     * @generated
     */
    public String getParentPlanId() {
        return parentPlanId;
    }

    /**
     * Sets the '{@link ProjTemplateDetail#getParentPlanId() <em>parentPlanId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newParentPlanId
     *            the new value of the '{@link ProjTemplateDetail#getParentPlanId() parentPlanId}'
     *            feature.
     * @generated
     */
    public void setParentPlanId(String newParentPlanId) {
        parentPlanId = newParentPlanId;
    }

    /**
     * Returns the value of '<em><b>phase</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>phase</b></em>' feature
     * @generated
     */
    public String getPhase() {
        return phase;
    }

    /**
     * Sets the '{@link ProjTemplateDetail#getPhase() <em>phase</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPhase
     *            the new value of the '{@link ProjTemplateDetail#getPhase() phase}' feature.
     * @generated
     */
    public void setPhase(String newPhase) {
        phase = newPhase;
    }

    /**
     * Returns the value of '<em><b>planLevel</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>planLevel</b></em>' feature
     * @generated
     */
    public String getPlanLevel() {
        return planLevel;
    }

    /**
     * Sets the '{@link ProjTemplateDetail#getPlanLevel() <em>planLevel</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanLevel
     *            the new value of the '{@link ProjTemplateDetail#getPlanLevel() planLevel}'
     *            feature.
     * @generated
     */
    public void setPlanLevel(String newPlanLevel) {
        planLevel = newPlanLevel;
    }

    /**
     * Returns the value of '<em><b>num</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>num</b></em>' feature
     * @generated
     */
    public int getNum() {
        return num;
    }

    /**
     * Sets the '{@link ProjTemplateDetail#getNum() <em>num</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newNum
     *            the new value of the '{@link ProjTemplateDetail#getNum() num}' feature.
     * @generated
     */
    public void setNum(int newNum) {
        num = newNum;
    }

    /**
     * Returns the value of '<em><b>workTime</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>workTime</b></em>' feature
     * @generated
     */
    public String getWorkTime() {
        return workTime;
    }

    /**
     * Sets the '{@link ProjTemplateDetail#getWorkTime() <em>workTime</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newWorkTime
     *            the new value of the '{@link ProjTemplateDetail#getWorkTime() workTime}' feature.
     * @generated
     */
    public void setWorkTime(String newWorkTime) {
        workTime = newWorkTime;
    }

    /**
     * Returns the value of '<em><b>remark</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>remark</b></em>' feature
     * @generated
     */
    public String getRemark() {
        return remark;
    }

    /**
     * Sets the '{@link ProjTemplateDetail#getRemark() <em>remark</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newRemark
     *            the new value of the '{@link ProjTemplateDetail#getRemark() remark}' feature.
     * @generated
     */
    public void setRemark(String newRemark) {
        remark = newRemark;
    }

    /**
     * Returns the value of '<em><b>projTemplateId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>projTemplateId</b></em>' feature
     * @generated
     */
    public String getProjTemplateId() {
        return projTemplateId;
    }

    /**
     * Sets the '{@link ProjTemplateDetail#getProjTemplateId() <em>projTemplateId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newProjTemplateId
     *            the new value of the '{@link ProjTemplateDetail#getProjTemplateId()
     *            projTemplateId}' feature.
     * @generated
     */
    public void setProjTemplateId(String newProjTemplateId) {
        projTemplateId = newProjTemplateId;
    }

    /**
     * Returns the value of '<em><b>documents</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>documents</b></em>' feature
     * @generated
     */
    public String getDocuments() {
        return documents;
    }

    /**
     * Sets the '{@link ProjTemplateDetail#getDocuments() <em>documents</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newDocuments
     *            the new value of the '{@link ProjTemplateDetail#getDocuments() documents}'
     *            feature.
     * @generated
     */
    public void setDocuments(String newDocuments) {
        documents = newDocuments;
    }

    /**
     * Returns the value of '<em><b>milestone</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>milestone</b></em>' feature
     * @generated
     */
    public String getMilestone() {
        return milestone;
    }

    /**
     * Sets the '{@link ProjTemplateDetail#getMilestone() <em>milestone</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newMilestone
     *            the new value of the '{@link ProjTemplateDetail#getMilestone() milestone}'
     *            feature.
     * @generated
     */
    public void setMilestone(String newMilestone) {
        milestone = newMilestone;
    }

    /**
     * Returns the value of '<em><b>planNumber</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>planNumber</b></em>' feature
     * @generated
     */
    public int getPlanNumber() {
        return planNumber;
    }

    /**
     * Sets the '{@link ProjTemplateDetail#getPlanNumber() <em>planNumber</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanNumber
     *            the new value of the '{@link ProjTemplateDetail#getPlanNumber() planNumber}'
     *            feature.
     * @generated
     */
    public void setPlanNumber(int newPlanNumber) {
        planNumber = newPlanNumber;
    }

    public ProjTemplate getProjTemplateInfo() {
        return projTemplateInfo;
    }

    public void setProjTemplateInfo(ProjTemplate projTemplateInfo) {
        this.projTemplateInfo = projTemplateInfo;
    }

    public BusinessConfig getPlanLevelInfo() {
        return planLevelInfo;
    }

    public void setPlanLevelInfo(BusinessConfig planLevelInfo) {
        this.planLevelInfo = planLevelInfo;
    }

    public BusinessConfig getPhaseInfo() {
        return phaseInfo;
    }

    public void setPhaseInfo(BusinessConfig phaseInfo) {
        this.phaseInfo = phaseInfo;
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
        return "ProjTemplateDetail " + " [name: " + getName() + "]" + " [parentPlanId: "
               + getParentPlanId() + "]" + " [phase: " + getPhase() + "]" + " [planLevel: "
               + getPlanLevel() + "]" + " [num: " + getNum() + "]" + " [workTime: "
               + getWorkTime() + "]" + " [remark: " + getRemark() + "]" + " [projTemplateId: "
               + getProjTemplateId() + "]" + " [documents: " + getDocuments() + "]"
               + " [milestone: " + getMilestone() + "]" + " [planNumber: " + getPlanNumber() + "]";
    }

}
