package com.glaway.ids.project.plan.entity;


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

import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.plan.dto.ResourceLinkInfoDto;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.alibaba.fastjson.annotation.JSONField;
import com.glaway.foundation.businessobject.entity.BusinessObject;
import com.glaway.ids.config.entity.BusinessConfig;


/**
 * A representation of the model object '<em><b>Plan</b></em>'. <!-- begin-user-doc --> <!--
 * end-user-doc -->
 * @author wangyangzan
 * @generated
 */
@Entity(name = "Plan")
@Table(name = "PM_PLAN")
public class Plan extends BusinessObject
{

    /**
     * <!-- begin-user-doc --> 计划序号 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private int planNumber = 0;

    /**
     * <!-- begin-user-doc --> 表单ID<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String formId = null;

    /**
     * <!-- begin-user-doc --> 计划顺序 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String planOrder = null;

    /**
     * 是否项目管理员创建
     */
    @Transient()
    private Boolean isCreateByPmo = null;

    /**
     * <!-- begin-user-doc --> 计划名称 <!-- end-user-doc -->
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
     * 项目名称
     */
    @Transient()
    private String projectName = null;

    /**
     * 当前用户
     */
    @Transient()
    private TSUserDto currentUser = null;

    /**
     * <!-- begin-user-doc --> 父节点id <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String parentPlanId = null;

    /**
     * 父节点计划信息
     */
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "parentPlanId", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private Plan parentPlan;

    /**
     * 父节点计划名称
     */
    @Transient()
    private String parentPlanName = null;
    
    /**
     * 父计划序号
     */
    @Transient()
    private Integer parentPlanNo = null;

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
     * 负责人部门
     */
    @Transient()
    private String ownerDept = null;

    /**
     * 负责人
     */
    @Transient()
    private String ownerRealName = null;

    /**
     * 应用情况
     */
    @Transient()
    private String implementation = null;

    /**
     * <!-- begin-user-doc --> 发布人 <!-- end-user-doc -->
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
     * <!-- begin-user-doc --> 发布时间 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignTime = null;

    /**
     * 发布开始时间
     */
    @Transient()
    @Temporal(TemporalType.DATE)
    private Date assignTimeStart = null;

    /**
     * 发布结束时间
     */
    @Transient()
    @Temporal(TemporalType.DATE)
    private Date assignTimeEnd = null;

    /**
     * <!-- begin-user-doc --> 流程发起人 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String launcher = null;     

    /**
     * 流程发起人信息
     */
    @Transient()
    private TSUserDto launcherInfo;

    /**
     * <!-- begin-user-doc --> 流程发起时间 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date launchTime = null;

    /**
     * 创建者信息
     */
    @Transient()
    private TSUserDto creator;

    /**
     * <!-- begin-user-doc --> 计划等级id <!-- end-user-doc -->
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
     * 计划等级中文
     */
    @Transient()
    private String planLevelName = null;

    /**
     * <!-- begin-user-doc --> 进度 <!-- end-user-doc -->
     */
    @Basic()
    private String progressRate = "0";

    /**
     * <!-- begin-user-doc --> 计划开始时间 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Temporal(TemporalType.DATE)
    private Date planStartTime = null;

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
     * <!-- begin-user-doc --> 参考工期 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String workTimeReference = "0";

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
     * <!-- begin-user-doc --> 实际完成时间 <!-- end-user-doc -->
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
    @Column(length = 2048)
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
     * <!-- begin-user-doc --> 流程状态 "NORMAL":无流程 "ORDERED":下达流程中 "CHANGE":变更流程中
     * "FEEDBACKING":完工反馈流程中 <!-- end-user-doc -->
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
     * <!-- begin-user-doc --> 是否批量下达驳回的 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String isAssignBack = "false";

    /**
     * <!-- begin-user-doc --> 是否单条下达驳回的 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String isAssignSingleBack = "false";

    /**
     * <!-- begin-user-doc --> 是否单条变更驳回的 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String isChangeSingleBack = "false";

    /**
     * 里程碑中文
     */
    @Transient()
    private String milestoneName;

    /**
     * 前置计划id
     */
    @Transient()
    private String preposeIds = null;
    
    /**
     * 前置计划序号
     */
    @Transient()
    private String preposeNos = null;

    /**
     * 前置计划
     */
    @Transient()
    private String preposePlans = null;

    /**
     * 文档
     */
    @Transient()
    private String documents;

    /**
     * 父节点顺序
     */
    @Transient()
    private String parentStorey = null;

    /**
     * <!-- begin-user-doc --> 同级顺序 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private Integer storeyNo = null;

    /**
     * 上层计划id
     */
    @Transient()
    private String beforePlanId = null;

