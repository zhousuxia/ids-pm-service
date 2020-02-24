package com.glaway.ids.project.plan.listener;

import com.alibaba.fastjson.JSON;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.ids.project.plan.service.PlanFlowForworkServiceI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 计划变更流程通过监听接口
 *
 * @author Administrator
 * @version 2015年3月25日
 * @see
 * @since
 */
@Api(tags = {"计划变更流程通过监听接口"})
@RestController
@RequestMapping("/feign/planChangeOneExcutionRestController")
public class PlanChangeOneExcutionRestController {
    @Autowired
    private FeignUserService userService;
    @Autowired
    private PlanFlowForworkServiceI planFlowForworkServiceI;

    @ApiOperation(value = "计划变更流程通过监听接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "variables", value = "流程变量", dataType = "String")
    })
    @RequestMapping("/notify")
    public void notify(@RequestParam("variables") String variables) {
        Map<String, Object> variablesMap = JSON.parseObject(variables, Map.class);
        String oid = variablesMap.get("oid").toString();
        String userId = variablesMap.get("userId").toString();
        planFlowForworkServiceI.startPlanChangeFlowForWork(oid,userId,"","","");
    }
}
