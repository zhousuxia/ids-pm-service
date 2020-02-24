package com.glaway.ids.project.plan.dto;

import com.glaway.foundation.common.vdata.GLVData;

/**
 * 关注计划信息DTO
 */
public class ConcernPlanDto extends GLVData {

    private String planId;

    private String userId;

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
