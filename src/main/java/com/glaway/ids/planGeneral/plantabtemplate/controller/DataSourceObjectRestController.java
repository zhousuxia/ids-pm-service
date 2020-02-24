package com.glaway.ids.planGeneral.plantabtemplate.controller;

import io.swagger.annotations.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.planGeneral.plantabtemplate.businessserviceI.PlanTabTemplateBussinesServiceI;
import com.glaway.ids.planGeneral.plantabtemplate.dto.DataRollBack;
import com.glaway.ids.planGeneral.plantabtemplate.dto.DataSourceObjectDto;
import com.glaway.ids.planGeneral.plantabtemplate.dto.ObjectPropertyInfoDto;
import com.glaway.ids.planGeneral.plantabtemplate.entity.DataSourceObject;
import com.glaway.ids.planGeneral.plantabtemplate.entityserviceI.DataSourceObjectEntityServiceI;
import com.glaway.ids.util.CodeUtils;
import com.glaway.ids.util.Dto2Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author wuxing
 * @version V1.0
 * @Title: RestController
 * @Description: 页签模版管理RestController
 * @date 2019-08-27
 */
@Api(tags = {"页签模版管理接口"})
@RestController
@RequestMapping(value = "/feign/dataSourceObjectRestController")
public class DataSourceObjectRestController {
    //计划通用化业务Service
    @Autowired
    private PlanTabTemplateBussinesServiceI planTabTemplateBussinesServiceImpl;
    //数据源对象信息Service
    @Autowired
    private DataSourceObjectEntityServiceI dataSourceObjectEntityServiceImpl;

    /**
     * 功能描述：根据查询条件展示列表
     *
     * @param params 查询条件Map
     */
    @ApiOperation(value = "根据查询条件展示列表", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "searchDatagrid")
    public PageList searchDatagrid(@RequestBody Map<String, String> params) {
        return planTabTemplateBussinesServiceImpl.searchObjectSelectsDatagrid(params);
    }

    /**
     * 功能描述：保存数据
     *
     * @param dto
     */
    @ApiOperation(value = "保存或更新页签模板信息", httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveOrUpdate")
    public DataSourceObjectDto saveOrUpdate(@RequestBody DataSourceObjectDto dto) {
        DataSourceObject dataSourceObject = new DataSourceObject();
        Dto2Entity.copyProperties(dto, dataSourceObject);
        DataSourceObject info = dataSourceObjectEntityServiceImpl.saveOrUpdate(dataSourceObject);
        return (DataSourceObjectDto) CodeUtils.JsonBeanToBean(info, DataSourceObjectDto.class);
    }

    /**
     * 功能描述：根据页签模板TabId查询所有数据对象(id,objectPath)集合
     *
     * @param tabId
     */
    @ApiOperation(value = "根据页签模板TabId查询所有数据对象(id,objectPath)集合", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "tabId", value = "页签模板Id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getAllDataSourceObject")
    public List<Map<String, String>> getAllDataSousaveOrUpdaterceObject(@RequestParam(value = "tabId", required = false) String tabId) {
        return planTabTemplateBussinesServiceImpl.getAllDataSourceObject(tabId);
    }

    /**
     * 功能描述：根据主键Id查询数据
     *
     * @param id
     */
    @ApiOperation(value = "根据主键Id查询数据", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "页签模板id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryDataSourceObjectDtoById")
    public DataSourceObjectDto queryDataSourceObjectDtoById(@RequestParam(value = "id", required = false) String id) {
        DataSourceObject info = dataSourceObjectEntityServiceImpl.queryEntityById(id);
        return (DataSourceObjectDto) CodeUtils.JsonBeanToBean(info, DataSourceObjectDto.class);
    }

    /**
     * 功能描述：保存所有信息
     *
     * @param requestMap key:dataSourceObjectList,objectPropertyList
     */
    @ApiOperation(value = "保存页签所有信息", httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "saveAllInfo")
    public FeignJson saveAllInfo(@RequestBody Map<String, Object> requestMap) {
        //数据转换
        ObjectMapper mapper = new ObjectMapper();
        List<DataSourceObjectDto> dataSourceObjectList = mapper.convertValue(requestMap.get("dataSourceObjectList"), new TypeReference<List<DataSourceObjectDto>>() {
        });
        List<ObjectPropertyInfoDto> objectPropertyList = mapper.convertValue(requestMap.get("objectPropertyList"), new TypeReference<List<ObjectPropertyInfoDto>>() {
        });
        return planTabTemplateBussinesServiceImpl.saveAllInfo(dataSourceObjectList, objectPropertyList);
    }

    /**
     * 功能描述：删除数据
     *
     * @param id
     */
    @ApiOperation(value = "根据ID删除数据页签模板信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "页签模板id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doDelete")
    public FeignJson doDelete(@RequestParam(value = "id", required = false) String id) {
        return planTabTemplateBussinesServiceImpl.doDeleteDataSourcrObj(id);
    }

    /**
     * 功能描述：根据页签模板ID查询所有数据
     *
     * @param tabId
     */
    @ApiOperation(value = "根据页签模板ID查询所有数据", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "tabId", value = "页签模板id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getAllDataByTabId")
    public List<DataSourceObjectDto> getAllDataByTabId(@RequestParam(value = "tabId", required = false) String tabId) {
        return planTabTemplateBussinesServiceImpl.getAllDataByTabId(tabId);
    }

    /**
     * 功能描述：数据回滚
     *
     * @param requestMap key:dataSourceObjectList,objectPropertyList
     */
    @ApiOperation(value = "页签模板信息数据回滚", httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "dataRollBack")
    public FeignJson dataRollBack(@RequestBody Map<String, Object> requestMap) {
        //数据转换
        ObjectMapper mapper = new ObjectMapper();
        List<DataRollBack> dataSourceObjectList = mapper.convertValue(requestMap.get("dataSourceObjectList"), new TypeReference<List<DataRollBack>>() {
        });
        List<String> objectPropertyList = mapper.convertValue(requestMap.get("objectPropertyList"), new TypeReference<List<String>>() {
        });
        return planTabTemplateBussinesServiceImpl.dataRollBack(dataSourceObjectList, objectPropertyList);
    }

    /**
     * 功能描述：保存所有信息
     *
     * @param requestMap key:dataSourceObjectList,objectPropertyList
     */
    @ApiOperation(value = "修改修订页签所有信息", httpMethod = "POST")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "updateOrReviseInfo")
    public FeignJson updateOrReviseInfo(@RequestBody Map<String, Object> requestMap) {
        //数据转换
        ObjectMapper mapper = new ObjectMapper();
        List<DataSourceObjectDto> dataSourceObjectList = mapper.convertValue(requestMap.get("dataSourceObjectList"), new TypeReference<List<DataSourceObjectDto>>() {
        });
        List<ObjectPropertyInfoDto> objectPropertyList = mapper.convertValue(requestMap.get("objectPropertyList"), new TypeReference<List<ObjectPropertyInfoDto>>() {
        });
        return planTabTemplateBussinesServiceImpl.updateOrReviseInfo(dataSourceObjectList, objectPropertyList);
    }
}
