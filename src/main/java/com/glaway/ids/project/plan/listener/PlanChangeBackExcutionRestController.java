package com.glaway.ids.project.plan.listener;

import io.swagger.annotations.*;
import com.alibaba.fastjson.JSON;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.ids.project.plan.service.PlanFlowForworkServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 计划变更流程驳回监听服务接口
 * Created by LHR on 2019/8/7.
 */
@Api(tags = {"计划变更流程驳回监听服务接口"})
@RestController
@RequestMapping("/feign/planChangeBackExcutionRestController")
public class PlanChangeBackExcutionRestController {
    @Autowired
    private FeignUserService userService;
    @Autowired
    private PlanFlowForworkServiceI planFlowForworkServiceI;

    @ApiOperation(value = "notify")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "variables", value = "variables", dataType = "String")
    })
    @RequestMapping("/notify")
    public void notify(@RequestParam("variables") String variables) {
        Map<String, Object> variablesMap = JSON.parseObject(variables, Map.class);
        String oid = variablesMap.get("oid").toString();
        String userId = variablesMap.get("userId").toString();
        planFlowForworkServiceI.planChangeBack(oid,userId);
    }
}
