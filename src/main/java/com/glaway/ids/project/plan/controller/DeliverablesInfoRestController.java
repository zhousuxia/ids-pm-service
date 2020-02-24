package com.glaway.ids.project.plan.controller;

import com.glaway.foundation.cache.service.RedisService;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.OperationLog;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.VDataUtil;
import com.glaway.foundation.core.common.controller.BaseController;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.system.lifecycle.service.LifeCycleStatusServiceI;
import com.glaway.ids.config.service.ParamSwitchServiceI;
import com.glaway.ids.project.plan.dto.DeliverablesInfoDto;
import com.glaway.ids.project.plan.dto.PlanDto;
import com.glaway.ids.project.plan.entity.DeliverablesInfo;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.service.DeliverablesInfoServiceI;
import com.glaway.ids.project.plan.service.PlanFlowForworkServiceI;
import com.glaway.ids.project.plan.service.PlanServiceI;
import com.glaway.ids.project.projectmanager.dto.ProjDocRelationDto;
import com.glaway.ids.project.projectmanager.entity.ProjDocRelation;
import com.glaway.ids.project.projectmanager.service.ProjLibServiceI;
import com.glaway.ids.util.CodeUtils;
import com.glaway.ids.util.Dto2Entity;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author blcao
 * @version V1.0
 * @Title: Controller
 * @Description: 交付物
 * @date 2015-03-30 09:11:17
 */
@Api(tags = {"计划交付项接口"})
@RestController
@RequestMapping("/feign/deliverablesInfoRestController")
public class DeliverablesInfoRestController extends BaseController {
    /**
     * Logger for this class
     */
    private static final OperationLog log = BaseLogFactory.getOperationLog(DeliverablesInfoRestController.class);
    /**
     *
     */
    @Autowired
    private DeliverablesInfoServiceI deliverablesInfoService;
    /**
     *
     */
    @Autowired
    private ParamSwitchServiceI paramSwitchService;
    /**
     *
     */
    @Autowired
    private ProjLibServiceI projLibService;
    /**
     * 项目计划管理接口
     */
    @Autowired
    private PlanServiceI planService;

    @Autowired
    private PlanFlowForworkServiceI planFlowForworkService;
    /**
     * redis缓存服务
     */
    @Autowired
    private RedisService redisService;
    /**
     * 项目计划管理接口
     */
    @Autowired
    private LifeCycleStatusServiceI lifeCycleStatusService;

