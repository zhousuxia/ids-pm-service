package com.glaway.ids.project.menu.dto;


import com.glaway.foundation.businessobject.entity.BusinessObject;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.vdata.GLVData;
import com.glaway.ids.config.dto.EpsConfigDto;
import com.glaway.ids.config.entity.BusinessConfig;
import com.glaway.ids.config.entity.EpsConfig;
import com.glaway.ids.project.plan.dto.BusinessConfigDto;
import com.google.gson.annotations.Until;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;


/**
 * A representation of the model object '<em><b>Project</b></em>'. <!-- begin-user-doc --> <!--
 * end-user-doc -->
 * 
 * @author wangyangzan
 * @generated
 */

public class ProjectDto extends GLVData
{

    /**
     * <!-- begin-user-doc -->项目名称 <!-- end-user-doc -->
     * 
     * @generated
     */
    private String name = null;

    /**
     * <!-- begin-user-doc -->项目编号 <!-- end-user-doc -->
     * 
     * @generated
     */
    private String projectNumber = null;

    /**
     * <!-- begin-user-doc -->项目审批状态：1-审批中, 0-审批外（撤销或正常结束或未发起流程） <!-- end-user-doc -->
     * 
     * @generated
     */
    private String status = null;

    /**
     * <!-- begin-user-doc -->项目开始时间 <!-- end-user-doc -->
     * 
     * @generated
     */
    private Date startProjectTime = null;

    /**
     * <!-- begin-user-doc -->项目结束时间 <!-- end-user-doc -->
     * 
     * @generated
     */
    private Date endProjectTime = null;

    /**
     * <!-- begin-user-doc -->工期类型 <!-- end-user-doc -->
     * 
     * @generated
     */
    private String projectTimeType = null;

    /**
     * <!-- begin-user-doc -->项目模板 <!-- end-user-doc -->
     * 
     * @generated
     */
    private String projectTemplate = null;

    /**
     * <!-- begin-user-doc -->备注 <!-- end-user-doc -->
     * 
     * @generated
     */
    private String remark = null;

    /**
     * 时间控件 用于查询时使用（开始时间前）
     */
    private Date queryBefStartProjTime = null;

    /**
     * 时间控件 用于查询时使用（开始时间后）
     */
    private Date queryAftStartProjTime = null;

    /**
     * 时间控件 用于查询时使用（结束时间前）
     */
    private Date queryBefEndProjTime = null;

    /**
     * 时间控件 用于查询时使用（结束时间后）
     */
    private Date queryAftEndProjTime = null;

    /**
     * <!-- begin-user-doc -->项目生命周期状态 <!-- end-user-doc -->
     * 
     * @generated
     */
    private String prevLifeStatus = null;

    /**
     * 
     */
    private EpsConfigDto epsInfo;

    /**
     * <!-- begin-user-doc -->项目分类 <!-- end-user-doc -->
     * 
     * @generated
     */
    private String eps = null;

    /**
     * 项目分类
     */
    private String epsName = null;

    /**
     * <!-- begin-user-doc -->项目阶段 <!-- end-user-doc -->
     * 
     * @generated
     */
    private String phase = null;

    /**
     * 阶段信息
     */
    private BusinessConfigDto phaseInfo;

    /**
     * <!-- begin-user-doc -->项目进度 <!-- end-user-doc -->
     * 
     * @generated
     */
    private double process = 0.0;

    /**
     * <!-- begin-user-doc --> 记录审批流程中是否被驳回：1-已被驳回, 0-未被驳回 需与status联合起来使用，status为"1"时才判断是否被驳回 <!--
     * end-user-doc -->
     * 
     * @generated
     */
    private String isRefuse = null;

    /**
     * <!-- begin-user-doc -->项目经理ID <!-- end-user-doc -->
     * 
     * @generated
     */
    private String projectManagers = null;

    /**
     * <!-- begin-user-doc -->项目经理的名字 <!-- end-user-doc -->
     * 
     * @generated
     */
    private String projectManagerNames = null;

    /** 是否存在流程 0:不存在; 1:存在 */
    private String flowFlag = null;

    /**
     * <!-- begin-user-doc -->提交审批人 <!-- end-user-doc -->
     */

    private TSUserDto creator;

