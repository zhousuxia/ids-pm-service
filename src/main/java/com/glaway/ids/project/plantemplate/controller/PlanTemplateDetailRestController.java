package com.glaway.ids.project.plantemplate.controller;

import io.swagger.annotations.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaway.foundation.core.common.controller.BaseController;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.project.plan.dto.InputsDto;
import com.glaway.ids.project.plan.dto.PlanDto;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plantemplate.dto.PlanTemplateDetailDto;
import com.glaway.ids.util.CodeUtils;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.I18nUtil;
import com.glaway.foundation.common.util.JsonUtil;
import com.glaway.foundation.common.util.PropertiesUtil;
import com.glaway.ids.constant.CommonConstants;
import com.glaway.ids.models.HttpClientUtil;
import com.glaway.ids.models.JsonResult;
import com.glaway.ids.project.plan.entity.DeliverablesInfo;
import com.glaway.ids.project.plan.entity.Inputs;
import com.glaway.ids.project.plan.service.InputsServiceI;
import com.glaway.ids.project.plantemplate.entity.PlanTempOptLog;
import com.glaway.ids.project.plantemplate.entity.PlanTemplate;
import com.glaway.ids.project.plantemplate.entity.PlanTemplateDetail;
import com.glaway.ids.project.plantemplate.service.PlanTemplateDetailServiceI;
import com.glaway.ids.project.plantemplate.service.PlanTemplateServiceI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author duanpengfei
 * @version V1.0
 * @Title: Controller
 * @Description: 计划模板详情接口
 * @date 2015-03-27 16:54:10
 */
@Api(tags = {"计划模板详情接口"})
@RestController
@RequestMapping("/feign/planTemplateDetailRestController")
public class PlanTemplateDetailRestController extends BaseController {
    @Autowired
    private PlanTemplateDetailServiceI planTemplateDetailService;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 任务输入Service
     */
    @Autowired
    private InputsServiceI inputsService;

    @ApiOperation(value = "获取计划模板/项目模板数据id与前置计划集合",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "plantemplateId", value = "模板id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "查询类型", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPlanTemplateOrProjTemplateDetailPreposes")
    public FeignJson getPlanTemplateOrProjTemplateDetailPreposes(@RequestParam(value = "plantemplateId", required = false) String plantemplateId, @RequestParam(value = "type", required = false) String type) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        Map<String, String> map = new HashMap<String, String>();
        try {
            map = planTemplateDetailService.getPlanTemplateOrProjTemplateDetailPreposes(plantemplateId, type);
            j.setObj(map);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ApiOperation(value = "获取计划模板数据id与前置计划id集合",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "plantemplateId", value = "模板id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPlanTemplateDetailPreposesId")
    public FeignJson getPlanTemplateDetailPreposesId(@RequestParam("plantemplateId") String plantemplateId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        Map<String, String> map = new HashMap<String, String>();
        try {
            map = planTemplateDetailService.getPlanTemplateDetailPreposesId(plantemplateId);
            j.setObj(map);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ApiOperation(value = "获取计划模板数据列表",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planTemplateId", value = "模板id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPlanTemplateDetailList")
    public FeignJson getPlanTemplateDetailList(@RequestParam("planTemplateId") String planTemplateId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            List<PlanTemplateDetail> list = planTemplateDetailService.getPlanTemplateDetailList(planTemplateId);
            List<PlanTemplateDetailDto> dtoList = CodeUtils.JsonListToList(list, PlanTemplateDetailDto.class);
            j.setObj(dtoList);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ApiOperation(value = "获取计划模板/项目模板数据交付项",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "plantemplateId", value = "模板id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "查询类型", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPlanTemplateOrProjTemplateDetailDeliverables")
    public Map<String, String> getPlanTemplateOrProjTemplateDetailDeliverables(@RequestParam("plantemplateId") String plantemplateId, @RequestParam("type") String type) {
        Map<String, String> map = new HashMap<>();
        map = planTemplateDetailService.getPlanTemplateOrProjTemplateDetailDeliverables(plantemplateId, type);
        return map;
    }

    @ApiOperation(value = "获取计划模板数据输入项名称",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPlanTemplateDetailInputsName")
    public Map<String, String> getPlanTemplateDetailInputsName(@RequestBody List<InputsDto> list) {
        Map<String, String> map = new HashMap<>();
        List<Inputs> inputsList = JSON.parseArray(JSON.toJSONString(list), Inputs.class);
        map = planTemplateDetailService.getPlanTemplateDetailInputsName(inputsList);
        return map;
    }

    @ApiOperation(value = "获取输入项来源",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPlanTemplateDetailInputsOrigin")
    public Map<String, String> getPlanTemplateDetailInputsOrigin(@RequestBody Map<String, Object> paramsMap) {
        Map<String, String> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        List<InputsDto> tempInputsList = mapper.convertValue(paramsMap.get("inputsList"), new TypeReference<List<InputsDto>>() {
        });
        List<PlanTemplateDetailDto> tempPlanTemplateDetailList = mapper.convertValue(paramsMap.get("planTemplateDetailList"), new TypeReference<List<PlanTemplateDetailDto>>() {
        });
        Map<String, String> detailIdDeliverablesMap = (Map<String, String>) paramsMap.get("detailIdDeliverablesMap");
        List<Inputs> inputsList = JSON.parseArray(JSON.toJSONString(tempInputsList), Inputs.class);
        List<PlanTemplateDetail> planTemplateDetailList = JSON.parseArray(JSON.toJSONString(tempPlanTemplateDetailList), PlanTemplateDetail.class);
        map = planTemplateDetailService.getPlanTemplateDetailInputsOrigin(inputsList, planTemplateDetailList, detailIdDeliverablesMap);
        return map;
    }
}