    /**
     * <!-- begin-user-doc --> 反馈流程实例id <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String feedbackProcInstId = null;

    /**
     * <!-- begin-user-doc --> 反馈时记录之前的进度，如果反馈流程被驳回，则恢复之前的进度到progressRate字段 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String feedbackRateBefore = null;

    /**
     * <!-- begin-user-doc --> 计划资源 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String planSource = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String fromTemplate = null;

    /**
     * <!-- begin-user-doc --> 流程分解XML<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Column(columnDefinition = "CLOB")
    private String flowResolveXml = null;

    /**
     * <!-- begin-user-doc -->任务操作类型：计划分解、流程分解、下发评审任务、撤销计划分解、撤销流程分解等 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String opContent = null;

    /**
     * <!-- begin-user-doc --> 计划类型 (无用字段，后续去除)<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String planType = null;

    /**
     * <!-- begin-user-doc --> 计划类别：WBS计划、任务计划、流程计划 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String taskType = "";

    /**
     * <!-- begin-user-doc --> 计划类型：研发类、评审类、风险类等 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String taskNameType = null;

    /**
     * <!-- begin-user-doc --> 单元格id <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String cellId = null;

    /**
     * <!-- begin-user-doc --> 是否必要 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String required = null;

    /**
     * 顺序
     */
    @Transient()
    private String order = null;

    /**
     * 标识该数据是否查询结果
     */
    @Transient()
    private String result = "false";

    /**
     * 是否存在流程 0:不存在; 1:存在
     */
    @Transient()
    private String flowFlag = null;

    /**
     * 父节点临时id
     */
    @Transient()
    private String _parentId = null;

    /**
     * 资源列表
     */
    @Transient()
    private List<ResourceLinkInfoDto> rescLinkInfoList = new ArrayList<ResourceLinkInfoDto>();

    /**
     * 交付物列表
     */
    @Transient()
    private List<DeliverablesInfo> deliInfoList = new ArrayList<DeliverablesInfo>();

    /**
     * 输入列表
     */
    @Transient()
    private List<Inputs> inputList = new ArrayList<Inputs>();

    /**
     * 前置计划列表
     */
    @Transient()
    private List<Plan> preposeList = new ArrayList<Plan>();

    /**
     * <!-- begin-user-doc --> 废弃时间 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Temporal(TemporalType.DATE)
    private Date invalidTime = null;
    
    /**
     * 交付物名称
     */
    @Transient()
    private String deliverablesName = null;
    
    
    @Transient()
    private String planTemplateId = null;
    
    /**
     * 计划模板：是否必要
     */
    @Transient()
    private String isNecessary = null;

    /**
     * 关联ID：与其他项目进行关联的-第三方主键ID
     */
    @Transient()
    private String relationId = null;

    /**
     * 关联编码：与第三方系统关联的编码
     */
    @Transient()
    private String relationCode = null;
    
    
    /**
     * 视图id
     */
    @Transient()
    private String planViewInfoId = null;

    /**
     * 计划待接收流程id
     */
    @Basic()
    private String planReceivedProcInstId = null;

    /**
     * 计划待接收流程完成时间
     */
    @Basic()
    @Temporal(TemporalType.DATE)
    private Date planReceivedCompleteTime = null;

    /**
     * 计划委派流程id
     */
    @Basic()
    private String planDelegateProcInstId = null;

    /**
     * <!-- begin-user-doc --> 计划委派流程是否结束（false：流程未结束，receive:流程同意，refuse流程驳回） <!-- end-user-doc -->
     *
     * @generated
     */
    @Basic()
    private String isDelegateComplete = "false";

    /**
     * 绑定的页签组合模板id
     */
    @Basic
    private String tabCbTemplateId = null;

    /**
     * Returns the value of '<em><b>planNumber</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>planNumber</b></em>' feature
     * @generated
     */
    public int getPlanNumber()
    {
        return planNumber;
    }

    /**
     * Sets the '{@link Plan#getPlanNumber() <em>planNumber</em>}' feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanNumber
     *            the new value of the '{@link Plan#getPlanNumber() planNumber}' feature.
     * @generated
     */
    public void setPlanNumber(int newPlanNumber)
    {
        planNumber = newPlanNumber;
    }

    /**
     * Returns the value of '<em><b>formId</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>formId</b></em>' feature
     * @generated
     */
    public String getFormId()
    {
        return formId;
    }

    /**
     * Sets the '{@link Plan#getFormId() <em>formId</em>}' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @param newFormId
     *            the new value of the '{@link Plan#getFormId() formId}' feature.
     * @generated
     */
    public void setFormId(String newFormId)
    {
        formId = newFormId;
    }

    /**
     * Returns the value of '<em><b>planOrder</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>planOrder</b></em>' feature
     * @generated
     */
    public String getPlanOrder()
    {
        return planOrder;
    }

    /**
     * Sets the '{@link Plan#getPlanOrder() <em>planOrder</em>}' feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanOrder
     *            the new value of the '{@link Plan#getPlanOrder() planOrder}' feature.
     * @generated
     */
    public void setPlanOrder(String newPlanOrder)
    {
        planOrder = newPlanOrder;
    }

    /**
     * Returns the value of '<em><b>planName</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>planName</b></em>' feature
     * @generated
     */
    public String getPlanName()
    {
        return planName;
    }

    /**
     * Sets the '{@link Plan#getPlanName() <em>planName</em>}' feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanName
     *            the new value of the '{@link Plan#getPlanName() planName}' feature.
     * @generated
     */
    public void setPlanName(String newPlanName)
    {
        planName = newPlanName;
    }

    /**
     * Returns the value of '<em><b>projectId</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>projectId</b></em>' feature
     * @generated
     */
    public String getProjectId()
    {
        return projectId;
    }

