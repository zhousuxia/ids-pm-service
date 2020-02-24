package com.glaway.ids.planGeneral.plantabtemplate.controller;

import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.planGeneral.plantabtemplate.businessserviceI.PlanTabTemplateBussinesServiceI;
import com.glaway.ids.planGeneral.plantabtemplate.dto.ObjectPropertyInfoDto;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @Title: RestController
 * @Description: 页签模板配置-源数据属性接口
 * @author wuxing
 * @date 2019-08-27
 * @version V1.0
 */
@Api(tags = {"页签模板配置-源数据属性接口"})
@RestController
@RequestMapping(value = "/feign/objectPropertyRestController")
public class ObjectPropertyRestController {

    //计划通用化业务Service
    @Autowired
    private PlanTabTemplateBussinesServiceI planTabTemplateBussinesServiceImpl;

    /**
     * 功能描述：根据页签模板ID查询所有数据
     * @param tabId
     */
    @ApiOperation(value = "根据页签模板ID查询元属性所有数据",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "tabId", value = "页签模板ID", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getAllPropertyByTabId")
    public List<ObjectPropertyInfoDto> getAllPropertyByTabId(@RequestParam(value = "tabId",required = false) String tabId){
        return planTabTemplateBussinesServiceImpl.getAllPropertyByTabId(tabId);
    }

    /**
     * 功能描述：删除数据
     * @param id
     */
    @ApiOperation(value = "根据数据对象ID删除数据",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "数据对象ID", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doDelete")
    public FeignJson doDelete(@RequestParam(value = "id",required = false) String id){
        return planTabTemplateBussinesServiceImpl.doDeleteObjProperty(id);
    }
}
