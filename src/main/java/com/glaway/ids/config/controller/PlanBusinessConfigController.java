package com.glaway.ids.config.controller;

import io.swagger.annotations.*;
import com.glaway.foundation.common.util.JsonUtil;
import com.glaway.foundation.common.util.VDataUtil;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.config.entity.BusinessConfig;
import com.glaway.ids.config.service.PlanBusinessConfigServiceI;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.core.common.controller.BaseController;
import com.glaway.foundation.core.common.hibernate.qbc.CriteriaQuery;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.ids.project.plan.dto.BusinessConfigDto;
import com.glaway.ids.util.CodeUtils;
import com.glaway.ids.util.Dto2Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务配置（项目阶段、计划等级、计划变更类别）处理接口
 *
 * @author xshen
 * @version 2015年3月26日
 * @see PlanBusinessConfigController
 * @since
 */
@Api(tags = {"业务配置（项目阶段、计划等级、计划变更类别）处理接口"})
@RestController
@RequestMapping("/feign/planBusinessConfigController")
public class PlanBusinessConfigController extends BaseController {

    /**
     * 项目阶段、计划等级、计划变更类别配置处理接口服务
     */
    @Autowired
    private PlanBusinessConfigServiceI planBusinessConfigService;

    /**
     * 根据条件查询业务配置列表
     *
     * @param conditionList
     * @param isPage
     * @return
     */
    @ApiOperation(value = "根据条件查询业务配置列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "isPage", value = "是否分页", dataType = "boolean")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryEntity")
    public PageList queryEntity(@RequestBody List<ConditionVO> conditionList, @RequestParam("isPage") boolean isPage) {
        PageList pageList = planBusinessConfigService.queryEntity(conditionList, isPage);
        return pageList;
    }

    /**
     * 业务配置新增/修改
     *
     * @param businessConfig
     */
    @ApiOperation(value = "业务配置新增/修改", httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "businessConfigSaveOrUpdate")
    public void businessConfigSaveOrUpdate(@RequestBody BusinessConfig businessConfig) {
        planBusinessConfigService.businessConfigSaveOrUpdate(businessConfig);
    }

    /**
     * 查询业务配置
     *
     * @param cq
     * @param flag
     */
    @ApiOperation(value = "查询业务配置", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "flag", value = "是否分页", dataType = "boolean")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getDataGrid")
    public void getDataGrid(@RequestBody CriteriaQuery cq, @RequestParam("flag") boolean flag) {
        planBusinessConfigService.getDataGrid(cq, flag);
    }

    /**
     * 获取业务配置信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "获取业务配置信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "业务配置id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getBusinessConfig")
    public BusinessConfigDto getBusinessConfig(@RequestParam("id") String id) {
        BusinessConfig config = planBusinessConfigService.getBusinessConfig(id);
        BusinessConfigDto dto = new BusinessConfigDto();
        Dto2Entity.copyProperties(config, dto);
        return dto;
    }

    /**
     * 根据业务配置类型获取业务配置信息
     *
     * @param configType
     * @return
     */
    @ApiOperation(value = "根据业务配置类型获取业务配置信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "configType", value = "业务配置类型", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getBusinessConfigListByConfigType")
    public List<BusinessConfigDto> getBusinessConfigListByConfigType(@RequestParam("configType") String configType) {
        List<BusinessConfig> list = planBusinessConfigService.getBusinessConfigListByConfigType(configType);
        List<BusinessConfigDto> dtoList = CodeUtils.JsonListToList(list, BusinessConfigDto.class);
        return dtoList;
    }

    /**
     * 根据业务配置类型获取所有业务配置信息（包括逻辑删除的数据）
     *
     * @param configType
     * @return
     */
    @ApiOperation(value = "根据业务配置类型获取所有业务配置信息（包括逻辑删除的数据）", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "configType", value = "业务配置类型", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "searchAllBusinessConfigs")
    public List<BusinessConfigDto> searchAllBusinessConfigs(@RequestParam(value = "configType",required = false) String configType){
        List<BusinessConfig> list = planBusinessConfigService.getAllBusinessConfigListByConfigType(configType);
        List<BusinessConfigDto> dtoList = CodeUtils.JsonListToList(list, BusinessConfigDto.class);
        return dtoList;
    }

