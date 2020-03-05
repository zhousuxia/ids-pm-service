package com.glaway.ids.project.plan.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.OperationLog;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.ResourceUtil;
import com.glaway.foundation.common.util.UserUtil;
import com.glaway.foundation.common.util.VDataUtil;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.common.vo.TreeNode;
import com.glaway.foundation.core.common.controller.BaseController;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.ids.config.service.ParamSwitchServiceI;
import com.glaway.ids.constant.PlanImAndExConstants;
import com.glaway.ids.project.menu.dto.ProjectDto;
import com.glaway.ids.project.menu.entity.Project;
import com.glaway.ids.project.plan.dto.*;
import com.glaway.ids.project.plan.entity.*;
import com.glaway.ids.project.plan.service.DeliverablesChangeServiceI;
import com.glaway.ids.project.plan.service.PlanExcuteServiceI;
import com.glaway.ids.project.plan.service.PlanServiceI;
import com.glaway.ids.project.plan.service.ResourceChangeServiceI;
import com.glaway.ids.project.plantemplate.support.planTemplateDetail.vo.PlanTemplateDetailReq;
import com.glaway.ids.project.planview.service.PlanViewServiceI;
import com.glaway.ids.project.projectmanager.service.ProjLibServiceI;
import com.glaway.ids.project.projectmanager.vo.ProjLibDocumentVo;
import com.glaway.ids.util.CodeUtils;
import com.glaway.ids.util.Dto2Entity;
import com.glaway.ids.util.mpputil.MppInfo;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

/**
 * 计划接口
 *
 * @author wangshen
 * @version V1.0
 * @Title: Controller
 * @Description: 项目模板Controller
 * @date 2015-03-23 15:59:25
 */
@Api(tags = {"计划接口"})
@RestController
@RequestMapping("/feign/planRestController")
public class PlanRestController extends BaseController {
    /**
     * Logger for this class
     */
    private static final OperationLog log = BaseLogFactory.getOperationLog(PlanRestController.class);
    @Autowired
    private PlanServiceI planService;
    @Autowired
    private PlanViewServiceI planViewService;
    @Autowired
    private ParamSwitchServiceI paramSwitchService;
    @Autowired
    private ProjLibServiceI projLibService;
    @Autowired
    private DeliverablesChangeServiceI deliverablesChangeServiceI;
    @Autowired
    private FeignUserService userService;
    @Autowired
    private ResourceChangeServiceI resourceChangeServiceI;
    @Autowired
    private PlanExcuteServiceI planExcuteServiceI;

