package com.glaway.ids.project.plantemplate.entity;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.glaway.foundation.businessobject.entity.BusinessObject;
import com.glaway.ids.config.entity.BusinessConfig;
import com.glaway.ids.project.plan.entity.DeliverablesInfo;
import com.glaway.ids.project.plan.entity.Inputs;


/**
 * A representation of the model object '<em><b>PlanTemplateDetail</b></em>'.
 * <!-- begin-user-doc -->
 * 计划模版细节表
 * <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "PlanTemplateDetail")
@Table(name = "PM_PLAN_TEMPLATE_DL", schema = "")
public class PlanTemplateDetail extends BusinessObject {

    /**
     * <!-- begin-user-doc -->
     * 计划名称
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String name = null;

    /**
     * <!-- begin-user-doc -->
     * 父计划ID
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String parentPlanId = null;

    /**
     * <!-- begin-user-doc -->
     * 计划等级
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String planLevel = null;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "planLevel", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private BusinessConfig planLevelInfo = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private int num = 0;

    /**
     * <!-- begin-user-doc -->
     * 工期
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String workTime = null;

    /**
     * <!-- begin-user-doc -->
     * 备注
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Column(length = 1000)
    private String remark = null;

    /**
     * <!-- begin-user-doc -->
     * 计划模版ID
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String planTemplateId = null;
    
    /**
     * <!-- begin-user-doc -->
     * 项目模版ID
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String projectTemplateId = null;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "planTemplateId", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private PlanTemplate planTemplateInfo = null;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "projectTemplateId", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private PlanTemplate projectTemplateInfo = null;

    /**
     * <!-- begin-user-doc -->
     * 里程碑
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String milestone = null;

    /**
     * <!-- begin-user-doc -->
     * 计划序号
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private int planNumber = 0;
    
    /**
     * 是否必要
     */
    @Basic()
    private String isNecessary = null;

    /**
     * 文档
     */
    @Transient()
    private String documents = null;

    /**
     * <!-- begin-user-doc -->
     * 子计划的开始时间,临时保存
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Temporal(TemporalType.DATE)
    private Date planStartTime = null;
    
    /**
     * 输入
     */
    @Transient()
    private List<Inputs> inputsList= new ArrayList<Inputs>();
    
    
    /**
     * 输出
     */
    @Transient()
    private List<DeliverablesInfo> deliverablesInfo = new ArrayList<DeliverablesInfo>();
    
    /**
     * 前置计划id
     */
    @Transient()
    private String preposeIds = null;
    
    /**
     * 前置计划名称
     */
    @Transient()
    private String preposeNames = null;
    
    /**
     * 计划等级名称
     */
    @Transient()
    private String planLevelName = null;
    
    /**
     * 里程碑显示
     */
    @Transient()
    private String mileStoneShow = null;
    
    /**
     * 来源<br>
     */
    @Transient
    private String origin = null;
    
    /**
     * 输入项名称<br>
     */
    @Transient
    private String inputsName;
    
    /**
     * 排序
     */
    @Basic()
    private Integer storeyNo;

    /**
     * <!-- begin-user-doc --> 计划类型：研发类、评审类、风险类等 <!-- end-user-doc -->
     *
     * @generated
     */
    @Basic()
    private String taskNameType;

    /**
     * 绑定的页签组合模板id
     */
    @Basic
    private String tabCbTemplateId;

    /**
     * <!-- begin-user-doc --> 计划类别：WBS计划、任务计划、流程计划 <!-- end-user-doc -->
     *
     * @generated
     */
    @Basic()
    private String taskType;

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
     * Sets the '{@link PlanTemplateDetail#getName() <em>name</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newName
     *            the new value of the '{@link PlanTemplateDetail#getName() name}' feature.
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
     * Sets the '{@link PlanTemplateDetail#getParentPlanId() <em>parentPlanId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newParentPlanId
     *            the new value of the '{@link PlanTemplateDetail#getParentPlanId() parentPlanId}'
     *            feature.
     * @generated
     */
    public void setParentPlanId(String newParentPlanId) {
        parentPlanId = newParentPlanId;
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
     * Sets the '{@link PlanTemplateDetail#getPlanLevel() <em>planLevel</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanLevel
     *            the new value of the '{@link PlanTemplateDetail#getPlanLevel() planLevel}'
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
     * Sets the '{@link PlanTemplateDetail#getNum() <em>num</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newNum
     *            the new value of the '{@link PlanTemplateDetail#getNum() num}' feature.
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
     * Sets the '{@link PlanTemplateDetail#getWorkTime() <em>workTime</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newWorkTime
     *            the new value of the '{@link PlanTemplateDetail#getWorkTime() workTime}' feature.
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
     * Sets the '{@link PlanTemplateDetail#getRemark() <em>remark</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newRemark
     *            the new value of the '{@link PlanTemplateDetail#getRemark() remark}' feature.
     * @generated
     */
    public void setRemark(String newRemark) {
        remark = newRemark;
    }

    /**
     * Returns the value of '<em><b>planTemplateId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>planTemplateId</b></em>' feature
     * @generated
     */
    public String getPlanTemplateId() {
        return planTemplateId;
    }

    /**
     * Sets the '{@link PlanTemplateDetail#getPlanTemplateId() <em>planTemplateId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanTemplateId
     *            the new value of the '{@link PlanTemplateDetail#getPlanTemplateId()
     *            planTemplateId}' feature.
     * @generated
     */
    public void setPlanTemplateId(String newPlanTemplateId) {
        planTemplateId = newPlanTemplateId;
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
     * Sets the '{@link PlanTemplateDetail#getMilestone() <em>milestone</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newMilestone
     *            the new value of the '{@link PlanTemplateDetail#getMilestone() milestone}'
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
     * Sets the '{@link PlanTemplateDetail#getPlanNumber() <em>planNumber</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanNumber
     *            the new value of the '{@link PlanTemplateDetail#getPlanNumber() planNumber}'
     *            feature.
     * @generated
     */
    public void setPlanNumber(int newPlanNumber) {
        planNumber = newPlanNumber;
    }

    /**
     * Returns the value of '<em><b>planStartTime</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>planStartTime</b></em>' feature
     * @generated
     */
    public Date getPlanStartTime() {
        return planStartTime;
    }

    /**
     * Sets the '{@link PlanTemplateDetail#getPlanStartTime() <em>planStartTime</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanStartTime
     *            the new value of the '{@link PlanTemplateDetail#getPlanStartTime() planStartTime}
     *            ' feature.
     * @generated
     */
    public void setPlanStartTime(Date newPlanStartTime) {
        planStartTime = newPlanStartTime;
    }

    public BusinessConfig getPlanLevelInfo() {
        return planLevelInfo;
    }

    public void setPlanLevelInfo(BusinessConfig planLevelInfo) {
        this.planLevelInfo = planLevelInfo;
    }

    public PlanTemplate getPlanTemplateInfo() {
        return planTemplateInfo;
    }

    public void setPlanTemplateInfo(PlanTemplate planTemplateInfo) {
        this.planTemplateInfo = planTemplateInfo;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }
    

    public List<Inputs> getInputsList() {
        return inputsList;
    }

    public void setInputsList(List<Inputs> inputsList) {
        this.inputsList = inputsList;
    }

    public List<DeliverablesInfo> getDeliverablesInfo() {
        return deliverablesInfo;
    }

    public void setDeliverablesInfo(List<DeliverablesInfo> deliverablesInfo) {
        this.deliverablesInfo = deliverablesInfo;
    }
    
    
    public String getIsNecessary() {
        return isNecessary;
    }

    public void setIsNecessary(String isNecessary) {
        this.isNecessary = isNecessary;
    }

    

    public String getPreposeIds() {
        return preposeIds;
    }

    public void setPreposeIds(String preposeIds) {
        this.preposeIds = preposeIds;
    }
    
    

    public String getPreposeNames() {
        return preposeNames;
    }

    public void setPreposeNames(String preposeNames) {
        this.preposeNames = preposeNames;
    }

    public String getPlanLevelName() {
        return planLevelName;
    }

    public void setPlanLevelName(String planLevelName) {
        this.planLevelName = planLevelName;
    }

    public String getMileStoneShow() {
        return mileStoneShow;
    }

    public void setMileStoneShow(String mileStoneShow) {
        this.mileStoneShow = mileStoneShow;
    }

    public Integer getStoreyNo() {
        return storeyNo;
    }

    public void setStoreyNo(Integer storeyNo) {
        this.storeyNo = storeyNo;
    }
    
    public String getProjectTemplateId() {
        return projectTemplateId;
    }

    public void setProjectTemplateId(String projectTemplateId) {
        this.projectTemplateId = projectTemplateId;
    }

    public PlanTemplate getProjectTemplateInfo() {
        return projectTemplateInfo;
    }

    public void setProjectTemplateInfo(PlanTemplate projectTemplateInfo) {
        this.projectTemplateInfo = projectTemplateInfo;
    }
    
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getInputsName() {
        return inputsName;
    }

    public void setInputsName(String inputsName) {
        this.inputsName = inputsName;
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

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
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
        return "PlanTemplateDetail{" +
                "name='" + name + '\'' +
                ", parentPlanId='" + parentPlanId + '\'' +
                ", planLevel='" + planLevel + '\'' +
                ", planLevelInfo=" + planLevelInfo +
                ", num=" + num +
                ", workTime='" + workTime + '\'' +
                ", remark='" + remark + '\'' +
                ", planTemplateId='" + planTemplateId + '\'' +
                ", projectTemplateId='" + projectTemplateId + '\'' +
                ", planTemplateInfo=" + planTemplateInfo +
                ", projectTemplateInfo=" + projectTemplateInfo +
                ", milestone='" + milestone + '\'' +
                ", planNumber=" + planNumber +
                ", isNecessary='" + isNecessary + '\'' +
                ", documents='" + documents + '\'' +
                ", planStartTime=" + planStartTime +
                ", inputsList=" + inputsList +
                ", deliverablesInfo=" + deliverablesInfo +
                ", preposeIds='" + preposeIds + '\'' +
                ", preposeNames='" + preposeNames + '\'' +
                ", planLevelName='" + planLevelName + '\'' +
                ", mileStoneShow='" + mileStoneShow + '\'' +
                ", origin='" + origin + '\'' +
                ", inputsName='" + inputsName + '\'' +
                ", storeyNo=" + storeyNo +
                ", taskNameType='" + taskNameType + '\'' +
                ", tabCbTemplateId='" + tabCbTemplateId + '\'' +
                ", taskType='" + taskType + '\'' +
                '}';
    }


}