    /**
     * Sets the '{@link Plan#getProjectId() <em>projectId</em>}' feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newProjectId
     *            the new value of the '{@link Plan#getProjectId() projectId}' feature.
     * @generated
     */
    public void setProjectId(String newProjectId)
    {
        projectId = newProjectId;
    }

    /**
     * Returns the value of '<em><b>parentPlanId</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>parentPlanId</b></em>' feature
     * @generated
     */
    public String getParentPlanId()
    {
        return parentPlanId;
    }

    /**
     * Sets the '{@link Plan#getParentPlanId() <em>parentPlanId</em>}' feature. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @param newParentPlanId
     *            the new value of the '{@link Plan#getParentPlanId() parentPlanId}' feature.
     * @generated
     */
    public void setParentPlanId(String newParentPlanId)
    {
        parentPlanId = newParentPlanId;
    }

    /**
     * Returns the value of '<em><b>owner</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>owner</b></em>' feature
     * @generated
     */
    public String getOwner()
    {
        return owner;
    }

    /**
     * Sets the '{@link Plan#getOwner() <em>owner</em>}' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @param newOwner
     *            the new value of the '{@link Plan#getOwner() owner}' feature.
     * @generated
     */
    public void setOwner(String newOwner)
    {
        owner = newOwner;
    }

    /**
     * Returns the value of '<em><b>assigner</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>assigner</b></em>' feature
     * @generated
     */
    public String getAssigner()
    {
        return assigner;
    }

    /**
     * Sets the '{@link Plan#getAssigner() <em>assigner</em>}' feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newAssigner
     *            the new value of the '{@link Plan#getAssigner() assigner}' feature.
     * @generated
     */
    public void setAssigner(String newAssigner)
    {
        assigner = newAssigner;
    }

    /**
     * Returns the value of '<em><b>assignTime</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>assignTime</b></em>' feature
     * @generated
     */
    public Date getAssignTime()
    {
        return assignTime;
    }

    /**
     * Sets the '{@link Plan#getAssignTime() <em>assignTime</em>}' feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newAssignTime
     *            the new value of the '{@link Plan#getAssignTime() assignTime}' feature.
     * @generated
     */
    public void setAssignTime(Date newAssignTime)
    {
        assignTime = newAssignTime;
    }

    /**
     * Returns the value of '<em><b>launcher</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>launcher</b></em>' feature
     * @generated
     */
    public String getLauncher()
    {
        return launcher;
    }

    /**
     * Sets the '{@link Plan#getLauncher() <em>launcher</em>}' feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newLauncher
     *            the new value of the '{@link Plan#getLauncher() launcher}' feature.
     * @generated
     */
    public void setLauncher(String newLauncher)
    {
        launcher = newLauncher;
    }

    /**
     * Returns the value of '<em><b>launchTime</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>launchTime</b></em>' feature
     * @generated
     */
    public Date getLaunchTime()
    {
        return launchTime;
    }

    /**
     * Sets the '{@link Plan#getLaunchTime() <em>launchTime</em>}' feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newLaunchTime
     *            the new value of the '{@link Plan#getLaunchTime() launchTime}' feature.
     * @generated
     */
    public void setLaunchTime(Date newLaunchTime)
    {
        launchTime = newLaunchTime;
    }

    /**
     * Returns the value of '<em><b>planLevel</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>planLevel</b></em>' feature
     * @generated
     */
    public String getPlanLevel()
    {
        return planLevel;
    }

    /**
     * Sets the '{@link Plan#getPlanLevel() <em>planLevel</em>}' feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanLevel
     *            the new value of the '{@link Plan#getPlanLevel() planLevel}' feature.
     * @generated
     */
    public void setPlanLevel(String newPlanLevel)
    {
        planLevel = newPlanLevel;
    }

    /**
     * Returns the value of '<em><b>progressRate</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>progressRate</b></em>' feature
     * @generated
     */
    public String getProgressRate()
    {
        return progressRate;
    }

    /**
     * Sets the '{@link Plan#getProgressRate() <em>progressRate</em>}' feature. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @param newProgressRate
     *            the new value of the '{@link Plan#getProgressRate() progressRate}' feature.
     * @generated
     */
    public void setProgressRate(String newProgressRate)
    {
        progressRate = newProgressRate;
    }

    /**
     * Returns the value of '<em><b>planStartTime</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>planStartTime</b></em>' feature
     * @generated
     */
    public Date getPlanStartTime()
    {
        return planStartTime;
    }

    /**
     * Sets the '{@link Plan#getPlanStartTime() <em>planStartTime</em>}' feature. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newPlanStartTime
     *            the new value of the '{@link Plan#getPlanStartTime() planStartTime}' feature.
     * @generated
     */
    public void setPlanStartTime(Date newPlanStartTime)
    {
        planStartTime = newPlanStartTime;
    }

    /**
     * Returns the value of '<em><b>planEndTime</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>planEndTime</b></em>' feature
     * @generated
     */
    public Date getPlanEndTime()
    {
        return planEndTime;
    }

    /**
     * Sets the '{@link Plan#getPlanEndTime() <em>planEndTime</em>}' feature. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @param newPlanEndTime
     *            the new value of the '{@link Plan#getPlanEndTime() planEndTime}' feature.
     * @generated
     */
    public void setPlanEndTime(Date newPlanEndTime)
    {
        planEndTime = newPlanEndTime;
    }

