package com.glaway.ids.project.plan.listener;

import io.swagger.annotations.*;
import com.alibaba.fastjson.JSON;
import com.glaway.ids.project.plan.service.PlanServiceI;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Description: 计划下达驳回监听服务接口
 * @author: sunmeng
 * @ClassName: PlanAssignBackExcutionRestController
 * @Date: 2019/8/1-9:25
 * @since
 */
@Api(tags = {"计划下发驳回监听服务接口"})
@RestController
@RequestMapping("/feign/planAssignBackExcutionRestController")
public class PlanAssignBackExcutionRestController {
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
            planService.planAssignBack(formId);
        }
    }
}
