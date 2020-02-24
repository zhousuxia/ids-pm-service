package com.glaway.ids.project.plan.fallback;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.config.dto.RDTaskVO;
import com.glaway.ids.project.plan.dto.FlowTaskCellConnectVo;
import com.glaway.ids.project.plan.dto.FlowTaskParentVo;
import com.glaway.ids.project.plan.dto.InputsDto;
import com.glaway.ids.project.plan.dto.TaskCellBasicInfoVO;
import com.glaway.ids.project.plan.dto.TaskCellDeliverItemVO;
import com.glaway.ids.project.plan.dto.TaskProcTemCellConnectVO;
import com.glaway.ids.project.plan.dto.TaskProcTemplateDto;
import com.glaway.ids.project.plan.dto.TaskProcTemplateVo;
import com.glaway.ids.project.plan.service.RdFlowTaskFlowResolveRemoteFeignServiceI;

import feign.hystrix.FallbackFactory;

@Component
public class RdFlowTaskFlowResolveRemoteFeignServiceCallBack implements FallbackFactory<RdFlowTaskFlowResolveRemoteFeignServiceI> {

    @Override
    public RdFlowTaskFlowResolveRemoteFeignServiceI create(Throwable cause) {
        return new RdFlowTaskFlowResolveRemoteFeignServiceI(){

            @Override
            public List<TaskProcTemplateVo> getProcTemplateAllList(String nameValue,
                                                                   String pageValue,
                                                                   String rowsKey) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public RDTaskVO getXmlbyPlanId(String planId) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void doDeloutputByPlanOutputId(String id) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public FeignJson isHaveLinkPlanId(String parentPlanId) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void getSaveRdTaskInfo(RDTaskVO vo, String templateId, String outUserId,
                                          String approveType, String procInstId, String formId,
                                          String planMapStr, String inputMapStr,
                                          String outputMapStr) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void getDoUpdateXml(String in, String userId, String parentPlanId,
                                       String cellIds, String cellContact) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public String saveFlowTask1(Map<String, Object> paramMap) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void getSaveTaskInfoFromPlan1(String userId, String parentPlanId,
                                                 String planName, String owner, String remark,
                                                 String planId, String workTime, String cellId) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public FeignJson getParentPlanIdByFlowTaskId(String flowTaskId) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public FeignJson getPlanIdByFlowTaskParentId(String flowTaskParentId) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void saveRdfInputByPlan(String cellAndNameInputStr, String parentPlanId,
                                           String cellId, String userId) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public FeignJson getFlowTaskDeliverablesInfoInitBusinessObject() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void saveFlowTaskCellConns(String cellContact, String parentPlanId) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public List<FlowTaskCellConnectVo> queryFlowTaskCellConnectVoList(String parentPlanId,
                                                                              String cellId) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setInputsAddInnerTask(InputsDto currentinputs, String currentUserId) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void setInputsPlanLink(InputsDto currentinputs, String currentUserId) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public TaskProcTemplateDto getProcTemplateEntity(String id) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public List<TaskCellBasicInfoVO> getTasKCellBaseInfoByTemplateId(String templateId) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public List<TaskCellDeliverItemVO> getTemplateOutputs(String templateId) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public List<TaskCellDeliverItemVO> getTemplateInputs(String templateId) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public List<TaskProcTemCellConnectVO> findTaskProcTemCellConnList(String templateId) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public FeignJson getSwitchfromRdFlow(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void saveRdfDeliverByPlan(String parentPlanId, String cellId, String name,
                                             String userId, String outputId) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void setInputsAddOutInputTask(InputsDto dto, String userId) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public FeignJson getMaxWorkTimeByTemp(String templateId) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void saveTaskFeedbackByPlanInfo(String rdTaskId, String progressrate, String progressrateremark, String filepathp, String checkP, String userId, String type) {

            }

            @Override
            public void saveRdTaskFeedBackInfoByPlanTaskFeedBackInfo(String planId,
                                                                     String procInstId,
                                                                     String bizCurrent,
                                                                     TSUserDto user,
                                                                     String leader) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void saveRdTaskFeedBackInfoByPlanTaskFeedBackGoBackInfo(String planId, String userId) {

            }
            
            @Override
            public FeignJson getDeliverablesInfoInitBusinessObject() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public FeignJson getResourceInitBusinessObject() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void delFlowTaskInfoByFlowTaskParentId(String flowTaskParentId, String formId) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public FeignJson startFlowTaskChangeProcessForPlan(String leader, String deptLeader,
                                                               String formId, String userId,
                                                               String type) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public FlowTaskParentVo getFlowTaskParentVoByFormId(String formId) {
                // TODO Auto-generated method stub
                return null;
            }
        };
    }

}