    /**
     * 根据deliverablesInfo条件检索交付物
     *
     * @param deliverablesInfo
     * @param page
     * @param rows
     * @param isPage
     * @return
     */
    @ApiOperation(value = "根据deliverablesInfo条件检索交付物", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "rows", value = "每页数量", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "isPage", value = "是否分页", dataType = "boolean")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryDeliverableList")
    public List<DeliverablesInfoDto> queryDeliverableList(@RequestBody DeliverablesInfoDto deliverablesInfo, @RequestParam("page") int page,
                                                          @RequestParam("rows") int rows, @RequestParam("isPage") boolean isPage) {
        DeliverablesInfo deli = new DeliverablesInfo();
        Dto2Entity.copyProperties(deliverablesInfo, deli);
        List<DeliverablesInfo> list = deliverablesInfoService.queryDeliverableList(deli, page, rows, isPage);
        List<DeliverablesInfoDto> resList = new ArrayList<>();
        try {
            resList = (List<DeliverablesInfoDto>) VDataUtil.getVDataByEntity(list, DeliverablesInfoDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resList;
    }

    /**
     * 获取满足deliverablesInfo条件的交付物的数目
     *
     * @param deliverablesInfo
     * @return
     */
    @ApiOperation(value = "获取满足deliverablesInfo条件的交付物的数目", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getCount")
    public long getCount(@RequestBody DeliverablesInfoDto deliverablesInfo) {
        long cnt = 0;
        DeliverablesInfo deli = new DeliverablesInfo();
        Dto2Entity.copyProperties(deliverablesInfo, deli);
        cnt = deliverablesInfoService.getCount(deli);
        return cnt;
    }

    /**
     * 根据id删除交付物
     *
     * @param id
     */
    @ApiOperation(value = "根据id删除交付物", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "交付物id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "deleteDeliverablesById")
    public void deleteDeliverablesById(@RequestParam("id") String id) {
        deliverablesInfoService.deleteDeliverablesById(id);
    }

    /**
     * 交付物对象初始化
     *
     * @param deliverablesInfoDto
     * @return
     */
    @ApiOperation(value = "交付物对象初始化", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "initDeliverablesInfo")
    public String initDeliverablesInfo(@RequestBody DeliverablesInfoDto deliverablesInfoDto) {
        DeliverablesInfo deli = new DeliverablesInfo();
        Dto2Entity.copyProperties(deliverablesInfoDto, deli);
        String str = deliverablesInfoService.initDeliverablesInfo(deli);
        return str;
    }

    /**
     * 增加交付项
     *
     * @param deliverablesInfoDto
     */
    @ApiOperation(value = "增加交付项", httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveDeliverablesInfo")
    public void saveDeliverablesInfo(@RequestBody DeliverablesInfoDto deliverablesInfoDto) {
        DeliverablesInfo deli = new DeliverablesInfo();
        Dto2Entity.copyProperties(deliverablesInfoDto, deli);
        deliverablesInfoService.saveDeliverablesInfo(deli);
    }

    /**
     * 批量增加交付项
     *
     * @param names
     * @param dto
     */
    @ApiOperation(value = "批量增加交付项", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "names", value = "交付项名称集合", dataType = "String")
    })
    @RequestMapping(value = "doAddDelDeliverForWork")
    public void doAddDelDeliverForWork(@RequestParam("names") String names, @RequestBody DeliverablesInfoDto dto) {
        DeliverablesInfo deliverablesInfo = new DeliverablesInfo();
        Dto2Entity.copyProperties(dto, deliverablesInfo);
        planFlowForworkService.doAddDelDeliverForWork(names, deliverablesInfo);
    }

    /**
     * 批量删除交付项及其相关的后置输入项
     *
     * @param ids
     */
    @ApiOperation(value = "批量删除交付项及其相关的后置输入项", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "交付项ids", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doBatchDelDeliverForWork")
    public void doBatchDelDeliverForWork(@RequestParam("ids") String ids) {
        planFlowForworkService.doBatchDelDeliverForWork(ids);
    }

    /**
     * 获取交付项生命周期
     *
     * @return
     */
    @ApiOperation(value = "获取交付项生命周期", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getLifeCycleStatusList")
    public FeignJson getLifeCycleStatusList() {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            String lifeCycleStr = deliverablesInfoService.getLifeCycleStatusList();
            j.setObj(lifeCycleStr);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    /**
     * 根据id获取交付项信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id获取交付项信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "交付项id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getDeliverablesInfoEntity")
    public DeliverablesInfoDto getDeliverablesInfoEntity(@RequestParam("ids") String id) {
        DeliverablesInfoDto dto = new DeliverablesInfoDto();
        try {
            DeliverablesInfo p = deliverablesInfoService.getDeliverablesInfoEntity(id);
            dto = (DeliverablesInfoDto) VDataUtil.getVDataByEntity(p, DeliverablesInfoDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    /**
     * 根据前置计划Ids获取前置计划的输出
     *
     * @param preposeIds
     */
    @ApiOperation(value = "获取前置计划的输出", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "preposeIds", value = "前置计划Ids", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getPreposePlanDeliverables")
    List<DeliverablesInfoDto> getPreposePlanDeliverables(HttpServletRequest request, HttpServletResponse response, @RequestParam("preposeIds") String preposeIds) {
        List<DeliverablesInfoDto> dtoList = new ArrayList<DeliverablesInfoDto>();
        try {
            List<DeliverablesInfo> rdTaskDeliverablesInfo = deliverablesInfoService.getPreposePlanDeliverables(preposeIds);
            if (!CommonUtil.isEmpty(rdTaskDeliverablesInfo)) {
                dtoList = (List<DeliverablesInfoDto>) VDataUtil.getVDataByEntity(rdTaskDeliverablesInfo, DeliverablesInfoDto.class);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dtoList;
    }

    /**
     * 根据useObjectType、useObjectId查找相关交付物
     *
     * @param useObjectType
     * @param useObjectId
     */
    @ApiOperation(value = "根据useObjectType、useObjectId查找相关交付物", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "useObjectType", value = "交付项关联对象类型", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "useObjectId", value = "交付项关联对象Id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getDeliverablesByUseObeject")
    List<DeliverablesInfoDto> getDeliverablesByUseObeject(@RequestParam("useObjectType") String useObjectType, @RequestParam("useObjectId") String useObjectId) {
        List<DeliverablesInfoDto> dtoList = new ArrayList<DeliverablesInfoDto>();
        try {
            List<DeliverablesInfo> rdTaskDeliverablesInfo = deliverablesInfoService.getDeliverablesByUseObeject(useObjectType, useObjectId);
            ;
            if (!CommonUtil.isEmpty(rdTaskDeliverablesInfo)) {
                dtoList = (List<DeliverablesInfoDto>) VDataUtil.getVDataByEntity(rdTaskDeliverablesInfo, DeliverablesInfoDto.class);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dtoList;
    }

    /**
     * 获取输入增加时所选择的deliverablesInfo的交付物信息
     *
     * @param ids
     */
    @ApiOperation(value = "获取输入增加时所选择的deliverablesInfo的交付物信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "交付物信息ids", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getSelectedPreposePlanDeliverables")
    List<DeliverablesInfoDto> getSelectedPreposePlanDeliverables(@RequestParam("ids") String ids) {
        List<DeliverablesInfoDto> dtoList = new ArrayList<DeliverablesInfoDto>();
        try {
            List<DeliverablesInfo> rdTaskDeliverablesInfo = deliverablesInfoService.getSelectedPreposePlanDeliverables(ids);
            if (!CommonUtil.isEmpty(rdTaskDeliverablesInfo)) {
                dtoList = (List<DeliverablesInfoDto>) VDataUtil.getVDataByEntity(rdTaskDeliverablesInfo, DeliverablesInfoDto.class);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dtoList;
    }

    /**
     * 删除计划的输入、输出、删除计划输入相关的输入
     *
     * @param planId
     */
    @ApiOperation(value = "删除计划的输入、输出、删除计划输入相关的输入", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planId", value = "计划ID", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "deleteDeliverablesByPlanId")
    void deleteDeliverablesByPlanId(@RequestParam("planId") String planId) {
        deliverablesInfoService.deleteDeliverablesByPlanId(planId);
    }

    /**
     * 获取所有父计划ID为parentPlanId的计划的交付项信息
     *
     * @param parentPlanId
     * @return
     */
    @ApiOperation(value = "获取所有父计划ID为parentPlanId的计划的交付项信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "parentPlanId", value = "父计划ID", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getAllDeliverablesByUseObeject")
    List<DeliverablesInfoDto> getAllDeliverablesByUseObeject(@RequestParam("parentPlanId") String parentPlanId) {
        List<DeliverablesInfoDto> dtoList = new ArrayList<DeliverablesInfoDto>();
        try {
            List<DeliverablesInfo> rdTaskDeliverablesInfo = deliverablesInfoService.getAllDeliverablesByUseObeject(parentPlanId);
            if (!CommonUtil.isEmpty(rdTaskDeliverablesInfo)) {
                dtoList = (List<DeliverablesInfoDto>) VDataUtil.getVDataByEntity(rdTaskDeliverablesInfo, DeliverablesInfoDto.class);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dtoList;
    }

    /**
     * 判断交付项状态信息
     *
     * @param plan
     * @param isOut
     * @return
     */
    @ApiOperation(value = "判断交付项状态信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "isOut", value = "计划有无交付项子计划标识", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getJudgePlanAllDocumantWithStatus")
    Integer getJudgePlanAllDocumantWithStatus(@RequestBody PlanDto plan, @RequestParam("isOut") String isOut) {
        Plan p = new Plan();
        Dto2Entity.copyProperties(plan, p);
        Integer count = 0;
        count = deliverablesInfoService.getJudgePlanAllDocumantWithStatus(p, isOut);
        return count;
    }

    /**
     * 项目计划页面查看初始化时获取项目库
     *
     * @param map
     * @return
     */
    @ApiOperation(value = "项目计划页面查看初始化时获取项目库", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "listView")
    public FeignJson listView(@RequestBody Map<String, Object> map) {
        return deliverablesInfoService.listView(map);
    }

    /**
     * 交付项初始化
     *
     * @param document
     */
    @ApiOperation(value = "交付项初始化", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "initBusinessObject")
    public void initBusinessObject(@RequestBody DeliverablesInfoDto document) {
        DeliverablesInfo deliverablesInfo = new DeliverablesInfo();
        Dto2Entity.copyProperties(document, deliverablesInfo);
        deliverablesInfoService.initBusinessObject(deliverablesInfo);
    }

    /**
     * 获取子计划的交付项关联文档信息
     *
     * @param planId
     * @return
     */
    @ApiOperation(value = "获取子计划的交付项关联文档信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planId", value = "计划Id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryFinishDeliverable")
    public Map<String, ProjDocRelationDto> queryFinishDeliverable(@RequestParam(value = "planId", required = false) String planId) {
        Map<String, ProjDocRelationDto> linkMap = new HashMap<String, ProjDocRelationDto>();
        Map<String, ProjDocRelation> deliverablesMap = new HashMap<String, ProjDocRelation>();
        deliverablesMap = deliverablesInfoService.queryFinishDeliverable(planId, deliverablesMap);
        if(deliverablesMap.size()>0) {
            for (String cur : deliverablesMap.keySet()) {
                ProjDocRelation entity = deliverablesMap.get(cur);
                try {
                    if (!CommonUtil.isEmpty(entity)) {
                        ProjDocRelationDto dto= (ProjDocRelationDto) VDataUtil.getVDataByEntity(entity, ProjDocRelationDto.class);
                        linkMap.put(cur,dto);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return linkMap;
    }

    /**
     * 更新交付项信息
     *
     * @param dto
     * @return
     */
    @ApiOperation(value = "更新交付项信息", httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "updateDeliverablesInfo")
    public FeignJson updateDeliverablesInfo(@RequestBody DeliverablesInfoDto dto) {
        DeliverablesInfo deliverablesInfo = new DeliverablesInfo();
        Dto2Entity.copyProperties(dto, deliverablesInfo);
        return deliverablesInfoService.updateDeliverablesInfo(deliverablesInfo);
    }

    /**
     * 通过PLM系统更新交付项信息
     *
     * @param dto
     * @return
     */
    @ApiOperation(value = "通过PLM系统更新交付项信息", httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "updateDeliverablesInfoByPlm")
    public FeignJson updateDeliverablesInfoByPlm(@RequestBody DeliverablesInfoDto dto) {
        DeliverablesInfo deliverablesInfo = new DeliverablesInfo();
        Dto2Entity.copyProperties(dto, deliverablesInfo);
        return deliverablesInfoService.updateDeliverablesInfoByPlm(deliverablesInfo);
    }

    /**
     * 获取项目下的计划的交付项信息
     *
     * @param projectId
     * @return
     */
    @ApiOperation(value = "获取项目下的计划的交付项信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目Id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getDeliverablesByProject")
    public List<DeliverablesInfoDto> getDeliverablesByProject(@RequestParam(value = "projectId") String projectId) {
        List<DeliverablesInfo> deliverablesByProject = deliverablesInfoService.getDeliverablesByProject(projectId);
        List<DeliverablesInfoDto> list = CodeUtils.JsonListToList(deliverablesByProject, DeliverablesInfoDto.class);
        return list;
    }
}
