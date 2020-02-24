package com.glaway.ids.project.plan.entity;


import com.glaway.foundation.businessobject.entity.BusinessObject;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.ids.config.entity.BusinessConfig;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.plan.dto.ResourceLinkInfoDto;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A representation of the model object '<em><b>TemporaryPlan</b></em>'.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "TemporaryPlan")
@Table(name = "PM_TEMPORARY_PLAN")
public class TemporaryPlan extends BusinessObject {

    /**
     * <!-- begin-user-doc --> 关联计划id <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String planId = null;

    /**
     * <!-- begin-user-doc --> 计划序号 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private int planNumber = 0;

    /**
     * <!-- begin-user-doc --> 计划顺序 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String planOrder = null;

    /**
     * <!-- begin-user-doc --> 计划名称<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String planName = null;

    /**
     * <!-- begin-user-doc --> 项目id <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String projectId = null;

    /**
     * 项目信息
     */
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "projectId", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private Project project;

    /**
     * <!-- begin-user-doc --> 父节点计划id <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String parentPlanId = null;

    /**
     * 父计划信息
     */
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "parentPlanId", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private Plan parentPlan;

    /**
     * 父计划名称
     */
    @Transient()
    private String parentPlanName = null;

    /**
     * <!-- begin-user-doc --> 负责人id <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String owner = null;

    /**
     * 负责人信息
     */
    @Transient()
    private TSUserDto ownerInfo;

    /**
     * 创建者
     */
    @Transient()
    private TSUserDto creator;

    /**
     * 责任部门
     */
    @Transient()
    private String ownerDept = null;

    /**
     * 负责人真实名称
     */
    @Transient()
    private String ownerRealName = null;

    /**
     * <!-- begin-user-doc --> 应用情况 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String implementation = null;

    /**
     * <!-- begin-user-doc -->下达人 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String assigner = null;

    /**
     * 下达人信息
     */
    @Transient()
    private TSUserDto assignerInfo;

    /**
     * <!-- begin-user-doc --> 下达时间<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Temporal(TemporalType.DATE)
    private Date assignTime = null;

    /**
     * 下达开始时间
     */
    @Transient()
    @Temporal(TemporalType.DATE)
    private Date assignTimeStart = null;

    /**
     * 下达结束时间
     */
    @Transient()
    @Temporal(TemporalType.DATE)
    private Date assignTimeEnd = null;

    /**
     * <!-- begin-user-doc --> 发起人 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String launcher = null;

    /**
     * <!-- begin-user-doc --> 发起时间 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Temporal(TemporalType.DATE)
    private Date launchTime = null;

    /**
     * 发起人 信息
     */
    @Transient()
    private TSUserDto launcherInfo;

    /**
     * <!-- begin-user-doc --> 计划等级 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String planLevel = null;

    /**
     * 计划等级信息
     */
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "planLevel", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private BusinessConfig planLevelInfo;

    /**
     * 计划等级名称
     */
    @Transient()
    private String planLevelName = null;

    /**
     * <!-- begin-user-doc --> 进度 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String progressRate = null;

    /**
     * <!-- begin-user-doc --> 计划开始时间 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Temporal(TemporalType.DATE)
    private Date planStartTime = null;

    /**
     * 计划开始时间的结束时间
     */
    @Transient()
    private String planStartTimeView = null;

    /**
     * 计划开始时间的开始时间
     */
    @Transient()
    @Temporal(TemporalType.DATE)
    private Date planStartTimeStart = null;

    /**
     * 计划开始时间的结束时间
     */
    @Transient()
    @Temporal(TemporalType.DATE)
    private Date planStartTimeEnd = null;

    /**
     * <!-- begin-user-doc --> 计划结束时间 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Temporal(TemporalType.DATE)
    private Date planEndTime = null;

    /**
     * 计划结束时间的开始时间
     */
    @Transient()
    private String planEndTimeView = null;

    /**
     * 计划结束时间的开始时间
     */
    @Transient()
    @Temporal(TemporalType.DATE)
    private Date planEndTimeStart = null;

    /**
     * 计划结束时间的结束时间
     */
    @Transient()
    @Temporal(TemporalType.DATE)
    private Date planEndTimeEnd = null;

