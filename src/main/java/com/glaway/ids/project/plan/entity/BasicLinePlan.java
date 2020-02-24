package com.glaway.ids.project.plan.entity;


import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.entity.GLObject;
import com.glaway.ids.config.entity.BusinessConfig;
import com.glaway.ids.project.menu.entity.Project;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 基线计划信息
 */
@Entity(name = "BasicLinePlan")
@Table(name = "PM_BASICLINE_PLAN")
public class BasicLinePlan extends GLObject {

    /**
     * 所属基线ID
     * 
     * @generated
     */
    @Basic()
    private String basicLineId = null;

    /**
     * 所属基线
     */
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "basicLineId", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private BasicLine basicLine;

    /**
     * 计划ID
     * 
     * @generated
     */
    @Basic()
    private String planId = null;

    /**
     * 计划名称
     * 
     * @generated
     */
    @Basic()
    private String planName = null;

    /**
     * 所属项目ID
     * 
     * @generated
     */
    @Basic()
    private String projectId = null;

    /**
     * 所属项目
     */
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "projectId", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private Project project;

    /**
     * 父计划ID
     * 
     * @generated
     */
    @Basic()
    private String parentPlanId = null;

    /**
     * 计划等级
     * 
     * @generated
     */
    @Basic()
    private String planLevel = null;

    /**
     * 计划等级
     */
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "planLevel", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private BusinessConfig planLevelInfo;

    /**
     * 生命周期
     * 
     * @generated
     */
    @Basic()
    private String bizCurrent = null;

    /**
     * 生命周期
     * 
     * @generated
     */
    @Basic()
    private String status = null;

    /**
     * 负责人
     * 
     * @generated
     */
    @Basic()
    private String owner = null;

    /**
     * 负责人
     */
    @Transient
    private TSUserDto ownerInfo;

    /**
     * 开始时间
     * 
     * @generated
     */
    @Basic()
    @Temporal(TemporalType.DATE)
    private Date planStartTime = null;

    /**
     * 结束时间
     * 
     * @generated
     */
    @Basic()
    @Temporal(TemporalType.DATE)
    private Date planEndTime = null;

    /**
     * 工期
     * 
     * @generated
     */
    @Basic()
    private String workTime = null;

    /**
     * 输出
     * 
     * @generated
     */
    @Basic()
    @Column(length = 4000)
    private String deliverables = null;

    /**
     * 计划创建人
     * 
     * @generated
     */
    @Basic()
    private String creator = null;

    /**
     * 计划创建人
     */
    @Transient()
    private TSUserDto creatorInfo;

    /**
     * 责任人部门
     */
    @Transient()
    private String ownerDept = null;

    /**
     * 下达人
     * 
     * @generated
     */
    @Basic()
    private String assigner = null;

    /**
     * 下达人
     */
    @Transient()
    private TSUserDto assignerInfo;

    /**
     * 下达时间
     * 
     * @generated
     */
    @Basic()
    @Temporal(TemporalType.DATE)
    private Date assignTime = null;

    /**
     * 备注
     * 
     * @generated
     */
    @Basic()
    @Column(length = 2048)
    private String remark = null;

    /**
     * 是否里程碑
     * 
     * @generated
     */
    @Basic()
    private String milestone = "false";

    /**
     * 是否里程碑
     */
    @Transient()
    private String milestoneName = null;

    /**
     * 风险
     * 
     * @generated
     */
    @Basic()
    private String risk = null;

    /**
     * 前置计划ID
     */
    @Transient()
    private String preposeIds = null;

    /**
     * 
     */
    @Transient()
    private String parentStorey = null;

    /**
     * 同级排序序号
     * 
     * @generated
     */
    @Basic()
    private int storeyNo = 0;

    /**
     * 应用情况
     * 
     * @generated
     */
    @Basic()
    private String implementation = null;

    /**
     * 进度
     * 
     * @generated
     */
    @Basic()
    private String progressRate = null;

    /**
     * 临时父节点id
     */
    @Transient()
    private String _parentId = null;

    /**
     * 顺序
     */
    @Transient()
    private String order = null;

    /**
     * flg
     */
    @Transient()
    private String compareFlag = null;

    /**
     * 子计划
     */
    @Transient()
    private List<BasicLinePlan> children = new ArrayList<BasicLinePlan>();

