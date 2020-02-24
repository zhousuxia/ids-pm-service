package com.glaway.ids.rdtask.task.listener;

import io.swagger.annotations.*;
import com.alibaba.fastjson.JSON;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.service.PlanServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Description: 计划模板驳回处理方式
 * @author: sunmeng
 * @ClassName: TaskFeedbackGoBackkddRestController
 * @Date: 2019/8/9-14:11
 * @since
 */
@Api(tags = {"计划完工反馈驳回监听接口（KDD）"})
@RestController
@RequestMapping("/feign/taskFeedbackGoBackkddRestController")
public class TaskFeedbackGoBackkddRestController {
    @Autowired
    private PlanServiceI planService;

    @ApiOperation(value = "notify")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "variables", value = "variables", dataType = "String")
    })
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
        String id = arrays[1];
        Plan plan = planService.getEntity(id);
        plan.setFeedbackProcInstId("");
        planService.updateEntity(plan);
    }
}