    @ApiOperation(value = "通过项目id和当前用户id查找用户设置的视图关系",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getUserViewProjectLinkByProjectId")
    public UserPlanViewProjectDto getUserViewProjectLinkByProjectId(@RequestParam("projectId") String projectId, @RequestParam("userId") String userId) {
        UserPlanViewProject userPlanViewProject = planService.getUserViewProjectLinkByProjectId(projectId, userId);
        UserPlanViewProjectDto dto = new UserPlanViewProjectDto();
        try {
            dto = (UserPlanViewProjectDto) VDataUtil.getVDataByEntity(userPlanViewProject, UserPlanViewProjectDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    @ApiOperation(value = "获取视图树",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "数据类型", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String"),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "constructionPlanViewTree")
    public List<PlanViewInfoDto> constructionPlanViewTree(@RequestParam("projectId") String projectId,
                                                          @RequestParam("type") String type, @RequestParam(value = "userId", required = false) String userId,@RequestParam(value = "orgId")String orgId) {
        List<PlanViewInfoDto> list = planViewService.constructionPlanViewTree(projectId, type, userId,orgId);
        return list;
    }

    @ApiOperation(value = "获取视图的展示列信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planViewInfoId", value = "视图id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getColumnInfoListByPlanViewInfoId")
    public List<PlanViewColumnInfoDto> getColumnInfoListByPlanViewInfoId(@RequestParam("planViewInfoId") String planViewInfoId) {
        List<PlanViewColumnInfoDto> dtoList = planViewService.getColumnInfoListByPlanViewInfoId(planViewInfoId);
        return dtoList;
    }

    @ApiOperation(value = "获取计划视图信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "视图id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPlanViewEntity")
    public PlanViewInfoDto getPlanViewEntity(@RequestParam("id") String id) {
        PlanViewInfoDto dto = planViewService.getPlanViewEntity(id);
        return dto;
    }

    @ApiOperation(value = "通过视图id查找视图查询条件",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planViewInfoId", value = "视图id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getSearchConditionListByViewId")
    public List<PlanViewSearchConditionDto> getSearchConditionListByViewId(@RequestParam("planViewInfoId") String planViewInfoId) {
        List<PlanViewSearchConditionDto> dtoList = planViewService.getSearchConditionListByViewId(planViewInfoId);
        return dtoList;
    }

    @ApiOperation(value = "保存用户与项目视图的关系",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "planViewId", value = "视图id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveUserViewProjectLink")
    public void saveUserViewProjectLink(@RequestParam("projectId") String projectId, @RequestParam("planViewId") String planViewId, @RequestBody TSUserDto curUser, @RequestParam("orgId") String orgId) {
        planService.saveUserViewProjectLink(projectId, planViewId, curUser, orgId);
    }

    @RequestMapping(value = "queryPlanListForTreegridView")
    public List<PlanDto> queryPlanListForTreegridView(@RequestBody Map<String, Object> mapList) {
        ObjectMapper mapper = new ObjectMapper();
        PlanDto dto = mapper.convertValue(mapList.get("planDto"), PlanDto.class);
        String planViewInfoId = String.valueOf(mapList.get("planViewInfoId"));
        List<ConditionVO> conditionList = mapper.convertValue(mapList.get("conditionList"), new TypeReference<List<ConditionVO>>() {
        });
        String userName = String.valueOf(mapList.get("userName"));
        String progressRate = String.valueOf(mapList.get("progressRate"));
        String workTime = String.valueOf(mapList.get("workTime"));
        String userId = String.valueOf(mapList.get("userId"));
        List<PlanDto> dtoList = planService.queryPlanListForTreegridView(dto, planViewInfoId, conditionList, userName, progressRate, workTime, userId);
        return dtoList;
    }

    @RequestMapping(value = "queryPlanListForTreegridWithView")
    public List<PlanDto> queryPlanListForTreegridWithView(@RequestBody List<PlanDto> planList, @RequestParam("planViewInfoId") String planViewInfoId,
                                                          @RequestParam("userId") String userId) {
        List<PlanDto> dtoList = planService.queryPlanListForTreegridWithView(planList, planViewInfoId, userId);
        return dtoList;
    }

    @RequestMapping(value = "queryPlanListForTreegrid")
    public List<PlanDto> queryPlanListForTreegrid(@RequestBody PlanDto planDto) {
        List<PlanDto> dtoList = planService.queryPlanListForTreegrid(planDto);
        return dtoList;
    }

    @ApiOperation(value = "初始化计划生命周期",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "initPlan")
    public String initPlan(@RequestBody PlanDto planDto) {
        String planStr = planService.initPlan(planDto);
        return planStr;
    }

    @ApiOperation(value = "获取计划生命周期",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getLifeCycleStatusList")
    public FeignJson getLifeCycleStatusList() {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            String lifeCycleStr = planService.getLifeCycleStatusList();
            j.setObj(lifeCycleStr);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "获取计划信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "计划id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPlanEntity")
    public PlanDto getPlanEntity(@RequestParam("id") String id) {
        PlanDto plan = planService.getPlanEntity(id);
        return plan;
    }

    @ApiOperation(value = "获取变更计划对象",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "变更计划id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getTemporaryPlanEntity")
    public TemporaryPlanDto getTemporaryPlanEntity(@RequestParam("id") String id) {
        TemporaryPlanDto plan = planService.getTemporaryPlanEntity(id);
        return plan;
    }

    @ApiOperation(value = "根据开关名称获取开关状态",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "switchName", value = "开关名称", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getSwitch")
    public FeignJson getSwitch(@RequestParam("switchName") String switchName) {
        FeignJson j = new FeignJson();
        String res = paramSwitchService.getSwitch(switchName);
        if (CommonUtil.isEmpty(res)) {
            res = "";
        }
        j.setObj(res);
        return j;
    }

    @ApiOperation(value = "根据引用文档的Id查询项目文档关系对象",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "quoteId", value = "引用文档id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getDocRelation")
    public FeignJson getDocRelation(@RequestParam("quoteId") String quoteId) {
        FeignJson j = new FeignJson();
        String str = projLibService.getDocRelation(quoteId);
        j.setObj(str);
        return j;
    }

    @ApiOperation(value = "根据文件id获得vo对象",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "docId", value = "文件id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getProjDocmentVoById")
    public ProjLibDocumentVo getProjDocmentVoById(@RequestParam("docId") String docId) {
        ProjLibDocumentVo vo = projLibService.getProjDocmentVoById(docId);
        return vo;
    }

    @ApiOperation(value = "根据plan条件检索非废弃的计划",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getFoldIdByProjectId")
    public String getFoldIdByProjectId(@RequestParam(value = "projectId", required = false) String projectId) {
        String str = projLibService.getFoldIdByProjectId(projectId);
        return str;
    }

    @ApiOperation(value = "查询除废弃状态的计划",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryPlansExceptInvalid")
    public List<PlanDto> queryPlansExceptInvalid(@RequestBody PlanDto planDto) {
        Plan plan = new Plan();
        Dto2Entity.copyProperties(planDto, plan);
        List<Plan> planList = planService.queryPlansExceptInvalid(plan);
        List<PlanDto> resList = new ArrayList<PlanDto>();
       /* try {
            resList = JSON.parseArray(JSON.toJSONString(planList),PlanDto.class);;
        } catch (Exception e) {
        }*/
        try {
            resList = (List<PlanDto>) VDataUtil.getVDataByEntity(planList, PlanDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resList;
    }

    @ApiOperation(value = "获取计划id与计划名称的集合",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "parentPlanId", value = "父计划id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping("/queryPlanIdAndNameMap")
    public FeignJson queryPlanIdAndNameMap(@RequestParam(value = "parentPlanId", required = false) String parentPlanId) {
        return planService.queryPlanIdAndNameMap(parentPlanId);
    }

    @ApiOperation(value = "新增计划及相关数据",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planStr", value = "计划数据", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveAsCreate")
    public FeignJson saveAsCreate(@RequestParam("planStr") String planStr,@RequestBody Map<String,Object> map) {
        PlanDto planDto = JSON.parseObject(planStr, PlanDto.class);
        Plan plan = new Plan();
        Dto2Entity.copyProperties(planDto, plan);
        ObjectMapper mapper = new ObjectMapper();
        String orgId = String.valueOf(map.get("orgId"));
        TSUserDto user = mapper.convertValue(map.get("user"),TSUserDto.class);
        List<InputsDto> inputsDtoList = mapper.convertValue(map.get("inputList"),new TypeReference<List<InputsDto>>(){});
        List<ResourceLinkInfoDto> resourceLinkInfoDtoList = mapper.convertValue(map.get("resourceLinkInfoList"),new TypeReference<List<ResourceLinkInfoDto>>(){});
        if(CommonUtil.isEmpty(resourceLinkInfoDtoList)){
            resourceLinkInfoDtoList = new ArrayList<>();
        }
        List<Inputs> inputsList = CodeUtils.JsonListToList(inputsDtoList,Inputs.class);
        List<ResourceLinkInfo> resourceLinkInfo = CodeUtils.JsonListToList(resourceLinkInfoDtoList,ResourceLinkInfo.class);
        return planService.saveAsCreate(plan, user, inputsList,resourceLinkInfo,orgId);
    }

    @ApiOperation(value = "更新计划及相关数据",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveAsUpdate")
    public void saveAsUpdate(@RequestBody Map<String, Object> map) {
        ObjectMapper mapper = new ObjectMapper();
        PlanDto planDto = mapper.convertValue(map.get("plan"), PlanDto.class);
        TSUserDto user = mapper.convertValue(map.get("user"), TSUserDto.class);
        Plan plan = new Plan();
        Dto2Entity.copyProperties(planDto, plan);
        String orgId = String.valueOf(map.get("orgId"));
        List<ResourceLinkInfoDto> resourceLinkInfoDtoList = mapper.convertValue(map.get("resourceLinkInfoList"),new TypeReference<List<ResourceLinkInfoDto>>(){});
        if(CommonUtil.isEmpty(resourceLinkInfoDtoList)){
            resourceLinkInfoDtoList = new ArrayList<>();
        }
        List<ResourceLinkInfo> resourceLinkInfo = CodeUtils.JsonListToList(resourceLinkInfoDtoList,ResourceLinkInfo.class);
        planService.saveAsUpdate(plan, user,resourceLinkInfo,orgId);
    }

    @RequestMapping(value = "getPlanAllChildren")
    public List<PlanDto> getPlanAllChildren(@RequestBody PlanDto dto) {
        Plan plan = new Plan();
        Dto2Entity.copyProperties(dto, plan);
        List<Plan> list = planService.getPlanAllChildren(plan);
        List<PlanDto> result = new ArrayList<>();
        try {
            result = (List<PlanDto>) VDataUtil.getVDataByEntity(list, PlanDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ApiOperation(value = "获取项目列表",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryProjectList")
    public FeignJson queryProjectList(@RequestBody List<ConditionVO> conditionList) {
        PageList pageList = planService.queryProjectList(conditionList);
        FeignJson j = new FeignJson();
        Map<String, Object> map = new HashMap<>();
        map.put("resultList", pageList.getResultList());
        map.put("count", pageList.getCount());
        j.setAttributes(map);
        return j;
    }

    @ApiOperation(value = "计划下达前校验来源是否为空",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "计划ids", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "useObjectType", value = "数据类型", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "checkOriginIsNullBeforeSub")
    public FeignJson checkOriginIsNullBeforeSub(@RequestParam(value = "ids", required = false) String ids,
                                                @RequestParam(value = "useObjectType", required = false) String useObjectType) {
        return planService.checkOriginIsNullBeforeSub(ids, useObjectType);
    }

    @ApiOperation(value = "计划单条发布",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "计划id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "pdAssign")
    public FeignJson pdAssign(@RequestBody PlanDto dto, @RequestParam(value = "id", required = false) String id) {
        Plan plan = new Plan();
        Dto2Entity.copyProperties(dto, plan);
        return planService.pdAssign(plan, id);
    }

    @ApiOperation(value = "获取项目下计划的最小开始时间",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getMinPlanStartTimeByProject")
    public Date getMinPlanStartTimeByProject(@RequestBody ProjectDto t) {
        Project project = new Project();
        Dto2Entity.copyProperties(t, project);
        Date date = planService.getMinPlanStartTimeByProject(project);
        return date;
    }

    @ApiOperation(value = "获取项目下计划的最大结束时间",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getMaxPlanEndTimeByProject")
    public Date getMaxPlanEndTimeByProject(@RequestBody ProjectDto t) {
        Project project = new Project();
        Dto2Entity.copyProperties(t, project);
        Date date = planService.getMaxPlanEndTimeByProject(project);
        return date;
    }

    @ApiOperation(value = "删除计划",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "deletePlans")
    public void deletePlans(@RequestBody List<PlanDto> list) {
        List<Plan> planList = JSON.parseArray(JSON.toJSONString(list), Plan.class);
        planService.deletePlans(planList);
    }

    @ApiOperation(value = "判断计划状态",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "judgePlanStatusByPlan")
    public long judgePlanStatusByPlan(@RequestBody PlanDto planDto) {
        Plan plan = new Plan();
        Dto2Entity.copyProperties(planDto, plan);
        long count = 0;
        count = planService.judgePlanStatusByPlan(plan);
        return count;
    }

    @ApiOperation(value = "统计拟制中计划数量",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getEditingPlanCount")
    public long getEditingPlanCount(@RequestBody PlanDto planDto) {
        Plan plan = new Plan();
        Dto2Entity.copyProperties(planDto, plan);
        long count = 0;
        count = planService.getEditingPlanCount(plan);
        return count;
    }


    @RequestMapping(value = "updatePlan")
    public FeignJson updatePlan(@RequestBody PlanDto planDto) {
        FeignJson j = new FeignJson();
        try {
            Plan plan = new Plan();
            Dto2Entity.copyProperties(planDto, plan);
            planService.initBusinessObject(plan);
            if(!CommonUtil.isEmpty(plan.getId())) {
                Plan planOld = planService.getEntity(plan.getId());
                if(!CommonUtil.isEmpty(planOld.getBizCurrent())) {
                    plan.setBizCurrent(planOld.getBizCurrent());
                }
            }
            planService.updatePlan(plan);
            j.setSuccess(true);
        } catch (Exception e) {
            j.setSuccess(false);
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "根据plan条件检索计划",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "rows", value = "每页数量", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "isPage", value = "是否分页", dataType = "boolean")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryPlanList")
    public List<PlanDto> queryPlanList(@RequestBody PlanDto plan, @RequestParam(value = "page") int page, @RequestParam(value = "rows") int rows, @RequestParam(value = "isPage") boolean isPage) {
        Plan p = new Plan();
        Dto2Entity.copyProperties(plan, p);
        List<Plan> list = planService.queryPlanList(p, page, rows, isPage);
        List<PlanDto> planList = new ArrayList<>();
        try {
            planList = (List<PlanDto>) VDataUtil.getVDataByEntity(list, PlanDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //手动添加用户信息
        planList.forEach(plan1 -> {
            if (StringUtils.isNotEmpty(plan1.getOwner())) {
                TSUserDto userDto = userService.getUserByUserId(plan1.getOwner());
                plan1.setOwnerInfo(userDto);
            }
        });
        return planList;
    }

    @RequestMapping(value = "inputForWork")
    public FeignJson inputForWork(@RequestBody Map<String, Object> map, @RequestParam(value = "projectIdForPlan",required = false) String projectIdForPlan, @RequestParam(value = "type",required = false) String type,
                                  @RequestParam(value = "userId",required = false) String userId) {
        ObjectMapper mapper = new ObjectMapper();
        Set<String> mapKeys = mapper.convertValue(map.get("mapKeys"), new TypeReference<Set<String>>(){});
        PlanDto dto = mapper.convertValue(map.get("planDto"), PlanDto.class);
        Map<String, List<String>> preposePlanIdMap = mapper.convertValue(map.get("preposePlanIdMap"), new TypeReference<Map<String, List<String>>>(){});
        List<List<Map<String, Object>>> taskMapList = mapper.convertValue(map.get("taskList"), new TypeReference<List<List<Map<String, Object>>>>(){});
        Plan plan = new Plan();
        Dto2Entity.copyProperties(dto, plan);
        FeignJson j = new FeignJson();
        try {
            planService.inputForWork(mapKeys, projectIdForPlan, plan, type, userId, taskMapList, preposePlanIdMap);
        } catch (IOException e) {
            j.setSuccess(false);
            j.setMsg("导入失败");
        }catch (GWException e){
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ApiOperation(value = "项目计划单条下达选人页面信息获取",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "assingnId", value = "提交者id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "goAssignPlanOne")
    public FeignJson goAssignPlanOne(@RequestParam(value = "assingnId", required = false) String assignId,
                                     @RequestParam(value = "userId", required = false) String userId) {
        return planService.goAssignPlanOne(assignId, userId);
    }

    @ApiOperation(value = "获取流程应用对象信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "流程应用对象id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getApprovePlanFormEntity")
    public ApprovePlanFormDto getApprovePlanFormEntity(@RequestParam("id") String id) {
        ApprovePlanFormDto dto = planService.getApprovePlanFormEntity(id);
        return dto;
    }

    @ApiOperation(value = "通过planId判断是否存在于流程任务中",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planId", value = "计划id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPlanIdByLinkPlanId2")
    FeignJson getPlanIdByLinkPlanId2(@RequestParam(value = "planId") String planId) {
        FeignJson j = new FeignJson();
        String cur = planService.getPlanIdByLinkPlanId2(planId);
        j.setObj(cur);
        return j;
    }

    /**
     * @param changeFlowTaskList
     * @return
     * @see
     */
    @ApiOperation(value = "流程任务排序",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "taskSort")
    List<FlowTaskVo> taskSort(@RequestBody List<FlowTaskVo> changeFlowTaskList) {
        return planService.taskSort(changeFlowTaskList);
    }


    @RequestMapping(value = "getOutPower")
    String getOutPower(@RequestParam(value = "folderId") String folderId
            , @RequestParam(value = "planId") String planId, @RequestParam(value = "userId") String userId) {
        return planService.getOutPower(folderId, planId, userId);
    }

    @ApiOperation(value = "获取项目库文档权限",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "folderId", value = "文件夹id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "planId", value = "计划id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getOutPowerFj")
    public FeignJson getOutPowerFj(@RequestParam(value = "folderId") String folderId,
                                   @RequestParam(value = "planId") String planId, @RequestParam(value = "userId") String userId) {
        String str = planService.getOutPower(folderId, planId, userId);
        FeignJson fj = new FeignJson();
        fj.setObj(str);
        return fj;
    }

    /**
     * 根据approvePlanInfo条件检索计划
     */
    @ApiOperation(value = "根据approvePlanInfo条件检索计划",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "rows", value = "每页数量", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "isPage", value = "是否分页", dataType = "boolean")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryAssignList")
    List<ApprovePlanInfoDto> queryAssignList(@RequestBody ApprovePlanInfoDto dto, @RequestParam(value = "page", required = false) int page, @RequestParam(value = "rows", required = false) int rows,
                                             @RequestParam(value = "isPage") boolean isPage) {
        ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
        Dto2Entity.copyProperties(dto, approvePlanInfo);
        List<ApprovePlanInfoDto> listEnd = new ArrayList<ApprovePlanInfoDto>();
        List<ApprovePlanInfo> list = planService.queryAssignList(approvePlanInfo, page, rows, isPage);
        try {
            listEnd = (List<ApprovePlanInfoDto>) VDataUtil.getVDataByEntity(list, ApprovePlanInfoDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listEnd;
    }

    /**
     * 查询选择来源计划列表
     *
     * @author zhousuxia
     */
    @ApiOperation(value = "查询选择来源计划列表",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryPlanInputsList")
    List<PlanDto> queryPlanInputsList(@RequestBody PlanDto plan) {
        Plan p = new Plan();
        Dto2Entity.copyProperties(plan, p);
        List<Plan> list = planService.queryPlanInputsList(p);
        List<PlanDto> planList = new ArrayList<PlanDto>();
        try {
            planList = (List<PlanDto>) VDataUtil.getVDataByEntity(list, PlanDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return planList;
    }

    /**
     * 根据plan条件检索计划
     *
     * @param plan
     * @param page
     * @param rows
     * @param isPage
     */
    @ApiOperation(value = "根据条件检索计划",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "rows", value = "每页数量", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "isPage", value = "是否分页", dataType = "boolean")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryPlanOrderByStarttime")
    List<PlanDto> queryPlanOrderByStarttime(@RequestBody PlanDto plan, @RequestParam(value = "page", required = false) int page
            , @RequestParam(value = "rows", required = false) int rows, @RequestParam(value = "isPage", required = false) boolean isPage) {
        Plan p = new Plan();
        Dto2Entity.copyProperties(plan, p);
        List<Plan> list = planService.queryPlanOrderByStarttime(p, page, rows, isPage);
        List<PlanDto> planList = new ArrayList<PlanDto>();
        try {
            planList = (List<PlanDto>) VDataUtil.getVDataByEntity(list, PlanDto.class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return planList;
    }

    @ApiOperation(value = "保存计划",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "savePlanByPlanDto")
    void savePlanByPlanDto(@RequestBody PlanDto toPlan) {
        Plan p = new Plan();
        Dto2Entity.copyProperties(toPlan, p);
        planService.savePlanByPlanDto(p);
    }

    @ApiOperation(value = "获取项目下最大的计划编号",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getMaxPlanNumber")
    int getMaxPlanNumber(@RequestBody PlanDto parent) {
        Plan p = new Plan();
        Dto2Entity.copyProperties(parent, p);
        int number = 0;
        number = planService.getMaxPlanNumber(p);
        return number;
    }

    @ApiOperation(value = "获取计划类表数据",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryEntity")
    public PageList queryEntity(@RequestBody Map<String, Object> map) {
        List<ConditionVO> conditionList = new ObjectMapper().convertValue(map.get("conditionList"), new TypeReference<List<ConditionVO>>() {
        });
        String projectId = (String) map.get("projectId");
        String userName = (String) map.get("userName");
        String progressRate = (String) map.get("progressRate");
        String workTime = (String) map.get("workTime");
        return planService.queryEntity(conditionList, projectId, userName, progressRate, workTime);
    }

    @ApiOperation(value = "获取计划视图中已选择计划列表",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryEntityForSelectedPlan")
    public PageList queryEntityForSelectedPlan(@RequestBody Map<String, Object> map) {
        List<ConditionVO> conditionList = new ObjectMapper().convertValue(map.get("conditionList"), new TypeReference<List<ConditionVO>>() {
        });
        String projectId = (String) map.get("projectId");
        String userName = (String) map.get("userName");
        String progressRate = (String) map.get("progressRate");
        String workTime = (String) map.get("workTime");
        String planIds = (String) map.get("planIds");
        return planService.queryEntityForSelectedPlan(conditionList, projectId, userName, progressRate, workTime, planIds);
    }

    @ApiOperation(value = "启动计划发布流程",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "startAssignProcess")
    public FeignJson startAssignProcess(@RequestBody Map<String, String> params) {
        return planService.startAssignProcess(params);
    }

    @ApiOperation(value = "根据计划id更新任务操作类型",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planId", value = "计划id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "opContent", value = "任务操作类型", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "updateOpContentByPlanId")
    void updateOpContentByPlanId(@RequestBody TSUserDto usetDto, @RequestParam(value = "planId", required = false) String planId, @RequestParam(value = "opContent", required = false) String opContent) {
        planService.updateOpContentByPlanId(usetDto, planId, opContent);
    }

    @ApiOperation(value = "废弃计划",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "计划id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "discardPlans")
    public void discardPlans(@RequestParam(value = "id") String id, @RequestParam(value = "userId", required = false) String userId) {
        planService.discardPlans(id, userId);
    }

    @ApiOperation(value = "计划前置选择页面初始化",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "formId", value = "表单id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "assignListViewTree")
    public FeignJson assignListViewTree(@RequestBody PlanDto dto, @RequestParam(value = "formId", required = false) String formId,
                                        @RequestParam(value = "userId", required = false) String userId) {
        Plan plan = new Plan();
        Dto2Entity.copyProperties(dto, plan);
        return planService.assignListViewTree(plan, formId, userId);
    }

    @ApiOperation(value = "获取项目库文档权限",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "folderId", value = "文件夹id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "planId", value = "计划id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getOutPowerForPlanList")
    public String getOutPowerForPlanList(@RequestParam(value = "folderId") String folderId, @RequestParam(value = "planId") String planId, @RequestParam(value = "userId") String userId) {
        return planService.getOutPowerForPlanList(folderId, planId, userId);
    }

    @ApiOperation(value = "驳回到首节点再次提交工作流",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "startPlanFlow")
    public FeignJson startPlanFlow(@RequestBody Map<String, String> map) {
        return planService.startPlanFlow(map);
    }

    @ApiOperation(value = "项目计划基本信息tab页面跳转数据获取",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "goBasicCheck")
    public Map<String, Object> goBasicCheck(@RequestBody PlanDto dto) {
        Plan plan = new Plan();
        Dto2Entity.copyProperties(dto, plan);
        return planService.goBasicCheck(plan);
    }

    @ApiOperation(value = "获取计划生命周期",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPlanLifeStatus")
    public FeignJson getPlanLifeStatus() {
        return planService.getPlanLifeStatus();
    }

    @ApiOperation(value = "获取计划类型",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planName", value = "计划名称", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getTaskNameType")
    public FeignJson getTaskNameType(@RequestParam(value = "planName", required = false) String planName) {
        return planService.getTaskNameType(planName);
    }

    @ApiOperation(value = "将计划数据转为json格式返回",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "changePlansToJSONObjects")
    public List<JSONObject> changePlansToJSONObjects(@RequestBody List<PlanDto> planList) {
        List<Plan> list = CodeUtils.JsonListToList(planList, Plan.class);
        return planService.changePlansToJSONObjects(list);
    }

    @ApiOperation(value = "通过计划变更id获取变更信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "计划变更id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPlanownerApplychangeInfo")
    public PlanownerApplychangeInfoDto getPlanownerApplychangeInfo(@RequestParam(value = "id", required = false) String id) {
        PlanownerApplychangeInfo planownerApplychangeInfo = planService.getPlanownerApplychangeInfo(id);
        PlanownerApplychangeInfoDto dto = new PlanownerApplychangeInfoDto();
        try {
            dto = (PlanownerApplychangeInfoDto) VDataUtil.getVDataByEntity(planownerApplychangeInfo, PlanownerApplychangeInfoDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    @ApiOperation(value = "查询计划前置列表信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userName", value = "用户名", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "progressRate", value = "进度", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "workTime", value = "工期", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping("getOutplanPreposesForSearch")
    public List<PlanDto> getOutplanPreposesForSearch(@RequestBody List<ConditionVO> conditionList, @RequestParam(value = "projectId") String projectId, @RequestParam(value = "userName") String userName,
                                                     @RequestParam(value = "progressRate") String progressRate, @RequestParam(value = "workTime") String workTime) {
        List<PlanDto> planList = new ArrayList<PlanDto>();
        try {
            PageList pageList = planService.queryEntity(conditionList, projectId, userName,
                    progressRate, workTime);
            List<Plan> resultList = pageList.getResultList();
            planList = (List<PlanDto>) VDataUtil.getVDataByEntity(resultList, PlanDto.class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return planList;
    }

    @ApiOperation(value = "获取视图计划树列表",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planIds", value = "计划ids", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping("queryPlanListForCustomViewTreegrid")
    public List<PlanDto> queryPlanListForCustomViewTreegrid(@RequestBody PlanDto planDto, @RequestParam("planIds") String planIds) {
        Plan plan = new Plan();
        Dto2Entity.copyProperties(planDto, plan);
        List<Plan> tempList = planService.queryPlanListForCustomViewTreegrid(plan, planIds);
        List<PlanDto> list = JSON.parseArray(JSON.toJSONString(tempList), PlanDto.class);
        return list;
    }

    @ApiOperation(value = "下发评审任务",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "orderReviewTaskType", value = "任务类型", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "orderReviewTask")
    public FeignJson orderReviewTask(@RequestBody PlanDto dto, @RequestParam(value = "orderReviewTaskType", required = false) String orderReviewTaskType,
                                     @RequestParam(value = "userId", required = false) String userId) {
        Plan plan = new Plan();
        Dto2Entity.copyProperties(dto, plan);
        return planService.orderReviewTaskFj(plan, orderReviewTaskType, userId);
    }

    @ApiOperation(value = "获取除拟制中状态外的计划列表",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "rows", value = "每页数量", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "isPage", value = "是否分页", dataType = "boolean")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryPlanListExceptEditing")
    public List<PlanDto> queryPlanListExceptEditing(@RequestBody PlanDto plan, @RequestParam(value = "page") int page, @RequestParam(value = "rows") int rows, @RequestParam(value = "isPage") boolean isPage) {
        Plan o = (Plan) CodeUtils.JsonBeanToBean(plan, Plan.class);
        List<Plan> list = planService.queryPlanListExceptEditing(o, page, rows, isPage);
        return CodeUtils.JsonListToList(list, PlanDto.class);
    }

    @ApiOperation(value = "获取计划列表",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPlanInfoList")
    public List<PlanDto> getPlanInfoList(@RequestBody PlanDto p) {
//        Plan o = (Plan) CodeUtils.JsonBeanToBean(p, Plan.class);
        return planService.getPlanInfoList(p);
    }

    @ApiOperation(value = "获取计划模板的计划树列表",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryPlanListForTemplateTreegrid")
    public List<PlanDto> queryPlanListForTemplateTreegrid(@RequestBody PlanDto planDto) {
        Plan plan = new Plan();
        Dto2Entity.copyProperties(planDto, plan);
        List<Plan> planList = planService.queryPlanListForTemplateTreegrid(plan);
        List<PlanDto> resList = JSON.parseArray(JSON.toJSONString(planList), PlanDto.class);
        return resList;
    }

    @ApiOperation(value = "获取项目计划所对应的输入列表",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getDetailInputsList")
    public Map<String, List<InputsDto>> getDetailInputsList(@RequestBody PlanDto planDto) {
        Map<String, List<InputsDto>> map = new HashMap<>();
        Plan plan = new Plan();
        Dto2Entity.copyProperties(planDto, plan);
        map = planService.getDetailInputsList(plan);
        return map;
    }

    @ApiOperation(value = "获取计划id与计划输出的集合",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getDeliverableListMap")
    public Map<String, List<DeliverablesInfoDto>> getDeliverableListMap() {
        Map<String, List<DeliverablesInfoDto>> map = new HashMap<>();
        map = planService.getDeliverableListMap();
        return map;
    }

    @ApiOperation(value = "通过流程任务id获取计划id",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "parentPlanId", value = "流程任务id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping("getPlanIdByFlowTaskParentId")
    public FeignJson getPlanIdByFlowTaskParentId(@RequestParam(value = "parentPlanId") String parentPlanId) {
        FeignJson j = new FeignJson();
        String res = planService.getPlanIdByFlowTaskParentId(parentPlanId);
        j.setObj(res);
        return j;
    }

    @ApiOperation(value = "根据计划id删除交付项",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planId", value = "计划id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "deleteDeliverablesByPlanId")
    public void deleteDeliverablesByPlanId(@RequestParam(value = "planId", required = false) String planId) {
        planService.deleteDeliverablesByPlanId(planId);
    }

    @ApiOperation(value = "计划导出excel",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "exportXls")
    public List<PlanDto> exportXls(@RequestBody PlanDto plan, @RequestParam(value = "projectId") String projectId) {
        Plan o = (Plan) CodeUtils.JsonBeanToBean(plan, Plan.class);
        List<Plan> list = planService.exportXls(o, projectId);
        return CodeUtils.JsonListToList(list, PlanDto.class);
    }

    @ApiOperation(value = "单条计划导出excel",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "exportXlsSingle")
    public List<PlanDto> exportXlsSingle(@RequestBody PlanDto plan) {
        Plan o = (Plan) CodeUtils.JsonBeanToBean(plan, Plan.class);
        List<Plan> list = planService.exportXlsSingle(o);
        return CodeUtils.JsonListToList(list, PlanDto.class);
    }

    @ApiOperation(value = "通过计划模板编号保存计划及子计划",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planId", value = "计划id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "来源标志", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "savePlanByPlanTemplateId")
    public FeignJson savePlanByPlanTemplateId(@RequestBody PlanTemplateDetailReq planTemplateDetailReq,
                                              @RequestParam(value = "planId", required = false) String planId, @RequestParam(value = "type", required = false) String type,
                                              @RequestParam(value = "userId", required = false) String userId, @RequestParam(value = "orgId", required = false) String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            planService.savePlanByPlanTemplateId(planTemplateDetailReq, planId, type, userId, orgId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "根据计划ID查找其所有子计划（包括计划本身和所有子孙计划）",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPlanAllChildrenByProjectId")
    public List<PlanDto> getPlanAllChildrenByProjectId(@RequestParam(value = "projectId") String projectId) {
        List<Plan> list = planService.getPlanAllChildrenByProjectId(projectId);
        return CodeUtils.JsonListToList(list, PlanDto.class);
    }

    @ApiOperation(value = "通过mpp导入计划",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveMppInfo")
    public List<MppInfo> saveMppInfo(@RequestBody List<PlanDto> plans) {
        List<Plan> list = CodeUtils.JsonListToList(plans, Plan.class);
        return planService.saveMppInfo(list);
    }

    @ApiOperation(value = "获取项目分类/计划等级信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "项目分类/计划等级id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getBusinessConfigEntity")
    public BusinessConfigDto getBusinessConfigEntity(@RequestParam("id") String id) {
        BusinessConfigDto businessConfigDto = planService.getBusinessConfigEntity(id);
        return businessConfigDto;
    }

    @ApiOperation(value = "通过excel导入计划",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doImportExcel")
    public FeignJson doImportExcel(@RequestParam(value = "userId",required = false) String userId,@RequestParam(value = "projectId",required = false) String projectId,@RequestParam(value = "type",required = false) String type,
                                   @RequestParam(value = "planId",required = false) String planId,@RequestBody List<Map<String,String>> map) {
        FeignJson feignJson = new FeignJson();
        Plan plan = new Plan();
        if (!CommonUtil.isEmpty(projectId)) {
            plan.setProjectId(projectId);
        }
        if (!CommonUtil.isEmpty(type)) {
            if (PlanImAndExConstants.NEXT_PLAN.equals(type)) {
                Plan temp = new Plan();
                temp = planService.findBusinessObjectById(Plan.class, planId);
                plan.setId(temp.getId());
                plan.setParentPlanId(temp.getParentPlanId());
            } else {
                plan.setId(planId);
            }
        }
        try {
            Map<String,Object> returnMap = planService.doImportExcel(userId,plan, type, map);
            feignJson.setObj(returnMap);
        } catch (Exception e) {
            feignJson.setMsg(e.getMessage());
            feignJson.setSuccess(false);
            e.printStackTrace();
        }
        return feignJson;
    }

    @ApiOperation(value = "获取变更输入列表",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "rows", value = "每页数量", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "isPage", value = "是否分页", dataType = "boolean")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryInputChangeList")
    public List<TempPlanInputsDto> queryInputChangeList(@RequestBody TempPlanInputsDto tempPlanInputsDto, @RequestParam(value = "page") int page, @RequestParam(value = "rows") int rows,
                                                        @RequestParam(value = "isPage") boolean isPage) {
        TempPlanInputs tempPlanInputs = (TempPlanInputs) CodeUtils.JsonBeanToBean(tempPlanInputsDto, TempPlanInputs.class);
        List<TempPlanInputs> tempPlanInputs1 = deliverablesChangeServiceI.queryInputChangeList(tempPlanInputs, page, rows, isPage);
        List<TempPlanInputsDto> list = CodeUtils.JsonListToList(tempPlanInputs1, TempPlanInputsDto.class);
        return list;
    }

    @ApiOperation(value = "获取变更输出列表",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "rows", value = "每页数量", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "isPage", value = "是否分页", dataType = "boolean")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryDeliverableChangeList")
    public List<TempPlanDeliverablesInfoDto> queryDeliverableChangeList(@RequestBody TempPlanDeliverablesInfoDto tempPlanInputsDto, @RequestParam(value = "page") int page, @RequestParam(value = "rows") int rows,
                                                                        @RequestParam(value = "isPage") boolean isPage) {
        TempPlanDeliverablesInfo tempPlanInputs = (TempPlanDeliverablesInfo) CodeUtils.JsonBeanToBean(tempPlanInputsDto, TempPlanDeliverablesInfo.class);
        List<TempPlanDeliverablesInfo> tempPlanInputs1 = deliverablesChangeServiceI.queryDeliverableChangeList(tempPlanInputs, page, rows, isPage);
        List<TempPlanDeliverablesInfoDto> list = CodeUtils.JsonListToList(tempPlanInputs1, TempPlanDeliverablesInfoDto.class);
        return list;
    }

    @ApiOperation(value = "获取变更资源列表",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "rows", value = "每页数量", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "isPage", value = "是否分页", dataType = "boolean")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryResourceChangeList")
    public List<TempPlanResourceLinkInfoDto> queryResourceChangeList(@RequestBody TempPlanResourceLinkInfoDto tempPlanInputsDto, @RequestParam(value = "page") int page, @RequestParam(value = "rows") int rows,
                                                                     @RequestParam(value = "isPage") boolean isPage) {
        TempPlanResourceLinkInfo tempPlanInputs = (TempPlanResourceLinkInfo) CodeUtils.JsonBeanToBean(tempPlanInputsDto, TempPlanResourceLinkInfo.class);
        List<TempPlanResourceLinkInfo> tempPlanInputs1 = resourceChangeServiceI.queryResourceChangeList(tempPlanInputs, page, rows, isPage);
        List<TempPlanResourceLinkInfoDto> list = CodeUtils.JsonListToList(tempPlanInputs1, TempPlanResourceLinkInfoDto.class);
        return list;
    }

    @ApiOperation(value = "通过计划编号获得Form编号",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "taskNumber", value = "计划id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "计划类型", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getTaskNumberByPlan")
    public FeignJson getTaskNumberByPlan(@RequestParam(value = "taskNumber", required = false) String taskNumber,
                                         @RequestParam(value = "type", required = false) String type) {
        FeignJson j = new FeignJson();
        String str = planService.getTaskNumberByPlan(taskNumber, type);
        j.setObj(str);
        return j;
    }

    @ApiOperation(value = "关注计划",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planId", value = "计划id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "concernCode", value = "关注代码", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "concernPlan")
    public FeignJson concernPlan(@RequestParam(value = "planId", required = false) String planId,
                                 @RequestParam(value = "concernCode", required = false) String concernCode,
                                 @RequestParam(value = "userId", required = false) String userId) {
        FeignJson j = planService.concernPlan(planId, concernCode, userId);
        return j;
    }

    @ApiOperation(value = "执行待接收流程的接收分支",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planId", value = "计划id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "flag", value = "流程标识", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "continuePlanReceiveProcess")
    public FeignJson continuePlanReceiveProcess(@RequestParam(value = "planId", required = false) String planId, @RequestParam(value = "flag", required = false) String flag, @RequestBody TSUserDto curUser) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            planService.continuePlanReceiveProcess(planId, flag, curUser);
        } catch (Exception e) {
            j.setSuccess(false);
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "执行计划待接收流程的驳回分支",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planId", value = "计划id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "refuseReason", value = "驳回原因", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "refuseRemark", value = "驳回备注", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "refusePlanReceiveProcess")
    public FeignJson refusePlanReceiveProcess(@RequestParam(value = "planId", required = false) String planId, @RequestBody TSUserDto curUser,
                                              @RequestParam(value = "refuseReason", required = false) String refuseReason, @RequestParam(value = "refuseRemark", required = false) String refuseRemark,
                                              @RequestParam(value = "orgId", required = false) String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            planService.refusePlanReceiveProcess(planId, curUser, refuseReason, refuseRemark, orgId);
        } catch (Exception e) {
            j.setSuccess(false);
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "获取计划驳回信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "refuseId", value = "驳回id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPlanRefuseInfoEntity")
    public PlanRefuseInfoDto getPlanRefuseInfoEntity(@RequestParam(value = "refuseId", required = false) String refuseId) {
        PlanRefuseInfo planRefuseInfo = planService.getPlanRefuseInfoEntity(refuseId);
        PlanRefuseInfoDto planRefuseInfoDto = new PlanRefuseInfoDto();
        Dto2Entity.copyProperties(planRefuseInfo, planRefuseInfoDto);
        return planRefuseInfoDto;
    }

    @ApiOperation(value = "执行计划待接收流程的委派分支",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planId", value = "计划id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "delegateUserId", value = "被委派人id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "changeType", value = "变更类型", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "leaderId", value = "室领导id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "departLeaderId", value = "部门领导id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "changeRemark", value = "变更原因", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "flag", value = "流程标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "delegatePlanReceiveProcess")
    public FeignJson delegatePlanReceiveProcess(@RequestParam(value = "planId", required = false) String planId, @RequestBody TSUserDto curUser,
                                                @RequestParam(value = "delegateUserId", required = false) String delegateUserId, @RequestParam(value = "changeType", required = false) String changeType,
                                                @RequestParam(value = "leaderId", required = false) String leaderId, @RequestParam(value = "departLeaderId", required = false) String departLeaderId,
                                                @RequestParam(value = "changeRemark", required = false) String changeRemark, @RequestParam(value = "flag", required = false) String flag,
                                                @RequestParam(value = "orgId", required = false) String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            planService.delegatePlanReceiveProcess(planId, curUser, delegateUserId, changeType, leaderId, departLeaderId, changeRemark, flag, orgId);
        } catch (Exception e) {
            j.setSuccess(false);
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "根据BOM节点生成计划信息",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planId", value = "计划id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "bomId", value = "bom节点Id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "code", value = "计划编号", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "计划名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doSaveBom")
    public FeignJson doSaveBom(@RequestParam(value = "planId", required = false) String planId, @RequestBody TSUserDto curUser,
                               @RequestParam(value = "bomId", required = false) String bomId, @RequestParam(value = "projectId", required = false) String projectId,
                               @RequestParam(value = "code", required = false) String code, @RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "orgId", required = false) String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            planService.doSaveBom(planId, curUser, bomId, projectId, code, name, orgId);
        } catch (Exception e) {
            j.setSuccess(false);
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "获取前置计划列表数据",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userName", value = "用户名", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "progressRate", value = "进度", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "workTime", value = "工期", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryEntityForPrepose")
    public PageList queryEntityForPrepose(@RequestBody List<ConditionVO> conditionList, @RequestParam(value = "projectId", required = false) String projectId,
                                          @RequestParam(value = "userName", required = false) String userName,
                                          @RequestParam(value = "progressRate", required = false) String progressRate, @RequestParam(value = "workTime", required = false) String workTime) {
        PageList pageList = planService.queryEntityForPrepose(conditionList, projectId, userName, progressRate, workTime);
        return pageList;
    }

    @ApiOperation(value = "通过项目id获取该项目下所有流程分解计划",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = false, dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getProcPlanList")
    public FeignJson getProcPlanList(@RequestParam(value = "projectId",required = false) String projectId,
                                     @RequestParam(value = "userId",required = false) String userId) {
        FeignJson j = new FeignJson();
        List<TreeNode> treeNodes = planService.getProcPlanList(projectId, userId);
        String json = JSONArray.toJSONString(treeNodes);
        j.setObj(json);
        return j;
    }
    
    @ApiOperation(value = "判断是否是父子节点一起发布的",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "父计划id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "checkisParentChildAllPublish")
    public FeignJson checkisParentChildAllPublish(@RequestParam(value = "id", required = false) String id) {
        return planService.checkisParentChildAllPublish(id);
    }


    @ApiOperation(value = "启动计划委派流程",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planId", value = "计划id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "delegateUserId", value = "被委派人id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "changeType", value = "变更类型", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "leaderId", value = "室领导id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "departLeaderId", value = "部门领导id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "changeRemark", value = "变更原因", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "startPlanDelegateProcess")
    public FeignJson startPlanDelegateProcess(@RequestParam(value = "planId",required = false) String planId,@RequestBody TSUserDto curUser,
                                              @RequestParam(value = "delegateUserId",required = false) String delegateUserId,@RequestParam(value = "changeType",required = false) String changeType,
                                              @RequestParam(value = "leaderId",required = false) String leaderId,@RequestParam(value = "departLeaderId",required = false) String departLeaderId,
                                              @RequestParam(value = "changeRemark",required = false) String changeRemark, @RequestParam(value = "orgId",required = false) String orgId){
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            planService.startPlanDelegateProcess(planId, curUser, delegateUserId, changeType, leaderId, departLeaderId, changeRemark, orgId);
        } catch (Exception e) {
            j.setSuccess(false);
        } finally {
            return j;
        }
    }


    @ApiOperation(value = "获取当前项目下所有计划的生命周期状态数量",httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "projId", value = "项目ID", dataType = "String")})
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getAllPlanLifeCycleArrayByProjId")
    public FeignJson getAllPlanLifeCycleArrayByProjId(@RequestParam(value = "projId", required = false) String projId) {
        return planService.getAllPlanLifeCycleArrayByProjId(projId);
    }

    @ApiOperation(value = "获取当前项目下所有计划完成情况",httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "projId", value = "项目ID", dataType = "String")})
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getAllPlanCompletionByProjId")
    public FeignJson getAllPlanCompletionByProjId(@RequestParam(value = "projId", required = false) String projId) {
        return planService.getAllPlanCompletionByProjId(projId);
    }

    @ApiOperation(value = "获取当前项目下所有计划执行情况",httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "projId", value = "项目ID", dataType = "String")})
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getAllPlanExecutByProjId")
    public FeignJson getAllPlanExecutByProjId(@RequestParam(value = "projId", required = false) String projId) {
        return planExcuteServiceI.queryPlanExcuetReport(projId);
    }

    @ApiOperation(value = "通过项目id以及需要查询的计划id集合快速查询计划",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目ID", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "avaliable", value = "是否可用", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryPlanMap")
    public Map<String, PlanDto> queryPlanMap(@RequestParam(value = "projectId",required = false) String projectId, @RequestParam(value = "avaliable",required = false) String avaliable,
                                  @RequestBody List<String> planIdList) {
        Map<String, PlanDto> resultMap = planService.queryPlanMap(projectId, avaliable, planIdList);
        return resultMap;
    }

    @ApiOperation(value = "通过项目id查询前置计划",httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "projId", value = "项目ID", dataType = "String")})
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "allPreposePlanList")
    public List<PreposePlanDto> allPreposePlanList(@RequestParam(value = "projId", required = false) String projId) {
        return planService.allPreposePlanList(projId);
    }

    @RequestMapping(value = "queryPlanListByMap")
    public List<PlanDto> queryPlanListByMap(@RequestBody Map<String,Object> map){
        List<PlanDto> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        PlanDto planDto = mapper.convertValue(map.get("planDto"),PlanDto.class);
        List<ConditionVO> conditionList = mapper.convertValue(map.get("conditionList"), new TypeReference<List<ConditionVO>>() {
        });
        String userName = String.valueOf(map.get("userName"));
        String progressRate = String.valueOf(map.get("progressRate"));
        String workTime = String.valueOf(map.get("workTime"));
        String userId = String.valueOf(map.get("userId"));
        String projectId = String.valueOf(map.get("projectId"));
        UserPlanViewProject link = planService.getUserViewProjectLinkByProjectId(projectId, userId);
        if(!CommonUtil.isEmpty(link.getPlanViewInfoId())){
            if(!CommonUtil.isEmpty(link.getProjectId())){
                if(link.getProjectId().equals(projectId)){
                    List<PlanDto> dtoList = planService.queryPlanListForTreegridView(planDto, link.getPlanViewInfoId(), conditionList, userName, progressRate, workTime, userId);
                    list = planService.queryPlanListForTreegridWithView(dtoList, link.getPlanViewInfoId(), userId);

                }else{
                    List<PlanDto> dtoList = planService.queryPlanListForTreegridView(planDto, "4028f00763ba3b200163ba6446bf000f", conditionList, userName, progressRate, workTime, userId);
                    list = planService.queryPlanListForTreegridWithView(dtoList,"4028f00763ba3b200163ba6446bf000f",userId);
                }
            }else{
                List<PlanDto> dtoList = planService.queryPlanListForTreegridView(planDto, link.getPlanViewInfoId(), conditionList, userName, progressRate, workTime, userId);
                list = planService.queryPlanListForTreegridWithView(dtoList,link.getPlanViewInfoId(),userId);
            }

        }else{
            list = planService.queryPlanListForTreegrid(planDto);
        }
        return list;
    }
}
