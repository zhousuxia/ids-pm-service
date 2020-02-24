package com.glaway.ids.planGeneral.plantabtemplate.controller;

import com.glaway.foundation.common.util.VDataUtil;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.planGeneral.plantabtemplate.businessserviceI.PlanTabTemplateBussinesServiceI;
import com.glaway.ids.planGeneral.plantabtemplate.dto.DataSourceObjectDto;
import com.glaway.ids.planGeneral.plantabtemplate.dto.ObjectPropertyInfoDto;
import com.glaway.ids.planGeneral.plantabtemplate.dto.TabTemplateDto;
import com.glaway.ids.planGeneral.plantabtemplate.entity.DataSourceObject;
import com.glaway.ids.planGeneral.plantabtemplate.entity.ObjectPropertyInfo;
import com.glaway.ids.planGeneral.plantabtemplate.entity.TabTemplate;
import com.glaway.ids.planGeneral.plantabtemplate.entityserviceI.DataSourceObjectEntityServiceI;
import com.glaway.ids.planGeneral.plantabtemplate.entityserviceI.ObjectPropertyInfoEntityServiceI;
import com.glaway.ids.planGeneral.plantabtemplate.entityserviceI.TabTemplateEntityServiceI;
import com.glaway.ids.planGeneral.tabCombinationTemplate.dto.TabCombinationTemplateDto;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entity.TabCombinationTemplate;
import com.glaway.ids.util.CodeUtils;
import com.glaway.ids.util.Dto2Entity;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @Title: RestController
 * @Description: 页签模版管理接口
 * @author wuxing
 * @date 2019-08-27
 * @version V1.0
 */
@Api(tags = {"页签模版管理接口"})
@RestController
@RequestMapping(value = "/feign/tabTemplateRestController")
public class TabTemplateRestController {

    //计划通用化业务Service
    @Autowired
    private PlanTabTemplateBussinesServiceI planTabTemplateBussinesServiceImpl;

    //页签模版管理Service
    @Autowired
    private TabTemplateEntityServiceI tabTemplateEntityServiceImpl;

    @Autowired
    private DataSourceObjectEntityServiceI dataSourceObjectEntityServiceI;

    @Autowired
    private ObjectPropertyInfoEntityServiceI objectPropertyInfoEntityServiceI;

    /**
     * 功能描述：根据查询条件展示列表
     * @param params 查询条件Map
     */
    @RequestMapping(value = "searchDatagrid")
    public PageList searchDatagrid(@RequestBody Map<String, String> params){
        return planTabTemplateBussinesServiceImpl.searchDatagrid(params);
    }

    /**
     * 功能描述：批量/单条 启用或禁用页签模版
     * @param ids id集合
     * @param status 状态(启用“1”或者禁用“0”)
     */
    @RequestMapping(value = "doStartOrStop")
    public FeignJson doStartOrStop(@RequestParam(value = "ids",required = false) String ids,
                            @RequestParam(value = "status",required = false) String status){
        FeignJson feignJson = planTabTemplateBussinesServiceImpl.doStartOrStop(ids, status);
        return feignJson;
    }

    /**
     * 功能描述：批量/单条 删除页签模版
     * @param ids id集合(“，”分隔)
     */
    @RequestMapping(value = "doBatchDelete")
    public FeignJson doBatchDelete(@RequestParam(value = "ids",required = false) String ids){
        FeignJson feignJson = planTabTemplateBussinesServiceImpl.doBatchDelete(ids);
        return feignJson;
    }

    /**
     * 功能描述：保存数据
     * @param dto
     */
    @RequestMapping(value = "doSave")
    public TabTemplateDto doSave(@RequestBody TabTemplateDto dto){
        TabTemplate info = new TabTemplate();
        Dto2Entity.copyProperties(dto, info);
        info = tabTemplateEntityServiceImpl.doSave(info);
        dto = (TabTemplateDto)CodeUtils.JsonBeanToBean(info, TabTemplateDto.class);
        return dto;
    }

    @RequestMapping(value = "queryDataSourceByTabId")
    public List<DataSourceObjectDto> queryDataSourceByTabId(@RequestParam(value = "id") String id){
        List<DataSourceObject> list = dataSourceObjectEntityServiceI.queryDataSourceByTabId(id);
        List<DataSourceObjectDto> list1 = CodeUtils.JsonListToList(list, DataSourceObjectDto.class);
        return list1;
    }

    @RequestMapping(value = "queryObjectPropertyInfoDtoTabId")
    public List<ObjectPropertyInfoDto> queryObjectPropertyInfoDtoTabId(@RequestParam(value = "id") String id){
        List<ObjectPropertyInfo> arrayList = new ArrayList<>();
        List<DataSourceObject> list = dataSourceObjectEntityServiceI.queryDataSourceByTabId(id);
        list.forEach(it -> {
            List<ObjectPropertyInfo> infoList = objectPropertyInfoEntityServiceI.queryInfoListByDataSourceId(it.getId());
            arrayList.addAll(infoList);
        });
        List<ObjectPropertyInfoDto> list1 = CodeUtils.JsonListToList(arrayList, ObjectPropertyInfoDto.class);
        return list1;
    }

