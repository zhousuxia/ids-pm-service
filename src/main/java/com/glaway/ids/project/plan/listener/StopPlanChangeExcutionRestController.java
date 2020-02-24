package com.glaway.ids.project.plan.listener;

import io.swagger.annotations.*;
import com.alibaba.fastjson.JSON;
import com.glaway.foundation.activiti.facade.WorkFlowFacade;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.fdk.dev.service.threemember.FeignUserService;
import com.glaway.ids.constant.PlanConstants;
import com.glaway.ids.project.plan.entity.ApprovePlanForm;
import com.glaway.ids.project.plan.entity.ApprovePlanInfo;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.entity.TemporaryPlan;
import com.glaway.ids.project.plan.service.PlanFlowForworkServiceI;
import com.glaway.ids.project.plan.service.PlanServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by LHR on 2019/8/8.
 */
@Api(tags = {"计划变更流程结束监听接口"})
@RestController
@RequestMapping("/feign/stopPlanChangeExcutionRestController")
public class StopPlanChangeExcutionRestController {
    @Autowired
    private PlanServiceI planService;
    @Autowired
    private SessionFacade sessionFacade;
    @Autowired
    private WorkFlowFacade workFlowFacade;

    @ApiOperation(value = "notify")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "variables", value = "variables", dataType = "String")
    })
    @RequestMapping("/notify")
    public void notify(@RequestParam("variables") String variables) {
        Map<String, Object> variablesMap = JSON.parseObject(variables, Map.class);
        String oid = variablesMap.get("oid").toString();
        if (oid == null || oid.equals("")) {
            return;
        }
        String[] arrays = oid.split(":");
        if (arrays.length < 2) {
            return;
        }
        String formId = arrays[1];

        planService.updatePlanInfo(formId);

    }
}