    /**
     * 计划类型
     * 
     * @generated
     */
    @Basic()
    private String planType = null;

    /**
     * Returns the value of '<em><b>basicLineId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>basicLineId</b></em>' feature
     * @generated
     */
    public String getBasicLineId() {
        return basicLineId;
    }

    /**
     * Sets the '{@link BasicLinePlan#getBasicLineId() <em>basicLineId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newBasicLineId
     *            the new value of the '{@link BasicLinePlan#getBasicLineId() basicLineId}'
     *            feature.
     * @generated
     */
    public void setBasicLineId(String newBasicLineId) {
        basicLineId = newBasicLineId;
    }

    /**
     * Returns the value of '<em><b>planId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>planId</b></em>' feature
     * @generated
     */
    public String getPlanId() {
        return planId;
    }

    /**
     * Sets the '{@link BasicLinePlan#getPlanId() <em>planId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanId
     *            the new value of the '{@link BasicLinePlan#getPlanId() planId}' feature.
     * @generated
     */
    public void setPlanId(String newPlanId) {
        planId = newPlanId;
    }

    /**
     * Returns the value of '<em><b>planName</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>planName</b></em>' feature
     * @generated
     */
    public String getPlanName() {
        return planName;
    }

    /**
     * Sets the '{@link BasicLinePlan#getPlanName() <em>planName</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanName
     *            the new value of the '{@link BasicLinePlan#getPlanName() planName}' feature.
     * @generated
     */
    public void setPlanName(String newPlanName) {
        planName = newPlanName;
    }

