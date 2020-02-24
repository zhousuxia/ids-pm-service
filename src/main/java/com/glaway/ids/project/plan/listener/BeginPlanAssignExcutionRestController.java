package com.glaway.ids.project.plan.listener;

import io.swagger.annotations.*;
import com.alibaba.fastjson.JSON;
import com.glaway.ids.project.plan.entity.ApprovePlanInfo;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.service.PlanServiceI;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Description: 〈一句话功能简述〉
 * @author: sunmeng
 * @ClassName: beginPlanAssignExcutionRestController
 * @Date: 2019/8/1-9:17
 * @since
 */
@Api(tags = {"计划下发监听接口"})
@RestController
@RequestMapping("/feign/beginPlanAssignExcutionRestController")
public class BeginPlanAssignExcutionRestController {
    @Autowired
    private PlanServiceI planService;

    /**
     * 计划下发监听处理
     *
     * @param variables
     */
    @ApiOperation(value = "计划下发监听处理", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "variables", value = "流程变量", dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "接口调用成功"))
    @RequestMapping(value = "notify")
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
        // 审批通过之后更新计划生命周期并将记录下达人
        if (StringUtils.isNotEmpty(formId)) {
            ApprovePlanInfo approvePlanInfo = new ApprovePlanInfo();
            approvePlanInfo.setFormId(formId);
            planService.queryAssignListAndUpdate(approvePlanInfo, 1, 10, false);
        }
    }
}
