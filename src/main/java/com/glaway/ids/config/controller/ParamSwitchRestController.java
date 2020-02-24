package com.glaway.ids.config.controller;

import io.swagger.annotations.*;

import java.util.ArrayList;
import java.util.List;

import com.glaway.foundation.common.util.VDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.OperationLog;
import com.glaway.foundation.core.common.controller.BaseController;
import com.glaway.ids.config.dto.ParamSwitchDto;
import com.glaway.ids.config.entity.ParamSwitch;
import com.glaway.ids.config.service.ParamSwitchServiceI;
import com.glaway.ids.util.Dto2Entity;

/**
 * @author wqb
 * @version V1.0
 * @Title: Controller
 * @Description: 参数
 * @date 2019年7月29日 13:50:19
 */
@Api(tags = {"项目计划参数接口"})
@RestController
@RequestMapping("/feign/paramSwitchRestController")
public class ParamSwitchRestController extends BaseController {
    private static final OperationLog log = BaseLogFactory.getOperationLog(ParamSwitchRestController.class);
    /**
     * 项目计划参数接口
     */
    @Autowired
    private ParamSwitchServiceI paramSwitchService;

    /**
     * 根据条件获取项目计划参数列表
     *
     * @param paramSwitch wqb 2019年7月29日 13:47:35
     */
    @ApiOperation(value = "根据条件获取项目计划参数列表", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "/getSwitch")
    List<ParamSwitchDto> getSwitch(@RequestBody ParamSwitchDto paramSwitch) {
        List<ParamSwitchDto> list = new ArrayList<ParamSwitchDto>();
        ParamSwitch bc = new ParamSwitch();
        Dto2Entity.copyProperties(paramSwitch, bc);
        List<ParamSwitch> endList = paramSwitchService.search(bc);
        try {
            list = (List<ParamSwitchDto>) VDataUtil.getVDataByEntity(endList, ParamSwitchDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 设置项目计划参数状态
     *
     * @param status
     * @param id     wqb 2019年7月29日 14:56:55
     */
    @ApiOperation(value = "设置项目计划参数状态", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "status", value = "状态", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "项目计划参数id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "/updateStatusById")
    void updateStatusById(@RequestParam("status") String status, @RequestParam("id") String id) {
        paramSwitchService.updateStatusById(status, id);
    }
}
