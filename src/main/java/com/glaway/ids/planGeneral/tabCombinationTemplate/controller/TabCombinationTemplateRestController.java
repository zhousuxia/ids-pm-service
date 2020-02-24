package com.glaway.ids.planGeneral.tabCombinationTemplate.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.VDataUtil;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.system.lifecycle.entity.LifeCycleStatus;
import com.glaway.ids.planGeneral.plantabtemplate.businessserviceI.PlanTabTemplateBussinesServiceI;
import com.glaway.ids.planGeneral.plantabtemplate.entity.TabTemplate;
import com.glaway.ids.planGeneral.plantabtemplate.entityserviceI.TabTemplateEntityServiceI;
import com.glaway.ids.planGeneral.tabCombinationTemplate.businessServiceI.TabCbTemplateBusinessServiceI;
import com.glaway.ids.planGeneral.tabCombinationTemplate.dto.TabCombinationTemplateDto;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entity.CombinationTemplateInfo;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entity.TabCombinationTemplate;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entityServiceI.CombinationTemplateEntityServiceI;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entityServiceI.TabCbTemplateEntityServiceI;
import com.glaway.ids.planGeneral.tabCombinationTemplate.util.TabTemplateUtil;
import com.glaway.ids.planGeneral.tabCombinationTemplate.vo.CombinationTemplateVo;
import com.glaway.ids.util.CodeUtils;
import io.swagger.annotations.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 页签组合模板
 * @author: sunmeng
 * @ClassName: TabCombinationTemplateRestController
 * @Date: 2019/8/29-17:59
 * @since
 */
@Api(tags = {"页签组合模板接口"})
@RestController
@RequestMapping("/feign/tabCombinationTemplateRestController")
public class TabCombinationTemplateRestController {

    @Autowired
    private TabCbTemplateEntityServiceI tabCbTemplateEntityService;

    @Autowired
    private TabCbTemplateBusinessServiceI tabCbTemplateBusinessService;

    @Autowired
    private CombinationTemplateEntityServiceI combinationTemplateEntityService;

    @Autowired
    private TabTemplateEntityServiceI tabTemplateEntityService;

    @Autowired
    private PlanTabTemplateBussinesServiceI planTabTemplateBussinesService;

