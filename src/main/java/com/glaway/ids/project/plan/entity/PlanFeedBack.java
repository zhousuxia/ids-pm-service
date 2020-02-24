package com.glaway.ids.project.plan.entity;

import com.glaway.foundation.common.entity.GLObject;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Description: 计划反馈
 * @author: sunmeng
 * @ClassName: PlanFeedBack
 * @Date: 2019/10/16-13:51
 * @since
 */
@Entity(name = "PlanFeedBack")
@Table(name = "PM_PLAN_FEEDBACK")
@Data
public class PlanFeedBack extends GLObject {

    //生命周期状态
    private String lifeCycleStatus;

    //权重占比
    private String weightPercent;
}