    /**
     * <!-- begin-user-doc --> 工期 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String workTime = null;

    /**
     * <!-- begin-user-doc --> 实际开始时间 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Temporal(TemporalType.DATE)
    private Date actualStartTime = null;

    /**
     * 实际开始时间的开始时间
     */
    @Transient()
    @Temporal(TemporalType.DATE)
    private Date actualStartTimeStart = null;

    /**
     * 实际开始时间的结束时间
     */
    @Transient()
    @Temporal(TemporalType.DATE)
    private Date actualStartTimeEnd = null;

    /**
     * <!-- begin-user-doc --> 实际结束时间 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Temporal(TemporalType.DATE)
    private Date actualEndTime = null;

    /**
     * 实际结束时间的开始时间
     */
    @Transient()
    @Temporal(TemporalType.DATE)
    private Date actualEndTimeStart = null;

    /**
     * 实际结束时间的结束时间
     */
    @Transient()
    @Temporal(TemporalType.DATE)
    private Date actualEndTimeEnd = null;

    /**
     * <!-- begin-user-doc --> 备注 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Column(columnDefinition = "", length = 2048)
    private String remark = null;

    /**
     * 状态
     */
    @Transient()
    private String status = null;

    /**
     * <!-- begin-user-doc --> 项目状态 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String projectStatus = "";

    /**
     * <!-- begin-user-doc --> 流程状态<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String flowStatus = "NORMAL";

    /**
     * <!-- begin-user-doc --> 里程碑 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String milestone = "false";

    /**
     * 里程碑中文
     */
    @Transient()
    private String milestoneName = null;

    /**
     * <!-- begin-user-doc --> 风险 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String risk = null;

    /**
     * <!-- begin-user-doc --> 变更类型<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String changeType = null;

    /**
     * 变更类型信息
     */
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "changeType", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private BusinessConfig changeTypeInfo;

    /**
     * <!-- begin-user-doc --> 变更备注 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Column(length = 2048)
    private String changeRemark = null;

    /**
     * <!-- begin-user-doc --> 附件id <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String changeInfoDocId = null;

    /**
     * <!-- begin-user-doc -->附件名称<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String changeInfoDocName = null;

    /**
     * <!-- begin-user-doc --> 附件路径<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String changeInfoDocPath = null;

    /**
     * <!-- begin-user-doc --> 前置计划id<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String preposeIds = null;

    /**
     * 前置计划
     */
    @Transient()
    private String preposePlans = null;

    /**
     * 文件
     */
    @Transient()
    private String documents = null;

    /**
     * 父节点顺序
     */
    @Transient()
    private String parentStorey = null;

    /**
     * <!-- begin-user-doc --> 顺序 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private Integer storeyNo = null;

    /**
     * <!-- begin-user-doc --> 上层计划id <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String beforePlanId = null;

    /**
     * <!-- begin-user-doc --> 计划类别 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String taskType = "";

    /**
     * <!-- begin-user-doc -->活动名称类型<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String taskNameType = null;

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
     * <!-- begin-user-doc --> 废弃时间 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Temporal(TemporalType.DATE)
    private Date invalidTime = null;

    /**
     * 
     */
    @Transient()
    private List<Plan> preposeList = new ArrayList<Plan>();

    /**
     * 
     */
    @Transient()
    private List<ResourceLinkInfoDto> rescLinkInfoList = new ArrayList<ResourceLinkInfoDto>();

