package com.glaway.ids.project.plan.service;

import com.glaway.foundation.common.service.CommonService;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.project.plan.entity.PlanExcuteInfo;


public interface PlanExcuteServiceI extends CommonService {

    /**
     * 通过计划ID和项目ID查询主数据
     * @param planId 计划ID
     * @param projId 项目ID
     * @return
     */
    PlanExcuteInfo queryInfoByPlanIdAndProjId(String planId, String projId);

    /**
     * 插入新数据
     * @param planId 计划ID
     * @param projId 项目ID
     * @param taskType 任务类型
     * @param excuteType 执行方式
     * @return
     */
    void saveInfo(String planId, String projId, String taskType, String excuteType);

    /**
     * 通过项目ID查询报表数据
     * @param projId 项目ID
     * @return
     */
    FeignJson queryPlanExcuetReport(String projId);
}
