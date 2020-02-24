package com.glaway.ids.project.plan.service;


import java.util.List;
import java.util.Map;

import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.service.CommonService;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.project.plan.dto.ChangeFlowTaskCellConnectVo;
import com.glaway.ids.project.plan.dto.FlowTaskCellConnectVo;
import com.glaway.ids.project.plan.dto.FlowTaskOutChangeVO;
import com.glaway.ids.project.plan.dto.FlowTaskParentVo;
import com.glaway.ids.project.plan.dto.FlowTaskPreposeVo;
import com.glaway.ids.project.plan.dto.FlowTaskVo;
import com.glaway.ids.project.plan.entity.Inputs;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.entity.PlanownerApplychangeInfo;


/**
 * 应用研发流程模板接口
 * 
 * @author wqb
 * @version 2019年10月20日 09:32:38
 * @see ApplyProcTemplateServiceI
 */


public interface ApplyProcTemplateServiceI extends CommonService {
   
    /**
     * 调用研发流程模板分解任务/计划
     * 
     * @param parentId
     *            任务/计划ID
     * @param templateId
     *            研发流程模板ID
     * @return
     * @see
     */
    boolean templateResolveForPlan(String parentId, String templateId,String currentId);

    /**
     * 刷新研发流程模板信息
     * @param flowResolveXml
     * @param cellWorkTimeMap
     * @return
     */
    String refreshFlowResolveXml(String flowResolveXml, Map<String, String> cellWorkTimeMap);

}