    /**
     * 
     */
    @Transient()
    private List<DeliverablesInfo> deliInfoList = new ArrayList<DeliverablesInfo>();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String formId = null;

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
     * Sets the '{@link TemporaryPlan#getPlanId() <em>planId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newPlanId
     *            the new value of the '{@link TemporaryPlan#getPlanId() planId}' feature.
     * @generated
     */
    public void setPlanId(String newPlanId) {
        planId = newPlanId;
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
     * Sets the '{@link TemporaryPlan#getPlanNumber() <em>planNumber</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newPlanNumber
     *            the new value of the '{@link TemporaryPlan#getPlanNumber() planNumber}' feature.
     * @generated
     */
    public void setPlanNumber(int newPlanNumber) {
        planNumber = newPlanNumber;
    }

    /**
     * Returns the value of '<em><b>planOrder</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of '<em><b>planOrder</b></em>' feature
     * @generated
     */
    public String getPlanOrder() {
        return planOrder;
    }

    /**
     * Sets the '{@link TemporaryPlan#getPlanOrder() <em>planOrder</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newPlanOrder
     *            the new value of the '{@link TemporaryPlan#getPlanOrder() planOrder}' feature.
     * @generated
     */
    public void setPlanOrder(String newPlanOrder) {
        planOrder = newPlanOrder;
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
     * Sets the '{@link TemporaryPlan#getPlanName() <em>planName</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newPlanName
     *            the new value of the '{@link TemporaryPlan#getPlanName() planName}' feature.
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
     * Sets the '{@link TemporaryPlan#getProjectId() <em>projectId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newProjectId
     *            the new value of the '{@link TemporaryPlan#getProjectId() projectId}' feature.
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
     * Sets the '{@link TemporaryPlan#getParentPlanId() <em>parentPlanId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newParentPlanId
     *            the new value of the '{@link TemporaryPlan#getParentPlanId() parentPlanId}'
     *            feature.
     * @generated
     */
    public void setParentPlanId(String newParentPlanId) {
        parentPlanId = newParentPlanId;
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
     * Sets the '{@link TemporaryPlan#getOwner() <em>owner</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newOwner
     *            the new value of the '{@link TemporaryPlan#getOwner() owner}' feature.
     * @generated
     */
    public void setOwner(String newOwner) {
        owner = newOwner;
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
     * Sets the '{@link TemporaryPlan#getImplementation() <em>implementation</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newImplementation
     *            the new value of the '{@link TemporaryPlan#getImplementation() implementation}'
     *            feature.
     * @generated
     */
    public void setImplementation(String newImplementation) {
        implementation = newImplementation;
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
     * Sets the '{@link TemporaryPlan#getAssigner() <em>assigner</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newAssigner
     *            the new value of the '{@link TemporaryPlan#getAssigner() assigner}' feature.
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
     * Sets the '{@link TemporaryPlan#getAssignTime() <em>assignTime</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newAssignTime
     *            the new value of the '{@link TemporaryPlan#getAssignTime() assignTime}' feature.
     * @generated
     */
    public void setAssignTime(Date newAssignTime) {
        assignTime = newAssignTime;
    }

    /**
     * Returns the value of '<em><b>launcher</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of '<em><b>launcher</b></em>' feature
     * @generated
     */
    public String getLauncher() {
        return launcher;
    }

    /**
     * Sets the '{@link TemporaryPlan#getLauncher() <em>launcher</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newLauncher
     *            the new value of the '{@link TemporaryPlan#getLauncher() launcher}' feature.
     * @generated
     */
    public void setLauncher(String newLauncher) {
        launcher = newLauncher;
    }

    /**
     * Returns the value of '<em><b>launchTime</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of '<em><b>launchTime</b></em>' feature
     * @generated
     */
    public Date getLaunchTime() {
        return launchTime;
    }

    /**
     * Sets the '{@link TemporaryPlan#getLaunchTime() <em>launchTime</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newLaunchTime
     *            the new value of the '{@link TemporaryPlan#getLaunchTime() launchTime}' feature.
     * @generated
     */
    public void setLaunchTime(Date newLaunchTime) {
        launchTime = newLaunchTime;
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
     * Sets the '{@link TemporaryPlan#getPlanLevel() <em>planLevel</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newPlanLevel
     *            the new value of the '{@link TemporaryPlan#getPlanLevel() planLevel}' feature.
     * @generated
     */
    public void setPlanLevel(String newPlanLevel) {
        planLevel = newPlanLevel;
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
     * Sets the '{@link TemporaryPlan#getProgressRate() <em>progressRate</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newProgressRate
     *            the new value of the '{@link TemporaryPlan#getProgressRate() progressRate}'
     *            feature.
     * @generated
     */
    public void setProgressRate(String newProgressRate) {
        progressRate = newProgressRate;
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
     * Sets the '{@link TemporaryPlan#getPlanStartTime() <em>planStartTime</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newPlanStartTime
     *            the new value of the '{@link TemporaryPlan#getPlanStartTime() planStartTime}'
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
     * Sets the '{@link TemporaryPlan#getPlanEndTime() <em>planEndTime</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newPlanEndTime
     *            the new value of the '{@link TemporaryPlan#getPlanEndTime() planEndTime}'
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
     * Sets the '{@link TemporaryPlan#getWorkTime() <em>workTime</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newWorkTime
     *            the new value of the '{@link TemporaryPlan#getWorkTime() workTime}' feature.
     * @generated
     */
    public void setWorkTime(String newWorkTime) {
        workTime = newWorkTime;
    }

    /**
     * Returns the value of '<em><b>actualStartTime</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of '<em><b>actualStartTime</b></em>' feature
     * @generated
     */
    public Date getActualStartTime() {
        return actualStartTime;
    }

    /**
     * Sets the '{@link TemporaryPlan#getActualStartTime() <em>actualStartTime</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newActualStartTime
     *            the new value of the '{@link TemporaryPlan#getActualStartTime() actualStartTime}'
     *            feature.
     * @generated
     */
    public void setActualStartTime(Date newActualStartTime) {
        actualStartTime = newActualStartTime;
    }

    /**
     * Returns the value of '<em><b>actualEndTime</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of '<em><b>actualEndTime</b></em>' feature
     * @generated
     */
    public Date getActualEndTime() {
        return actualEndTime;
    }

    /**
     * Sets the '{@link TemporaryPlan#getActualEndTime() <em>actualEndTime</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newActualEndTime
     *            the new value of the '{@link TemporaryPlan#getActualEndTime() actualEndTime}'
     *            feature.
     * @generated
     */
    public void setActualEndTime(Date newActualEndTime) {
        actualEndTime = newActualEndTime;
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
     * Sets the '{@link TemporaryPlan#getRemark() <em>remark</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newRemark
     *            the new value of the '{@link TemporaryPlan#getRemark() remark}' feature.
     * @generated
     */
    public void setRemark(String newRemark) {
        remark = newRemark;
    }

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
     * Sets the '{@link TemporaryPlan#getProjectStatus() <em>projectStatus</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newProjectStatus
     *            the new value of the '{@link TemporaryPlan#getProjectStatus() projectStatus}'
     *            feature.
     * @generated
     */
    public void setProjectStatus(String newProjectStatus) {
        projectStatus = newProjectStatus;
    }

    /**
     * Returns the value of '<em><b>flowStatus</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of '<em><b>flowStatus</b></em>' feature
     * @generated
     */
    public String getFlowStatus() {
        return flowStatus;
    }

    /**
     * Sets the '{@link TemporaryPlan#getFlowStatus() <em>flowStatus</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newFlowStatus
     *            the new value of the '{@link TemporaryPlan#getFlowStatus() flowStatus}' feature.
     * @generated
     */
    public void setFlowStatus(String newFlowStatus) {
        flowStatus = newFlowStatus;
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
     * Sets the '{@link TemporaryPlan#getMilestone() <em>milestone</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newMilestone
     *            the new value of the '{@link TemporaryPlan#getMilestone() milestone}' feature.
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
     * Sets the '{@link TemporaryPlan#getRisk() <em>risk</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newRisk
     *            the new value of the '{@link TemporaryPlan#getRisk() risk}' feature.
     * @generated
     */
    public void setRisk(String newRisk) {
        risk = newRisk;
    }

    /**
     * Returns the value of '<em><b>changeType</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of '<em><b>changeType</b></em>' feature
     * @generated
     */
    public String getChangeType() {
        return changeType;
    }

    /**
     * Sets the '{@link TemporaryPlan#getChangeType() <em>changeType</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newChangeType
     *            the new value of the '{@link TemporaryPlan#getChangeType() changeType}' feature.
     * @generated
     */
    public void setChangeType(String newChangeType) {
        changeType = newChangeType;
    }

    /**
     * Returns the value of '<em><b>changeRemark</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of '<em><b>changeRemark</b></em>' feature
     * @generated
     */
    public String getChangeRemark() {
        return changeRemark;
    }

    /**
     * Sets the '{@link TemporaryPlan#getChangeRemark() <em>changeRemark</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newChangeRemark
     *            the new value of the '{@link TemporaryPlan#getChangeRemark() changeRemark}'
     *            feature.
     * @generated
     */
    public void setChangeRemark(String newChangeRemark) {
        changeRemark = newChangeRemark;
    }

    /**
     * Returns the value of '<em><b>changeInfoDocId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of '<em><b>changeInfoDocId</b></em>' feature
     * @generated
     */
    public String getChangeInfoDocId() {
        return changeInfoDocId;
    }

    /**
     * Sets the '{@link TemporaryPlan#getChangeInfoDocId() <em>changeInfoDocId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newChangeInfoDocId
     *            the new value of the '{@link TemporaryPlan#getChangeInfoDocId() changeInfoDocId}'
     *            feature.
     * @generated
     */
    public void setChangeInfoDocId(String newChangeInfoDocId) {
        changeInfoDocId = newChangeInfoDocId;
    }

    /**
     * Returns the value of '<em><b>changeInfoDocName</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of '<em><b>changeInfoDocName</b></em>' feature
     * @generated
     */
    public String getChangeInfoDocName() {
        return changeInfoDocName;
    }

    /**
     * Sets the '{@link TemporaryPlan#getChangeInfoDocName() <em>changeInfoDocName</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newChangeInfoDocName
     *            the new value of the '{@link TemporaryPlan#getChangeInfoDocName()
     *            changeInfoDocName}' feature.
     * @generated
     */
    public void setChangeInfoDocName(String newChangeInfoDocName) {
        changeInfoDocName = newChangeInfoDocName;
    }

    /**
     * Returns the value of '<em><b>changeInfoDocPath</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of '<em><b>changeInfoDocPath</b></em>' feature
     * @generated
     */
    public String getChangeInfoDocPath() {
        return changeInfoDocPath;
    }

    /**
     * Sets the '{@link TemporaryPlan#getChangeInfoDocPath() <em>changeInfoDocPath</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newChangeInfoDocPath
     *            the new value of the '{@link TemporaryPlan#getChangeInfoDocPath()
     *            changeInfoDocPath}' feature.
     * @generated
     */
    public void setChangeInfoDocPath(String newChangeInfoDocPath) {
        changeInfoDocPath = newChangeInfoDocPath;
    }

    /**
     * Returns the value of '<em><b>preposeIds</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of '<em><b>preposeIds</b></em>' feature
     * @generated
     */
    public String getPreposeIds() {
        return preposeIds;
    }

    /**
     * Sets the '{@link TemporaryPlan#getPreposeIds() <em>preposeIds</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newPreposeIds
     *            the new value of the '{@link TemporaryPlan#getPreposeIds() preposeIds}' feature.
     * @generated
     */
    public void setPreposeIds(String newPreposeIds) {
        preposeIds = newPreposeIds;
    }

    /**
     * Returns the value of '<em><b>storeyNo</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of '<em><b>storeyNo</b></em>' feature
     * @generated
     */
    public Integer getStoreyNo() {
        return storeyNo;
    }

    /**
     * Sets the '{@link TemporaryPlan#getStoreyNo() <em>storeyNo</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newStoreyNo
     *            the new value of the '{@link TemporaryPlan#getStoreyNo() storeyNo}' feature.
     * @generated
     */
    public void setStoreyNo(Integer newStoreyNo) {
        storeyNo = newStoreyNo;
    }

    /**
     * Returns the value of '<em><b>beforePlanId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of '<em><b>beforePlanId</b></em>' feature
     * @generated
     */
    public String getBeforePlanId() {
        return beforePlanId;
    }

    /**
     * Sets the '{@link TemporaryPlan#getBeforePlanId() <em>beforePlanId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newBeforePlanId
     *            the new value of the '{@link TemporaryPlan#getBeforePlanId() beforePlanId}'
     *            feature.
     * @generated
     */
    public void setBeforePlanId(String newBeforePlanId) {
        beforePlanId = newBeforePlanId;
    }

    /**
     * Returns the value of '<em><b>taskType</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of '<em><b>taskType</b></em>' feature
     * @generated
     */
    public String getTaskType() {
        return taskType;
    }

    /**
     * Sets the '{@link TemporaryPlan#getTaskType() <em>taskType</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newTaskType
     *            the new value of the '{@link TemporaryPlan#getTaskType() taskType}' feature.
     * @generated
     */
    public void setTaskType(String newTaskType) {
        taskType = newTaskType;
    }

    /**
     * Returns the value of '<em><b>taskNameType</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of '<em><b>taskNameType</b></em>' feature
     * @generated
     */
    public String getTaskNameType() {
        return taskNameType;
    }

    /**
     * Sets the '{@link TemporaryPlan#getTaskNameType() <em>taskNameType</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newTaskNameType
     *            the new value of the '{@link TemporaryPlan#getTaskNameType() taskNameType}'
     *            feature.
     * @generated
     */
    public void setTaskNameType(String newTaskNameType) {
        taskNameType = newTaskNameType;
    }

    /**
     * Returns the value of '<em><b>invalidTime</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of '<em><b>invalidTime</b></em>' feature
     * @generated
     */
    public Date getInvalidTime() {
        return invalidTime;
    }

    /**
     * Sets the '{@link TemporaryPlan#getInvalidTime() <em>invalidTime</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newInvalidTime
     *            the new value of the '{@link TemporaryPlan#getInvalidTime() invalidTime}'
     *            feature.
     * @generated
     */
    public void setInvalidTime(Date newInvalidTime) {
        invalidTime = newInvalidTime;
    }

    /**
     * Returns the value of '<em><b>formId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of '<em><b>formId</b></em>' feature
     * @generated
     */
    public String getFormId() {
        return formId;
    }

    /**
     * Sets the '{@link TemporaryPlan#getFormId() <em>formId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newFormId
     *            the new value of the '{@link TemporaryPlan#getFormId() formId}' feature.
     * @generated
     */
    public void setFormId(String newFormId) {
        formId = newFormId;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Plan getParentPlan() {
        return parentPlan;
    }

    public void setParentPlan(Plan parentPlan) {
        this.parentPlan = parentPlan;
    }

    public String getParentPlanName() {
        return parentPlanName;
    }

    public void setParentPlanName(String parentPlanName) {
        this.parentPlanName = parentPlanName;
    }

    public TSUserDto getOwnerInfo() {
        return ownerInfo;
    }

    public void setOwnerInfo(TSUserDto ownerInfo) {
        this.ownerInfo = ownerInfo;
    }

    public TSUserDto getCreator() {
        return creator;
    }

    public void setCreator(TSUserDto creator) {
        this.creator = creator;
    }

    public String getOwnerDept() {
        return ownerDept;
    }

    public void setOwnerDept(String ownerDept) {
        this.ownerDept = ownerDept;
    }

    public String getOwnerRealName() {
        return ownerRealName;
    }

    public void setOwnerRealName(String ownerRealName) {
        this.ownerRealName = ownerRealName;
    }

    public TSUserDto getAssignerInfo() {
        return assignerInfo;
    }

    public void setAssignerInfo(TSUserDto assignerInfo) {
        this.assignerInfo = assignerInfo;
    }

    public Date getAssignTimeStart() {
        return assignTimeStart;
    }

    public void setAssignTimeStart(Date assignTimeStart) {
        this.assignTimeStart = assignTimeStart;
    }

    public Date getAssignTimeEnd() {
        return assignTimeEnd;
    }

    public void setAssignTimeEnd(Date assignTimeEnd) {
        this.assignTimeEnd = assignTimeEnd;
    }

    public TSUserDto getLauncherInfo() {
        return launcherInfo;
    }

    public void setLauncherInfo(TSUserDto launcherInfo) {
        this.launcherInfo = launcherInfo;
    }

    public BusinessConfig getPlanLevelInfo() {
        return planLevelInfo;
    }

    public void setPlanLevelInfo(BusinessConfig planLevelInfo) {
        this.planLevelInfo = planLevelInfo;
    }

    public String getPlanLevelName() {
        return planLevelName;
    }

    public void setPlanLevelName(String planLevelName) {
        this.planLevelName = planLevelName;
    }

    public String getPlanStartTimeView() {
        return planStartTimeView;
    }

    public void setPlanStartTimeView(String planStartTimeView) {
        this.planStartTimeView = planStartTimeView;
    }

    public Date getPlanStartTimeStart() {
        return planStartTimeStart;
    }

    public void setPlanStartTimeStart(Date planStartTimeStart) {
        this.planStartTimeStart = planStartTimeStart;
    }

    public Date getPlanStartTimeEnd() {
        return planStartTimeEnd;
    }

    public void setPlanStartTimeEnd(Date planStartTimeEnd) {
        this.planStartTimeEnd = planStartTimeEnd;
    }

    public String getPlanEndTimeView() {
        return planEndTimeView;
    }

    public void setPlanEndTimeView(String planEndTimeView) {
        this.planEndTimeView = planEndTimeView;
    }

    public Date getPlanEndTimeStart() {
        return planEndTimeStart;
    }

    public void setPlanEndTimeStart(Date planEndTimeStart) {
        this.planEndTimeStart = planEndTimeStart;
    }

    public Date getPlanEndTimeEnd() {
        return planEndTimeEnd;
    }

    public void setPlanEndTimeEnd(Date planEndTimeEnd) {
        this.planEndTimeEnd = planEndTimeEnd;
    }

    public Date getActualStartTimeStart() {
        return actualStartTimeStart;
    }

    public void setActualStartTimeStart(Date actualStartTimeStart) {
        this.actualStartTimeStart = actualStartTimeStart;
    }

    public Date getActualStartTimeEnd() {
        return actualStartTimeEnd;
    }

    public void setActualStartTimeEnd(Date actualStartTimeEnd) {
        this.actualStartTimeEnd = actualStartTimeEnd;
    }

    public Date getActualEndTimeStart() {
        return actualEndTimeStart;
    }

    public void setActualEndTimeStart(Date actualEndTimeStart) {
        this.actualEndTimeStart = actualEndTimeStart;
    }

    public Date getActualEndTimeEnd() {
        return actualEndTimeEnd;
    }

    public void setActualEndTimeEnd(Date actualEndTimeEnd) {
        this.actualEndTimeEnd = actualEndTimeEnd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    public BusinessConfig getChangeTypeInfo() {
        return changeTypeInfo;
    }

    public void setChangeTypeInfo(BusinessConfig changeTypeInfo) {
        this.changeTypeInfo = changeTypeInfo;
    }

    public String getPreposePlans() {
        return preposePlans;
    }

    public void setPreposePlans(String preposePlans) {
        this.preposePlans = preposePlans;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
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

    public List<Plan> getPreposeList() {
        return preposeList;
    }

    public void setPreposeList(List<Plan> preposeList) {
        this.preposeList = preposeList;
    }

    public List<ResourceLinkInfoDto> getRescLinkInfoList() {
        return rescLinkInfoList;
    }

    public void setRescLinkInfoList(List<ResourceLinkInfoDto> rescLinkInfoList) {
        this.rescLinkInfoList = rescLinkInfoList;
    }

    public List<DeliverablesInfo> getDeliInfoList() {
        return deliInfoList;
    }

    public void setDeliInfoList(List<DeliverablesInfo> deliInfoList) {
        this.deliInfoList = deliInfoList;
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
        return "TemporaryPlan " + " [planId: " + getPlanId() + "]" + " [planNumber: "
               + getPlanNumber() + "]" + " [planOrder: " + getPlanOrder() + "]" + " [planName: "
               + getPlanName() + "]" + " [projectId: " + getProjectId() + "]" + " [parentPlanId: "
               + getParentPlanId() + "]" + " [owner: " + getOwner() + "]" + " [implementation: "
               + getImplementation() + "]" + " [assigner: " + getAssigner() + "]"
               + " [assignTime: " + getAssignTime() + "]" + " [launcher: " + getLauncher() + "]"
               + " [launchTime: " + getLaunchTime() + "]" + " [planLevel: " + getPlanLevel() + "]"
               + " [progressRate: " + getProgressRate() + "]" + " [planStartTime: "
               + getPlanStartTime() + "]" + " [planEndTime: " + getPlanEndTime() + "]"
               + " [workTime: " + getWorkTime() + "]" + " [actualStartTime: "
               + getActualStartTime() + "]" + " [actualEndTime: " + getActualEndTime() + "]"
               + " [remark: " + getRemark() + "]" + " [projectStatus: " + getProjectStatus() + "]"
               + " [flowStatus: " + getFlowStatus() + "]" + " [milestone: " + getMilestone() + "]"
               + " [risk: " + getRisk() + "]" + " [changeType: " + getChangeType() + "]"
               + " [changeRemark: " + getChangeRemark() + "]" + " [changeInfoDocId: "
               + getChangeInfoDocId() + "]" + " [changeInfoDocName: " + getChangeInfoDocName()
               + "]" + " [changeInfoDocPath: " + getChangeInfoDocPath() + "]" + " [preposeIds: "
               + getPreposeIds() + "]" + " [storeyNo: " + getStoreyNo() + "]" + " [beforePlanId: "
               + getBeforePlanId() + "]" + " [taskType: " + getTaskType() + "]"
               + " [taskNameType: " + getTaskNameType() + "]" + " [invalidTime: "
               + getInvalidTime() + "]" + " [formId: " + getFormId() + "]";
    }
}