    /**
     * Returns the value of '<em><b>workTime</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>workTime</b></em>' feature
     * @generated
     */
    public String getWorkTime()
    {
        return workTime;
    }

    /**
     * Sets the '{@link Plan#getWorkTime() <em>workTime</em>}' feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newWorkTime
     *            the new value of the '{@link Plan#getWorkTime() workTime}' feature.
     * @generated
     */
    public void setWorkTime(String newWorkTime)
    {
        workTime = newWorkTime;
    }

    /**
     * Returns the value of '<em><b>workTimeReference</b></em>' feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>workTimeReference</b></em>' feature
     * @generated
     */
    public String getWorkTimeReference()
    {
        return workTimeReference;
    }

    /**
     * Sets the '{@link Plan#getWorkTimeReference() <em>workTimeReference</em>}' feature. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newWorkTimeReference
     *            the new value of the '{@link Plan#getWorkTimeReference() workTimeReference}'
     *            feature.
     * @generated
     */
    public void setWorkTimeReference(String newWorkTimeReference)
    {
        workTimeReference = newWorkTimeReference;
    }

    /**
     * Returns the value of '<em><b>actualStartTime</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>actualStartTime</b></em>' feature
     * @generated
     */
    public Date getActualStartTime()
    {
        return actualStartTime;
    }

    /**
     * Sets the '{@link Plan#getActualStartTime() <em>actualStartTime</em>}' feature. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newActualStartTime
     *            the new value of the '{@link Plan#getActualStartTime() actualStartTime}' feature.
     * @generated
     */
    public void setActualStartTime(Date newActualStartTime)
    {
        actualStartTime = newActualStartTime;
    }

    /**
     * Returns the value of '<em><b>actualEndTime</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>actualEndTime</b></em>' feature
     * @generated
     */
    public Date getActualEndTime()
    {
        return actualEndTime;
    }

    /**
     * Sets the '{@link Plan#getActualEndTime() <em>actualEndTime</em>}' feature. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newActualEndTime
     *            the new value of the '{@link Plan#getActualEndTime() actualEndTime}' feature.
     * @generated
     */
    public void setActualEndTime(Date newActualEndTime)
    {
        actualEndTime = newActualEndTime;
    }

    /**
     * Returns the value of '<em><b>remark</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>remark</b></em>' feature
     * @generated
     */
    public String getRemark()
    {
        return remark;
    }

    /**
     * Sets the '{@link Plan#getRemark() <em>remark</em>}' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @param newRemark
     *            the new value of the '{@link Plan#getRemark() remark}' feature.
     * @generated
     */
    public void setRemark(String newRemark)
    {
        remark = newRemark;
    }

    /**
     * Returns the value of '<em><b>projectStatus</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>projectStatus</b></em>' feature
     * @generated
     */
    public String getProjectStatus()
    {
        return projectStatus;
    }

    /**
     * Sets the '{@link Plan#getProjectStatus() <em>projectStatus</em>}' feature. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newProjectStatus
     *            the new value of the '{@link Plan#getProjectStatus() projectStatus}' feature.
     * @generated
     */
    public void setProjectStatus(String newProjectStatus)
    {
        projectStatus = newProjectStatus;
    }

    /**
     * Returns the value of '<em><b>flowStatus</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>flowStatus</b></em>' feature
     * @generated
     */
    public String getFlowStatus()
    {
        return flowStatus;
    }

    /**
     * Sets the '{@link Plan#getFlowStatus() <em>flowStatus</em>}' feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newFlowStatus
     *            the new value of the '{@link Plan#getFlowStatus() flowStatus}' feature.
     * @generated
     */
    public void setFlowStatus(String newFlowStatus)
    {
        flowStatus = newFlowStatus;
    }

    /**
     * Returns the value of '<em><b>milestone</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>milestone</b></em>' feature
     * @generated
     */
    public String getMilestone()
    {
        return milestone;
    }

    /**
     * Sets the '{@link Plan#getMilestone() <em>milestone</em>}' feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newMilestone
     *            the new value of the '{@link Plan#getMilestone() milestone}' feature.
     * @generated
     */
    public void setMilestone(String newMilestone)
    {
        milestone = newMilestone;
    }

    /**
     * Returns the value of '<em><b>storeyNo</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>storeyNo</b></em>' feature
     * @generated
     */
    public Integer getStoreyNo()
    {
        return storeyNo;
    }

    /**
     * Sets the '{@link Plan#getStoreyNo() <em>storeyNo</em>}' feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newStoreyNo
     *            the new value of the '{@link Plan#getStoreyNo() storeyNo}' feature.
     * @generated
     */
    public void setStoreyNo(Integer newStoreyNo)
    {
        storeyNo = newStoreyNo;
    }

    /**
     * Returns the value of '<em><b>feedbackProcInstId</b></em>' feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>feedbackProcInstId</b></em>' feature
     * @generated
     */
    public String getFeedbackProcInstId()
    {
        return feedbackProcInstId;
    }

    /**
     * Sets the '{@link Plan#getFeedbackProcInstId() <em>feedbackProcInstId</em>}' feature. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newFeedbackProcInstId
     *            the new value of the '{@link Plan#getFeedbackProcInstId() feedbackProcInstId}'
     *            feature.
     * @generated
     */
    public void setFeedbackProcInstId(String newFeedbackProcInstId)
    {
        feedbackProcInstId = newFeedbackProcInstId;
    }

