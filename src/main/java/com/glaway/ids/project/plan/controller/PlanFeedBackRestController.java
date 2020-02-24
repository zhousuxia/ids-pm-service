package com.glaway.ids.project.plan.controller;

import io.swagger.annotations.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaway.foundation.common.util.VDataUtil;
import com.glaway.foundation.core.common.controller.BaseController;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.ids.project.plan.dto.PlanFeedBackDto;
import com.glaway.ids.project.plan.entity.PlanFeedBack;
import com.glaway.ids.project.plan.service.PlanFeedBackServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 计划完工反馈接口
 * @author: sunmeng
 * @ClassName: PlanFeedBackRestController
 * @Date: 2019/10/16-14:25
 * @since
 */
@Api(tags = {"计划完工反馈接口"})
@RestController
@RequestMapping("/feign/planFeedBackRestController")
public class PlanFeedBackRestController extends BaseController {
    @Autowired
    private PlanFeedBackServiceI planFeedBackService;

    @ApiOperation(value = "查询计划完工反馈数据",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping("/queryData")
    public FeignJson queryData() {
        FeignJson j = new FeignJson();
        List<PlanFeedBack> list = planFeedBackService.getPlanFeedBack();
        List<PlanFeedBackDto> dtos = new ArrayList<>();
        try {
            dtos = (List<PlanFeedBackDto>) VDataUtil.getVDataByEntity(list, PlanFeedBackDto.class);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        j.setObj(dtos);
        return j;
    }

    @ApiOperation(value = "保存计划完工反馈信息",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping("/saveFeedBack")
    public FeignJson saveFeedBack(@RequestBody Map<String, String> params, @RequestParam(value = "userId", required = false) String userId) {
        return planFeedBackService.saveFeedBack(params, userId);
    }

    @ApiOperation(value = "计算反馈进度",httpMethod = "GET")
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping("/calculateWeight")
    public FeignJson calculateWeight(@RequestBody Map<String, Object> map) {
        return planFeedBackService.calculateWeightForFeign(map);
    }

    @ApiOperation(value = "根据相应的生命周期状态获取权重占比",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "status", value = "生命周期状态", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping("/calculateWeightByStatus")
    public FeignJson calculateWeightByStatus(@RequestParam(value = "status", required = false) String status) {
        FeignJson j = new FeignJson();
        int weight = planFeedBackService.calculateWeightByStatus(status);
        j.setObj(weight);
        return j;
    }
}
