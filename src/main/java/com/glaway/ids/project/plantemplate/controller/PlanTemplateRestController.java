package com.glaway.ids.project.plantemplate.controller;

import com.glaway.ids.constant.PlanImAndExConstants;
import io.swagger.annotations.*;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaway.foundation.activiti.facade.WorkFlowFacade;
import com.glaway.foundation.cache.service.RedisService;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.OperationLog;
import com.glaway.foundation.common.util.*;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.core.common.controller.BaseController;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.system.lifecycle.entity.LifeCycleStatus;
import com.glaway.ids.config.service.PlanBusinessConfigServiceI;
import com.glaway.ids.models.HttpClientUtil;
import com.glaway.ids.models.JsonResult;
import com.glaway.ids.project.plan.dto.InputsDto;
import com.glaway.ids.project.plan.dto.PlanDto;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plantemplate.constant.PlantemplateConstant;
import com.glaway.ids.project.plantemplate.dto.PlanTempOptLogDto;
import com.glaway.ids.project.plantemplate.dto.PlanTemplateDetailDto;
import com.glaway.ids.project.plantemplate.dto.PlanTemplateDto;
import com.glaway.ids.project.plantemplate.entity.PlanTempOptLog;
import com.glaway.ids.project.plantemplate.entity.PlanTemplate;
import com.glaway.ids.project.plantemplate.entity.PlanTemplateDetail;
import com.glaway.ids.project.plantemplate.service.PlanTemplateServiceI;
import com.glaway.ids.project.plantemplate.support.planTemplate.vo.PlanTemplateReq;
import com.glaway.ids.project.plantemplate.support.planTemplateDetail.vo.PlanTemplateExcelVo;
import com.glaway.ids.project.plantemplate.utils.SupportConstants;
import com.glaway.ids.util.CodeUtils;
import com.glaway.ids.util.Dto2Entity;
import com.glaway.ids.util.mpputil.MppInfo;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.mpxj.Task;
import org.apache.commons.lang.xwork.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author onlineGenerator
 * @version V1.0
 * @Title: Controller
 * @Description: 计划模板接口
 * @date 2015-03-20 15:40:03
 */
