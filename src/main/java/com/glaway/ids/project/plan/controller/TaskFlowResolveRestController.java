package com.glaway.ids.project.plan.controller;

import io.swagger.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.OperationLog;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.core.common.controller.BaseController;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.ids.project.plan.dto.ChangeFlowTaskCellConnectVo;
import com.glaway.ids.project.plan.dto.FlowTaskOutChangeVO;
import com.glaway.ids.project.plan.dto.FlowTaskParentVo;
import com.glaway.ids.project.plan.dto.FlowTaskPreposeVo;
import com.glaway.ids.project.plan.dto.FlowTaskVo;
import com.glaway.ids.project.plan.dto.InputsDto;
import com.glaway.ids.project.plan.dto.PlanDto;
import com.glaway.ids.project.plan.dto.PlanownerApplychangeInfoDto;
import com.glaway.ids.project.plan.dto.PreposePlanDto;
import com.glaway.ids.project.plan.entity.Inputs;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.entity.PlanownerApplychangeInfo;
import com.glaway.ids.project.plan.entity.PreposePlan;
import com.glaway.ids.project.plan.service.PlanFlowForworkServiceI;
import com.glaway.ids.project.plan.service.PreposePlanServiceI;
import com.glaway.ids.project.plan.service.TaskFlowResolveServiceI;
import com.glaway.ids.util.Dto2Entity;

/**
 * @author wqb
 * @version V1.0
 * @Title: Controller
 * @Description: 分解计划
 * @date 2019年7月29日 19:53:49
 */
@Api(tags = {"计划分解接口"})
@RestController
@RequestMapping("/feign/taskFlowResolveRestController")
public class TaskFlowResolveRestController extends BaseController {
    /**
     * Logger for this class
     */
    // private static final Logger logger = Logger.getLogger(DeliverablesInfoController.class);
    private static final OperationLog log = BaseLogFactory.getOperationLog(TaskFlowResolveRestController.class);
    /**
     * 前置计划Service
     */
    @Autowired
    private PreposePlanServiceI preposePlanService;
    /**
     * 前置计划taskFlowResolveService
     */
    @Autowired
    private TaskFlowResolveServiceI taskFlowResolveService;
    /**
     * 前置计划taskFlowResolveService
     */
    @Autowired
    private PlanFlowForworkServiceI planFlowForworkService;
    /**
     * 用户管理接口
     */
    @Autowired
    private FeignUserService userService;

