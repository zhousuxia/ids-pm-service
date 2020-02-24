package com.glaway.ids.project.planview.controller;

import io.swagger.annotations.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaway.foundation.cache.service.RedisService;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.OperationLog;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.I18nUtil;
import com.glaway.foundation.common.util.UserUtil;
import com.glaway.foundation.common.util.VDataUtil;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.common.vo.TreeNode;
import com.glaway.foundation.core.common.controller.BaseController;
import com.glaway.foundation.core.common.model.json.AjaxJson;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.config.service.PlanBusinessConfigServiceI;
import com.glaway.ids.project.plan.dto.PlanViewInfoDto;
import com.glaway.ids.project.plan.dto.PlanViewSearchConditionDto;
import com.glaway.ids.project.plan.entity.PlanViewInfo;
import com.glaway.ids.project.plan.entity.PlanViewSearchCondition;
import com.glaway.ids.project.plan.entity.PlanViewSetCondition;
import com.glaway.ids.project.planview.dto.PlanViewSetConditionDto;
import com.glaway.ids.project.planview.service.PlanViewServiceI;
import com.glaway.ids.util.Dto2Entity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 计划视图实现方法
 * 〈功能详细描述〉
 *
 * @author likaiyong
 * @version 2018年5月30日
 * @see PlanViewRestController
 * @since
 */
@Api(tags = {"计划视图接口"})
@RestController
@RequestMapping("/feign/planViewRestController")
public class PlanViewRestController extends BaseController {
    private static final OperationLog log = BaseLogFactory.getOperationLog(PlanViewRestController.class);
    /**
     * 配置业务接口
     */
    @Autowired
    private PlanBusinessConfigServiceI businessConfigService;
    /**
     * redis缓存服务
     */
    @Autowired
    private RedisService redisService;
    @Autowired
    private PlanViewServiceI planViewService;

    @ApiOperation(value = "是否更新视图",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planViewInfoId", value = "视图id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "isUpdateCondition")
    public boolean isUpdateCondition(@RequestParam("planViewInfoId") String planViewInfoId, @RequestParam("userId") String userId) {
        boolean flag = planViewService.isUpdateCondition(planViewInfoId, userId);
        return flag;
    }

