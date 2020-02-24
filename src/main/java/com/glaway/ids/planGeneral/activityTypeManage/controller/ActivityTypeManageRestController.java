package com.glaway.ids.planGeneral.activityTypeManage.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaway.foundation.common.util.VDataUtil;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.core.common.controller.BaseController;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.planGeneral.activityTypeManage.activityTypeManageBusinessServiceI.ActivityTypeManageBusinessServiceI;
import com.glaway.ids.planGeneral.activityTypeManage.activityTypeManageEntityServiceI.ActivityTypeManageEntityServiceI;
import com.glaway.ids.planGeneral.activityTypeManage.dto.ActivityTypeManageDto;
import com.glaway.ids.planGeneral.activityTypeManage.entity.ActivityTypeManage;
import com.glaway.ids.util.CodeUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 活动类型接口
 */
@Api(tags = {"活动类型接口"})
@RestController
@RequestMapping("/feign/activityTypeManageRestController")
public class ActivityTypeManageRestController extends BaseController {

    /**
     * 活动类型业务处理接口
     */
    @Autowired
    private ActivityTypeManageBusinessServiceI activityTypeManageBusinessServiceI;

    /**
     * 活动类型管理接口
     */
    @Autowired
    private ActivityTypeManageEntityServiceI activityTypeManageEntityServiceI;

    /**
     * 根据条件获取活动类型
     *
     * @param feignJson
     * @return
     */
    @ApiOperation(value = "根据条件获取活动类型", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryEntity")
    public FeignJson queryEntity(@RequestBody FeignJson feignJson) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = feignJson.getAttributes();
        List<ConditionVO> conditionList = mapper.convertValue(map.get("conditionList"), new TypeReference<List<ConditionVO>>() {
        });
        Map<String, String> params = mapper.convertValue(map.get("params"), new TypeReference<Map<String, String>>() {
        });
        return activityTypeManageEntityServiceI.queryEntity(conditionList, params, false);
    }

    /**
     * 增加或修改活动类型
     *
     * @param userId
     * @param name
     * @param remark
     * @param id
     * @return
     */
    @ApiOperation(value = "增加活动类型", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "活动类型名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "备注", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "活动类型id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doAddActivityTypeManage")
    public FeignJson doAddActivityTypeManage(@RequestParam(value = "userId") String userId, @RequestParam(value = "name") String name, @RequestParam(value = "remark") String remark, @RequestParam(value = "id", required = false) String id) {
        return activityTypeManageBusinessServiceI.doAddActivityTypeManage(userId, name, remark, id);
    }

    /**
     * 批量删除活动类型
     *
     * @param userId
     * @param ids
     * @return
     */
    @ApiOperation(value = "批量删除活动类型", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "用户ID", value = "userId", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "活动类型ids", value = "ids", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doDeleteBatch")
    public FeignJson doDeleteBatch(@RequestParam(value = "userId") String userId, @RequestParam(value = "ids") String ids) {
        return activityTypeManageBusinessServiceI.doDeleteBatch(userId, ids);
    }

    /**
     * 活动类型批量删除前的校验
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "活动类型批量删除前的校验", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "活动类型ids", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "deleteBatchBeforeCheckDate")
    public FeignJson deleteBatchBeforeCheckDate(@RequestParam(value = "ids") String ids) {
        return activityTypeManageBusinessServiceI.deleteBatchBeforeCheckDate(ids);
    }

    /**
     * 批量禁用启用活动类型
     *
     * @param userId
     * @param ids
     * @param status
     * @return
     */
    @ApiOperation(value = "批量禁用启用活动类型", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "ids", value = "活动类型ids", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "status", value = "禁用启用状态", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "doStartOrStop")
    public FeignJson doStartOrStop(@RequestParam(value = "userId") String userId, @RequestParam(value = "ids") String ids, @RequestParam(value = "status") String status) {
        return activityTypeManageBusinessServiceI.doStartOrStop(userId, ids, status);
    }

    /**
     * 根据活动类型id获取活动类型信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据活动类型id获取活动类型信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "活动类型id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "queryActivityTypeManageById")
    public ActivityTypeManageDto queryActivityTypeManageById(@RequestParam(value = "id") String id) {
        ActivityTypeManage activityTypeManage = activityTypeManageEntityServiceI.queryActivityTypeManageById(id);
        ActivityTypeManageDto o = (ActivityTypeManageDto) CodeUtils.JsonBeanToBean(activityTypeManage, ActivityTypeManageDto.class);
        return o;
    }

    /**
     * 获取所有未删除的活动类型
     *
     * @param flag
     * @return
     */
    @ApiOperation(value = "获取所有未删除的活动类型", httpMethod = "GET")
    @ApiImplicitParams(@ApiImplicitParam(paramType = "query", name = "flag", value = "查询标识", required = false, dataType = "Boolean"))
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getAllActivityTypeManage")
    public List<ActivityTypeManageDto> getAllActivityTypeManage(@RequestParam(value = "flag", required = false) Boolean flag) {
        List<ActivityTypeManage> list = activityTypeManageEntityServiceI.getAllActivityTypeManage(flag);
        List<ActivityTypeManageDto> results = new ArrayList<>();
        try {
            results = (List<ActivityTypeManageDto>) VDataUtil.getVDataByEntity(list, ActivityTypeManageDto.class);
        } catch (Exception e) {
        }
        return results;
    }

    /**
     * 获取所有活动类型
     *
     * @return
     */
    @ApiOperation(value = "获取所有活动类型", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getAllActivityTypeManages")
    public List<ActivityTypeManageDto> getAllActivityTypeManages() {
        List<ActivityTypeManage> list = activityTypeManageEntityServiceI.getAllActivityTypeManage();
        List<ActivityTypeManageDto> results = new ArrayList<>();
        try {
            results = (List<ActivityTypeManageDto>) VDataUtil.getVDataByEntity(list, ActivityTypeManageDto.class);
        } catch (Exception e) {
        }
        return results;
    }

    @ApiOperation(value = "获取所有未删除的活动类型以<id,name>形式返回", httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "getAllActivityTypeManageMap")
    public Map<String, String> getAllActivityTypeManageMap() {
        Map<String , String> activityMap = new HashMap<>();
        List<ActivityTypeManage> list = activityTypeManageEntityServiceI.getAllActivityTypeManage(false);
        activityMap = list.stream().collect(Collectors.toMap(ActivityTypeManage::getId,ActivityTypeManage::getName));
        return activityMap;
    }
}
