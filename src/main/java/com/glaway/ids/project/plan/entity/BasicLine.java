package com.glaway.ids.project.plan.entity;


import com.glaway.foundation.businessobject.entity.BusinessObject;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.ids.project.menu.entity.Project;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;


/**
 * 基线信息
 */
@Entity(name = "BasicLine")
@Table(name = "PM_BASICLINE")
public class BasicLine extends BusinessObject {

    /**
     * 基线所属项目ID
     * 
     * @generated
     */
    @Basic()
    private String projectId = null;

    /**
     * 基线所属项目
     */
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "projectId", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private Project project;

    /**
     * 名称
     * 
     * @generated
     */
    @Basic()
    private String basicLineName = null;

    /**
     * 流程实例id
     * 
     * @generated
     */
    @Basic()
    private String procInstId = null;

    /**
     * 备注
     * 
     * @generated
     */
    @Basic()
    @Column(length = 2048, name = "")
    private String remark = null;

    /**
     * 创建人
     */
    @Transient
    private TSUserDto createByInfo;

    /**
     * 
     */
    private TSUserDto launcherInfo;

    /** 是否存在流程 0:不存在; 1:存在 */
    @Transient()
    private String flowFlag = null;

    /**
     * <!-- begin-user-doc --> 发起人 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String launcher = "";

    /**
     * <!-- begin-user-doc --> 发起时间 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Temporal(TemporalType.DATE)
    private Date launchTime = null;

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
     * Sets the '{@link BasicLine#getProjectId() <em>projectId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newProjectId
     *            the new value of the '{@link BasicLine#getProjectId() projectId}' feature.
     * @generated
     */
    public void setProjectId(String newProjectId) {
        projectId = newProjectId;
    }

    /**
     * Returns the value of '<em><b>basicLineName</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>basicLineName</b></em>' feature
     * @generated
     */
    public String getBasicLineName() {
        return basicLineName;
    }

    /**
     * Sets the '{@link BasicLine#getBasicLineName() <em>basicLineName</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newBasicLineName
     *            the new value of the '{@link BasicLine#getBasicLineName() basicLineName}'
     *            feature.
     * @generated
     */
    public void setBasicLineName(String newBasicLineName) {
        basicLineName = newBasicLineName;
    }

    /**
     * Returns the value of '<em><b>procInstId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>procInstId</b></em>' feature
     * @generated
     */
    public String getProcInstId() {
        return procInstId;
    }

    /**
     * Sets the '{@link BasicLine#getProcInstId() <em>procInstId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newProcInstId
     *            the new value of the '{@link BasicLine#getProcInstId() procInstId}' feature.
     * @generated
     */
    public void setProcInstId(String newProcInstId) {
        procInstId = newProcInstId;
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
     * Sets the '{@link BasicLine#getRemark() <em>remark</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newRemark
     *            the new value of the '{@link BasicLine#getRemark() remark}' feature.
     * @generated
     */
    public void setRemark(String newRemark) {
        remark = newRemark;
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
     * Sets the '{@link BasicLine#getLauncher() <em>launcher</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newLauncher
     *            the new value of the '{@link BasicLine#getLauncher() launcher}' feature.
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
     * Sets the '{@link BasicLine#getLaunchTime() <em>launchTime</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newLaunchTime
     *            the new value of the '{@link BasicLine#getLaunchTime() launchTime}' feature.
     * @generated
     */
    public void setLaunchTime(Date newLaunchTime) {
        launchTime = newLaunchTime;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public TSUserDto getCreateByInfo() {
        return createByInfo;
    }

    public void setCreateByInfo(TSUserDto createByInfo) {
        this.createByInfo = createByInfo;
    }

    public TSUserDto getLauncherInfo() {
        return launcherInfo;
    }

    public void setLauncherInfo(TSUserDto launcherInfo) {
        this.launcherInfo = launcherInfo;
    }

    public String getFlowFlag() {
        return flowFlag;
    }

    public void setFlowFlag(String flowFlag) {
        this.flowFlag = flowFlag;
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
        return "BasicLine " + " [projectId: " + getProjectId() + "]" + " [basicLineName: "
               + getBasicLineName() + "]" + " [procInstId: " + getProcInstId() + "]"
               + " [remark: " + getRemark() + "]" + " [launcher: " + getLauncher() + "]"
               + " [launchTime: " + getLaunchTime() + "]";
    }
}