    /**
     * Returns the value of '<em><b>feedbackRateBefore</b></em>' feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>feedbackRateBefore</b></em>' feature
     * @generated
     */
    public String getFeedbackRateBefore()
    {
        return feedbackRateBefore;
    }

    /**
     * Sets the '{@link Plan#getFeedbackRateBefore() <em>feedbackRateBefore</em>}' feature. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newFeedbackRateBefore
     *            the new value of the '{@link Plan#getFeedbackRateBefore() feedbackRateBefore}'
     *            feature.
     * @generated
     */
    public void setFeedbackRateBefore(String newFeedbackRateBefore)
    {
        feedbackRateBefore = newFeedbackRateBefore;
    }

    /**
     * Returns the value of '<em><b>planSource</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>planSource</b></em>' feature
     * @generated
     */
    public String getPlanSource()
    {
        return planSource;
    }

    /**
     * Sets the '{@link Plan#getPlanSource() <em>planSource</em>}' feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanSource
     *            the new value of the '{@link Plan#getPlanSource() planSource}' feature.
     * @generated
     */
    public void setPlanSource(String newPlanSource)
    {
        planSource = newPlanSource;
    }

    /**
     * Returns the value of '<em><b>fromTemplate</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>fromTemplate</b></em>' feature
     * @generated
     */
    public String getFromTemplate()
    {
        return fromTemplate;
    }

    /**
     * Sets the '{@link Plan#getFromTemplate() <em>fromTemplate</em>}' feature. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @param newFromTemplate
     *            the new value of the '{@link Plan#getFromTemplate() fromTemplate}' feature.
     * @generated
     */
    public void setFromTemplate(String newFromTemplate)
    {
        fromTemplate = newFromTemplate;
    }

    /**
     * Returns the value of '<em><b>flowResolveXml</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>flowResolveXml</b></em>' feature
     * @generated
     */
    public String getFlowResolveXml()
    {
        return flowResolveXml;
    }

    /**
     * Sets the '{@link Plan#getFlowResolveXml() <em>flowResolveXml</em>}' feature. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newFlowResolveXml
     *            the new value of the '{@link Plan#getFlowResolveXml() flowResolveXml}' feature.
     * @generated
     */
    public void setFlowResolveXml(String newFlowResolveXml)
    {
        flowResolveXml = newFlowResolveXml;
    }

    /**
     * Returns the value of '<em><b>opContent</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>opContent</b></em>' feature
     * @generated
     */
    public String getOpContent()
    {
        return opContent;
    }

    /**
     * Sets the '{@link Plan#getOpContent() <em>opContent</em>}' feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newOpContent
     *            the new value of the '{@link Plan#getOpContent() opContent}' feature.
     * @generated
     */
    public void setOpContent(String newOpContent)
    {
        opContent = newOpContent;
    }

    /**
     * Returns the value of '<em><b>planType</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>planType</b></em>' feature
     * @generated
     */
    public String getPlanType()
    {
        return planType;
    }

    /**
     * Sets the '{@link Plan#getPlanType() <em>planType</em>}' feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPlanType
     *            the new value of the '{@link Plan#getPlanType() planType}' feature.
     * @generated
     */
    public void setPlanType(String newPlanType)
    {
        planType = newPlanType;
    }

    /**
     * Returns the value of '<em><b>taskType</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>taskType</b></em>' feature
     * @generated
     */
    public String getTaskType()
    {
        return taskType;
    }

    /**
     * Sets the '{@link Plan#getTaskType() <em>taskType</em>}' feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newTaskType
     *            the new value of the '{@link Plan#getTaskType() taskType}' feature.
     * @generated
     */
    public void setTaskType(String newTaskType)
    {
        taskType = newTaskType;
    }

    /**
     * Returns the value of '<em><b>taskNameType</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>taskNameType</b></em>' feature
     * @generated
     */
    public String getTaskNameType()
    {
        return taskNameType;
    }

    /**
     * Sets the '{@link Plan#getTaskNameType() <em>taskNameType</em>}' feature. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @param newTaskNameType
     *            the new value of the '{@link Plan#getTaskNameType() taskNameType}' feature.
     * @generated
     */
    public void setTaskNameType(String newTaskNameType)
    {
        taskNameType = newTaskNameType;
    }

    /**
     * Returns the value of '<em><b>cellId</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>cellId</b></em>' feature
     * @generated
     */
    public String getCellId()
    {
        return cellId;
    }

    /**
     * Sets the '{@link Plan#getCellId() <em>cellId</em>}' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @param newCellId
     *            the new value of the '{@link Plan#getCellId() cellId}' feature.
     * @generated
     */
    public void setCellId(String newCellId)
    {
        cellId = newCellId;
    }

    /**
     * Returns the value of '<em><b>required</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>required</b></em>' feature
     * @generated
     */
    public String getRequired()
    {
        return required;
    }

    /**
     * Sets the '{@link Plan#getRequired() <em>required</em>}' feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newRequired
     *            the new value of the '{@link Plan#getRequired() required}' feature.
     * @generated
     */
    public void setRequired(String newRequired)
    {
        required = newRequired;
    }

