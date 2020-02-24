/*
 * 文件名：ReviewBaseInfoVo.java
 * 版权：Copyright by www.glaway.com
 * 描述：
 * 修改人：SYC
 * 修改时间：2015年12月17日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.glaway.ids.review.vo;

import java.util.Date;



public class ReviewBaseInfoVo {
    
    /**
     * 状态 
     * 
     */
    private String bizCurrent;
    
    /**
     * 评审名称 
     * 
     */
    private String name = null;

    /**
     * 任务ID 
     * 
     */
    private String taskId = null;


    /**
     * 所属项目ID 
     * 
     */
    private String projectId = null;
    
    /**
     * 项目名称
     */
    private String projectName=null;


    /**
     * 组织者ID
     * 
     */
    private String organizerId = null;




    /**
     * 申请截止时间
     */
    private Date applyOverTime = null;
    
    /**
     * 计划完成时间
     */
    private Date planEndTime = null;

    /**
     * 申请人ID
     * 
     */
    private String applicantId = null;

    /**
     * 申请人姓名 
     */
    private String applicantName = null;


    /**
     * 申请时间
     */
    private Date applyTime = null;
    
    /**
     * 评审类型
     */
    private String reviewType = null;
    
    
    
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getBizCurrent() {
        return bizCurrent;
    }

    public void setBizCurrent(String bizCurrent) {
        this.bizCurrent = bizCurrent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    public Date getApplyOverTime() {
        return applyOverTime;
    }

    public void setApplyOverTime(Date applyOverTime) {
        this.applyOverTime = applyOverTime;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getReviewType() {
        return reviewType;
    }

    public void setReviewType(String reviewType) {
        this.reviewType = reviewType;
    }

    public Date getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(Date planEndTime) {
        this.planEndTime = planEndTime;
    }
    
    
    
}
