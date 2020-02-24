package com.glaway.ids.project.plan.controller;

import com.glaway.ids.util.Dto2Entity;
import io.swagger.annotations.*;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.core.common.controller.BaseController;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.project.plan.dto.*;
import com.glaway.ids.project.plan.entity.*;
import com.glaway.ids.project.plan.service.PlanChangeServiceI;
import com.glaway.ids.util.CodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by LHR on 2019/8/6.
 */
@Api(tags = {"计划变更接口"})
@RestController
@RequestMapping("/feign/planChangeRestController")
public class PlanChangeRestController extends BaseController {
    @Autowired
    private PlanChangeServiceI planChangeServiceI;

    @ApiOperation(value = "初始化变更计划信息",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "initTemporaryPlanDto")
    public String initTemporaryPlanDto(@RequestBody TemporaryPlanDto temporaryPlan) {
        String planStr = planChangeServiceI.initPlanChange(temporaryPlan);
        return planStr;
    }

    @ApiOperation(value = "保存或新增变更计划信息",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "curUserId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveOrUpdateTemporaryPlan")
    public String saveOrUpdateTemporaryPlan(@RequestBody TemporaryPlanDto temporaryPlan, @RequestParam(value = "curUserId") String curUserId, @RequestParam(value = "orgId") String orgId) {
        TemporaryPlan o = (TemporaryPlan) CodeUtils.JsonBeanToBean(temporaryPlan, TemporaryPlan.class);
        return planChangeServiceI.saveOrUpdateTemporaryPlan(o, curUserId, orgId);
    }

    @ApiOperation(value = "获取变更计划id",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "uploadSuccessPath", value = "附件路径", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "uploadSuccessFileName", value = "附件名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getTemporaryPlanId")
    public String getTemporaryPlanId(@RequestBody TemporaryPlanDto temporaryPlan, @RequestParam(value = "uploadSuccessPath") String uploadSuccessPath,
                                     @RequestParam(value = "uploadSuccessFileName") String uploadSuccessFileName, @RequestParam(value = "userId") String userId, @RequestParam(value = "orgId") String orgId) {
        TemporaryPlan plan = JSON.parseObject(JSON.toJSONString(temporaryPlan), TemporaryPlan.class);
        return planChangeServiceI.getTemporaryPlanId(plan, uploadSuccessPath, uploadSuccessFileName, userId, orgId);
    }

    @ApiOperation(value = "启动计划变更流程",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "startPlanChangeForWork")
    public void startPlanChangeForWork(@RequestBody FeignJson json) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = json.getAttributes();
        List<PlanDto> planList = mapper.convertValue(map.get("planList"), new TypeReference<List<PlanDto>>() {
        });
        List<TempPlanResourceLinkInfoDto> resourceLinkInfoList = mapper.convertValue(map.get("resourceLinkInfoList"), new TypeReference<List<TempPlanResourceLinkInfoDto>>() {
        });
        List<TempPlanInputsDto> inputList = mapper.convertValue(map.get("inputList"), new TypeReference<List<TempPlanInputsDto>>() {
        });
        List<TempPlanDeliverablesInfoDto> deliverablesInfoList = mapper.convertValue(map.get("deliverablesInfoList"), new TypeReference<List<TempPlanDeliverablesInfoDto>>() {
        });
        TSUserDto actor = mapper.convertValue(map.get("actor"), TSUserDto.class);
        String leader = (String) map.get("leader");
        String deptLeader = (String) map.get("deptLeader");
        String changeType = (String) map.get("changeType");
        String temporaryId = (String) map.get("temporaryId");
        String userId = (String) map.get("userId");
        String typeIds = (String) map.get("typeIds");
        planChangeServiceI.startPlanChangeForWork(CodeUtils.JsonListToList(planList, Plan.class), CodeUtils.JsonListToList(resourceLinkInfoList, TempPlanResourceLinkInfo.class),
                CodeUtils.JsonListToList(inputList, TempPlanInputs.class), CodeUtils.JsonListToList(deliverablesInfoList, TempPlanDeliverablesInfo.class), actor, leader, deptLeader, changeType, temporaryId, userId, typeIds);
    }

    @ApiOperation(value = "获取变更计划信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "计划id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getTemporaryPlan")
    public TemporaryPlanDto getTemporaryPlan(@RequestParam(value = "id") String id) {
        return planChangeServiceI.getTemporaryPlan(id);
    }

    @ApiOperation(value = "根据计划id获取历史计划列表信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "useObjectId", value = "计划id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPlanHistoryListByPlanId")
    public List<PlanHistoryDto> getPlanHistoryListByPlanId(@RequestParam("useObjectId") String useObjectId) {
        List<PlanHistory> tempList = planChangeServiceI.getPlanHistoryListByPlanId(useObjectId);
        List<PlanHistoryDto> dtoList = JSON.parseArray(JSON.toJSONString(tempList), PlanHistoryDto.class);
        return dtoList;
    }

    @ApiOperation(value = "根据计划id获取变更计划列表信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "useObjectId", value = "计划id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getTemporaryPlanListByPlanId")
    public List<TemporaryPlanDto> getTemporaryPlanListByPlanId(@RequestParam("useObjectId") String useObjectId) {
        List<TemporaryPlan> list = planChangeServiceI.getTemporaryPlanListByPlanId(useObjectId);
        List<TemporaryPlanDto> dtoList = JSON.parseArray(JSON.toJSONString(list), TemporaryPlanDto.class);
        return dtoList;
    }

    @ApiOperation(value = "根据temporaryPlan条件检索计划",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "rows", value = "每页数量", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "isPage", value = "是否分页", dataType = "boolean")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryTemporaryPlanList")
    public List<TemporaryPlanDto> queryTemporaryPlanList(@RequestBody TemporaryPlanDto temporaryPlanDto, @RequestParam(value = "page") int page, @RequestParam(value = "rows") int rows, @RequestParam(value = "isPage") boolean isPage) {
        TemporaryPlan temporaryPlan = (TemporaryPlan) CodeUtils.JsonBeanToBean(temporaryPlanDto, TemporaryPlan.class);
        List<TemporaryPlan> list = planChangeServiceI.queryTemporaryPlanList(temporaryPlan, page, rows, isPage);
        List<TemporaryPlanDto> dtoList = JSON.parseArray(JSON.toJSONString(list), TemporaryPlanDto.class);
        return dtoList;
    }

    @ApiOperation(value = "查找资源临时信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "rows", value = "每页数量", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "isPage", value = "是否分页", dataType = "boolean")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryTempPlanResourceLinkList")
    public List<TempPlanResourceLinkInfoDto> queryTempPlanResourceLinkList(@RequestBody TempPlanResourceLinkInfoDto tempPlanResourceLinkInfoDto, @RequestParam(value = "page") int page,  @RequestParam(value = "rows") int rows,@RequestParam(value = "isPage") boolean isPage){
        TempPlanResourceLinkInfo tempPlanResourceLinkInfo = new TempPlanResourceLinkInfo();
        Dto2Entity.copyProperties(tempPlanResourceLinkInfoDto,tempPlanResourceLinkInfo);
        List<TempPlanResourceLinkInfo> list = planChangeServiceI.queryTempPlanResourceLinkList(tempPlanResourceLinkInfo,page,rows,isPage);
        List<TempPlanResourceLinkInfoDto> resList = CodeUtils.JsonListToList(list,TempPlanResourceLinkInfoDto.class);
        return resList;
    }
}