    /**
     * 通过活动类型ID判断当前活动类型是否绑定页签组合模板
     * @param id
     * @return
     */
    @ApiOperation(value="通过活动类型ID判断当前活动类型是否绑定页签组合模板", httpMethod="GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "id", value = "活动类型ID", required = false, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "templateId", value = "页签组合模板ID", required = false, dataType = "String")
    })
    @ApiResponses(@ApiResponse(code=200,message="接口调用成功"))
    @RequestMapping("/isActivityTypeManageUse")
    public FeignJson isActivityTypeManageUse(@RequestParam(value = "id",required = false) String id, @RequestParam(value = "templateId",required = false) String templateId) {
        FeignJson j = tabCbTemplateBusinessService.isActivityTypeManageUse(id, templateId);
        return j;
    }

    /**
     * 保存页签组合模板信息
     * @param param
     * @return
     */
    @ApiOperation(value="保存页签组合模板信息", httpMethod="POST")
    @ApiResponses(@ApiResponse(code=200,message="接口调用成功"))
    @RequestMapping("/saveTabCbTemplateInfo")
    public FeignJson saveTabCbTemplateInfo(@RequestBody Map<String,Object> param) {
        FeignJson j = tabCbTemplateBusinessService.saveTabCbTemplateInfo(param);
        return j;
    }

    /**
     * 修改页签组合模板信息
     * @param param
     * @return
     */
    @ApiOperation(value="修改页签组合模板信息", httpMethod="POST")
    @ApiResponses(@ApiResponse(code=200,message="接口调用成功"))
    @RequestMapping("/updateTabCbTemplateInfo")
    public FeignJson updateTabCbTemplateInfo(@RequestBody Map<String,Object> param) {
        FeignJson j = tabCbTemplateBusinessService.updateTabCbTemplateInfo(param);
        return j;
    }

    /**
     * 修改页签组合模板信息
     * @param param
     * @return
     */
    @ApiOperation(value="保存页签组合模板并重新提交流程", httpMethod="POST")
    @ApiResponses(@ApiResponse(code=200,message="接口调用成功"))
    @RequestMapping("/backSaveAndSubmit")
    public FeignJson backSaveAndSubmit(@RequestBody Map<String,Object> param) {
        FeignJson j = tabCbTemplateBusinessService.updateTabCbTemplateInfo(param);
        return j;
    }

    /**
     * 根据条件查找页签组合模板信息
     * @param params
     * @return
     */
    @ApiOperation(value = "根据条件查找页签组合模板信息", httpMethod="GET")
    @ApiResponses(@ApiResponse(code=200,message="接口调用成功"))
    @RequestMapping("/queryEntity")
    public FeignJson searchDataGrid(@RequestBody Map<String,Object> params) {
        ObjectMapper mapper = new ObjectMapper();
        List<ConditionVO> conditionList = mapper.convertValue(params.get("condition"),new TypeReference<List<ConditionVO>>(){});
        FeignJson j = tabCbTemplateBusinessService.searchDataGrid(conditionList,true);
        return j;
    }

    /**
     * 根据id获取页签组合模板信息
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id获取页签组合模板信息", httpMethod="GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "页签组合模板id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code=200,message="接口调用成功"))
    @RequestMapping("/findTabCbTempById")
    public FeignJson findTabCbTempById(@RequestParam(value = "id",required = false) String id) {
        FeignJson j = new FeignJson();
        TabCombinationTemplate tabCombinationTemplate = tabCbTemplateEntityService.findTabCbTempById(id);
        TabCombinationTemplateDto dto = new TabCombinationTemplateDto();
        try {
            dto = (TabCombinationTemplateDto) VDataUtil.getVDataByEntity(tabCombinationTemplate,TabCombinationTemplateDto.class);
            j.setObj(dto);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 通过页签组合模板信息获取组合模板信息
     * @param tabCbTemplateId
     * @return
     */
    @ApiOperation(value = "通过页签组合模板信息获取组合模板信息", httpMethod="GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "tabCbTemplateId", value = "页签组合模板id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code=200,message="接口调用成功"))
    @RequestMapping("/getCombTemplateInfos")
    public FeignJson getCombTemplateInfos(@RequestParam(value = "tabCbTemplateId",required = false) String tabCbTemplateId) {

        List<CombinationTemplateInfo> infos = combinationTemplateEntityService.getInfosBytabCbTemplateId(tabCbTemplateId);
        //页签模板数据,包括禁用、删除的
        List<TabTemplate> tabTemplates = tabTemplateEntityService.getAllTabTemplates("","");

        List<CombinationTemplateVo> voList = new ArrayList<>();
        for (CombinationTemplateInfo info : infos) {
            CombinationTemplateVo vo = new CombinationTemplateVo();
            vo.setId(info.getId());
            vo.setDisplayAccess(info.getDisplayAccess());
            vo.setTypeId(info.getTypeId());
            vo.setName(info.getName());
            vo.setOrderNum(info.getOrderNum());
            tabTemplates.forEach(t -> {
                if (t.getId().equals(info.getTypeId())) {
                   vo.setTabName(t.getName());
                   vo.setTabType(TabTemplateUtil.tabTemplateType.get(t.getTabType()));
                   vo.setUrl(t.getExternalURL());
                   vo.setBizVersion(t.getBizVersion());
                   vo.setDisplayUsage(t.getDisplayUsage());
                }
            });
            voList.add(vo);
        }

        FeignJson j = new FeignJson();
        j.setObj(voList);
        return j;
    }

    /**
     * 通过计划ID获取对应的页签组合模板
     * @param planId
     * @param activityId
     * @param displayAccess
     * @return
     */
    @ApiOperation(value="通过计划ID获取对应的组合模板信息", httpMethod="GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "planId", value = "计划ID", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "activityId", value = "活动类型ID", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "displayAccess", value = "显示方式", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code=200,message="接口调用成功"))
    @RequestMapping("/getCombTemplateInfosByPlanId")
    public FeignJson getCombTemplateInfosByPlanId(@RequestParam(value = "planId",required = false) String planId,@RequestParam(value = "activityId",required = false) String activityId,
                                                  @RequestParam(value = "displayAccess",required = false) String displayAccess) {
        TabCombinationTemplate tabCombinationTemplate = new TabCombinationTemplate();
        if(!CommonUtil.isEmpty(activityId)){
            tabCombinationTemplate = tabCbTemplateEntityService.findTabCbTemplatesByActivityId(activityId).get(0);
        }else{
            if(!CommonUtil.isEmpty(planId)) {
                 tabCombinationTemplate = tabCbTemplateBusinessService.getTabCbTemplateByPlanId(planId);
            }
        }

        if (!CommonUtil.isEmpty(tabCombinationTemplate)) {
            
            List<CombinationTemplateInfo> infos = combinationTemplateEntityService.getInfosBytabCbTemplateId(tabCombinationTemplate.getId());
            //页签模板数据,包括禁用、删除的
            List<TabTemplate> tabTemplates = tabTemplateEntityService.getAllTabTemplates("","");

            //通过sql获取页签模板对应的服务信息(页签模板id,projectModel)
            Map<String,String> projectModels = planTabTemplateBussinesService.getProjectModel();

            List<CombinationTemplateVo> voList = new ArrayList<>();
            for (CombinationTemplateInfo info : infos) {                
//                if(!CommonUtil.isEmpty(displayAccess) && (displayAccess.equals(info.getDisplayAccess()) || info.getDisplayAccess().equals("1"))111) {
                
                    CombinationTemplateVo vo = new CombinationTemplateVo();
                    vo.setId(info.getId());
                    vo.setDisplayAccess(info.getDisplayAccess());
                    vo.setTypeId(info.getTypeId());
                    vo.setName(info.getName());
                    vo.setOrderNum(info.getOrderNum());
                    vo.setProjectModel(projectModels.get(info.getTypeId()));
                    tabTemplates.forEach(t -> {
                        if (t.getId().equals(info.getTypeId())) {
                            vo.setTabName(t.getName());
                            vo.setTabType(TabTemplateUtil.tabTemplateType.get(t.getTabType()));
                            vo.setUrl(t.getExternalURL());
                            vo.setDisplayUsage(t.getDisplayUsage());
                        }
                    });
                    voList.add(vo);
//                }  
            }

            FeignJson j = new FeignJson();
            j.setObj(voList);
            return j;
        } else {
            return new FeignJson();
        }
    }

    /**
     * 批量删除页签组合模板信息
     * @param ids
     * @return
     */
    @ApiOperation(value = "doBatchDel", httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "页签组合模板ids", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code=200,message="接口调用成功"))
    @RequestMapping("/doBatchDel")
    public FeignJson doBatchDel(@RequestParam(value = "ids",required = false) String ids) {
        FeignJson j = tabCbTemplateBusinessService.doBatchDel(ids);
        return j;
    }

    /**
     * 批量启用禁用页签组合模板
     * @param ids
     * @param status
     * @param userId
     * @return
     */
    @ApiOperation(value = "批量启用禁用页签组合模板", httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "页签组合模板ids", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "status", value = "启用禁用状态", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户Id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code=200,message="接口调用成功"))
    @RequestMapping("/doStatusChange")
    public FeignJson doStatusChange(@RequestParam(value = "ids",required = false) String ids,
                                    @RequestParam(value = "status",required = false) String status,@RequestParam(value = "userId",required = false) String userId) {
        FeignJson j = tabCbTemplateBusinessService.doStatusChange(ids, status,userId);
        return j;
    }

    /**
     * 根据活动类型id&组合页签模板id获取绑定的页签组合模板
     * @param activityId
     * @return
     */
    @ApiOperation(value="根据活动类型id&组合页签模板id获取绑定的页签组合模板", httpMethod="GET")
    @ApiImplicitParams(@ApiImplicitParam(paramType="query", name = "activityId", value = "活动类型ID", required = false, dataType = "String"))
    @ApiResponses(@ApiResponse(code=200,message="接口调用成功"))
    @RequestMapping("/findTabCbTempByActivityId")
    public List<TabCombinationTemplateDto> findTabCbTempByActivityId(@RequestParam(value = "activityId",required = false) String activityId){
        List<TabCombinationTemplate> list = tabCbTemplateEntityService.findTabCbTemplatesByActivityId(activityId);
        List<TabCombinationTemplateDto> dtoList = CodeUtils.JsonListToList(list,TabCombinationTemplateDto.class);
        return dtoList;
    }

    /**
     * 根据活动类型id&组合页签模板id获取绑定的页签组合模板
     * @param planId 计划id
     * @return
     */
    @ApiOperation(value="根据计划id获取绑定的页签组合模板", httpMethod="GET")
    @ApiImplicitParams(@ApiImplicitParam(paramType="query", name = "planId", value = "计划", required = false, dataType = "String"))
    @ApiResponses(@ApiResponse(code=200,message="接口调用成功"))
    @RequestMapping("/findTabCbTempByPlanId")
    public TabCombinationTemplateDto findTabCbTempByPlanId(@RequestParam(value = "planId",required = false) String planId){
        TabCombinationTemplate tabCombinationTemplate = new TabCombinationTemplate();
        TabCombinationTemplateDto dto = new TabCombinationTemplateDto();
        if(!CommonUtil.isEmpty(planId)) {
            tabCombinationTemplate = tabCbTemplateBusinessService.getTabCbTemplateByPlanId(planId);
        }
        try {
            dto = (TabCombinationTemplateDto) VDataUtil.getVDataByEntity(tabCombinationTemplate,TabCombinationTemplateDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    /**
     * 获取所有组合模板名称
     * @return
     */
    @ApiOperation(value="根获取所有组合模板名称", httpMethod="GET")
    @ApiResponses(@ApiResponse(code=200,message="接口调用成功"))
    @RequestMapping("/queryAllName")
    public List<String> queryAllName(){
        List<String> list = tabCbTemplateEntityService.queryAllName();
        return list;
    }

    @ApiOperation(value="页签组合模板提交审批", httpMethod="POST")
    @ApiResponses(@ApiResponse(code=200,message="接口调用成功"))
    @RequestMapping("/doSubmitApprove")
    public FeignJson doSubmitApprove(@RequestBody Map<String,String> map) {
        FeignJson j = tabCbTemplateBusinessService.doSubmitApprove(map);
        return j;
    }

    @ApiOperation(value = "获取页签组合模板版本信息及数量", httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bizId", value = "版本id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "页码", dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "pageNum", value = "每页数量", dataType = "Integer")
    })
    @ApiResponses(@ApiResponse(code=200,message="接口调用成功"))
    @RequestMapping("/getVersionDatagridStr")
    public FeignJson getVersionDatagridStr(@RequestParam(value = "bizId",required = false) String bizId, @RequestParam("pageSize") Integer pageSize,
                                           @RequestParam("pageNum") Integer pageNum) {
        FeignJson j = new FeignJson();
        try {
            List<TabCombinationTemplate> templates = tabCbTemplateEntityService.getVersionHistory(bizId, pageSize, pageNum);
            //根据创建时间排序
            if (templates.size() > 0) {
                templates.sort(((o1, o2) -> {
                    return o2.getCreateTime().compareTo(o1.getCreateTime());
                }));
            }
            List<TabCombinationTemplateDto> templateDtos = new ArrayList<>();
            templateDtos = (List<TabCombinationTemplateDto>) VDataUtil.getVDataByEntity(templates,TabCombinationTemplateDto.class);
            templateDtos = tabCbTemplateBusinessService.setStatusByLifeCycleStatus(templateDtos);
            long count = 0;
            count = tabCbTemplateEntityService.getVersionCount(bizId);Map<String, Object> map = new HashMap<>();
            map.put("list", templateDtos);
            map.put("count", count);
            j.setAttributes(map);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value="页签组合模板版本回退", httpMethod="POST")
    @ApiResponses(@ApiResponse(code=200,message="接口调用成功"))
    @RequestMapping("/backVersion")
    public FeignJson backVersion(@RequestBody Map<String,String> params) {
        return tabCbTemplateBusinessService.backVersion(params);
    }

    @ApiOperation(value="页签组合模板页面状态下拉加载", httpMethod="GET")
    @ApiResponses(@ApiResponse(code=200,message="接口调用成功"))
    @RequestMapping("/getLifeCycleStatusList")
    public FeignJson getLifeCycleStatusList() {
        FeignJson j = new FeignJson();
        String jonStr = "";
        List<LifeCycleStatus> lifeCycleStatuses = tabCbTemplateBusinessService.getLifeCycleStatusList();
        //根据orderNumber排序
        lifeCycleStatuses.sort(((o1, o2) -> o1.getOrderNum().compareTo(o2.getOrderNum())));
        JSONArray jList = new JSONArray();
        if (lifeCycleStatuses.size() > 0) {
            for (LifeCycleStatus cycleStatus : lifeCycleStatuses) {
                JSONObject obj = new JSONObject();
                obj.put("id", cycleStatus.getName());
                obj.put("name", cycleStatus.getTitle());
                jList.add(obj);
            }
            jonStr = jList.toString();
        }
        j.setObj(jonStr);
        return j;
    }
}
