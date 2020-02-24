package com.glaway.ids.rdtask.task.listener;

import io.swagger.annotations.*;
import com.alibaba.fastjson.JSON;
import com.glaway.ids.rdtask.task.service.TaskFeedbackServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Description: 〈一句话功能简述〉
 * @author: sunmeng
 * @ClassName: TaskFeedbackGoBackRestController
 * @Date: 2019/8/13-10:05
 * @since
 */
@Api(tags = {"计划完工反馈驳回监听接口"})
@RestController
@RequestMapping("/feign/taskFeedbackGoBackRestController")
public class TaskFeedbackGoBackRestController {
    @Autowired
    private TaskFeedbackServiceI taskFeedbackService;

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
        taskFeedbackService.goBackNotify(id, variablesMap);
    }
}