    /**
     * 获取项目阶段信息
     *
     * @return
     */
    @ApiOperation(value = "获取项目阶段信息", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getProjectPhaseList")
    public String getProjectPhaseList() {
        String listStr = planBusinessConfigService.getProjectPhaseList();
        return listStr;
    }

    /**
     * 根据条件查询业务配置信息
     *
     * @param bcon
     * @return
     */
    @ApiOperation(value = "根据条件查询业务配置信息", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "searchBusinessConfigAccurate")
    public String searchBusinessConfigAccurate(@RequestBody BusinessConfigDto bcon) {
        BusinessConfig bc = new BusinessConfig();
        Dto2Entity.copyProperties(bcon, bc);
        String listStr = planBusinessConfigService.searchBusinessConfigAccurateForStr(bc);
        return listStr;
    }

    /**
     * 获取有效的业务配置信息
     *
     * @param dto
     * @return
     */
    @ApiOperation(value = "获取有效的业务配置信息", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "searchUseableBusinessConfigs")
    public List<BusinessConfigDto> searchUseableBusinessConfigs(@RequestBody BusinessConfigDto dto) {
        BusinessConfig bc = new BusinessConfig();
        Dto2Entity.copyProperties(dto, bc);
        List<BusinessConfig> busList = planBusinessConfigService.searchUseableBusinessConfigsForStr(bc);
        List<BusinessConfigDto> list = CodeUtils.JsonListToList(busList,BusinessConfigDto.class);
        return list;
    }

    /**
     * 根据业务配置属性搜索业务配置信息
     *
     * @return
     * @see
     */
    @ApiOperation(value = "根据业务配置属性搜索业务配置信息", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "searchBusinessConfigs")
    public List<BusinessConfigDto> searchBusinessConfigs(@RequestBody BusinessConfigDto dto) {
        BusinessConfig bc = new BusinessConfig();
        Dto2Entity.copyProperties(dto, bc);
        List<BusinessConfig> listStr = planBusinessConfigService.searchBusinessConfigs(bc);
        List<BusinessConfigDto> endList = new ArrayList<BusinessConfigDto>();
        try {
            endList = (List<BusinessConfigDto>) VDataUtil.getVDataByEntity(listStr, BusinessConfigDto.class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return endList;
    }

    /**
     * 根据id获取业务配置信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id获取业务配置信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "业务配置id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getBusinessConfigEntity")
    public BusinessConfigDto getBusinessConfigEntity(@RequestParam("id") String id) {
        BusinessConfig bc = planBusinessConfigService.getBusinessConfig(id);
        BusinessConfigDto dto = new BusinessConfigDto();
        try {
            dto = (BusinessConfigDto) VDataUtil.getVDataByEntity(bc, BusinessConfigDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    /**
     * 根据名称获取业务配置信息
     *
     * @param configTypeName
     * @return
     */
    @ApiOperation(value = "根据名称获取业务配置信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "configTypeName", value = "业务配置名称", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getConfigTypeName")
    public FeignJson getConfigTypeName(@RequestParam("configTypeName") String configTypeName) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            String typeName = planBusinessConfigService.getConfigTypeName(configTypeName);
            j.setObj(typeName);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 保存Excel导入的业务配置信息
     *
     * @param dataFromExcel
     * @param configType
     * @param userId
     * @param orgId
     * @return
     */
    @ApiOperation(value = "保存Excel导入的业务配置信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "configType", value = "业务配置类型", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织ID", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doData")
    public FeignJson doData(@RequestBody List<String> dataFromExcel, @RequestParam("configType") String configType, @RequestParam("userId") String userId, @RequestParam("orgId") String orgId) {
        FeignJson fj = new FeignJson();
        String res = "";
        res = planBusinessConfigService.doData(dataFromExcel, configType, userId, orgId);
        fj.setObj(res);
        return fj;
    }
}
