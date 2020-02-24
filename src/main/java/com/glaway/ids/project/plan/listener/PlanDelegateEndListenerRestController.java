package com.glaway.ids.project.plan.listener;

import io.swagger.annotations.*;
import com.alibaba.fastjson.JSON;
import com.glaway.ids.project.plan.service.PlanServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Description: 计划委培流程监听接口
 * @author: sunmeng
 * @ClassName: PlanAssignExcutionRestController
 * @Date: 2019/7/30-16:15
 * @since
 */
@Api(tags = {"计划委培流程监听接口"})
@RestController
@RequestMapping("/feign/planDelegateEndListenerRestController")
public class PlanDelegateEndListenerRestController {
    @Autowired
    private PlanServiceI planService;

    @ApiOperation(value = "notify")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "variables", value = "variables", dataType = "String")
    })
    @RequestMapping("/notify")
    public void notify(@RequestParam("variables") String variables) {
        Map<String, Object> variablesMap = JSON.parseObject(variables, Map.class);
        String oid = variablesMap.get("oid").toString();
        String delegateUserId = variablesMap.get("delegateUserId").toString();
        String userId = variablesMap.get("userId").toString();
        if (oid == null || oid.equals("")) {
            return;
        }
        String[] arrays = oid.split(":");
        if (arrays.length < 2) {
            return;
        }
        String planId = arrays[1];
        planService.updatePlanOwnerAndRestartPlanReceivedProcess(planId, delegateUserId, userId);
    }
}
