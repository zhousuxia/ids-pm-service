package com.glaway.ids.project.plan.controller;

import io.swagger.annotations.*;
import com.alibaba.fastjson.JSONObject;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.project.plan.dto.BasicLineDto;
import com.glaway.ids.project.plan.dto.BasicLinePlanDto;
import com.glaway.ids.project.plan.entity.BasicLine;
import com.glaway.ids.project.plan.entity.BasicLinePlan;
import com.glaway.ids.project.plan.service.BasicLineServiceI;
import com.glaway.ids.util.CodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 基线接口
 */
@Api(tags = {"基线接口"})
@RestController
@RequestMapping("/feign/basicLineRestController")
public class BasicLineRestController {

    /**
     * 基线接口服务
     */
    @Autowired
    BasicLineServiceI basicLineService;

    /**
     * 获取基线生命周期
     *
     * @return
     */
    @ApiOperation(value = "获取基线生命周期", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getLifeCycleStatusList")
    public String getLifeCycleStatusList() {
        String lifeCycleStr = basicLineService.getLifeCycleStatusList();
        return lifeCycleStr;
    }

    /**
     * 根据ID获取基线信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据ID获取基线信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "基线id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getBasicLineEntity")
    public BasicLineDto getBasicLineEntity(@RequestParam(value = "id") String id) {
        BasicLine basicLine = basicLineService.getBasicLineEntity(id);
        BasicLineDto o = (BasicLineDto) CodeUtils.JsonBeanToBean(basicLine, BasicLineDto.class);
        return o;
    }

    /**
     * 获取基线计划
     *
     * @param basicLinePlan
     * @param page
     * @param rows
     * @param isPage
     * @return
     */
    @ApiOperation(value = "获取基线计划", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "rows", value = "每页数量", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "isPage", value = "是否分页", dataType = "boolean")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryBasicLinePlanList")
    public List<BasicLinePlanDto> queryBasicLinePlanList(@RequestBody BasicLinePlanDto basicLinePlan, @RequestParam(value = "page") int page, @RequestParam(value = "rows") int rows, @RequestParam(value = "isPage") boolean isPage) {
        BasicLinePlan o = (BasicLinePlan) CodeUtils.JsonBeanToBean(basicLinePlan, BasicLinePlan.class);
        List<BasicLinePlan> list = basicLineService.queryBasicLinePlanList(o, page, rows, isPage);
        return CodeUtils.JsonListToList(list, BasicLinePlanDto.class);
    }

    /**
     * 将基线计划信息转为JSONObjects
     *
     * @param validList
     * @return
     */
    @ApiOperation(value = "将基线计划信息转为JSONObjects", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "changePlansToJSONObjects")
    public List<JSONObject> changePlansToJSONObjects(@RequestBody List<BasicLinePlanDto> validList) {
        List<BasicLinePlan> list = CodeUtils.JsonListToList(validList, BasicLinePlan.class);
        return basicLineService.changePlansToJSONObjects(list);
    }

    /**
     * 根据ID获取基线计划信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据ID获取基线计划信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "基线计划id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getBasicLinePlanEntity")
    public BasicLinePlanDto getBasicLinePlanEntity(@RequestParam(value = "id") String id) {
        BasicLinePlan basicLine = basicLineService.getBasicLinePlanEntity(id);
        BasicLinePlanDto o = (BasicLinePlanDto) CodeUtils.JsonBeanToBean(basicLine, BasicLinePlan.class);
        return o;
    }

    /**
     * 保存基线信息
     *
     * @param basicLine
     * @param ids
     * @param basicLineName
     * @param remark
     * @param type
     * @param projectId
     * @return
     */
    @ApiOperation(value = "saveBasicLine", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "计划ids", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "basicLineName", value = "基线名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "备注", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目Id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveBasicLine")
    public FeignJson saveBasicLine(@RequestBody BasicLineDto basicLine, @RequestParam(value = "ids", required = false) String ids, @RequestParam(value = "basicLineName", required = false) String basicLineName, @RequestParam(value = "remark", required = false) String remark, @RequestParam(value = "type", required = false) String type, @RequestParam(value = "projectId", required = false) String projectId) {
        BasicLine o = (BasicLine) CodeUtils.JsonBeanToBean(basicLine, BasicLine.class);
        return basicLineService.saveBasicLine(o, ids, basicLineName, remark, type, projectId);
    }

    /**
     * 删除基线
     *
     * @param basicLine
     */
    @ApiOperation(value = "删除基线", httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "deleteBasicLine")
    public void deleteBasicLine(@RequestBody BasicLineDto basicLine) {
        BasicLine o = (BasicLine) CodeUtils.JsonBeanToBean(basicLine, BasicLine.class);
        basicLineService.deleteBasicLine(o);
    }

    /**
     * 冻结基线
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "冻结基线", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "基线id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doFrozeBasicLine")
    public FeignJson doFrozeBasicLine(@RequestParam(value = "id") String id) {
        return basicLineService.doFrozeBasicLine(id);
    }

    /**
     * 启用基线
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "启用基线", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "基线id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doUseBasicLine")
    public FeignJson doUseBasicLine(@RequestParam(value = "id") String id) {
        return basicLineService.doUseBasicLine(id);
    }

    /**
     * 启动基线审批流程
     *
     * @param basicLineId
     * @param leader
     * @param deptLeader
     * @param userId
     * @return
     */
    @ApiOperation(value = "启动基线审批流程", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "basicLineId", value = "基线id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "leader", value = "室领导", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "deptLeader", value = "部领导", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "当前用户Id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "startBasicLine")
    public FeignJson startBasicLine(@RequestParam(value = "basicLineId") String basicLineId, @RequestParam(value = "leader") String leader, @RequestParam(value = "deptLeader") String deptLeader, @RequestParam(value = "userId") String userId) {
        return basicLineService.startBasicLine(basicLineId, leader, deptLeader, userId);
    }

    /**
     * 驳回之后再次启动基线审批流程
     *
     * @param basicLine
     * @param basicLineId
     * @param taskId
     * @param basicLineName
     * @param remark
     * @return
     */
    @ApiOperation(value = "驳回之后再次启动基线审批流程", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "basicLineId", value = "基线id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "taskId", value = "流程任务ID", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "basicLineName", value = "基线名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "备注", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "startBasicLineFlow")
    public FeignJson startBasicLineFlow(@RequestBody BasicLineDto basicLine, @RequestParam(value = "basicLineId") String basicLineId, @RequestParam(value = "taskId") String taskId, @RequestParam(value = "basicLineName") String basicLineName, @RequestParam(value = "remark") String remark) {
        BasicLine o = (BasicLine) CodeUtils.JsonBeanToBean(basicLine, BasicLine.class);
        return basicLineService.startBasicLineFlow(o, basicLineId, taskId, basicLineName, remark);
    }

    /**
     * 根据条件获取基线信息
     *
     * @param conditionList
     * @param projectId
     * @param userName
     * @return
     */
    @ApiOperation(value = "根据条件获取基线信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目Id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userName", value = "用户userName", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "searchDatagrid")
    public FeignJson searchDatagrid(@RequestBody List<ConditionVO> conditionList, @RequestParam(value = "projectId") String projectId, @RequestParam(value = "userName") String userName) {
        FeignJson feignJson = new FeignJson();
        String s = basicLineService.searchDatagrid(conditionList, projectId, userName);
        feignJson.setObj(s);
        return feignJson;
    }
}
