package com.glaway.ids.project.plan.service;


import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.common.constant.FeignConstants;
import com.glaway.ids.config.dto.RDTaskVO;
import com.glaway.ids.project.plan.dto.FlowTaskCellConnectVo;
import com.glaway.ids.project.plan.dto.FlowTaskParentVo;
import com.glaway.ids.project.plan.dto.InputsDto;
import com.glaway.ids.project.plan.dto.TaskCellBasicInfoVO;
import com.glaway.ids.project.plan.dto.TaskCellDeliverItemVO;
import com.glaway.ids.project.plan.dto.TaskProcTemCellConnectVO;
import com.glaway.ids.project.plan.dto.TaskProcTemplateDto;
import com.glaway.ids.project.plan.dto.TaskProcTemplateVo;
import com.glaway.ids.project.plan.fallback.RdFlowTaskFlowResolveRemoteFeignServiceCallBack;


/**
 * 分解
 * 
 * @author wqb
 * @version 2019年7月29日 21:43:16
 * @see RdFlowTaskFlowResolveRemoteFeignServiceI
 * @since
 */

@FeignClient(value = FeignConstants.ID_RDFLOW_SERVICE,fallback = RdFlowTaskFlowResolveRemoteFeignServiceCallBack.class)
public interface RdFlowTaskFlowResolveRemoteFeignServiceI {
    
    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/procTemplateRestController/getProcTemplateAllList.do")
    List<TaskProcTemplateVo> getProcTemplateAllList(@RequestParam(value = "nameValue",required = false) String nameValue
        ,@RequestParam(value = "nameValue",required = false) String pageValue,@RequestParam(value = "nameValue",required = false) String rowsKey);
    
    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/getXmlbyPlanId.do")
    RDTaskVO getXmlbyPlanId(@RequestParam(value = "planId",required = false) String planId);
        
    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/doDeloutputByPlanOutputId.do")
    void doDeloutputByPlanOutputId(@RequestParam(value = "id",required = false) String id);
    
    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/isHaveLinkPlanId.do")
    FeignJson isHaveLinkPlanId(@RequestParam(value = "parentPlanId",required = false) String parentPlanId);
    
    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/getSaveRdTaskInfo.do")
    void getSaveRdTaskInfo(@RequestBody RDTaskVO vo,@RequestParam(value = "templateId",required = false) String templateId,@RequestParam(value = "outUserId",required = false) String outUserId,@RequestParam(value = "approveType",required = false) String approveType,
                           @RequestParam(value = "procInstId",required = false) String procInstId , @RequestParam(value = "formId",required = false) String formId,@RequestParam(value = "planMapStr",required = false) String planMapStr
                           ,@RequestParam(value = "inputMapStr",required = false) String inputMapStr,@RequestParam(value = "outputMapStr",required = false) String outputMapStr);
    
    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/getDoUpdateXml.do")
    void getDoUpdateXml(@RequestParam(value = "in") String in,@RequestParam(value = "userId") String userId, @RequestParam(value = "parentPlanId") String parentPlanId
                        , @RequestParam(value = "cellIds") String cellIds,@RequestParam(value = "cellContact") String cellContact);
    
