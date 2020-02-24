package com.glaway.ids.project.plan.controller;

import io.swagger.annotations.*;
import com.alibaba.fastjson.JSON;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.OperationLog;
import com.glaway.foundation.common.util.VDataUtil;
import com.glaway.foundation.core.common.controller.BaseController;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.project.plan.dto.PlanDto;
import com.glaway.ids.project.plan.dto.PreposePlanDto;
import com.glaway.ids.project.plan.dto.TempPlanResourceLinkInfoDto;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.entity.PreposePlan;
import com.glaway.ids.project.plan.entity.TempPlanResourceLinkInfo;
import com.glaway.ids.project.plan.service.PreposePlanServiceI;
import com.glaway.ids.project.plantemplate.service.PreposePlanTemplateServiceI;
import com.glaway.ids.util.CodeUtils;
import com.glaway.ids.util.Dto2Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wqb
 * @version V1.0
 * @Title: Controller
 * @Description: 前置计划
 * @date 2019年7月29日 19:53:49
 */
@Api(tags = {"前置计划接口"})
@RestController
@RequestMapping("/feign/preposePlanRestController")
public class PreposePlanRestController extends BaseController {
    /**
     * Logger for this class
     */
    // private static final Logger logger = Logger.getLogger(DeliverablesInfoController.class);
    private static final OperationLog log = BaseLogFactory.getOperationLog(PreposePlanRestController.class);
    /**
     * 前置计划Service
     */
    @Autowired
    private PreposePlanServiceI preposePlanService;
    @Autowired
    private PreposePlanTemplateServiceI preposePlanTemplateService;

    @ApiOperation(value = "根据计划ID查询其前置计划", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPreposePlansByPlanId")
    public List<PreposePlanDto> getPreposePlansByPlanId(@RequestBody PlanDto dto) {
        Plan inp = new Plan();
        Dto2Entity.copyProperties(dto, inp);
        List<PreposePlan> list = preposePlanService.getPreposePlansByPlanId(inp);
        List<PreposePlanDto> resList = new ArrayList<>();
        try {
            resList = (List<PreposePlanDto>) VDataUtil.getVDataByEntity(list, PreposePlanDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resList;
    }

    @ApiOperation(value = "根据计划ID查询其后置计划",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPostposesByPreposeId")
    public List<PreposePlanDto> getPostposesByPreposeId(@RequestBody PlanDto dto) {
        Plan inp = new Plan();
        Dto2Entity.copyProperties(dto, inp);
        List<PreposePlan> list = preposePlanService.getPostposesByPreposeId(inp);
        List<PreposePlanDto> resList = JSON.parseArray(JSON.toJSONString(list), PreposePlanDto.class);
        return resList;
    }

    /**
     * 根据计划ID查询其前置计划
     *
     * @return
     * @see
     */
    @ApiOperation(value = "根据计划ID查询其前置计划",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPreposePlansByParent")
    List<PreposePlanDto> getPreposePlansByParent(@RequestBody PlanDto parent) {
        Plan inp = new Plan();
        Dto2Entity.copyProperties(parent, inp);
        List<PreposePlan> list = preposePlanService.getPreposePlansByParent(inp);
        List<PreposePlanDto> resList = new ArrayList<>();
//        List<PreposePlanDto> resList = JSON.parseArray(JSON.toJSONString(list),PreposePlanDto.class);
        try {
            resList = (List<PreposePlanDto>) VDataUtil.getVDataByEntity(list, PreposePlanDto.class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resList;
    }

    @ApiOperation(value = "获取前置计划信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "前置计划id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPreposePlanEntity")
    PreposePlanDto getPreposePlanEntity(@RequestParam("id") String id) {
        PreposePlan preposePlan = preposePlanService.getPreposePlanEntity(id);
        PreposePlanDto resList = new PreposePlanDto();
        try {
            resList = (PreposePlanDto) VDataUtil.getVDataByEntity(preposePlan, PlanDto.class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resList;
    }

    @ApiOperation(value = "删除前置计划",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "前置计划id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "deleteById")
    void deleteById(@RequestParam("id") String id) {
        preposePlanService.deleteById(id);
    }

    @ApiOperation(value = "获取该计划的后置计划ids",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planId", value = "计划id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPostPlanListByPlanId")
    public FeignJson getPostPlanListByPlanId(@RequestParam(value = "planId", required = false) String planId, @RequestBody Map<String, String> preposeMap) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            String postIds = preposePlanTemplateService.getPostPlanListByPlanId(planId, preposeMap);
            j.setObj(postIds);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "查询变更资源列表",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "rows", value = "每页数量", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "isPage", value = "是否分页", dataType = "boolean")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryResourceChangeListOrderBy")
    public List<TempPlanResourceLinkInfoDto> queryResourceChangeListOrderBy(@RequestBody TempPlanResourceLinkInfoDto tempPlanResourceLinkInfoDto, @RequestParam(value = "page") int page, @RequestParam(value = "rows") int rows, @RequestParam(value = "isPage") boolean isPage) {
        TempPlanResourceLinkInfo tempPlanResourceLinkInfo = (TempPlanResourceLinkInfo) CodeUtils.JsonBeanToBean(tempPlanResourceLinkInfoDto, TempPlanResourceLinkInfo.class);
        List<TempPlanResourceLinkInfo> list = preposePlanTemplateService.queryResourceChangeListOrderBy(tempPlanResourceLinkInfo, page, rows, isPage);
        List<TempPlanResourceLinkInfoDto> list1 = CodeUtils.JsonListToList(list, TempPlanResourceLinkInfoDto.class);
        return list1;
    }
}
