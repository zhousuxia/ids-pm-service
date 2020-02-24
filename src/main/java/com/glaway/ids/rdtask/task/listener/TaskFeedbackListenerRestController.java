package com.glaway.ids.rdtask.task.listener;

import io.swagger.annotations.*;
import com.alibaba.fastjson.JSON;
import com.glaway.ids.project.plan.service.PlanServiceI;
import com.glaway.ids.rdtask.task.service.TaskFeedbackServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Description: 计划模板正常流程处理方式, 完工反馈流程监听
 * @author: sunmeng
 * @ClassName: TaskFeedbackRestController
 * @Date: 2019/8/9-13:47
 * @since
 */
@Api(tags = {" 完工反馈流程监听接口"})
@RestController
@RequestMapping("/feign/taskFeedbackListenerRestController")
public class TaskFeedbackListenerRestController {
    @Autowired
    private TaskFeedbackServiceI taskFeedbackService;
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
        taskFeedbackService.notify(id, variablesMap);
    }
}
