package com.glaway.ids.project.plan.entity;

import com.glaway.foundation.common.entity.GLObject;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity(name = "PlanExcuteInfo")
@Table(name = "PM_PLAM_EXCUTE_INFO")
public class PlanExcuteInfo extends GLObject {

    //计划主键ID
    private String planId;

    //任务主键ID
    private String projectId;

    //任务类型--计划类型
    private String taskType;

    //执行方式--驳回/接收/委派
    private String excuteType;

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getExcuteType() {
        return excuteType;
    }

    public void setExcuteType(String excuteType) {
        this.excuteType = excuteType;
    }

    @Override
    public String toString() {
        return "PlanExcuteInfo{" + "planId='" + planId + '\'' + ", projectId='" + projectId + '\''
               + ", taskType='" + taskType + '\'' + ", excuteType='" + excuteType + '\'' + '}';
    }
}