    /**
     * Returns the value of '<em><b>invalidTime</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>invalidTime</b></em>' feature
     * @generated
     */
    public Date getInvalidTime()
    {
        return invalidTime;
    }

    /**
     * Sets the '{@link Plan#getInvalidTime() <em>invalidTime</em>}' feature. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @param newInvalidTime
     *            the new value of the '{@link Plan#getInvalidTime() invalidTime}' feature.
     * @generated
     */
    public void setInvalidTime(Date newInvalidTime)
    {
        invalidTime = newInvalidTime;
    }

    public Boolean getIsCreateByPmo()
    {
        return isCreateByPmo;
    }

    public void setIsCreateByPmo(Boolean isCreateByPmo)
    {
        this.isCreateByPmo = isCreateByPmo;
    }

    public Project getProject()
    {
        return project;
    }

    public void setProject(Project project)
    {
        this.project = project;
    }

    public Plan getParentPlan()
    {
        return parentPlan;
    }

    public void setParentPlan(Plan parentPlan)
    {
        this.parentPlan = parentPlan;
    }

    public String getParentPlanName()
    {
        return parentPlanName;
    }

    public void setParentPlanName(String parentPlanName)
    {
        this.parentPlanName = parentPlanName;
    }

    public TSUserDto getOwnerInfo()
    {
        return ownerInfo;
    }

    public void setOwnerInfo(TSUserDto ownerInfo)
    {
        this.ownerInfo = ownerInfo;
    }

    public String getOwnerDept()
    {
        return ownerDept;
    }

    public void setOwnerDept(String ownerDept)
    {
        this.ownerDept = ownerDept;
    }

    public String getOwnerRealName()
    {
        return ownerRealName;
    }

    public void setOwnerRealName(String ownerRealName)
    {
        this.ownerRealName = ownerRealName;
    }

    public String getImplementation()
    {
        return implementation;
    }

    public void setImplementation(String implementation)
    {
        this.implementation = implementation;
    }

    public TSUserDto getAssignerInfo()
    {
        return assignerInfo;
    }

    public void setAssignerInfo(TSUserDto assignerInfo)
    {
        this.assignerInfo = assignerInfo;
    }

    public Date getAssignTimeStart()
    {
        return assignTimeStart;
    }

    public void setAssignTimeStart(Date assignTimeStart)
    {
        this.assignTimeStart = assignTimeStart;
    }

    public Date getAssignTimeEnd()
    {
        return assignTimeEnd;
    }

    public void setAssignTimeEnd(Date assignTimeEnd)
    {
        this.assignTimeEnd = assignTimeEnd;
    }

    public TSUserDto getLauncherInfo()
    {
        return launcherInfo;
    }

    public void setLauncherInfo(TSUserDto launcherInfo)
    {
        this.launcherInfo = launcherInfo;
    }

    public TSUserDto getCreator()
    {
        return creator;
    }

    public void setCreator(TSUserDto creator)
    {
        this.creator = creator;
    }

    public BusinessConfig getPlanLevelInfo()
    {
        return planLevelInfo;
    }

    public void setPlanLevelInfo(BusinessConfig planLevelInfo)
    {
        this.planLevelInfo = planLevelInfo;
    }

    public String getPlanLevelName()
    {
        return planLevelName;
    }

    public void setPlanLevelName(String planLevelName)
    {
        this.planLevelName = planLevelName;
    }

    public Date getPlanStartTimeStart()
    {
        return planStartTimeStart;
    }

    public void setPlanStartTimeStart(Date planStartTimeStart)
    {
        this.planStartTimeStart = planStartTimeStart;
    }

    public Date getPlanStartTimeEnd()
    {
        return planStartTimeEnd;
    }

    public void setPlanStartTimeEnd(Date planStartTimeEnd)
    {
        this.planStartTimeEnd = planStartTimeEnd;
    }

    public Date getPlanEndTimeStart()
    {
        return planEndTimeStart;
    }

    public void setPlanEndTimeStart(Date planEndTimeStart)
    {
        this.planEndTimeStart = planEndTimeStart;
    }

    public Date getPlanEndTimeEnd()
    {
        return planEndTimeEnd;
    }

    public void setPlanEndTimeEnd(Date planEndTimeEnd)
    {
        this.planEndTimeEnd = planEndTimeEnd;
    }

    public Date getActualStartTimeStart()
    {
        return actualStartTimeStart;
    }

    public void setActualStartTimeStart(Date actualStartTimeStart)
    {
        this.actualStartTimeStart = actualStartTimeStart;
    }

    public Date getActualStartTimeEnd()
    {
        return actualStartTimeEnd;
    }

    public void setActualStartTimeEnd(Date actualStartTimeEnd)
    {
        this.actualStartTimeEnd = actualStartTimeEnd;
    }

    public Date getActualEndTimeStart()
    {
        return actualEndTimeStart;
    }

    public void setActualEndTimeStart(Date actualEndTimeStart)
    {
        this.actualEndTimeStart = actualEndTimeStart;
    }

    public Date getActualEndTimeEnd()
    {
        return actualEndTimeEnd;
    }