    @ApiOperation(value = "获取视图查询条件",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planViewInfoId", value = "视图id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getSearchConditionByPlanView")
    public List<PlanViewSearchConditionDto> getSearchConditionByPlanView(@RequestParam("planViewInfoId") String planViewInfoId) {
        List<PlanViewSearchCondition> tempList = planViewService.getSearchConditionByPlanView(planViewInfoId);
        List<PlanViewSearchConditionDto> list = JSON.parseArray(JSON.toJSONString(tempList), PlanViewSearchConditionDto.class);
        return list;
    }

    @ApiOperation(value = "保存查询条件",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planViewInfoId", value = "视图id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveSearchCondition")
    public FeignJson saveSearchCondition(@RequestBody Map<String, Object> viewMap, @RequestParam("planViewInfoId") String planViewInfoId, @RequestParam("orgId") String orgId) {
        FeignJson j = new FeignJson();
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<ConditionVO> conditionList = mapper.convertValue(viewMap.get("conditionList"), new TypeReference<List<ConditionVO>>() {
            });
            List<String> existConditionList = mapper.convertValue(viewMap.get("existConditionList"), new TypeReference<List<String>>() {
            });
            TSUserDto currentUser = mapper.convertValue(viewMap.get("currentUser"), TSUserDto.class);
            planViewService.saveSearchCondition(conditionList, planViewInfoId, existConditionList, currentUser, orgId);
            j.setSuccess(true);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "更新视图的展示列信息",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planViewInfoId", value = "视图id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "showColumnIds", value = "展示列ids", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "updatePlanViewColumn")
    public FeignJson updatePlanViewColumn(@RequestParam("planViewInfoId") String planViewInfoId, @RequestParam("showColumnIds") String showColumnIds,
                                          @RequestBody TSUserDto curUser, @RequestParam("orgId") String orgId) {
        FeignJson j = new FeignJson();
        try {
            planViewService.updatePlanViewColumn(planViewInfoId, showColumnIds, curUser, orgId);
            j.setSuccess(true);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "根据视图名称、状态及创建者获取视图列表",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planViewInfoName", value = "视图名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "status", value = "状态", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "createName", value = "用户名", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPlanViewInfoByViewNameAndStatusAndCreateName")
    public List<PlanViewInfoDto> getPlanViewInfoByViewNameAndStatusAndCreateName(@RequestParam("planViewInfoName") String planViewInfoName, @RequestParam("status") String status,
                                                                                 @RequestParam("createName") String createName) {
        List<PlanViewInfo> tempList = planViewService.getPlanViewInfoByViewNameAndStatusAndCreateName(planViewInfoName, status, createName);
        List<PlanViewInfoDto> list = JSON.parseArray(JSON.toJSONString(tempList), PlanViewInfoDto.class);
        return list;
    }

    @ApiOperation(value = "另存新视图",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planViewInfoId", value = "视图id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "planViewInfoName", value = "视图名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveAsNewView")
    public FeignJson saveAsNewView(@RequestParam("planViewInfoId") String planViewInfoId, @RequestParam("planViewInfoName") String planViewInfoName,
                                   @RequestBody TSUserDto curUser, @RequestParam("orgId") String orgId) {
        FeignJson j = new FeignJson();
        try {
            String viewId = planViewService.saveAsNewView(planViewInfoId, planViewInfoName, curUser, orgId);
            j.setObj(viewId);
            j.setSuccess(true);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "判断视图名称是否重复",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", value = "视图名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "status", value = "视图状态", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "视图id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getViewCountByStatusAndName")
    public boolean getViewCountByStatusAndName(@RequestParam("name") String name, @RequestParam("status") String status, @RequestParam("id") String id) {
        boolean flag = planViewService.getViewCountByStatusAndName(name, status, id);
        return flag;
    }

    @ApiOperation(value = "按部门设置保存",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "departmentId", value = "部门id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "视图名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveSetConditionByDeaprtment")
    public FeignJson saveSetConditionByDeaprtment(@RequestParam("departmentId") String departmentId, @RequestParam("name") String name,
                                                  @RequestBody TSUserDto curUser, @RequestParam("orgId") String orgId) {
        FeignJson j = new FeignJson();
        try {
            String planViewInfoId = planViewService.saveSetConditionByDeaprtment(departmentId, name, curUser, orgId);
            j.setObj(planViewInfoId);
            j.setSuccess(true);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "保存展示列信息",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planViewInfoId", value = "部门id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveColumnInfo")
    public FeignJson saveColumnInfo(@RequestParam("planViewInfoId") String planViewInfoId, @RequestBody TSUserDto curUser, @RequestParam("orgId") String orgId) {
        FeignJson j = new FeignJson();
        try {
            planViewService.saveColumnInfo(planViewInfoId, curUser, orgId);
            j.setSuccess(true);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "获取计划视图信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "视图id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPlanViewInfoEntity")
    public PlanViewInfoDto getPlanViewInfoEntity(@RequestParam("id") String id) {
        PlanViewInfo info = planViewService.getEntity(PlanViewInfo.class, id);
        PlanViewInfoDto dto = new PlanViewInfoDto();
        try {
            dto = (PlanViewInfoDto) VDataUtil.getVDataByEntity(info, PlanViewInfoDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    @ApiOperation(value = "根据视图ID查询设置条件对象",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planViewInfoId", value = "视图id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getBeanByPlanViewInfoId")
    public PlanViewSetConditionDto getBeanByPlanViewInfoId(@RequestParam("planViewInfoId") String planViewInfoId) {
        PlanViewSetCondition condition = planViewService.getBeanByPlanViewInfoId(planViewInfoId);
        PlanViewSetConditionDto dto = new PlanViewSetConditionDto();
        try {
            dto = (PlanViewSetConditionDto) VDataUtil.getVDataByEntity(condition, PlanViewSetConditionDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    @ApiOperation(value = "保存或更新视图",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveOrUpdatePlanViewInfo")
    public FeignJson saveOrUpdatePlanViewInfo(@RequestBody PlanViewInfoDto planViewInfoDto) {
        FeignJson j = new FeignJson();
        try {
            PlanViewInfo planViewInfo = new PlanViewInfo();
            Dto2Entity.copyProperties(planViewInfoDto, planViewInfo);
            if (CommonUtil.isEmpty(planViewInfo.getId())) {
                planViewService.save(planViewInfo);
            } else {
                planViewService.update(planViewInfo);
            }
            j.setObj(planViewInfo.getId());
            j.setSuccess(true);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "保存或更新视图查询条件",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveOrUpdatePlanViewSetCondition")
    public FeignJson saveOrUpdatePlanViewSetCondition(@RequestBody PlanViewSetConditionDto planViewSetConditionDto) {
        FeignJson j = new FeignJson();
        try {
            PlanViewSetCondition planViewSetCondition = new PlanViewSetCondition();
            Dto2Entity.copyProperties(planViewSetConditionDto, planViewSetCondition);
            if (CommonUtil.isEmpty(planViewSetCondition.getId())) {
                planViewService.save(planViewSetCondition);
            } else {
                planViewService.update(planViewSetCondition);
            }
            j.setObj(planViewSetCondition.getId());
            j.setSuccess(true);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "保存自定义视图设置",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveCustomView")
    public FeignJson saveCustomView(@RequestBody Map<String, Object> map, @RequestParam("projectId") String projectId, @RequestParam("orgId") String orgId) {
        FeignJson j = new FeignJson();
        try {
            ObjectMapper mapper = new ObjectMapper();
            PlanViewInfoDto planViewInfoDto = mapper.convertValue(map.get("info"), PlanViewInfoDto.class);
            TSUserDto curUser = mapper.convertValue(map.get("curUser"), TSUserDto.class);
            List<PlanViewSetConditionDto> tempconditionList = mapper.convertValue(map.get("conditionList"), new TypeReference<List<PlanViewSetConditionDto>>() {
            });
            PlanViewInfo planViewInfo = new PlanViewInfo();
            Dto2Entity.copyProperties(planViewInfoDto, planViewInfo);
            List<PlanViewSetCondition> conditionList = JSON.parseArray(JSON.toJSONString(tempconditionList), PlanViewSetCondition.class);
            planViewService.saveCustomView(planViewInfo, conditionList, projectId, curUser, orgId);
            j.setObj(planViewInfo.getId());
            j.setSuccess(true);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "获取视图列表",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getViewList")
    public List<PlanViewInfoDto> getViewList(@RequestParam("projectId") String projectId, @RequestBody TSUserDto userDto) {
        List<PlanViewInfo> tempList = planViewService.getViewList(projectId, userDto);
        List<PlanViewInfoDto> list = JSON.parseArray(JSON.toJSONString(tempList), PlanViewInfoDto.class);
        return list;
    }

    @ApiOperation(value = "获取视图设置条件",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planViewInfoId", value = "视图id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getSetConditionByPlanView")
    public List<PlanViewSetConditionDto> getSetConditionByPlanView(@RequestParam("planViewInfoId") String planViewInfoId) {
        List<PlanViewSetCondition> tempList = planViewService.getSetConditionByPlanView(planViewInfoId);
        List<PlanViewSetConditionDto> list = JSON.parseArray(JSON.toJSONString(tempList), PlanViewSetConditionDto.class);
        return list;
    }

    @ApiOperation(value = "发布视图",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planViewInfoId", value = "视图id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectIds", value = "项目ids", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userIds", value = "用户ids", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "视图名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "publishView")
    public FeignJson publishView(@RequestParam("planViewInfoId") String planViewInfoId, @RequestParam("projectIds") String projectIds,
                                 @RequestParam("userIds") String userIds, @RequestParam("name") String name, @RequestBody TSUserDto curUser, @RequestParam("orgId") String orgId) {
        FeignJson j = new FeignJson();
        try {
            planViewService.publishView(planViewInfoId, projectIds, userIds, name, curUser, orgId);
            j.setSuccess(true);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "取消发布视图",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planViewInfoId", value = "视图id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "status", value = "视图状态", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "cancelPublishView")
    public FeignJson cancelPublishView(@RequestParam("planViewInfoId") String planViewInfoId, @RequestParam("status") String status) {
        FeignJson j = new FeignJson();
        try {
            planViewService.cancelPublishView(planViewInfoId, status);
            j.setSuccess(true);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "删除视图",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planViewInfoId", value = "视图id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "deletePlanViewInfo")
    public FeignJson deletePlanViewInfo(@RequestParam("planViewInfoId") String planViewInfoId) {
        FeignJson j = new FeignJson();
        try {
            planViewService.deleteEntityById(PlanViewInfo.class, planViewInfoId);
            j.setSuccess(true);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }
}