    /**
     * 功能描述：根据ID查询数据
     * @param id
     * @return TabTemplateDto
     */
    @RequestMapping(value = "queryTabTemplate")
    public TabTemplateDto queryTabTemplate(@RequestParam(value = "id",required = false) String id){
        TabTemplate info = tabTemplateEntityServiceImpl.queryEntityById(id);
        return (TabTemplateDto)CodeUtils.JsonBeanToBean(info, TabTemplateDto.class);
    }

    /**
     * 功能描述：判断名称是否重复
     * @param name
     * @return boolean
     */
    @RequestMapping(value = "isRepeatName")
    public boolean isRepeatName(@RequestParam(value = "name",required = false) String name,
                                @RequestParam(value = "id",required = false) String id){
        return planTabTemplateBussinesServiceImpl.isRepeatTabTemplateInfoByName(name,id);
    }

    /**
     * 功能描述：根据ID复制数据
     * @param id
     * @return TabTemplateDto
     */
    @RequestMapping(value = "copyEntity")
    public TabTemplateDto copyEntity(@RequestParam(value = "id",required = false) String id){
        return planTabTemplateBussinesServiceImpl.copyTabTemplateEntity(id);
    }

    /**
     * 获取所有启动的未删除的页签模板
     * @return
     */
    @RequestMapping(value = "getAllTabTemplates")
    public List<TabTemplateDto> getAllTabTemplates(@RequestParam(value = "avaliable",required = false) String avaliable,
                                                   @RequestParam(value = "stopFlag",required = false) String stopFlag) {
        List<TabTemplate> templates = tabTemplateEntityServiceImpl.getAllTabTemplates(avaliable,stopFlag);
        List<TabTemplateDto> results = new ArrayList<>();
        try {
            results = (List<TabTemplateDto>) VDataUtil.getVDataByEntity(templates,TabTemplateDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * 功能描述：保存数据
     * @param dto
     */
    @RequestMapping(value = "doUpdateOrRevise")
    public TabTemplateDto doUpdateOrRevise(@RequestBody TabTemplateDto dto,@RequestParam(value = "userId",required = false) String userId,
                                           @RequestParam(value = "updateOrRevise",required = false) String updateOrRevise){
        TabTemplate info = new TabTemplate();
        Dto2Entity.copyProperties(dto, info);
        info = tabTemplateEntityServiceImpl.doUpdateOrRevise(info,userId,updateOrRevise);
        dto = (TabTemplateDto)CodeUtils.JsonBeanToBean(info, TabTemplateDto.class);
        return dto;
    }

    /**
     * 功能描述：回退/撤回
     * @param params
     */
    @RequestMapping(value = "doBack")
    public FeignJson doBack(@RequestBody Map<String, String> params){
        return tabTemplateEntityServiceImpl.doBack(params);
    }

    /**
     * 功能描述：提交审批
     * @param params
     */
    @RequestMapping(value = "doSubmitApprove")
    public FeignJson doSubmitApprove(@RequestBody Map<String, String> params){
        return tabTemplateEntityServiceImpl.doSubmitApprove(params);
    }

    @ApiOperation(value = "获取页签模板版本信息及数量", httpMethod="POST")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", name = "bizId", value = "版本id", dataType = "String"),
        @ApiImplicitParam(paramType = "query", name = "pageSize", value = "页码", dataType = "Integer"),
        @ApiImplicitParam(paramType = "query", name = "pageNum", value = "每页数量", dataType = "Integer")
    })
    @ApiResponses(@ApiResponse(code=200,message="接口调用成功"))
    @RequestMapping(value = "getVersionDatagridStr")
    public FeignJson getVersionDatagridStr(@RequestParam(value = "bizId",required = false) String bizId, @RequestParam("pageSize") Integer pageSize,
                                           @RequestParam("pageNum") Integer pageNum) {
        FeignJson j = new FeignJson();
        try {
            List<TabTemplate> tabTemplateList = tabTemplateEntityServiceImpl.getVersionHistory(bizId, pageSize, pageNum);
            List<TabTemplateDto>  tabTemplateDtoList= new ArrayList<>();
            tabTemplateDtoList = (List<TabTemplateDto>) VDataUtil.getVDataByEntity(tabTemplateList,TabTemplateDto.class);
            tabTemplateDtoList = tabTemplateEntityServiceImpl.setStatusByLifeCycleStatus(tabTemplateDtoList);
            long count = 0;
            count = tabTemplateEntityServiceImpl.getVersionCount(bizId);
            Map<String, Object> map = new HashMap<>();
            map.put("list", tabTemplateDtoList);
            map.put("count", count);
            j.setAttributes(map);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }
}