    public void setActualEndTimeEnd(Date actualEndTimeEnd)
    {
        this.actualEndTimeEnd = actualEndTimeEnd;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getMilestoneName()
    {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName)
    {
        this.milestoneName = milestoneName;
    }

    public String getPreposeIds()
    {
        return preposeIds;
    }

    public void setPreposeIds(String preposeIds)
    {
        this.preposeIds = preposeIds;
    }

    public String getPreposePlans()
    {
        return preposePlans;
    }

    public void setPreposePlans(String preposePlans)
    {
        this.preposePlans = preposePlans;
    }

    public String getDocuments()
    {
        return documents;
    }

    public void setDocuments(String documents)
    {
        this.documents = documents;
    }

    public String getParentStorey()
    {
        return parentStorey;
    }

    public void setParentStorey(String parentStorey)
    {
        this.parentStorey = parentStorey;
    }

    public String getBeforePlanId()
    {
        return beforePlanId;
    }

    public void setBeforePlanId(String beforePlanId)
    {
        this.beforePlanId = beforePlanId;
    }

    public String getOrder()
    {
        return order;
    }

    public void setOrder(String order)
    {
        this.order = order;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public String getFlowFlag()
    {
        return flowFlag;
    }

    public void setFlowFlag(String flowFlag)
    {
        this.flowFlag = flowFlag;
    }

    public String get_parentId()
    {
        return _parentId;
    }

    public void set_parentId(String _parentId)
    {
        this._parentId = _parentId;
    }

    public List<ResourceLinkInfoDto> getRescLinkInfoList()
    {
        return rescLinkInfoList;
    }

    public void setRescLinkInfoList(List<ResourceLinkInfoDto> rescLinkInfoList)
    {
        this.rescLinkInfoList = rescLinkInfoList;
    }

    public List<DeliverablesInfo> getDeliInfoList()
    {
        return deliInfoList;
    }

    public void setDeliInfoList(List<DeliverablesInfo> deliInfoList)
    {
        this.deliInfoList = deliInfoList;
    }

    public List<Inputs> getInputList()
    {
        return inputList;
    }

    public void setInputList(List<Inputs> inputList)
    {
        this.inputList = inputList;
    }

    public List<Plan> getPreposeList()
    {
        return preposeList;
    }

    public void setPreposeList(List<Plan> preposeList)
    {
        this.preposeList = preposeList;
    }

    public String getProjectName()
    {
        return projectName;
    }

    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }

    public TSUserDto getCurrentUser()
    {
        return currentUser;
    }

    public void setCurrentUser(TSUserDto currentUser)
    {
        this.currentUser = currentUser;
    }

    public String getIsAssignBack()
    {
        return isAssignBack;
    }

    public void setIsAssignBack(String isAssignBack)
    {
        this.isAssignBack = isAssignBack;
    }

    public String getIsAssignSingleBack()
    {
        return isAssignSingleBack;
    }

    public void setIsAssignSingleBack(String isAssignSingleBack)
    {
        this.isAssignSingleBack = isAssignSingleBack;
    }

    public String getIsChangeSingleBack()
    {
        return isChangeSingleBack;
    }

    public void setIsChangeSingleBack(String isChangeSingleBack)
    {
        this.isChangeSingleBack = isChangeSingleBack;
    }

    public Integer getParentPlanNo() {
        return parentPlanNo;
    }

    public void setParentPlanNo(Integer parentPlanNo) {
        this.parentPlanNo = parentPlanNo;
    }

    public String getPreposeNos() {
        return preposeNos;
    }

    public void setPreposeNos(String preposeNos) {
        this.preposeNos = preposeNos;
    }
    
    public String getDeliverablesName() {
        return deliverablesName;
    }

    public void setDeliverablesName(String deliverablesName) {
        this.deliverablesName = deliverablesName;
    }
    
    public String getPlanTemplateId() {
        return planTemplateId;
    }

    public void setPlanTemplateId(String planTemplateId) {
        this.planTemplateId = planTemplateId;
    }
    
    public String getIsNecessary() {
        return isNecessary;
    }

    public void setIsNecessary(String isNecessary) {
        this.isNecessary = isNecessary;
    }

    
    public String getPlanViewInfoId() {
        return planViewInfoId;
    }

    public void setPlanViewInfoId(String planViewInfoId) {
        this.planViewInfoId = planViewInfoId;
    }

    public Boolean getCreateByPmo() {
        return isCreateByPmo;
    }

    public void setCreateByPmo(Boolean createByPmo) {
        isCreateByPmo = createByPmo;
    }

    public String getPlanReceivedProcInstId() {
        return planReceivedProcInstId;
    }

    public void setPlanReceivedProcInstId(String planReceivedProcInstId) {
        this.planReceivedProcInstId = planReceivedProcInstId;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getRelationCode() {
        return relationCode;
    }

    public void setRelationCode(String relationCode) {
        this.relationCode = relationCode;
    }

    public Date getPlanReceivedCompleteTime() {
        return planReceivedCompleteTime;
    }

    public void setPlanReceivedCompleteTime(Date planReceivedCompleteTime) {
        this.planReceivedCompleteTime = planReceivedCompleteTime;
    }

    public String getPlanDelegateProcInstId() {
        return planDelegateProcInstId;
    }

    public void setPlanDelegateProcInstId(String planDelegateProcInstId) {
        this.planDelegateProcInstId = planDelegateProcInstId;
    }

    public String getIsDelegateComplete() {
        return isDelegateComplete;
    }

    public void setIsDelegateComplete(String isDelegateComplete) {
        this.isDelegateComplete = isDelegateComplete;
    }

    public String getTabCbTemplateId() {
        return tabCbTemplateId;
    }

    public void setTabCbTemplateId(String tabCbTemplateId) {
        this.tabCbTemplateId = tabCbTemplateId;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "planNumber=" + planNumber +
                ", formId='" + formId + '\'' +
                ", planOrder='" + planOrder + '\'' +
                ", isCreateByPmo=" + isCreateByPmo +
                ", planName='" + planName + '\'' +
                ", projectId='" + projectId + '\'' +
                ", project=" + project +
                ", projectName='" + projectName + '\'' +
                ", currentUser=" + currentUser +
                ", parentPlanId='" + parentPlanId + '\'' +
                ", parentPlan=" + parentPlan +
                ", parentPlanName='" + parentPlanName + '\'' +
                ", parentPlanNo=" + parentPlanNo +
                ", owner='" + owner + '\'' +
                ", ownerInfo=" + ownerInfo +
                ", ownerDept='" + ownerDept + '\'' +
                ", ownerRealName='" + ownerRealName + '\'' +
                ", implementation='" + implementation + '\'' +
                ", assigner='" + assigner + '\'' +
                ", assignerInfo=" + assignerInfo +
                ", assignTime=" + assignTime +
                ", assignTimeStart=" + assignTimeStart +
                ", assignTimeEnd=" + assignTimeEnd +
                ", launcher='" + launcher + '\'' +
                ", launcherInfo=" + launcherInfo +
                ", launchTime=" + launchTime +
                ", creator=" + creator +
                ", planLevel='" + planLevel + '\'' +
                ", planLevelInfo=" + planLevelInfo +
                ", planLevelName='" + planLevelName + '\'' +
                ", progressRate='" + progressRate + '\'' +
                ", planStartTime=" + planStartTime +
                ", planStartTimeStart=" + planStartTimeStart +
                ", planStartTimeEnd=" + planStartTimeEnd +
                ", planEndTime=" + planEndTime +
                ", planEndTimeStart=" + planEndTimeStart +
                ", planEndTimeEnd=" + planEndTimeEnd +
                ", workTime='" + workTime + '\'' +
                ", workTimeReference='" + workTimeReference + '\'' +
                ", actualStartTime=" + actualStartTime +
                ", actualStartTimeStart=" + actualStartTimeStart +
                ", actualStartTimeEnd=" + actualStartTimeEnd +
                ", actualEndTime=" + actualEndTime +
                ", actualEndTimeStart=" + actualEndTimeStart +
                ", actualEndTimeEnd=" + actualEndTimeEnd +
                ", remark='" + remark + '\'' +
                ", status='" + status + '\'' +
                ", projectStatus='" + projectStatus + '\'' +
                ", flowStatus='" + flowStatus + '\'' +
                ", milestone='" + milestone + '\'' +
                ", isAssignBack='" + isAssignBack + '\'' +
                ", isAssignSingleBack='" + isAssignSingleBack + '\'' +
                ", isChangeSingleBack='" + isChangeSingleBack + '\'' +
                ", milestoneName='" + milestoneName + '\'' +
                ", preposeIds='" + preposeIds + '\'' +
                ", preposeNos='" + preposeNos + '\'' +
                ", preposePlans='" + preposePlans + '\'' +
                ", documents='" + documents + '\'' +
                ", parentStorey='" + parentStorey + '\'' +
                ", storeyNo=" + storeyNo +
                ", beforePlanId='" + beforePlanId + '\'' +
                ", feedbackProcInstId='" + feedbackProcInstId + '\'' +
                ", feedbackRateBefore='" + feedbackRateBefore + '\'' +
                ", planSource='" + planSource + '\'' +
                ", fromTemplate='" + fromTemplate + '\'' +
                ", flowResolveXml='" + flowResolveXml + '\'' +
                ", opContent='" + opContent + '\'' +
                ", planType='" + planType + '\'' +
                ", taskType='" + taskType + '\'' +
                ", taskNameType='" + taskNameType + '\'' +
                ", cellId='" + cellId + '\'' +
                ", required='" + required + '\'' +
                ", order='" + order + '\'' +
                ", result='" + result + '\'' +
                ", flowFlag='" + flowFlag + '\'' +
                ", _parentId='" + _parentId + '\'' +
                ", rescLinkInfoList=" + rescLinkInfoList +
                ", deliInfoList=" + deliInfoList +
                ", inputList=" + inputList +
                ", preposeList=" + preposeList +
                ", invalidTime=" + invalidTime +
                ", deliverablesName='" + deliverablesName + '\'' +
                ", planTemplateId='" + planTemplateId + '\'' +
                ", isNecessary='" + isNecessary + '\'' +
                ", relationId='" + relationId + '\'' +
                ", relationCode='" + relationCode + '\'' +
                ", planViewInfoId='" + planViewInfoId + '\'' +
                ", planReceivedProcInstId='" + planReceivedProcInstId + '\'' +
                ", planReceivedCompleteTime=" + planReceivedCompleteTime +
                ", planDelegateProcInstId='" + planDelegateProcInstId + '\'' +
                ", isDelegateComplete='" + isDelegateComplete + '\'' +
                ", tabCbTemplateId='" + tabCbTemplateId + '\'' +
                '}';
    }
}