    /**
     * <!-- begin-user-doc -->记录流程实例id <!-- end-user-doc -->
     * 
     * @generated
     */
    private String procInstId = null;

    /**
     * 关闭时间
     * 
     * @generated
     */
    private Date closeTime = null;

    /**
     * 暂停时间
     * 
     * @generated
     */
    private Date pauseTime = null;

    /**
     * Returns the value of '<em><b>name</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>name</b></em>' feature
     * @generated
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the '{@link ProjectDto#getName() <em>name</em>}' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @param newName
     *            the new value of the '{@link ProjectDto#getName() name}' feature.
     * @generated
     */
    public void setName(String newName)
    {
        name = newName;
    }

    /**
     * Returns the value of '<em><b>projectNumber</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>projectNumber</b></em>' feature
     * @generated
     */
    public String getProjectNumber()
    {
        return projectNumber;
    }

    /**
     * Sets the '{@link ProjectDto#getProjectNumber() <em>projectNumber</em>}' feature. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newProjectNumber
     *            the new value of the '{@link ProjectDto#getProjectNumber() projectNumber}' feature.
     * @generated
     */
    public void setProjectNumber(String newProjectNumber)
    {
        projectNumber = newProjectNumber;
    }

    /**
     * Returns the value of '<em><b>status</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>status</b></em>' feature
     * @generated
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * Sets the '{@link ProjectDto#getStatus() <em>status</em>}' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @param newStatus
     *            the new value of the '{@link ProjectDto#getStatus() status}' feature.
     * @generated
     */
    public void setStatus(String newStatus)
    {
        status = newStatus;
    }

    /**
     * Returns the value of '<em><b>startProjectTime</b></em>' feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>startProjectTime</b></em>' feature
     * @generated
     */
    public Date getStartProjectTime()
    {
        return startProjectTime;
    }

    /**
     * Sets the '{@link ProjectDto#getStartProjectTime() <em>startProjectTime</em>}' feature. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newStartProjectTime
     *            the new value of the '{@link ProjectDto#getStartProjectTime() startProjectTime}'
     *            feature.
     * @generated
     */
    public void setStartProjectTime(Date newStartProjectTime)
    {
        startProjectTime = newStartProjectTime;
    }

    /**
     * Returns the value of '<em><b>endProjectTime</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>endProjectTime</b></em>' feature
     * @generated
     */
    public Date getEndProjectTime()
    {
        return endProjectTime;
    }

    /**
     * Sets the '{@link ProjectDto#getEndProjectTime() <em>endProjectTime</em>}' feature. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newEndProjectTime
     *            the new value of the '{@link ProjectDto#getEndProjectTime() endProjectTime}'
     *            feature.
     * @generated
     */
    public void setEndProjectTime(Date newEndProjectTime)
    {
        endProjectTime = newEndProjectTime;
    }

    /**
     * Returns the value of '<em><b>projectTimeType</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>projectTimeType</b></em>' feature
     * @generated
     */
    public String getProjectTimeType()
    {
        return projectTimeType;
    }

    /**
     * Sets the '{@link ProjectDto#getProjectTimeType() <em>projectTimeType</em>}' feature. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newProjectTimeType
     *            the new value of the '{@link ProjectDto#getProjectTimeType() projectTimeType}'
     *            feature.
     * @generated
     */
    public void setProjectTimeType(String newProjectTimeType)
    {
        projectTimeType = newProjectTimeType;
    }

    /**
     * Returns the value of '<em><b>projectTemplate</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>projectTemplate</b></em>' feature
     * @generated
     */
    public String getProjectTemplate()
    {
        return projectTemplate;
    }

