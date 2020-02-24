package com.glaway.ids.project.plan.controller;

import io.swagger.annotations.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.OperationLog;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.VDataUtil;
import com.glaway.foundation.core.common.controller.BaseController;
import com.glaway.ids.project.plan.dto.PlanLogDto;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.entity.PlanLog;
import com.glaway.ids.project.plan.form.PlanLogInfo;
import com.glaway.ids.project.plan.service.PlanLogServiceI;
import com.glaway.ids.util.Dto2Entity;

/**
 * 计划日志接口
 *
 * @author wqb
 * @version V1.0
 * @Title: Controller
 * @Description: 项目模板Controller
 * @date 2019年8月2日 15:25:43
 */
@Api(tags = {"计划日志接口"})
@RestController
@RequestMapping("/feign/planLogRestController")
public class PlanLogRestController extends BaseController {
    /**
     * Logger for this class
     */
    private static final OperationLog log = BaseLogFactory.getOperationLog(PlanLogRestController.class);
    @Autowired
    private PlanLogServiceI planLogService;

    @ApiOperation(value = "获取计划日志列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "rows", value = "每页数量", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "isPage", value = "是否分页", dataType = "boolean")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "findPlanLogByPlanId")
    List<PlanLogDto> findPlanLogByPlanId(@RequestBody PlanLogInfo planLogInfo, @RequestParam(value = "page", required = false) int page
            , @RequestParam(value = "rows", required = false) int rows, @RequestParam(value = "isPage", required = false) boolean isPage) {
        List<PlanLog> aList = planLogService.findPlanLogByPlanId(planLogInfo, page, rows, isPage);
        List<PlanLogDto> result = new ArrayList<PlanLogDto>();
        try {
            result = (List<PlanLogDto>) VDataUtil.getVDataByEntity(aList, PlanLogDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ApiOperation(value = "保存计划日志", httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "savePlanLog")
    void savePlanLog(@RequestBody PlanLogDto planLog) {
        PlanLog p = new PlanLog();
        Dto2Entity.copyProperties(planLog, p);
        planLogService.savePlanLog(p);
    }

    @ApiOperation(value = "获取计划日志信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "计划日志id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPlanLogEntity")
    PlanLogDto getPlanLogEntity(@RequestParam(value = "id", required = false) String id) {
        PlanLogDto result = new PlanLogDto();
        try {
            PlanLog planLog = planLogService.getPlanLogEntity(id);
            if (!CommonUtil.isEmpty(planLog)) {
                result = (PlanLogDto) VDataUtil.getVDataByEntity(planLog, PlanLogDto.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
