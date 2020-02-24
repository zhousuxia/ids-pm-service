package com.glaway.ids.project.plan.controller;

import io.swagger.annotations.*;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.util.VDataUtil;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.project.plan.dto.*;
import com.glaway.ids.project.plan.entity.*;
import com.glaway.ids.project.plan.service.PlanFlowForworkServiceI;
import com.glaway.ids.util.CodeUtils;
import com.glaway.ids.util.Dto2Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @Description: 计划流程处理接口
 * @author: sunmeng
 * @ClassName: PlanFlowForworkRestController
 * @Date: 2019/8/6-20:07
 * @since
 */
@Api(tags = {"计划流程处理接口"})
@RestController
@RequestMapping("/feign/planFlowForworkRestController")
public class PlanFlowForworkRestController {
    @Autowired
    private PlanFlowForworkServiceI planFlowForworkService;

    @ApiOperation(value = "修改资源事务",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "useRates", value = "useRates", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endTimes", value = "endTimes", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "startTimes", value = "startTimes", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping("/modifyResourceMassForWork")
    public FeignJson modifyResourceMassForWork(@RequestBody List<ResourceLinkInfoDto> resourceLst, @RequestParam(value = "useRates", required = false) String useRates,
                                               @RequestParam(value = "endTimes", required = false) String endTimes, @RequestParam(value = "startTimes", required = false) String startTimes) {
        FeignJson j = new FeignJson();
        List<ResourceLinkInfo> list = JSON.parseArray(JSON.toJSONString(resourceLst), ResourceLinkInfo.class);
        String[] useRate = useRates.split(",");
        String[] startTime = startTimes.split(",");
        String[] endTime = endTimes.split(",");
        try {
            planFlowForworkService.modifyResourceMassForWork(list, useRate, endTime, startTime);
        } catch (ParseException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ApiOperation(value = "批量删除资源",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "资源ids", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping("/doBatchDel")
    public FeignJson doBatchDel(@RequestParam(value = "ids", required = false) String ids) {
        return planFlowForworkService.doBatchDel(ids);
    }

    @ApiOperation(value = "撤销分解删除拟制中的子计划事务",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping("/deleteChildPlan")
    public void deleteChildPlan(@RequestBody PlanDto dto, @RequestParam(value = "userId", required = false) String userId) {
        Plan plan = new Plan();
        Dto2Entity.copyProperties(dto, plan);
        planFlowForworkService.deleteChildPlan(plan, userId);
    }

    @ApiOperation(value = "获取变更计划列表",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping("/getTempPlanListForWork")
    public List<TemporaryPlanDto> getTempPlanListForWork(@RequestBody FeignJson feignJson) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = feignJson.getAttributes();
        TemporaryPlanDto temporaryPlan = mapper.convertValue(map.get("temporaryPlan"), TemporaryPlanDto.class);
        List<PlanDto> planChangeList = mapper.convertValue(map.get("planChangeList"), new TypeReference<List<PlanDto>>() {
        });
        String userId = (String) map.get("userId");
        TemporaryPlan o = (TemporaryPlan) CodeUtils.JsonBeanToBean(temporaryPlan, TemporaryPlan.class);
        List<Plan> list = CodeUtils.JsonListToList(planChangeList, Plan.class);
        List<TemporaryPlan> tempPlanListForWork = planFlowForworkService.getTempPlanListForWork(o, list, userId);
        List<TemporaryPlanDto> list1 = null;
        try {
            list1 = (List<TemporaryPlanDto>) VDataUtil.getVDataByEntity(tempPlanListForWork, TemporaryPlanDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list1;
    }

    @ApiOperation(value = "启动计划批量变更流程",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping("/startPlanChangeMassForWork")
    public void startPlanChangeMassForWork(@RequestBody FeignJson feignJson) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = feignJson.getAttributes();
        List<TemporaryPlanDto> tempPlanList = mapper.convertValue(map.get("tempPlanList"), new TypeReference<List<TemporaryPlanDto>>() {
        });
        List<TempPlanResourceLinkInfoDto> resourceLinkInfoList = mapper.convertValue(map.get("resourceLinkInfoList"), new TypeReference<List<TempPlanResourceLinkInfoDto>>() {
        });
        TSUserDto actor = mapper.convertValue(map.get("actor"), TSUserDto.class);
        String leader = (String) map.get("leader");
        String deptLeader = (String) map.get("deptLeader");
        String changeType = (String) map.get("changeType");
        String userId = (String) map.get("userId");
        List<TemporaryPlan> list = CodeUtils.JsonListToList(tempPlanList, TemporaryPlan.class);
        List<TempPlanResourceLinkInfo> list2 = CodeUtils.JsonListToList(resourceLinkInfoList, TempPlanResourceLinkInfo.class);
        planFlowForworkService.startPlanChangeMassForWork(list, list2, actor, leader, deptLeader, changeType, userId);
    }

    @ApiOperation(value = "保存计划批量修改事务",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planStr", value = "计划信息", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping("/saveModifyListForWork")
    public FeignJson saveModifyListForWork(@RequestBody FeignJson feignJson) {
        String planStr = String.valueOf(feignJson.getObj());
        List<Object> changePlanList = (List<Object>) JSON.parseArray(planStr);
        /*  ObjectMapper mapper = new ObjectMapper();*/
        FeignJson j = new FeignJson();
        try {
            /*List<Object> resList = mapper.convertValue(planList,new TypeReference<List<Object>>(){});*/
            planFlowForworkService.saveModifyListForWork(changePlanList);
        } catch (ParseException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ApiOperation(value = "基线复制",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "基线ids", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "basicLineIdForCopy", value = "复制基线id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping("/copyBasicLineForWork")
    public void copyBasicLineForWork(@RequestBody BasicLineDto basicLine, @RequestParam(value = "ids") String ids, @RequestParam(value = "projectId") String projectId, @RequestParam(value = "basicLineIdForCopy") String basicLineIdForCopy) {
        BasicLine o = (BasicLine) CodeUtils.JsonBeanToBean(basicLine, BasicLine.class);
        planFlowForworkService.copyBasicLineForWork(o, ids, projectId, basicLineIdForCopy);
    }

    @ApiOperation(value = "新增交付物事务",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "names", value = "交付项名称", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping("/doAddDelDeliverForWork")
    public FeignJson doAddDelDeliverForWork(@RequestParam("names") String names, @RequestBody DeliverablesInfoDto deliverablesInfoDto) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            DeliverablesInfo deliverablesInfo = new DeliverablesInfo();
            Dto2Entity.copyProperties(deliverablesInfoDto, deliverablesInfo);
            planFlowForworkService.doAddDelDeliverForWork(names, deliverablesInfo);
            j.setObj(deliverablesInfo.getId());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "批量新增继承的交付物事务",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "names", value = "交付项名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "planIdForInherit", value = "计划id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "useObjectType", value = "类型", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping("/doAddInheritlistForTemplate")
    public FeignJson doAddInheritlistForTemplate(@RequestParam("names") String names, @RequestParam("planIdForInherit") String planIdForInherit,
                                                 @RequestParam("useObjectType") String useObjectType, @RequestBody TSUserDto userDto, @RequestParam("orgId") String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            planFlowForworkService.doAddInheritlistForTemplate(names, planIdForInherit, useObjectType, userDto, orgId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "新增继承的交付物事务",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "names", value = "交付项名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "planIdForInherit", value = "计划id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping("/doAddInheritlist")
    public FeignJson doAddInheritlist(@RequestParam(value = "names") String names, @RequestParam(value = "planIdForInherit") String planIdForInherit,
                                      @RequestBody TSUserDto curUser, @RequestParam(value = "orgId") String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            planFlowForworkService.doAddInheritlist(names, planIdForInherit, curUser, orgId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "根据任务id获取计划变更流程id",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "formId", value = "任务id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping("/getChangeTaskIdByFormId")
    public String getChangeTaskIdByFormId(@RequestParam(value = "formId") String formId) {
        return planFlowForworkService.getChangeTaskIdByFormId(formId);
    }

    @ApiOperation(value = "根据项目id获取资源列表",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping("/getResourceLinkInfosByProject")
    public List<ResourceLinkInfoDto> getResourceLinkInfosByProject(@RequestParam(value = "projectId") String projectId) {
        List<ResourceLinkInfo> list = planFlowForworkService.getResourceLinkInfosByProject(projectId);
        List<ResourceLinkInfoDto> list1 = CodeUtils.JsonListToList(list, ResourceLinkInfoDto.class);
        return list1;
    }

    @ApiOperation(value = "继续计划批量变更流程",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping("/startPlanChangeFlowForWork")
    public void startPlanChangeFlowForWork(@RequestBody FeignJson feignJson) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = feignJson.getAttributes();
        List<TempPlanDeliverablesInfoDto> deliverablesInfoList = mapper.convertValue(map.get("deliverablesInfoList"), new TypeReference<List<TempPlanDeliverablesInfoDto>>() {
        });
        List<TempPlanResourceLinkInfoDto> resourceLinkInfoList = mapper.convertValue(map.get("resourceLinkInfoList"), new TypeReference<List<TempPlanResourceLinkInfoDto>>() {
        });
        List<TempPlanInputsDto> inputList = mapper.convertValue(map.get("inputList"), new TypeReference<List<TempPlanInputsDto>>() {
        });
        String formId = (String) map.get("formId");
        String taskId = (String) map.get("taskId");
        String temporaryId = (String) map.get("temporaryId");
        String leader = (String) map.get("leader");
        String deptLeader = (String) map.get("deptLeader");
        String userId = (String) map.get("userId");
        planFlowForworkService.startPlanChangeFlowForWork(formId, taskId, temporaryId, CodeUtils.JsonListToList(resourceLinkInfoList, TempPlanResourceLinkInfo.class), CodeUtils.JsonListToList(deliverablesInfoList, TempPlanDeliverablesInfo.class), CodeUtils.JsonListToList(inputList, TempPlanInputs.class), leader, deptLeader, userId);
    }
}