@Api(tags = {"计划模板接口"})
@RestController
@RequestMapping("/feign/planTemplateRestController")
public class PlanTemplateRestController extends BaseController {
    /**
     * Logger for this class
     */
    private static final OperationLog log = BaseLogFactory.getOperationLog(PlanTemplateRestController.class);
    @Autowired
    private SessionFacade sessionFacade;
    /**
     * redis缓存服务
     */
    @Autowired
    private RedisService redisService;
    /**
     * 配置业务接口
     */
    @Autowired
    private PlanBusinessConfigServiceI businessConfigService;
    @Autowired
    private PlanTemplateServiceI planTemplateService;
    /**
     * 注入WorkFlowFacade
     */
    @Autowired
    private WorkFlowFacade workFlowFacade;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @ApiOperation(value = "获取计划模板生命周期数据",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getLifeCycleStatusList")
    public FeignJson getLifeCycleStatusList() {
        FeignJson fj = new FeignJson();
        String lifeCycleStr = planTemplateService.getLifeCycleStatusList();
        fj.setObj(lifeCycleStr);
        return fj;
    }

    @ApiOperation(value = "获取计划模板列表信息",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryEntity")
    public PageList queryEntity(@RequestBody Map<String, Object> map) {
        ObjectMapper mapper = new ObjectMapper();
        List<ConditionVO> conditionLst = mapper.convertValue(map.get("conditionLst"), new TypeReference<List<ConditionVO>>() {
        });
        Map<String, String> params = (Map<String, String>) map.get("params");
        PageList pageList = planTemplateService.queryEntity(conditionLst, params);
        return pageList;
    }

    @ApiOperation(value = "启用/禁用计划模板",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planTemplateId", value = "计划模板id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "curUserId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doStatusChange")
    public FeignJson doStatusChange(@RequestBody PlanTemplateReq planTemplateReq, @RequestParam("planTemplateId") String planTemplateId,
                                    @RequestParam("curUserId") String curUserId, @RequestParam("orgId") String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            if (!CommonUtil.isEmpty(planTemplateId)) {
                for (String curPlanTemplateId : planTemplateId.split(",")) {
                    PlanTemplate t = planTemplateService.getPlanTemplateEntity(curPlanTemplateId);
                    if (!CommonUtil.isEmpty(t)) {
                        if ("启用".equals(planTemplateReq.getStatus())) {
                            planTemplateService.doStatusChange(t, "qiyong", curUserId, orgId);
                            planTemplateService.saveTemplateOptLog(t.getBizId(), PlantemplateConstant.PLAN_TEMPLATE_START, curUserId, orgId);
                            j.setMsg("成功");
                            log.info(I18nUtil.getValue("com.glaway.ids.pm.project.plantemplate.plantemplate.doStartSuccess"));
                        } else if ("禁用".equals(planTemplateReq.getStatus())) {
                            planTemplateService.doStatusChange(t, "jinyong", curUserId, orgId);
                            planTemplateService.saveTemplateOptLog(t.getBizId(), PlantemplateConstant.PLAN_TEMPLATE_STOP, curUserId, orgId);
                            j.setMsg("成功");
                            log.info(I18nUtil.getValue("com.glaway.ids.pm.project.plantemplate.plantemplate.doStopSuccess"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("失败");
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "获取计划模板信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "计划模板id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPlanTemplateEntity")
    public PlanTemplateDto getPlanTemplateEntity(@RequestParam("id") String id) {
        PlanTemplate planTemplate = planTemplateService.getPlanTemplateEntity(id);
        PlanTemplateDto dto = new PlanTemplateDto();
        try {
            dto = (PlanTemplateDto) VDataUtil.getVDataByEntity(planTemplate, PlanTemplateDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    @ApiOperation(value = "获取模板中的计划",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planTemplateId", value = "计划模板id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getTemplatePlanDetailById")
    public List<PlanTemplateDetailDto> getTemplatePlanDetailById(@RequestParam("planTemplateId") String planTemplateId) {
        List<PlanTemplateDetail> detailList = planTemplateService.getTemplatePlanDetailById(planTemplateId);
        List<PlanTemplateDetailDto> resList = JSON.parseArray(JSON.toJSONString(detailList), PlanTemplateDetailDto.class);
        return resList;
    }

    @ApiOperation(value = "获取计划id-输入项集合",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryInputsListMap")
    public Map<String, List<InputsDto>> queryInputsListMap() {
        Map<String, List<InputsDto>> map = new HashMap<>();
        map = planTemplateService.queryInputsListMap();
        return map;
    }

    @ApiOperation(value = "计划模板同名校验",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "templateName", value = "模板名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "planTemplateId", value = "计划模板id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "checkTemplateNameBeforeSave")
    public boolean checkTemplateNameBeforeSave(@RequestParam("templateName") String templateName, @RequestParam("planTemplateId") String planTemplateId) {
        boolean flag = true;
        flag = planTemplateService.checkTemplateNameBeforeSave(templateName, planTemplateId);
        return flag;
    }

    @ApiOperation(value = "保存计划模板及其详细信息",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "templateName", value = "模板名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "planTemplateId", value = "计划模板id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "备注", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "数据来源", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "curUserId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "savePlanTemplate")
    public FeignJson savePlanTemplate(@RequestParam("templateName") String templateName, @RequestParam("planTemplateId") String planTemplateId, @RequestParam("remark") String remark, @RequestParam("type") String type,
                                      @RequestParam("curUserId") String curUserId, @RequestParam("orgId") String orgId, @RequestBody List<PlanDto> planList) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            PlanTemplate template = new PlanTemplate();
            template = planTemplateService.getPlanTemplateEntity(planTemplateId);
            List<Plan> resPlanList = JSON.parseArray(JSON.toJSONString(planList), Plan.class);
            if (CommonUtil.isEmpty(template)) {    //新增
//                type = "add";
                template = new PlanTemplate();
                template.setId(planTemplateId);
                template.setName(templateName);
                template.setRemark(remark);
                planTemplateService.savePlanTemplateAndDetail(template, curUserId, orgId, resPlanList);
                j.setObj(template.getId());
                j.setMsg("新增成功");
                log.info(I18nUtil.getValue("com.glaway.ids.pm.plantemplate.plantemplate.addSuccess"));
            } else {    //修改或修订
//                type = "update";
//                template.setName(templateName);
//                template.setRemark(remark);
                if (!CommonUtil.isEmpty(template.getBizVersion())) {
                    planTemplateService.updatePlanTemplateAndDetail(template, type, templateName, remark, curUserId, orgId, resPlanList);
                    if ("update".equals(type)) {
                        j.setMsg("修改成功");
                        log.info(I18nUtil.getValue("com.glaway.ids.pm.plantemplate.plantemplate.updateSuccess"));
                    } else {
                        j.setMsg("修订成功");
                        log.info(I18nUtil.getValue("com.glaway.ids.pm.project.plantemplate.plantemplate.reviseSuccess"));
                    }
                } else {
                    j.setSuccess(false);
                }
            }
            j.setObj(template.getId());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "根据计划模板编号把计划模板逻辑删除",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "deletePlanTemplate")
    public FeignJson deletePlanTemplate(@RequestBody PlanTemplateReq planTemplateReq, @RequestParam("userId") String userId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            planTemplateService.deletePlanTemplate(planTemplateReq, userId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "更新计划模板",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "updatePlanTemplate")
    public FeignJson updatePlanTemplate(@RequestBody PlanTemplateReq planTemplateReq, @RequestParam("userId") String userId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            planTemplateService.updatePlanTemplate(planTemplateReq, userId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "通过MPP保存计划模板详细",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "savePlanTemplateDetail")
    public FeignJson savePlanTemplateDetail(@RequestBody Map<String, Object> paramMap) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            ObjectMapper mapper = new ObjectMapper();
            PlanTemplateDto planTemplateDto = mapper.convertValue(paramMap.get("planTemplate"), PlanTemplateDto.class);
            PlanTemplate planTemplate = new PlanTemplate();
            Dto2Entity.copyProperties(planTemplateDto, planTemplate);
            List<Task> taskList = mapper.convertValue(paramMap.get("taskList"), new TypeReference<List<Task>>() {
            });
            TSUserDto curUserDto = mapper.convertValue(paramMap.get("curUserDto"), TSUserDto.class);
            String orgId = String.valueOf(paramMap.get("orgId"));
            planTemplateService.savePlanTemplateDetail(planTemplate, taskList, curUserDto, orgId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "通过计划模板详细组成MPP",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveMppInfo")
    public List<MppInfo> saveMppInfo(@RequestBody PlanTemplateReq planTemplateReq) {
        List<MppInfo> mppInfo = planTemplateService.saveMppInfo(planTemplateReq);
        return mppInfo;
    }

    @ApiOperation(value = "校验计划模板导入EXCEL数据",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "checkData")
    public FeignJson checkData(@RequestBody Map<String, Object> paramMap) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            ObjectMapper mapper = new ObjectMapper();
            int rowNum = Integer.valueOf(String.valueOf(paramMap.get("rowNum")));
            String checkStr = String.valueOf(paramMap.get("checkStr"));
            String switchStr = String.valueOf(paramMap.get("switchStr"));
            Map<String, String> standardNameMap = (Map<String, String>) paramMap.get("standardNameMap");
            Map<String, String> errorMsgMap = (Map<String, String>) paramMap.get("errorMsgMap");
            Map<String, String> deliveryNameMap = (Map<String, String>) paramMap.get("deliveryNameMap");
            List<String> numList = mapper.convertValue(paramMap.get("numList"), new TypeReference<List<String>>() {
            });
            Map<String, String> planLevelMap = (Map<String, String>) paramMap.get("planLevelMap");
            planTemplateService.checkData(rowNum, checkStr, switchStr, standardNameMap, errorMsgMap, deliveryNameMap, numList, planLevelMap);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "再校验计划模板导入EXCEL数据",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "checkData2")
    public FeignJson checkData2(@RequestBody Map<String, Object> map) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, PlanTemplateExcelVo> excelMap = (Map<String, PlanTemplateExcelVo>) map.get("excelMap");
            Map<String, String> errorMsgMap = (Map<String, String>) map.get("errorMsgMap");
            List<String> numList = mapper.convertValue(map.get("numList"), new TypeReference<List<String>>() {
            });
            planTemplateService.checkData2(excelMap, errorMsgMap, numList);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "通过Excel保存计划模板详细信息",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "savePlanTemplateDetailByExcel")
    public FeignJson savePlanTemplateDetailByExcel(@RequestBody Map<String, Object> map) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            ObjectMapper mapper = new ObjectMapper();
            PlanTemplateDto planTemplateDto = mapper.convertValue(map.get("planTemplate"), PlanTemplateDto.class);
            PlanTemplate planTemplate = new PlanTemplate();
            Dto2Entity.copyProperties(planTemplateDto, planTemplate);
            List<PlanTemplateExcelVo> dataTempList = mapper.convertValue(map.get("dataTempList"), new TypeReference<List<PlanTemplateExcelVo>>() {
            });
            Map<String, PlanTemplateExcelVo> excelMap = (Map<String, PlanTemplateExcelVo>) map.get("excelMap");
            Map<String, String> planLevelMap = (Map<String, String>) map.get("planLevelMap");
            String switchStr = String.valueOf(map.get("switchStr"));
            String orgId = String.valueOf(map.get("orgId"));
            TSUserDto userDto = mapper.convertValue(map.get("curUser"), TSUserDto.class);
            planTemplateService.savePlanTemplateDetailByExcel(planTemplate, dataTempList, excelMap, planLevelMap, switchStr, userDto, orgId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "复制计划模板",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "oldTemplId", value = "原始计划模板id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "newTemplName", value = "新计划模板名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "备注", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "copyTemplate")
    public FeignJson copyTemplate(@RequestParam("oldTemplId") String oldTemplId, @RequestParam("newTemplName") String newTemplName, @RequestParam("remark") String remark, @RequestBody TSUserDto userDto, @RequestParam("orgId") String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            planTemplateService.copyTemplate(oldTemplId, newTemplName, remark, userDto, orgId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }


    @RequestMapping(value = "findBusinessObjectById")
    public PlanTemplateDto findBusinessObjectById(@RequestParam("planTemplateId") String planTemplateId) {
        PlanTemplate planTemplate = planTemplateService.findBusinessObjectById(PlanTemplate.class, planTemplateId);
        PlanTemplateDto dto = new PlanTemplateDto();
        try {
            dto = (PlanTemplateDto) VDataUtil.getVDataByEntity(planTemplate, PlanTemplateDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    @ApiOperation(value = "计划模板版本回退",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "backVesion")
    public FeignJson backVesion(@RequestBody PlanTemplateDto planTemplateDto, @RequestParam("userId") String userId, @RequestParam("orgId") String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            PlanTemplate planTemplate = new PlanTemplate();
            Dto2Entity.copyProperties(planTemplateDto, planTemplate);
            planTemplateService.backVesion(planTemplate, userId, orgId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "计划模板撤销",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "revokeVesion")
    public FeignJson revokeVesion(@RequestBody PlanTemplateDto planTemplateDto, @RequestParam("userId") String userId, @RequestParam("orgId") String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            PlanTemplate planTemplate = new PlanTemplate();
            Dto2Entity.copyProperties(planTemplateDto, planTemplate);
            planTemplateService.revokeVesion(planTemplate, userId, orgId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "启动计划模板提交流程",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "startPlanTemplateProcess")
    public FeignJson startPlanTemplateProcess(@RequestBody Map<String, Object> paramMap) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> variables = (Map<String, Object>) paramMap.get("variables");
            PlanTemplateDto planTemplateDto = mapper.convertValue(paramMap.get("planTemplate"), PlanTemplateDto.class);
            TSUserDto userDto = mapper.convertValue(paramMap.get("curUser"), TSUserDto.class);
            String orgId = String.valueOf(paramMap.get("orgId"));
            PlanTemplate planTemplate = new PlanTemplate();
            Dto2Entity.copyProperties(planTemplateDto, planTemplate);
            planTemplateService.startPlanTemplateProcess(planTemplate, variables, userDto, orgId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "保存计划模板日志信息",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bizId", value = "版本id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "logInfo", value = "日志信息", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveTemplateOptLog")
    public FeignJson saveTemplateOptLog(@RequestParam("bizId") String bizId, @RequestParam("logInfo") String logInfo, @RequestBody TSUserDto userDto, @RequestParam("orgId") String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            planTemplateService.saveTemplateOptLog(bizId, logInfo, userDto, orgId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "继续计划模板提交流程",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "taskId", value = "任务id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "completePlanTemplateProcess")
    public FeignJson completePlanTemplateProcess(@RequestParam("taskId") String taskId, @RequestBody PlanTemplateDto planTemplateDto, @RequestParam("userId") String userId, @RequestParam("orgId") String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            PlanTemplate planTemplate = new PlanTemplate();
            Dto2Entity.copyProperties(planTemplateDto, planTemplate);
            planTemplateService.completePlanTemplateProcess(taskId, planTemplate, userId, orgId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "获取版本历史信息及数量",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bizId", value = "版本id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "页码", dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "pageNum", value = "每页数量", dataType = "Integer")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getVersionHistoryAndCount")
    public FeignJson getVersionHistoryAndCount(@RequestParam("bizId") String bizId, @RequestParam("pageSize") Integer pageSize, @RequestParam("pageNum") Integer pageNum) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            List<PlanTemplate> list = planTemplateService.getVersionHistory(bizId, pageSize, pageNum);
            List<PlanTemplateDto> dtoList = CodeUtils.JsonListToList(list, PlanTemplateDto.class);
            long count = 0;
            count = planTemplateService.getVersionCount(bizId);
            Map<String, Object> map = new HashMap<>();
            map.put("planTemplateDtoList", dtoList);
            map.put("count", count);
            j.setObj(map);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "根据bizId获取计划模板日志数据",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bizId", value = "版本id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "findPlanTempOptLogById")
    public List<PlanTempOptLogDto> findPlanTempOptLogById(@RequestParam("bizId") String bizId) {
        List<PlanTempOptLog> list = planTemplateService.findPlanTempOptLogById(bizId);
        List<PlanTempOptLogDto> dtoList = JSON.parseArray(JSON.toJSONString(list), PlanTempOptLogDto.class);
        return dtoList;
    }

    @ApiOperation(value = "通过计划保存计划模板",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "planId", value = "计划id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "savePlanTemplateByPlanject")
    public FeignJson savePlanTemplateByPlanject(@RequestParam(value = "projectId", required = false) String projectId,
                                                @RequestParam(value = "planId", required = false) String planId, @RequestBody PlanTemplateDto planTemplateDto,
                                                @RequestParam("userId") String userId, @RequestParam("orgId") String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            PlanTemplate planTemplate = new PlanTemplate();
            Dto2Entity.copyProperties(planTemplateDto, planTemplate);
            planTemplateService.savePlanTemplateByPlanject(projectId, planId, planTemplate, userId, orgId);
            j.setObj(planTemplate.getId());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "计划模板导入mpp",httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "importPlanTemplateMpp")
    public FeignJson importPlanTemplateMpp(@RequestBody Map<String, Object> map) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            ObjectMapper mapper = new ObjectMapper();
            PlanTemplateDto planTemplateDto = mapper.convertValue(map.get("planTemplate"), PlanTemplateDto.class);
            Set<String> mapKeys = mapper.convertValue(map.get("mapKeys"), new TypeReference<Set<String>>(){});
            List<List<String>> preposePlanIdList = mapper.convertValue(map.get("preposePlanIdList"), new TypeReference<List<List<String>>>(){});
            List<List<Map<String, Object>>> taskMapList = mapper.convertValue(map.get("taskList"), new TypeReference<List<List<Map<String, Object>>>>(){});
            TSUserDto curUser = mapper.convertValue(map.get("curUser"), TSUserDto.class);
            String orgId = String.valueOf(map.get("orgId"));
            PlanTemplate planTemplate = new PlanTemplate();
            Dto2Entity.copyProperties(planTemplateDto, planTemplate);
            planTemplateService.importPlanTemplateMpp(planTemplate, mapKeys, curUser, orgId,taskMapList,preposePlanIdList);
            j.setObj(planTemplate.getId());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "删除模板中的计划",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "计划ids", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "planTemplateId", value = "计划模板id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projTemplateId", value = "项目模板id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doDeletePlanTemplate")
    public FeignJson doDeletePlanTemplate(@RequestParam(value = "ids", required = false) String ids,
                                          @RequestParam(value = "planTemplateId", required = false) String planTemplateId, @RequestParam(value = "projTemplateId", required = false) String projTemplateId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            planTemplateService.doDeletePlanTemplate(ids, planTemplateId, projTemplateId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @RequestMapping(value = "getImportDataList")
    public FeignJson getImportDataList(@RequestBody List<Map<String,String>> map,@RequestParam(value = "userId",required = false) String userId,@RequestParam(value = "planTemplateId",required = false) String planTemplateId,@RequestParam(value = "orgId",required = false) String orgId){
        FeignJson feignJson = new FeignJson();
        try {
            Map<String,Object> returnMap = planTemplateService.doImportPlanTemplateExcel(map,userId,planTemplateId,orgId);
            feignJson.setObj(returnMap);
        } catch (Exception e) {
            feignJson.setMsg(e.getMessage());
            feignJson.setSuccess(false);
            e.printStackTrace();
        }
        return feignJson;
    }


}