    @ApiOperation(value = "获取前置计划根据计划id",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPreposePlansByPlanId")
    public List<PreposePlanDto> getPreposePlansByPlanId(@RequestBody PlanDto dto) {
        Plan inp = new Plan();
        Dto2Entity.copyProperties(dto, inp);
        List<PreposePlan> list = preposePlanService.getPreposePlansByPlanId(inp);
        List<PreposePlanDto> resList = JSON.parseArray(JSON.toJSONString(list), PreposePlanDto.class);
        return resList;
    }

    @ApiOperation(value = "获取变更信息对象中的信息集合",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "当前用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getChangeFlowTaskList")
    List<FlowTaskVo> getChangeFlowTaskList(@RequestBody FlowTaskParentVo flowTaskParent, @RequestParam(value = "userId", required = false) String userId) {
        return taskFlowResolveService.getChangeFlowTaskList(flowTaskParent, userId);
    }

    /**
     * 获取parent计划的子流程任务节点的前后置关系，并将其转换为ChangeFlowTaskCellConnect
     *
     * @param parent
     * @return
     * @throws GWException
     * @see
     */
    @ApiOperation(value = "获取流程变更的流程节点线信息",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getChangeFlowTaskConnectList")
    List<ChangeFlowTaskCellConnectVo> getChangeFlowTaskConnectList(@RequestBody FlowTaskParentVo parent) {
        return taskFlowResolveService.getChangeFlowTaskConnectList(parent);
    }

    /**
     * 更新流程变更编辑后的节点信息
     * @param dto
     * @param cellIds
     * @param parentPlanId
     * @param cellContact
     * @param userId
     */
    @ApiOperation(value = "更新流程变更编辑后的节点信息",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "cellIds", value = "节点id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "parentPlanId", value = "父计划id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "cellContact", value = "节点链接线", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "当前用户Id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "updateFlowTasks")
    void updateFlowTasks(@RequestBody PlanDto dto, @RequestParam(value = "cellIds", required = false) String cellIds, @RequestParam(value = "parentPlanId", required = false) String parentPlanId
            , @RequestParam(value = "cellContact", required = false) String cellContact, @RequestParam(value = "userId", required = false) String userId) {
        Plan temp = new Plan();
        Dto2Entity.copyProperties(dto, temp);
        taskFlowResolveService.updateFlowTasks(temp, cellIds, parentPlanId, cellContact, userId);
    }

    @ApiOperation(value = "保存应用研发流程模板后的节点信息",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "currentUserId", value = "当前用户Id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveFlowTask2")
    FeignJson saveFlowTask2(@RequestBody Map<String, Object> params, @RequestParam(value = "currentUserId", required = false) String currentUserId) {
        FeignJson feign = new FeignJson();
        Object planObj = params.get("plan");
        Plan temp = new Plan();
        if (!CommonUtil.isEmpty(planObj)) {
            temp = JSON.parseObject(JSON.toJSONString(planObj), new TypeReference<Plan>() {
            });
        }
        Object parentObj = params.get("parent");
        Plan tempParent = new Plan();
        if (!CommonUtil.isEmpty(parentObj)) {
            tempParent = JSON.parseObject(JSON.toJSONString(parentObj), new TypeReference<Plan>() {
            });
        }
        Object createOrgIdObj = params.get("createOrgId");
        if (!CommonUtil.isEmpty(createOrgIdObj)) {
            temp.setCreateOrgId(createOrgIdObj.toString());
        }
        String curId = taskFlowResolveService.saveFlowTask2(temp, tempParent, currentUserId);
        feign.setObj(curId);
        return feign;
    }

    @ApiOperation(value = "流程分解中的节点输入新增",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "计划id的String集合", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentUserId", value = "当前用户Id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doAddInputsNew")
    void doAddInputsNew(@RequestParam(value = "ids", required = false) String ids, @RequestBody InputsDto dto,
                        @RequestParam(value = "currentUserId", required = false) String currentUserId) {
        Inputs temp = new Inputs();
        Dto2Entity.copyProperties(dto, temp);
        taskFlowResolveService.doAddInputsNew(ids, temp, currentUserId);
    }

    /**
     * 删除输入事务
     *
     * @param ids
     */
    @ApiOperation(value = "流程分解中的节点输入删除",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "流程分解输入id的String集合", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doBatchDelInputsForWork")
    void doBatchDelInputsForWork(@RequestParam(value = "ids", required = false) String ids) {
        taskFlowResolveService.doBatchDelInputsForWork(ids);
    }

    /**
     * 新增交付物-继承父项目输出
     * 事物重构
     */
    @ApiOperation(value = "流程分解中的节点输出继承父项",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "names", value = "名称", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doAddInheritDocument")
    void doAddInheritDocument(@RequestParam(value = "names", required = false) String names, @RequestBody PlanDto temp) {
        Plan plan = new Plan();
        Dto2Entity.copyProperties(temp, plan);
        taskFlowResolveService.doAddInheritDocument(names, plan);
    }

    /**
     * 流程分解中的节点输出新增交付物
     * 事物重构
     */
    @ApiOperation(value = "流程分解中的节点输出新增交付物",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "names", value = "名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "当前用户Id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doAdd")
    void doAdd(@RequestParam(value = "type", required = false) String type, @RequestParam(value = "names", required = false) String names
            , @RequestParam(value = "userId", required = false) String userId, @RequestBody List<PlanDto> dtoList) {
        List<Plan> childList = new ArrayList<Plan>();
        childList = JSON.parseObject(JSON.toJSONString(dtoList), new TypeReference<List<Plan>>() {
        });
        taskFlowResolveService.doAdd(type, names, userId, childList);
    }

    /**
     * 流程分解中的节点输入批量新增
     * @param ids
     * @param temp
     */
    @ApiOperation(value = "流程分解中的节点输入批量新增",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "流程分解前置输出id的String集合", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doAddInputs")
    void doAddInputs(@RequestParam(value = "ids", required = false) String ids, @RequestBody InputsDto temp) {
        Inputs inputs = new Inputs();
        Dto2Entity.copyProperties(temp, inputs);
        taskFlowResolveService.doAddInputs(ids, inputs);
    }

    /**
     * 节点基本信息数据保存
     */
    @ApiOperation(value = "节点基本信息数据保存",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "fromTemplate", value = "是否来源于研发流程模板", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "planStartTime", value = "计划开始时间", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "planEndTime", value = "计划结束时间", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "workTime", value = "工期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "nameChange", value = "名称", dataType = "boolean")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doBatchSaveBasicInfo")
    void doBatchSaveBasicInfo(@RequestParam(value = "fromTemplate", required = false) String fromTemplate, @RequestBody PlanDto task, @RequestParam(value = "planStartTime", required = false) String planStartTime,
                              @RequestParam(value = "planEndTime", required = false) String planEndTime, @RequestParam(value = "workTime", required = false) String workTime
            , @RequestParam(value = "nameChange", required = false) boolean nameChange) {
        Plan plan = new Plan();
        Dto2Entity.copyProperties(task, plan);
        taskFlowResolveService.doBatchSaveBasicInfo(fromTemplate, plan, planStartTime, planEndTime, workTime, nameChange);
    }

    /**
     * 删除线上的输入输出关系
     * 事务重构
     */
    @ApiOperation(value = "删除线上的输入输出关系",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "deleteLineConnect")
    void deleteLineConnect(@RequestBody Map<String, Object> params) {
        Object toObj = params.get("to");
        Plan to = new Plan();
        if (!CommonUtil.isEmpty(toObj)) {
            to = JSON.parseObject(JSON.toJSONString(toObj), new TypeReference<Plan>() {
            });
        }
        Object fromObj = params.get("from");
        Plan from = new Plan();
        if (!CommonUtil.isEmpty(fromObj)) {
            from = JSON.parseObject(JSON.toJSONString(fromObj), new TypeReference<Plan>() {
            });
        }
        taskFlowResolveService.deleteLineConnect(to, from);
    }

    /**
     * 删除关联的输入
     * 事务重构
     */
    @ApiOperation(value = "删除关联的输入",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "deleteLinkInput")
    void deleteLinkInput(@RequestBody Map<String, Object> params) {
        Object toObj = params.get("to");
        Plan to = new Plan();
        if (!CommonUtil.isEmpty(toObj)) {
            to = JSON.parseObject(JSON.toJSONString(toObj), new TypeReference<Plan>() {
            });
        }
        Object fromObj = params.get("from");
        Plan from = new Plan();
        if (!CommonUtil.isEmpty(fromObj)) {
            from = JSON.parseObject(JSON.toJSONString(fromObj), new TypeReference<Plan>() {
            });
        }
        Object toListObj = params.get("toList");
        List<Plan> toList = new ArrayList<Plan>();
        if (!CommonUtil.isEmpty(toListObj)) {
            toList = JSON.parseObject(JSON.toJSONString(toListObj), new TypeReference<List<Plan>>() {
            });
        }
        taskFlowResolveService.deleteLinkInput(to, toList, from);
    }

    @ApiOperation(value = "删除选中的节点",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "parentPlanId", value = "父计划id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "cellId", value = "节点id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "deleteSelectedCell")
    void deleteSelectedCell(@RequestParam(value = "parentPlanId", required = false) String parentPlanId, @RequestParam(value = "cellId", required = false) String cellId) {
        taskFlowResolveService.deleteSelectedCell(parentPlanId, cellId);
    }

    @ApiOperation(value = "保存前置计划",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "preposeIds", value = "前置计划的String集合", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "useObjectId", value = "所属计划的id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveOutPreposePlan")
    void saveOutPreposePlan(@RequestParam(value = "preposeIds", required = false) String preposeIds, @RequestParam(value = "useObjectId", required = false) String useObjectId) {
        taskFlowResolveService.saveOutPreposePlan(preposeIds, useObjectId);
    }

    @ApiOperation(value = "选择研发流程模板进行研发流程分解",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "parentId", value = "父计划id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "templateId", value = "研发流程模板id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentId", value = "当前用户Id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "templateResolveForPlan")
    boolean templateResolveForPlan(@RequestParam(value = "parentId") String parentId, @RequestParam(value = "templateId") String templateId, @RequestParam(value = "currentId", required = false) String currentId) {
        boolean flag = taskFlowResolveService.templateResolveForPlan(parentId, templateId, currentId);
        return flag;
    }

    /**
     * 保存流程任务基本信息
     * 事物重构
     *
     * @param paramMap wqb 2019年7月3日 17:55:41
     */
    @ApiOperation(value = "保存流程任务基本信息",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveFlowTask1")
    void saveFlowTask1(HttpServletRequest request, HttpServletResponse response
            , @RequestBody Map<String, Object> paramMap) {
        Object planObj = paramMap.get("plan");
        Plan plan = new Plan();
        if (!CommonUtil.isEmpty(planObj)) {
            plan = JSON.parseObject(JSON.toJSONString(planObj), new TypeReference<Plan>() {
            });
        }
        Object parentObj = paramMap.get("parent");
        Plan parent = new Plan();
        if (!CommonUtil.isEmpty(parentObj)) {
            parent = JSON.parseObject(JSON.toJSONString(parentObj), new TypeReference<Plan>() {
            });
        }
        Object userObj = paramMap.get("user");
        TSUserDto user = new TSUserDto();
        if (!CommonUtil.isEmpty(userObj)) {
            user = JSON.parseObject(JSON.toJSONString(userObj), new TypeReference<TSUserDto>() {
            });
        }
        Object createOrgIdObj = paramMap.get("createOrgId");
        if (!CommonUtil.isEmpty(createOrgIdObj)) {
            plan.setCreateOrgId(createOrgIdObj.toString());
        }
        taskFlowResolveService.saveFlowTask1(plan, parent, user);
    }

    @ApiOperation(value = "研发流程变更启动工作流",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "leader", value = "审批人", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "当前用户Id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "startChangeApplyProcess")
    public FeignJson startChangeApplyProcess(@RequestBody PlanownerApplychangeInfoDto dto, @RequestParam(value = "leader", required = false) String leader,
                                             @RequestParam(value = "userId", required = false) String userId) {
        PlanownerApplychangeInfo planownerApplychangeInfo = new PlanownerApplychangeInfo();
        Dto2Entity.copyProperties(dto, planownerApplychangeInfo);
        return taskFlowResolveService.startChangeApplyProcess(planownerApplychangeInfo, leader, userId);
    }

    @RequestMapping(value = "startChangeApplyForWorkFlow")
    public FeignJson startChangeApplyForWorkFlow(@RequestBody Map<String, Object> map) {
        return taskFlowResolveService.startChangeApplyForWorkFlow(map);
    }

    @ApiOperation(value = "驳回到首节点再次提交工作流",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "cancelChangeApplyForWorkFlow")
    public FeignJson cancelChangeApplyForWorkFlow(@RequestBody Map<String, Object> map) {
        return taskFlowResolveService.cancelChangeApplyForWorkFlow(map);
    }

    /**
     * 分解下达计划驳回时，对应计划的处理；
     *
     * @param formId wqb 2019年8月12日 13:59:35
     */
    @ApiOperation(value = "分解下达计划驳回时，对应计划的处理",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "formId", value = "流程应用对象的id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "goBackFlowResolveSetPlanInfo")
    public void goBackFlowResolveSetPlanInfo(@RequestParam(value = "formId", required = false) String formId) {
        taskFlowResolveService.goBackFlowResolveSetPlanInfo(formId);
    }

    /**
     * 分解发布计划完成时，对应计划的处理；
     *
     * @param planId
     * @param id     wqb 2019年8月12日 13:59:35
     */
    @ApiOperation(value = "分解发布计划完成时，对应计划的处理",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planId", value = "计划Id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "人员id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "endPlanAssignExcution")
    void endPlanAssignExcution(@RequestParam(value = "planId", required = false) String planId, @RequestParam(value = "id", required = false) String id) {
        taskFlowResolveService.endPlanAssignExcution(planId, id);
    }

    /**
     * 流程发布驳回
     *
     * @param parentPlanId wqb 2019年8月12日 13:59:35
     */
    @ApiOperation(value = "流程发布驳回",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "parentPlanId", value = "父计划Id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "planAssignBack")
    void planAssignBack(@RequestParam(value = "parentPlanId", required = false) String parentPlanId) {
        taskFlowResolveService.planAssignBack(parentPlanId);
    }

    /**
     * 发布流程同步数据
     * @param procInstId
     * @param parentPlanId
     * @param actorId
     * @param formId
     * @param approveType
     */
    @ApiOperation(value = "发布流程同步数据",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "procInstId", value = "流程id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "parentPlanId", value = "父计划Id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "actorId", value = "用户Id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "formId", value = "流程应用对象的id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "approveType", value = "类型", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "startAssignProcessSavePlanInfo")
    void startAssignProcessSavePlanInfo(@RequestParam(value = "procInstId", required = false) String procInstId, @RequestParam(value = "parentPlanId", required = false) String parentPlanId, @RequestParam(value = "actorId", required = false) String actorId,
                                        @RequestParam(value = "formId", required = false) String formId, @RequestParam(value = "approveType", required = false) String approveType) {
        taskFlowResolveService.startAssignProcessSavePlanInfo(procInstId, parentPlanId, actorId, formId, approveType);
    }

    /**
     * 流程发布驳回并启动流程同步数据
     *
     * @param parentPlanId wqb 2019年8月12日 13:59:35
     */
    @ApiOperation(value = "流程发布驳回并启动流程同步数据",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "parentPlanId", value = "父计划Id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "startAssignProcessBackSavePlanInfo")
    void startAssignProcessBackSavePlanInfo(@RequestParam(value = "parentPlanId", required = false) String parentPlanId) {
        taskFlowResolveService.startAssignProcessBackSavePlanInfo(parentPlanId);
    }

    @ApiOperation(value = "获取变更的前置计划list集合",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getChangeFlowTaskPreposeListForChange")
    List<FlowTaskPreposeVo> getChangeFlowTaskPreposeListForChange(@RequestBody PlanDto plan) {
        Plan cur = new Plan();
        Dto2Entity.copyProperties(plan, cur);
        return taskFlowResolveService.getChangeFlowTaskPreposeList(cur);
    }

    @ApiOperation(value = "获取变更的连接线list集合",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getChangeFlowTaskConnectListForChange")
    List<ChangeFlowTaskCellConnectVo> getChangeFlowTaskConnectListForChange(@RequestBody PlanDto plan) {
        Plan cur = new Plan();
        Dto2Entity.copyProperties(plan, cur);
        return taskFlowResolveService.getChangeFlowTaskConnectList(cur);
    }

    @ApiOperation(value = "获取变更的子计划list集合",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "当前用户Id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getChangeFlowTaskListForChange")
    List<FlowTaskVo> getChangeFlowTaskListForChange(@RequestBody PlanDto plan, @RequestParam(value = "userId", required = false) String userId) {
        Plan cur = new Plan();
        Dto2Entity.copyProperties(plan, cur);
        return taskFlowResolveService.getChangeFlowTaskList(cur, userId);
    }

    @ApiOperation(value = "变更的节点保存",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveFlowTaskForChange1")
    List<FlowTaskVo> saveFlowTaskForChange1(@RequestBody Map<String, Object> params) {
        Object planObj = params.get("plan");
        FlowTaskVo plan = new FlowTaskVo();
        if (!CommonUtil.isEmpty(planObj)) {
            plan = JSON.parseObject(JSON.toJSONString(planObj), new TypeReference<FlowTaskVo>() {
            });
        }
        Object changeFlowTaskListObj = params.get("changeFlowTaskList");
        List<FlowTaskVo> changeFlowTaskList = new ArrayList<FlowTaskVo>();
        if (!CommonUtil.isEmpty(changeFlowTaskListObj)) {
            changeFlowTaskList = JSON.parseObject(JSON.toJSONString(changeFlowTaskListObj), new TypeReference<List<FlowTaskVo>>() {
            });
        }
        Object changeFlowTaskConnectListObj = params.get("changeFlowTaskConnectList");
        List<ChangeFlowTaskCellConnectVo> changeFlowTaskConnectList = new ArrayList<ChangeFlowTaskCellConnectVo>();
        if (!CommonUtil.isEmpty(changeFlowTaskConnectListObj)) {
            changeFlowTaskConnectList = JSON.parseObject(JSON.toJSONString(changeFlowTaskConnectListObj), new TypeReference<List<ChangeFlowTaskCellConnectVo>>() {
            });
        }
        Object parentObj = params.get("parent");
        Plan parent = new Plan();
        if (!CommonUtil.isEmpty(parentObj)) {
            parent = JSON.parseObject(JSON.toJSONString(parentObj), new TypeReference<Plan>() {
            });
        }
        return taskFlowResolveService.saveFlowTaskForChange1(plan, changeFlowTaskList, changeFlowTaskConnectList, parent);
    }

    @RequestMapping(value = "saveFlowTaskForChange2")
    List<FlowTaskVo> saveFlowTaskForChange2(@RequestBody Map<String, Object> params) {
        Object planObj = params.get("plan");
        FlowTaskVo plan = new FlowTaskVo();
        if (!CommonUtil.isEmpty(planObj)) {
            plan = JSON.parseObject(JSON.toJSONString(planObj), new TypeReference<FlowTaskVo>() {
            });
        }
        Object changeFlowTaskListObj = params.get("changeFlowTaskList");
        List<FlowTaskVo> changeFlowTaskList = new ArrayList<FlowTaskVo>();
        if (!CommonUtil.isEmpty(changeFlowTaskListObj)) {
            changeFlowTaskList = JSON.parseObject(JSON.toJSONString(changeFlowTaskListObj), new TypeReference<List<FlowTaskVo>>() {
            });
        }
        Object changeFlowTaskConnectListObj = params.get("changeFlowTaskConnectList");
        List<ChangeFlowTaskCellConnectVo> changeFlowTaskConnectList = new ArrayList<ChangeFlowTaskCellConnectVo>();
        if (!CommonUtil.isEmpty(changeFlowTaskConnectListObj)) {
            changeFlowTaskConnectList = JSON.parseObject(JSON.toJSONString(changeFlowTaskConnectListObj), new TypeReference<List<ChangeFlowTaskCellConnectVo>>() {
            });
        }
        Object parentObj = params.get("parent");
        Plan parent = new Plan();
        if (!CommonUtil.isEmpty(parentObj)) {
            parent = JSON.parseObject(JSON.toJSONString(parentObj), new TypeReference<Plan>() {
            });
        }
        Object flowTaskParentObj = params.get("flowTaskParent");
        FlowTaskParentVo flowTaskParent = new FlowTaskParentVo();
        if (!CommonUtil.isEmpty(flowTaskParentObj)) {
            flowTaskParent = JSON.parseObject(JSON.toJSONString(flowTaskParentObj), new TypeReference<FlowTaskParentVo>() {
            });
        }
        return taskFlowResolveService.saveFlowTaskForChange2(plan, parent, flowTaskParent, changeFlowTaskList, changeFlowTaskConnectList);
    }

    @ApiOperation(value = "变更驳回再提交",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "leader", value = "leader", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "deptLeader", value = "deptLeader", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "userId", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "startPlanChangeforBackProcess")
    boolean startPlanChangeforBackProcess(@RequestParam(value = "leader", required = false) String leader, @RequestParam(value = "deptLeader", required = false) String deptLeader,
                                          @RequestBody FlowTaskParentVo flowTaskParent, @RequestParam(value = "userId", required = false) String userId) {
        return taskFlowResolveService.startPlanChangeforBackProcess(leader, deptLeader, flowTaskParent, userId);
    }

    @ApiOperation(value = "变更再提交",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "当前用户Id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "leader", value = "室领导审批", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "deptLeader", value = "部领导审批", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "changeType", value = "类型", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "changeRemark", value = "备注", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "startPlanChange")
    FlowTaskOutChangeVO startPlanChange(@RequestParam(value = "userId", required = false) String userId, @RequestParam(value = "leader", required = false) String leader, @RequestParam(value = "deptLeader", required = false) String deptLeader,
                                        @RequestParam(value = "changeType", required = false) String changeType, @RequestParam(value = "changeRemark", required = false) String changeRemark,
                                        @RequestBody Map<String, Object> params) {
        Object flowTaskParentObj = params.get("flowTaskParent");
        FlowTaskParentVo flowTaskParent = new FlowTaskParentVo();
        if (!CommonUtil.isEmpty(flowTaskParentObj)) {
            flowTaskParent = JSON.parseObject(JSON.toJSONString(flowTaskParentObj), new TypeReference<FlowTaskParentVo>() {
            });
        }
        Object changeFlowTaskListObj = params.get("changeFlowTaskList");
        List<FlowTaskVo> changeFlowTaskList = new ArrayList<FlowTaskVo>();
        if (!CommonUtil.isEmpty(changeFlowTaskListObj)) {
            changeFlowTaskList = JSON.parseObject(JSON.toJSONString(changeFlowTaskListObj), new TypeReference<List<FlowTaskVo>>() {
            });
        }
        Object changeFlowTaskConnectListObj = params.get("changeFlowTaskConnectList");
        List<ChangeFlowTaskCellConnectVo> changeFlowTaskConnectList = new ArrayList<ChangeFlowTaskCellConnectVo>();
        if (!CommonUtil.isEmpty(changeFlowTaskConnectListObj)) {
            changeFlowTaskConnectList = JSON.parseObject(JSON.toJSONString(changeFlowTaskConnectListObj), new TypeReference<List<ChangeFlowTaskCellConnectVo>>() {
            });
        }
        FlowTaskOutChangeVO aChangeVO = taskFlowResolveService.startPlanChange(leader, deptLeader, changeType, changeRemark, flowTaskParent, changeFlowTaskList, changeFlowTaskConnectList, userId);
        return aChangeVO;
    }

    @ApiOperation(value = "变更审批通过修改相关信息",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "oid", value = "父计划id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户Id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "flowTaskIdStr", value = "节点id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "flowTaskInputsIdAndRDTaskInputsIdStr", value = "输入集合", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "flowTaskDeliversIdAndRDTaskDeliversIdStr", value = "输出集合", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "endSuccessChange")
    void endSuccessChange(@RequestParam(value = "oid", required = false) String oid, @RequestParam(value = "userId", required = false) String userId
            , @RequestParam(value = "flowTaskIdStr", required = false) String flowTaskIdStr, @RequestParam(value = "flowTaskInputsIdAndRDTaskInputsIdStr", required = false) String flowTaskInputsIdAndRDTaskInputsIdStr
            , @RequestParam(value = "flowTaskDeliversIdAndRDTaskDeliversIdStr", required = false) String flowTaskDeliversIdAndRDTaskDeliversIdStr) {
        planFlowForworkService.startPlanChangeFlowForWork(oid, userId, flowTaskIdStr, flowTaskInputsIdAndRDTaskInputsIdStr, flowTaskDeliversIdAndRDTaskDeliversIdStr);
    }

    @ApiOperation(value = "计划的流程流程变更审批通过修改相关信息",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "oid", value = "父计划id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户Id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "endSuccessChangeForPlan")
    void endSuccessChangeForPlan(@RequestParam(value = "oid", required = false) String oid, @RequestParam(value = "userId", required = false) String userId) {
        planFlowForworkService.startPlanChangeFlowForPlan(oid, userId);
    }
}