    /**
     * Sets the '{@link ProjectDto#getProjectTemplate() <em>projectTemplate</em>}' feature. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newProjectTemplate
     *            the new value of the '{@link ProjectDto#getProjectTemplate() projectTemplate}'
     *            feature.
     * @generated
     */
    public void setProjectTemplate(String newProjectTemplate)
    {
        projectTemplate = newProjectTemplate;
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
     * Sets the '{@link ProjectDto#getRemark() <em>remark</em>}' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @param newRemark
     *            the new value of the '{@link ProjectDto#getRemark() remark}' feature.
     * @generated
     */
    public void setRemark(String newRemark)
    {
        remark = newRemark;
    }

    /**
     * Returns the value of '<em><b>prevLifeStatus</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>prevLifeStatus</b></em>' feature
     * @generated
     */
    public String getPrevLifeStatus()
    {
        return prevLifeStatus;
    }

    /**
     * Sets the '{@link ProjectDto#getPrevLifeStatus() <em>prevLifeStatus</em>}' feature. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newPrevLifeStatus
     *            the new value of the '{@link ProjectDto#getPrevLifeStatus() prevLifeStatus}'
     *            feature.
     * @generated
     */
    public void setPrevLifeStatus(String newPrevLifeStatus)
    {
        prevLifeStatus = newPrevLifeStatus;
    }

    /**
     * Returns the value of '<em><b>eps</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>eps</b></em>' feature
     * @generated
     */
    public String getEps()
    {
        return eps;
    }

    /**
     * Sets the '{@link ProjectDto#getEps() <em>eps</em>}' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @param newEps
     *            the new value of the '{@link ProjectDto#getEps() eps}' feature.
     * @generated
     */
    public void setEps(String newEps)
    {
        eps = newEps;
    }

    /**
     * Returns the value of '<em><b>phase</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>phase</b></em>' feature
     * @generated
     */
    public String getPhase()
    {
        return phase;
    }

    /**
     * Sets the '{@link ProjectDto#getPhase() <em>phase</em>}' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @param newPhase
     *            the new value of the '{@link ProjectDto#getPhase() phase}' feature.
     * @generated
     */
    public void setPhase(String newPhase)
    {
        phase = newPhase;
    }

    /**
     * Returns the value of '<em><b>process</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>process</b></em>' feature
     * @generated
     */
    public double getProcess()
    {
        return process;
    }

    /**
     * Sets the '{@link ProjectDto#getProcess() <em>process</em>}' feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newProcess
     *            the new value of the '{@link ProjectDto#getProcess() process}' feature.
     * @generated
     */
    public void setProcess(double newProcess)
    {
        process = newProcess;
    }

    /**
     * Returns the value of '<em><b>isRefuse</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>isRefuse</b></em>' feature
     * @generated
     */
    public String getIsRefuse()
    {
        return isRefuse;
    }

    /**
     * Sets the '{@link ProjectDto#getIsRefuse() <em>isRefuse</em>}' feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newIsRefuse
     *            the new value of the '{@link ProjectDto#getIsRefuse() isRefuse}' feature.
     * @generated
     */
    public void setIsRefuse(String newIsRefuse)
    {
        isRefuse = newIsRefuse;
    }

    /**
     * Returns the value of '<em><b>projectManagers</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>projectManagers</b></em>' feature
     * @generated
     */
    public String getProjectManagers()
    {
        return projectManagers;
    }

    /**
     * Sets the '{@link ProjectDto#getProjectManagers() <em>projectManagers</em>}' feature. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newProjectManagers
     *            the new value of the '{@link ProjectDto#getProjectManagers() projectManagers}'
     *            feature.
     * @generated
     */
    public void setProjectManagers(String newProjectManagers)
    {
        projectManagers = newProjectManagers;
    }

    /**
     * Returns the value of '<em><b>projectManagerNames</b></em>' feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>projectManagerNames</b></em>' feature
     * @generated
     */
    public String getProjectManagerNames()
    {
        return projectManagerNames;
    }

    /**
     * Sets the '{@link ProjectDto#getProjectManagerNames() <em>projectManagerNames</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newProjectManagerNames
     *            the new value of the '{@link ProjectDto#getProjectManagerNames()
     *            projectManagerNames}' feature.
     * @generated
     */
    public void setProjectManagerNames(String newProjectManagerNames)
    {
        projectManagerNames = newProjectManagerNames;
    }

    /**
     * Returns the value of '<em><b>procInstId</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>procInstId</b></em>' feature
     * @generated
     */
    public String getProcInstId()
    {
        return procInstId;
    }

    /**
     * Sets the '{@link ProjectDto#getProcInstId() <em>procInstId</em>}' feature. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @param newProcInstId
     *            the new value of the '{@link ProjectDto#getProcInstId() procInstId}' feature.
     * @generated
     */
    public void setProcInstId(String newProcInstId)
    {
        procInstId = newProcInstId;
    }

    /**
     * Returns the value of '<em><b>closeTime</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>closeTime</b></em>' feature
     * @generated
     */
    public Date getCloseTime()
    {
        return closeTime;
    }

    /**
     * Sets the '{@link ProjectDto#getCloseTime() <em>closeTime</em>}' feature. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @param newCloseTime
     *            the new value of the '{@link ProjectDto#getCloseTime() closeTime}' feature.
     * @generated
     */
    public void setCloseTime(Date newCloseTime)
    {
        closeTime = newCloseTime;
    }

    /**
     * Returns the value of '<em><b>pauseTime</b></em>' feature. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>pauseTime</b></em>' feature
     * @generated
     */
    public Date getPauseTime()
    {
        return pauseTime;
    }

    /**
     * Sets the '{@link ProjectDto#getPauseTime() <em>pauseTime</em>}' feature. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @param newPauseTime
     *            the new value of the '{@link ProjectDto#getPauseTime() pauseTime}' feature.
     * @generated
     */
    public void setPauseTime(Date newPauseTime)
    {
        pauseTime = newPauseTime;
    }

    public Date getQueryBefStartProjTime()
    {
        return queryBefStartProjTime;
    }

    public void setQueryBefStartProjTime(Date queryBefStartProjTime)
    {
        this.queryBefStartProjTime = queryBefStartProjTime;
    }

    public Date getQueryAftStartProjTime()
    {
        return queryAftStartProjTime;
    }

    public void setQueryAftStartProjTime(Date queryAftStartProjTime)
    {
        this.queryAftStartProjTime = queryAftStartProjTime;
    }

    public Date getQueryBefEndProjTime()
    {
        return queryBefEndProjTime;
    }

    public void setQueryBefEndProjTime(Date queryBefEndProjTime)
    {
        this.queryBefEndProjTime = queryBefEndProjTime;
    }

    public Date getQueryAftEndProjTime()
    {
        return queryAftEndProjTime;
    }

    public void setQueryAftEndProjTime(Date queryAftEndProjTime)
    {
        this.queryAftEndProjTime = queryAftEndProjTime;
    }

    public EpsConfigDto getEpsInfo()
    {
        return epsInfo;
    }

    public void setEpsInfo(EpsConfigDto epsInfo)
    {
        this.epsInfo = epsInfo;
    }

    public String getEpsName()
    {
        return epsName;
    }

    public void setEpsName(String epsName)
    {
        this.epsName = epsName;
    }

    public BusinessConfigDto getPhaseInfo()
    {
        return phaseInfo;
    }

    public void setPhaseInfo(BusinessConfigDto phaseInfo)
    {
        this.phaseInfo = phaseInfo;
    }

    public String getFlowFlag()
    {
        return flowFlag;
    }

    public void setFlowFlag(String flowFlag)
    {
        this.flowFlag = flowFlag;
    }

    public TSUserDto getCreator()
    {
        return creator;
    }

    public void setCreator(TSUserDto creator)
    {
        this.creator = creator;
    }

    /**
     * A toString method which prints the values of all EAttributes of this instance. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String toString()
    {
        return "Project " + " [name: " + getName() + "]" + " [projectNumber: "
               + getProjectNumber() + "]" + " [status: " + getStatus() + "]"
               + " [startProjectTime: " + getStartProjectTime() + "]" + " [endProjectTime: "
               + getEndProjectTime() + "]" + " [projectTimeType: " + getProjectTimeType() + "]"
               + " [projectTemplate: " + getProjectTemplate() + "]" + " [remark: " + getRemark()
               + "]" + " [prevLifeStatus: " + getPrevLifeStatus() + "]" + " [eps: " + getEps()
               + "]" + " [phase: " + getPhase() + "]" + " [process: " + getProcess() + "]"
               + " [isRefuse: " + getIsRefuse() + "]" + " [projectManagers: "
               + getProjectManagers() + "]" + " [projectManagerNames: " + getProjectManagerNames()
               + "]" + " [procInstId: " + getProcInstId() + "]" + " [closeTime: " + getCloseTime()
               + "]" + " [pauseTime: " + getPauseTime() + "]";
    }

}