    /**
     * Returns the value of '<em><b>projectId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>projectId</b></em>' feature
     * @generated
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * Sets the '{@link BasicLinePlan#getProjectId() <em>projectId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newProjectId
     *            the new value of the '{@link BasicLinePlan#getProjectId() projectId}' feature.
     * @generated
     */
    public void setProjectId(String newProjectId) {
        projectId = newProjectId;
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
     * Sets the '{@link BasicLinePlan#getParentPlanId() <em>parentPlanId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newParentPlanId
     *            the new value of the '{@link BasicLinePlan#getParentPlanId() parentPlanId}'
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
     * Sets the '{@link BasicLinePlan#getPlanLevel() <em>planLevel</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanLevel
     *            the new value of the '{@link BasicLinePlan#getPlanLevel() planLevel}' feature.
     * @generated
     */
    public void setPlanLevel(String newPlanLevel) {
        planLevel = newPlanLevel;
    }

    /**
     * Returns the value of '<em><b>bizCurrent</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>bizCurrent</b></em>' feature
     * @generated
     */
    public String getBizCurrent() {
        return bizCurrent;
    }

    /**
     * Sets the '{@link BasicLinePlan#getBizCurrent() <em>bizCurrent</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newBizCurrent
     *            the new value of the '{@link BasicLinePlan#getBizCurrent() bizCurrent}' feature.
     * @generated
     */
    public void setBizCurrent(String newBizCurrent) {
        bizCurrent = newBizCurrent;
    }

    /**
     * Returns the value of '<em><b>status</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>status</b></em>' feature
     * @generated
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the '{@link BasicLinePlan#getStatus() <em>status</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newStatus
     *            the new value of the '{@link BasicLinePlan#getStatus() status}' feature.
     * @generated
     */
    public void setStatus(String newStatus) {
        status = newStatus;
    }

    /**
     * Returns the value of '<em><b>owner</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>owner</b></em>' feature
     * @generated
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the '{@link BasicLinePlan#getOwner() <em>owner</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newOwner
     *            the new value of the '{@link BasicLinePlan#getOwner() owner}' feature.
     * @generated
     */
    public void setOwner(String newOwner) {
        owner = newOwner;
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
     * Sets the '{@link BasicLinePlan#getPlanStartTime() <em>planStartTime</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanStartTime
     *            the new value of the '{@link BasicLinePlan#getPlanStartTime() planStartTime}'
     *            feature.
     * @generated
     */
    public void setPlanStartTime(Date newPlanStartTime) {
        planStartTime = newPlanStartTime;
    }

    /**
     * Returns the value of '<em><b>planEndTime</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>planEndTime</b></em>' feature
     * @generated
     */
    public Date getPlanEndTime() {
        return planEndTime;
    }

    /**
     * Sets the '{@link BasicLinePlan#getPlanEndTime() <em>planEndTime</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanEndTime
     *            the new value of the '{@link BasicLinePlan#getPlanEndTime() planEndTime}'
     *            feature.
     * @generated
     */
    public void setPlanEndTime(Date newPlanEndTime) {
        planEndTime = newPlanEndTime;
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
     * Sets the '{@link BasicLinePlan#getWorkTime() <em>workTime</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newWorkTime
     *            the new value of the '{@link BasicLinePlan#getWorkTime() workTime}' feature.
     * @generated
     */
    public void setWorkTime(String newWorkTime) {
        workTime = newWorkTime;
    }

    /**
     * Returns the value of '<em><b>deliverables</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>deliverables</b></em>' feature
     * @generated
     */
    public String getDeliverables() {
        return deliverables;
    }

    /**
     * Sets the '{@link BasicLinePlan#getDeliverables() <em>deliverables</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newDeliverables
     *            the new value of the '{@link BasicLinePlan#getDeliverables() deliverables}'
     *            feature.
     * @generated
     */
    public void setDeliverables(String newDeliverables) {
        deliverables = newDeliverables;
    }

    /**
     * Returns the value of '<em><b>creator</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>creator</b></em>' feature
     * @generated
     */
    public String getCreator() {
        return creator;
    }

    /**
     * Sets the '{@link BasicLinePlan#getCreator() <em>creator</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newCreator
     *            the new value of the '{@link BasicLinePlan#getCreator() creator}' feature.
     * @generated
     */
    public void setCreator(String newCreator) {
        creator = newCreator;
    }

    /**
     * Returns the value of '<em><b>assigner</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>assigner</b></em>' feature
     * @generated
     */
    public String getAssigner() {
        return assigner;
    }

    /**
     * Sets the '{@link BasicLinePlan#getAssigner() <em>assigner</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newAssigner
     *            the new value of the '{@link BasicLinePlan#getAssigner() assigner}' feature.
     * @generated
     */
    public void setAssigner(String newAssigner) {
        assigner = newAssigner;
    }

    /**
     * Returns the value of '<em><b>assignTime</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>assignTime</b></em>' feature
     * @generated
     */
    public Date getAssignTime() {
        return assignTime;
    }

    /**
     * Sets the '{@link BasicLinePlan#getAssignTime() <em>assignTime</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newAssignTime
     *            the new value of the '{@link BasicLinePlan#getAssignTime() assignTime}' feature.
     * @generated
     */
    public void setAssignTime(Date newAssignTime) {
        assignTime = newAssignTime;
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
     * Sets the '{@link BasicLinePlan#getRemark() <em>remark</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newRemark
     *            the new value of the '{@link BasicLinePlan#getRemark() remark}' feature.
     * @generated
     */
    public void setRemark(String newRemark) {
        remark = newRemark;
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
     * Sets the '{@link BasicLinePlan#getMilestone() <em>milestone</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newMilestone
     *            the new value of the '{@link BasicLinePlan#getMilestone() milestone}' feature.
     * @generated
     */
    public void setMilestone(String newMilestone) {
        milestone = newMilestone;
    }

    /**
     * Returns the value of '<em><b>risk</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>risk</b></em>' feature
     * @generated
     */
    public String getRisk() {
        return risk;
    }

    /**
     * Sets the '{@link BasicLinePlan#getRisk() <em>risk</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newRisk
     *            the new value of the '{@link BasicLinePlan#getRisk() risk}' feature.
     * @generated
     */
    public void setRisk(String newRisk) {
        risk = newRisk;
    }

    /**
     * Returns the value of '<em><b>storeyNo</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>storeyNo</b></em>' feature
     * @generated
     */
    public int getStoreyNo() {
        return storeyNo;
    }

    /**
     * Sets the '{@link BasicLinePlan#getStoreyNo() <em>storeyNo</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newStoreyNo
     *            the new value of the '{@link BasicLinePlan#getStoreyNo() storeyNo}' feature.
     * @generated
     */
    public void setStoreyNo(int newStoreyNo) {
        storeyNo = newStoreyNo;
    }

    /**
     * Returns the value of '<em><b>implementation</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>implementation</b></em>' feature
     * @generated
     */
    public String getImplementation() {
        return implementation;
    }

    /**
     * Sets the '{@link BasicLinePlan#getImplementation() <em>implementation</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newImplementation
     *            the new value of the '{@link BasicLinePlan#getImplementation() implementation}'
     *            feature.
     * @generated
     */
    public void setImplementation(String newImplementation) {
        implementation = newImplementation;
    }

    /**
     * Returns the value of '<em><b>progressRate</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>progressRate</b></em>' feature
     * @generated
     */
    public String getProgressRate() {
        return progressRate;
    }

    /**
     * Sets the '{@link BasicLinePlan#getProgressRate() <em>progressRate</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newProgressRate
     *            the new value of the '{@link BasicLinePlan#getProgressRate() progressRate}'
     *            feature.
     * @generated
     */
    public void setProgressRate(String newProgressRate) {
        progressRate = newProgressRate;
    }

    /**
     * Returns the value of '<em><b>planType</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>planType</b></em>' feature
     * @generated
     */
    public String getPlanType() {
        return planType;
    }

    /**
     * Sets the '{@link BasicLinePlan#getPlanType() <em>planType</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanType
     *            the new value of the '{@link BasicLinePlan#getPlanType() planType}' feature.
     * @generated
     */
    public void setPlanType(String newPlanType) {
        planType = newPlanType;
    }

    public BasicLine getBasicLine() {
        return basicLine;
    }

    public void setBasicLine(BasicLine basicLine) {
        this.basicLine = basicLine;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public BusinessConfig getPlanLevelInfo() {
        return planLevelInfo;
    }

    public void setPlanLevelInfo(BusinessConfig planLevelInfo) {
        this.planLevelInfo = planLevelInfo;
    }

    public TSUserDto getOwnerInfo() {
        return ownerInfo;
    }

    public void setOwnerInfo(TSUserDto ownerInfo) {
        this.ownerInfo = ownerInfo;
    }

    public TSUserDto getCreatorInfo() {
        return creatorInfo;
    }

    public void setCreatorInfo(TSUserDto creatorInfo) {
        this.creatorInfo = creatorInfo;
    }

    public String getOwnerDept() {
        return ownerDept;
    }

    public void setOwnerDept(String ownerDept) {
        this.ownerDept = ownerDept;
    }

    public TSUserDto getAssignerInfo() {
        return assignerInfo;
    }

    public void setAssignerInfo(TSUserDto assignerInfo) {
        this.assignerInfo = assignerInfo;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    public String getPreposeIds() {
        return preposeIds;
    }

    public void setPreposeIds(String preposeIds) {
        this.preposeIds = preposeIds;
    }

    public String getParentStorey() {
        return parentStorey;
    }

    public void setParentStorey(String parentStorey) {
        this.parentStorey = parentStorey;
    }

    public String get_parentId() {
        return _parentId;
    }

    public void set_parentId(String _parentId) {
        this._parentId = _parentId;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getCompareFlag() {
        return compareFlag;
    }

    public void setCompareFlag(String compareFlag) {
        this.compareFlag = compareFlag;
    }

    public List<BasicLinePlan> getChildren() {
        return children;
    }

    public void setChildren(List<BasicLinePlan> children) {
        this.children = children;
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
        return "BasicLinePlan " + " [basicLineId: " + getBasicLineId() + "]" + " [planId: "
               + getPlanId() + "]" + " [planName: " + getPlanName() + "]" + " [projectId: "
               + getProjectId() + "]" + " [parentPlanId: " + getParentPlanId() + "]"
               + " [planLevel: " + getPlanLevel() + "]" + " [bizCurrent: " + getBizCurrent() + "]"
               + " [status: " + getStatus() + "]" + " [owner: " + getOwner() + "]"
               + " [planStartTime: " + getPlanStartTime() + "]" + " [planEndTime: "
               + getPlanEndTime() + "]" + " [workTime: " + getWorkTime() + "]"
               + " [deliverables: " + getDeliverables() + "]" + " [creator: " + getCreator() + "]"
               + " [assigner: " + getAssigner() + "]" + " [assignTime: " + getAssignTime() + "]"
               + " [remark: " + getRemark() + "]" + " [milestone: " + getMilestone() + "]"
               + " [risk: " + getRisk() + "]" + " [storeyNo: " + getStoreyNo() + "]"
               + " [implementation: " + getImplementation() + "]" + " [progressRate: "
               + getProgressRate() + "]" + " [planType: " + getPlanType() + "]";
    }
}