    /**
     * 保存流程任务基本信息
     * 事物重构
     * @param paramMap    
     */
    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/saveFlowTask1.do")
    String saveFlowTask1(@RequestBody Map<String, Object> paramMap);
    
    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/getSaveTaskInfoFromPlan1.do")
    void getSaveTaskInfoFromPlan1(@RequestParam(value = "userId") String userId,@RequestParam(value = "parentPlanId") String parentPlanId,@RequestParam(value = "planName") String planName,
                                  @RequestParam(value = "owner") String owner,@RequestParam(value = "remark") String remark,
                                  @RequestParam(value = "planId") String planId,@RequestParam(value = "workTime") String workTime,@RequestParam(value = "cellId") String cellId);
    
    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/getParentPlanIdByFlowTaskId.do")
    FeignJson getParentPlanIdByFlowTaskId(@RequestParam(value = "flowTaskId") String flowTaskId);
    
    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/flowTaskParentId.do")
    FeignJson getPlanIdByFlowTaskParentId(@RequestParam(value = "flowTaskParentId") String flowTaskParentId);
    
    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/saveRdfInputByPlan.do")
    void saveRdfInputByPlan(@RequestParam(value = "cellAndNameInputStr") String cellAndNameInputStr,@RequestParam(value = "parentPlanId") String parentPlanId,
                            @RequestParam(value = "cellId") String cellId, @RequestParam(value = "userId")String userId);

    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/getFlowTaskDeliverablesInfoInitBusinessObject.do")
    FeignJson getFlowTaskDeliverablesInfoInitBusinessObject();

    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/saveFlowTaskCellConns.do")
    void saveFlowTaskCellConns(@RequestParam(value = "cellContact") String cellContact, @RequestParam(value = "parentPlanId") String parentPlanId);

    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/queryFlowTaskCellConnectVoList.do")
    List<FlowTaskCellConnectVo> queryFlowTaskCellConnectVoList(@RequestParam(value = "parentPlanId") String parentPlanId ,@RequestParam(value = "cellId") String cellId);

    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/setInputsAddInnerTask.do")
    void setInputsAddInnerTask(@RequestBody InputsDto currentinputs, @RequestParam(value = "currentUserId") String currentUserId);
    
    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/setInputsPlanLink.do")
    void setInputsPlanLink(@RequestBody InputsDto currentinputs, @RequestParam(value = "currentUserId") String currentUserId);
    
    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/getProcTemplateEntity.do")
    TaskProcTemplateDto getProcTemplateEntity(@RequestParam(value = "id",required = false) String id);
    
    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/getTasKCellBaseInfoByTemplateId.do")
    List<TaskCellBasicInfoVO> getTasKCellBaseInfoByTemplateId(@RequestParam(value = "templateId") String templateId);
    
    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/getTemplateOutputs.do")
    List<TaskCellDeliverItemVO> getTemplateOutputs(@RequestParam(value = "templateId") String templateId);
    
    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/getTemplateInputs.do")
    List<TaskCellDeliverItemVO> getTemplateInputs(@RequestParam(value = "templateId") String templateId);
    
    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/findTaskProcTemCellConnList.do")
    List<TaskProcTemCellConnectVO> findTaskProcTemCellConnList(@RequestParam(value = "templateId") String templateId);
    
    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/getSwitchfromRdFlow.do")
    FeignJson getSwitchfromRdFlow(@RequestParam(value = "name") String name);
    
    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/saveRdfDeliverByPlan.do")
    void saveRdfDeliverByPlan(@RequestParam(value = "parentPlanId") String parentPlanId,@RequestParam(value = "cellId") String cellId
                              , @RequestParam(value = "name") String name, @RequestParam(value = "userId") String userId,@RequestParam(value = "outputId") String outputId);
    
    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/setInputsAddOutInputTask.do")
    void setInputsAddOutInputTask(@RequestBody InputsDto dto,@RequestParam(value = "userId") String userId);
    
    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/getMaxWorkTimeByTemp.do")
    FeignJson getMaxWorkTimeByTemp(@RequestParam(value = "templateId") String templateId);

    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdTaskFeedbackRestController/saveTaskFeedbackByPlanInfo.do")
    void saveTaskFeedbackByPlanInfo(@RequestParam("rdTaskId")String rdTaskId,@RequestParam("progressrate")String progressrate,
                                    @RequestParam("progressrateremark")String progressrateremark,@RequestParam("filepathp")String filepathp,
                                    @RequestParam("checkP")String checkP,@RequestParam("userId")String userId,@RequestParam("type")String type);
    
    /**完工反馈相关信息同步到任务
     * @param planId
     * @param procInstId
     * @param bizCurrent
     * @param user
     * @param leader
     */
    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdTaskFeedbackRestController/saveRdTaskFeedBackInfoByPlanTaskFeedBackInfo.do")
    void saveRdTaskFeedBackInfoByPlanTaskFeedBackInfo(@RequestParam("planId") String planId,@RequestParam("procInstId") String procInstId
                                                      , @RequestParam("bizCurrent") String bizCurrent,@RequestBody TSUserDto user,@RequestParam("leader") String leader);

    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdTaskFeedbackRestController/saveRdTaskFeedBackInfoByPlanTaskFeedBackGoBackInfo.do")
    void saveRdTaskFeedBackInfoByPlanTaskFeedBackGoBackInfo(@RequestParam(value = "planId") String planId, @RequestParam(value = "userId") String userId);

    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdTaskDeliverablesInfoRestController/getDeliverablesInfoInitBusinessObject.do")
    FeignJson getDeliverablesInfoInitBusinessObject();

    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/getResourceInitBusinessObject.do")
    FeignJson getResourceInitBusinessObject();

    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/delFlowTaskInfoByFlowTaskParentId.do")
    void delFlowTaskInfoByFlowTaskParentId(@RequestParam("flowTaskParentId") String flowTaskParentId, @RequestParam("formId") String formId);

    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/startFlowTaskChangeProcessForPlan.do")
    FeignJson startFlowTaskChangeProcessForPlan(@RequestParam("leader") String leader, @RequestParam("deptLeader") String deptLeader, @RequestParam("formId") String formId,
                                                @RequestParam("userId") String userId, @RequestParam("type") String type);

    @RequestMapping(value = FeignConstants.IDS_RDFLOW_FEIGN_SERVICE+"/feign/rdfTaskFlowResolveRestController/getFlowTaskParentVoByFormId.do")
    FlowTaskParentVo getFlowTaskParentVoByFormId(@RequestParam("formId") String formId);
}
